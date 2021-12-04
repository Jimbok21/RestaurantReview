package com.example.courseworkpcversion.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.net.URI

object Constants {
    const val USERS: String = "users"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val LOGGED_IN_USER_IMAGE: String = "logged_in_profile_pic"
    const val DEFAULT_PROFILE_PIC: String = "https://firebasestorage.googleapis.com/v0/b/restaurantreviewer-12cf2.appspot.com/o/default_profile_pic.jpg?alt=media&token=f92aa759-5e4c-4fe9-a282-4754bda5cb1e"
    const val USER_PREFRENCES: String = "UserPrefs"
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val IMAGE: String = "image"


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