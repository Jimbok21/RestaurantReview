package com.example.courseworkpcversion

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.courseworkpcversion.utils.Constants
import com.example.courseworkpcversion.utils.GlideLoader

class SelectReviewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_review)

        var restaurantNameTextView = findViewById<TextView>(R.id.restaurantName)
        var ratingbarView = findViewById<RatingBar>(R.id.ratingbar)
        var imageView = findViewById<ImageView>(R.id.reviewImage)
        var reviewTextView = findViewById<TextView>(R.id.reviewText)

        val restaurantNameText = intent.getSerializableExtra(Constants.RESTAURANT_NAME)
        restaurantNameTextView.text = restaurantNameText.toString()

        val rating = intent.getSerializableExtra(Constants.RESTAURANT_RATING)
        ratingbarView.rating = rating as Float

        val image = intent.getSerializableExtra(Constants.RESTAURANT_IMAGE)
        var uriImage: Uri = Uri.parse(image.toString())
        GlideLoader(this@SelectReviewActivity).loadReviewPicture(uriImage, imageView)

        val reviewText = intent.getSerializableExtra(Constants.RESTAURANT_REVIEWTXT)
        reviewTextView.text = reviewText.toString()
    }

    fun getReview() {
    }
}