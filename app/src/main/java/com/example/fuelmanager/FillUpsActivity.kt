package com.example.fuelmanager

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.fuelmanager.adapter.FillUpsAdapter
import com.example.fuelmanager.data.FillUps
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_fill_ups.*
import kotlinx.android.synthetic.main.content_fill_ups.*

class FillUpsActivity : BaseActivity(){

    private lateinit var fillupsAdapter: FillUpsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fill_ups)

        fab.setOnClickListener {
            val createFillUpIntent = Intent(this, CreateFillUpActivity::class.java)
            startActivity(createFillUpIntent)
        }

        fillupsAdapter = FillUpsAdapter(applicationContext)
        FillUpRecyclerView.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        FillUpRecyclerView.adapter = fillupsAdapter
        initFillUpsListener()
    }

    private fun initFillUpsListener() {
        FirebaseDatabase.getInstance()
            .getReference("fillups")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                    val newFillUp = dataSnapshot.getValue<FillUps>(FillUps::class.java)
                    fillupsAdapter.addFillUp(newFillUp)
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    val key = dataSnapshot.key!!
                    FirebaseDatabase.getInstance().getReference("fillups").child(key).removeValue().addOnSuccessListener {
                        Toast.makeText(applicationContext, getString(R.string.fill_up_deleted), Toast.LENGTH_SHORT).show()
                    }

                    val imgRef = FirebaseDatabase.getInstance().getReference("fillups").child(key).child("imageURL")
                    Log.v("TAG", "key: " + key + "imgRef: " + imgRef)
                    /*imgRef.addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val imgURL = snapshot.value
                            val storedIMG = FirebaseStorage.getInstance().getReferenceFromUrl(imgURL)
                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }
                    })*/


                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }
}
