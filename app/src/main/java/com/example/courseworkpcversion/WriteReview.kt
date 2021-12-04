package com.example.courseworkpcversion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.courseworkpcversion.firestore.FirestoreClass
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore


class WriteReview : AppCompatActivity() {

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

    fun saveReviewButtonClicked(view: View) {
        val restaurantName = "Restaurant Test"
        val reviewTextInputEditText = findViewById<TextInputEditText>(R.id.textInputEditText)
        val ratingbar = findViewById<RatingBar>(R.id.ratingbar)
        val rating = ratingbar.rating
        val reviewText = reviewTextInputEditText.text.toString()
        val user = FirestoreClass().getCurrentUserID()
        if (rating == 0F) {
            val snackNoRating =
                Snackbar.make(view, getString(R.string.missingReviewRating), Snackbar.LENGTH_LONG)
            snackNoRating.view.setBackgroundColor(
                ContextCompat.getColor(
                    this@WriteReview,
                    R.color.ColourSnackbarError
                )
            )
            snackNoRating.show()
        } else if(restaurantName == "") {
            val snackNoRating =
                Snackbar.make(view, getString(R.string.missingRestaurant), Snackbar.LENGTH_LONG)
            snackNoRating.view.setBackgroundColor(
                ContextCompat.getColor(
                    this@WriteReview,
                    R.color.ColourSnackbarError
                )
            )
            snackNoRating.show()
        } else if(reviewText == "") {
            val snackNoRating =
                Snackbar.make(view, getString(R.string.missingReview), Snackbar.LENGTH_LONG)
            snackNoRating.view.setBackgroundColor(
                ContextCompat.getColor(
                    this@WriteReview,
                    R.color.ColourSnackbarError
                )
            )
            snackNoRating.show()
        } else {
            saveReview(restaurantName, rating, reviewText, user)
        }
    }

    fun saveReview(restaurantName: String, rating: Float, reviewText: String, userId: String) {
        val reviewDb = FirebaseFirestore.getInstance()
        val review: MutableMap<String, Any> = HashMap()
        review["restaurantName"] = restaurantName
        review["rating"] = rating
        review["reviewText"] = reviewText
        review["userId"] = userId

        reviewDb.collection("reviews").add(review).addOnSuccessListener { document ->
            startActivity(Intent(this@WriteReview, HomePageActivity::class.java))
        }.addOnFailureListener { e ->
            Log.e(
                this.javaClass.simpleName,
                "Registering the user failed", e)
        }
    }
    //do a hasmap and send to database
}