package com.example.courseworkpcversion

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.courseworkpcversion.models.Review

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
        var floatRating = review.rating
        if (floatRating == null) {
            floatRating = 0F
        }
        val intRating = floatRating.toInt()



        holder.imgView.setImageResource(R.drawable.ic_password)
        holder.txtMsg.text = review.reviewText
        holder.reviewBar.rating

        holder.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, SelectReviewActivity::class.java)
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
        var reviewBar = itemView.findViewById<View>(R.id.ratingbar) as RatingBar

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }
    }
}




//var intent = Intent(itemView.context, TeamDetail::class.java)
//itemView.context.startActivity(intent)