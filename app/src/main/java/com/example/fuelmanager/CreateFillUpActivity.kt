package com.example.fuelmanager

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import com.example.fuelmanager.data.FillUps
import com.example.fuelmanager.extensions.validateNonEmpty
import com.example.fuelmanager.fragments.DatePickerDialogFragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_fill_up.*
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.util.*

class CreateFillUpActivity : BaseActivity(), DatePickerDialogFragment.DateListener {

    companion object {
        private const val REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_fill_up)

        btn_ok.setOnClickListener{ okClick() }
        btnAttach.setOnClickListener{ attachClick() }
        etDate.setOnClickListener{ showDatePickerDialog() }
    }

    private fun okClick() {
        if (!validateForm()) {
            return
        }

        if (imgReceipt.visibility != View.VISIBLE) {
            uploadFillUp()
        } else {
            try {
                uploadFillUpWithImage()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun attachClick() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_CODE)
    }

    private fun validateForm() =
        etAmountOfLiter.validateNonEmpty() &&
        etDate.validateNonEmpty() &&
        etPrice.validateNonEmpty() &&
        etRegNum.validateNonEmpty() &&
        etTravelledKm.validateNonEmpty()


    private fun uploadFillUp(imageURL: String? = null) {
        val key = FirebaseDatabase.getInstance().reference.child("fillups").push().key ?: return

        val sum = etAmountOfLiter.text.toString().toDouble() * etPrice.text.toString().toDouble()
        val average = etAmountOfLiter.text.toString().toDouble() / etTravelledKm.text.toString().toDouble() * 100

        val newFillUp = FillUps(
            uid,
            etRegNum.text.toString(),
            etTravelledKm.text.toString().toDouble(),
            etAmountOfLiter.text.toString().toDouble(),
            etPrice.text.toString().toDouble(),
            sum,
            average,
            etDate.text.toString(),
            imageURL
        )

        FirebaseDatabase.getInstance().reference
            .child("fillups")
            .child(key)
            .setValue(newFillUp)
            .addOnCompleteListener {
                toast("Fill Up created")
                finish()
            }
    }

    private fun uploadFillUpWithImage() {
        val bitmap: Bitmap = (imgReceipt.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageInBytes = baos.toByteArray()

        val storageReference = FirebaseStorage.getInstance().reference
        val newImageName = URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8") + ".jpg"
        val newImageRef = storageReference.child("images/$newImageName")

        newImageRef.putBytes(imageInBytes)
            .addOnFailureListener { exception ->
                toast(exception.message)
            }
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                newImageRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                uploadFillUp(downloadUri.toString())
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap ?: return
            imgReceipt.setImageBitmap(imageBitmap)
            imgReceipt.visibility = View.VISIBLE
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialogFragment()
        datePicker.show( supportFragmentManager, "datePicker")
    }

    override fun onDateSelected(date: String) {
        etDate.setText(date)
    }
}
