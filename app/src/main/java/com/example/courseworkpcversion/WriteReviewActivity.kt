package com.example.courseworkpcversion

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.courseworkpcversion.firestore.FirestoreClass
import com.example.courseworkpcversion.utils.Constants
import com.example.courseworkpcversion.utils.GlideLoader
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.io.IOException


class WriteReviewActivity : AppCompatActivity() {

    private var reviewSelectedImageFileUri: Uri? = null

    lateinit var spinnerSelected : Spinner
    lateinit var result: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_review)

        spinnerSelected = findViewById<Spinner>(R.id.dropdownSpinner)
        result = findViewById(R.id.lemon)

        val options = arrayOf("Restaurtant 1", "Restaurant 2", "Restaurant 3")

        spinnerSelected.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        spinnerSelected.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                result.text = options.get(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                result.text = "please pick a Restaurant"
            }

        }
    }

    fun pickReviewPhoto(view: View) {
        ////checks if storage permission has been granted to access image files on the phone
        //will ask you for permission if it does not already have it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(galleryIntent, Constants.PICK_IMAGE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_STORAGE_PERMISSION_CODE
            )
            val lemon = Snackbar.make(view, "lemon ", Snackbar.LENGTH_LONG).show()
        }
    }

    fun saveReviewButtonClicked(view: View) {
        val restaurantName = "Restaurant Test"
        val reviewTextInputEditText = findViewById<TextInputEditText>(R.id.textInputEditText)
        val ratingbar = findViewById<RatingBar>(R.id.ratingbar)
        val rating = ratingbar.rating
        val reviewText = reviewTextInputEditText.text.toString()
        val user = FirestoreClass().getCurrentUserID()
        val image = reviewSelectedImageFileUri.toString()

        if (rating == 0F) {
            val snackNoRating =
                Snackbar.make(view, getString(R.string.missingReviewRating), Snackbar.LENGTH_LONG)
            snackNoRating.view.setBackgroundColor(
                ContextCompat.getColor(
                    this@WriteReviewActivity,
                    R.color.ColourSnackbarError
                )
            )
            snackNoRating.show()
        } else if(restaurantName == "") {
            val snackNoRating =
                Snackbar.make(view, getString(R.string.missingRestaurant), Snackbar.LENGTH_LONG)
            snackNoRating.view.setBackgroundColor(
                ContextCompat.getColor(
                    this@WriteReviewActivity,
                    R.color.ColourSnackbarError
                )
            )
            snackNoRating.show()
        } else if(reviewText == "") {
            val snackNoRating =
                Snackbar.make(view, getString(R.string.missingReview), Snackbar.LENGTH_LONG)
            snackNoRating.view.setBackgroundColor(
                ContextCompat.getColor(
                    this@WriteReviewActivity,
                    R.color.ColourSnackbarError
                )
            )
            snackNoRating.show()
        } else {
            saveReview(restaurantName, rating, reviewText, user, image)
        }
        uploadImage(view)
    }

    fun saveReview(restaurantName: String, rating: Float, reviewText: String, userId: String, image: String) {
        val reviewDb = FirebaseFirestore.getInstance()
        val review: MutableMap<String, Any> = HashMap()
        review["restaurantName"] = restaurantName
        review["rating"] = rating
        review["reviewText"] = reviewText
        review["userId"] = userId
        review["image"] = image

        reviewDb.collection("reviews").add(review).addOnSuccessListener { document ->
            //startActivity(Intent(this@WriteReview, HomePageActivity::class.java))
        }.addOnFailureListener { e ->
            Log.e(
                this.javaClass.simpleName,
                "Registering the user failed", e)
        }
    }

    fun uploadImage(view: View) {
        //saves the new profile picture
        if (reviewSelectedImageFileUri != null) {
            FirestoreClass().uploadImageToStorage(this, reviewSelectedImageFileUri)
            val snackSuccessLogin =
                Snackbar.make(
                    view,
                    getString(R.string.SuccessImageUpload),
                    Snackbar.LENGTH_LONG
                )
            snackSuccessLogin.view.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.ColourSnackbarSuccess
                )
            )
            snackSuccessLogin.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //sets the profile picture to the file selected locally
        super.onActivityResult(requestCode, resultCode, data)
        val reviewImageView = findViewById<ImageView>(R.id.reviewImage)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE)
                if (data != null) {
                    try {
                        reviewSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(reviewSelectedImageFileUri!!, reviewImageView)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        val snackError =
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                getString(R.string.imageSelectionFailed),
                                Snackbar.LENGTH_LONG
                            )
                        snackError.view.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.ColourSnackbarError
                            )
                        )
                        snackError.show()
                    }
                }
        }
    }
}