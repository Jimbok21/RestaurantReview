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
import android.widget.Spinner
import android.widget.ArrayAdapter
import com.example.courseworkpcversion.models.Restaurant

class WriteReviewActivity : AppCompatActivity() {

    //image on the device
    private var reviewSelectedImageFileUri: Uri? = null
    //image on the cloud storage
    private var reviewImageURL: String = ""

    private var restaurant: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {


        readData()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.write_review)

        val spinner = findViewById<Spinner>(R.id.dropdownSpinner)

        val RestaurantsList = readData()

        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, RestaurantsList)

        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                restaurant = RestaurantsList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun readData(): Array<String> {
        val restaurantsListTemp = arrayOf<String>(Constants.BOUCHON_DE_ROSSI, Constants.NEW_ICHIBAN, Constants.NANDOS)
        var i: Int = 0
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.RESTAURANTS).get().addOnCompleteListener { task ->
            if(task.isSuccessful) {

                for (document in task.result!!) {
                    val name = document.data["name"].toString()
                    restaurantsListTemp[i] = name
                    i++
                }
            }
        }
        return restaurantsListTemp
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
        }
    }


    fun saveReviewHandler(view: View) {

        val restaurantName = restaurant
        val reviewTextInputEditText = findViewById<TextInputEditText>(R.id.textInputEditText)
        val ratingbar = findViewById<RatingBar>(R.id.ratingbar)
        val rating = ratingbar.rating
        val reviewText = reviewTextInputEditText.text.toString()
        val user = FirestoreClass().getCurrentUserID()
        val image = ""
            if (rating == 0F) {
                val snackNoRating =
                    Snackbar.make(
                        view,
                        getString(R.string.missingReviewRating),
                        Snackbar.LENGTH_LONG
                    )
                snackNoRating.view.setBackgroundColor(
                    ContextCompat.getColor(
                        this@WriteReviewActivity,
                        R.color.ColourSnackbarError
                    )
                )
                snackNoRating.show()
            } else if (restaurantName == "") {
                val snackNoRating =
                    Snackbar.make(view, getString(R.string.missingRestaurant), Snackbar.LENGTH_LONG)
                snackNoRating.view.setBackgroundColor(
                    ContextCompat.getColor(
                        this@WriteReviewActivity,
                        R.color.ColourSnackbarError
                    )
                )
                snackNoRating.show()
            } else if (reviewText == "") {
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
           startActivity(Intent(this@WriteReviewActivity, HomePageActivity::class.java))
        }.addOnFailureListener { e ->
            Log.e(
                this.javaClass.simpleName,
                "Registering the user failed", e)
        }
    }

    fun uploadImage(view: View) {
        //saves the new review picture to the cloud storage
        if (reviewSelectedImageFileUri != null) {
            FirestoreClass().uploadImageToStorage(this, reviewSelectedImageFileUri)
        } else {
            saveReviewHandler(view)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //sets the review picture to the file selected locally
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

    fun reviewImageUploadSuccess(imageURL: String) {
        //putting data into the variables
        reviewImageURL = imageURL
        val restaurantName = restaurant
        val reviewTextInputEditText = findViewById<TextInputEditText>(R.id.textInputEditText)
        val ratingbar = findViewById<RatingBar>(R.id.ratingbar)
        val rating = ratingbar.rating
        val reviewText = reviewTextInputEditText.text.toString()
        val user = FirestoreClass().getCurrentUserID()
        //checks that the data is full. had to use toast here because the I could not get the view.
        //This is the only time I use toast in this app
        if (rating == 0F) {
            Toast.makeText(this, getString(R.string.missingReviewRating), Toast.LENGTH_LONG).show()
        } else if (restaurantName == "") {
            Toast.makeText(this, getString(R.string.missingRestaurant), Toast.LENGTH_LONG).show()
        } else if (reviewText == "") {
            Toast.makeText(this, getString(R.string.missingReview), Toast.LENGTH_LONG).show()
        } else {
            saveReview(restaurantName, rating, reviewText, user, imageURL)
        }
    }
}