package com.example.courseworkpcversion.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.net.URI

object Constants {
    const val USERS: String = "users"
    const val REVIEWS: String = "reviews"
    const val RESTAURANTS: String = "restaurants"
    const val NAME: String = "name"
    const val LONGITUDE: String = "longitude"
    const val LATITUDE: String = "latitude"
    const val BOUCHON_DE_ROSSI: String = "Bouchon De Rossi"
    const val NEW_ICHIBAN: String = "New Ichiban"
    const val NANDOS: String = "Nandos"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val LOGGED_IN_USER_IMAGE: String = "logged_in_profile_pic"
    const val DEFAULT_PROFILE_PIC: String = "https://firebasestorage.googleapis.com/v0/b/restaurantreviewer-12cf2.appspot.com/o/default_profile_pic.jpg?alt=media&token=f92aa759-5e4c-4fe9-a282-4754bda5cb1e"
    const val USER_PREFERENCES: String = "UserPrefs"
    const val USER_ID: String = "userId"
    const val RESTAURANT_NAME: String = "restaurantName"
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val REVIEW_IMAGE: String = "Review_Image"
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val IMAGE: String = "image"
    const val GUEST_ID: String = "wMZdjPOzVXdZmPAaJUneI0Tw5462"


    fun showImageChooser(activity: Activity) {
        //opens the phones gallery to pick a profile picture
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {

        //maps the mime type to file extension with a mime map
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}