package com.example.courseworkpcversion

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.courseworkpcversion.models.Review
import com.example.courseworkpcversion.utils.Constants

class MyAdapter (private val reviewList: MutableList<Review>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {


    /*
     * Inflate our views using the layout defined in row_layout.xml
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.row_layout, parent, false)

        return ViewHolder(v)
    }

    /*
     * Bind the data to the child views of the ViewHolder
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review: Review = reviewList[position]
        holder.txtMsg.text = review.reviewText
        var restaurant = review.restaurantName
        var restaurantImage = R.drawable.ic_restaurant
        if (restaurant == "Bouchon De Rossi") {
            restaurantImage = R.drawable.bouchon_de_rossi_image
        } else if (restaurant == "New Ichiban") {
            restaurantImage = R.drawable.new_ichiban_image
        } else if (restaurant == "Nandos") {
            restaurantImage = R.drawable.nandos_image
        }


        holder.imgView.setImageResource(restaurantImage)
        holder.txtMsg.text = review.reviewText
        holder.restaurantNameTxt.text = review.restaurantName
        holder.reviewBar.rating = review.rating

        //sends the extra data to the SelectReviewActivity
        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, SelectReviewActivity::class.java)
            intent.putExtra(Constants.REVIEW_NAME, review.restaurantName)
            intent.putExtra(Constants.REVIEW_RATING, review.rating)
            intent.putExtra(Constants.REVIEW_REVIEWTXT, review.reviewText)
            intent.putExtra(Constants.REVIEW_USER_ID, review.userId)
            intent.putExtra(Constants.REVIEW_IMAGE, review.image)
            intent.putExtra(Constants.REVIEW_ID, review.id)
            v.context.startActivity(intent)
        }
    }

    /*
     * Get the maximum size of the
     */
    override fun getItemCount(): Int {
        return reviewList.size
    }



    /*
     * The parent class that handles layout inflation and child view use
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var imgView = itemView.findViewById<View>(R.id.icon) as ImageView
        var txtMsg = itemView.findViewById<View>(R.id.reviewTextRecycler) as TextView
        val reviewBar = itemView.findViewById<View>(R.id.ratingbar) as RatingBar
        var restaurantNameTxt = itemView.findViewById<View>(R.id.restaurantName) as TextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }
    }
}