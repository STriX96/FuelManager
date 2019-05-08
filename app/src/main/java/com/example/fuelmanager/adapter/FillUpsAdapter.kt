package com.example.fuelmanager.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.fuelmanager.R
import com.example.fuelmanager.data.FillUps
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fuel_up_item.view.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class FillUpsAdapter (private val context: Context): RecyclerView.Adapter<FillUpsAdapter.ViewHolder>(){

    private val fillUpsList: MutableList<FillUps> = mutableListOf()
    private var lastPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRegNum: TextView = itemView.tvRegNum
        val remove_btn: ImageButton = itemView.remove_btn
        val tvAmountOfLiter: TextView = itemView.tvAmountOfLiter
        val tvTravelledKm: TextView = itemView.tvTravelledKm
        val tvAverage: TextView = itemView.tvAverage
        val tvPrice: TextView = itemView.tvPrice
        val tvSum: TextView = itemView.tvSum
        val tvDate: TextView = itemView.tvDate
        val imgReceipt: ImageView = itemView.imgReceipt
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(viewGroup.context)
            .inflate(R.layout.fuel_up_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val tmpFillUp = fillUpsList[position]
        viewHolder.tvRegNum.text = tmpFillUp.regNum
        viewHolder.tvAmountOfLiter.text = String.format("%.2f", tmpFillUp.amountOfLiter) + " l"
        viewHolder.tvTravelledKm.text = String.format("%.1f", tmpFillUp.traveledKm) + " Km"
        viewHolder.tvAverage.text = String.format("%.2f", tmpFillUp.average) + " l/100 Km"
        viewHolder.tvPrice.text = String.format("%.1f", tmpFillUp.price) + " Ft"
        viewHolder.tvSum.text = String.format("%.0f", tmpFillUp.sum) + " Ft"
        viewHolder.tvDate.text = tmpFillUp.date

        if (tmpFillUp.imageURL.isNullOrBlank()) {
            viewHolder.imgReceipt.visibility = View.GONE
        } else {
            Glide.with(context).load(tmpFillUp.imageURL).into(viewHolder.imgReceipt)
            viewHolder.imgReceipt.visibility = View.VISIBLE
        }

        viewHolder.remove_btn.setOnClickListener {
            deleteFillUp(tmpFillUp)
            FirebaseDatabase.getInstance().getReference("fillups").child(tmpFillUp.thiskey).removeValue()
            try {
                FirebaseStorage.getInstance().getReferenceFromUrl(tmpFillUp.imageURL).delete()
            } catch (e: Exception){}
        }

        setAnimation(viewHolder.itemView, position)
    }

    override fun getItemCount() = fillUpsList.size

    fun addFillUp(item: FillUps?) {
        item ?: return

        fillUpsList.add(item)
        notifyDataSetChanged()
    }

    fun deleteFillUp(item: FillUps?){
        fillUpsList.remove(item)
        notifyDataSetChanged()
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }





}