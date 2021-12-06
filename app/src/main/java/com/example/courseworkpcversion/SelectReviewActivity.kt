package com.example.courseworkpcversion

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.courseworkpcversion.firestore.FirestoreClass
import com.example.courseworkpcversion.utils.Constants
import com.example.courseworkpcversion.utils.GlideLoader
import com.google.android.material.snackbar.Snackbar

import android.view.View.OnTouchListener
import com.google.firebase.firestore.FirebaseFirestore


class SelectReviewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_review)

        var restaurantNameTextView = findViewById<TextView>(R.id.restaurantName)
        var ratingbarView = findViewById<RatingBar>(R.id.ratingbar)
        var imageView = findViewById<ImageView>(R.id.reviewImage)
        var reviewTextView = findViewById<TextView>(R.id.reviewText)
        var usernameView = findViewById<TextView>(R.id.username)

        //stops the reviewbar from being edited
        ratingbarView.setOnTouchListener(OnTouchListener { v, event -> true })
        ratingbarView.isFocusable = false

        //sets all of the data to the review data
        val restaurantNameText = intent.getSerializableExtra(Constants.REVIEW_NAME)
        restaurantNameTextView.text = restaurantNameText.toString()

        val rating = intent.getSerializableExtra(Constants.REVIEW_RATING)
        ratingbarView.rating = rating as Float

        val image = intent.getSerializableExtra(Constants.REVIEW_IMAGE)
        var uriImage: Uri = Uri.parse(image.toString())
        GlideLoader(this@SelectReviewActivity).loadReviewPicture(uriImage, imageView)

        val reviewText = intent.getSerializableExtra(Constants.REVIEW_REVIEWTXT)
        reviewTextView.text = reviewText.toString()

        var userID = intent.getSerializableExtra(Constants.REVIEW_USER_ID)

        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection(Constants.USERS).document(userID.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val data = document.data!![Constants.USERNAME].toString()
                    usernameView.text = data
                }
            }
    }

    fun edit(view: View) {
        val userID = intent.getSerializableExtra(Constants.REVIEW_USER_ID)
        if (userID != FirestoreClass().getCurrentUserID()) {
            val snackNotAuthor =
                Snackbar.make(view, getString(R.string.notAuthor), Snackbar.LENGTH_LONG)
            snackNotAuthor.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
            snackNotAuthor.show()
        }
    }
}