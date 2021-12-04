package com.example.courseworkpcversion.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.courseworkpcversion.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(imageUri: Uri, imageView: ImageView) {
        try {
            Glide.with(context).load(imageUri).centerCrop()
                .placeholder(R.drawable.default_profile_pic).into(imageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}