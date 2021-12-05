package com.example.courseworkpcversion.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.courseworkpcversion.*
import com.example.courseworkpcversion.models.User
import com.example.courseworkpcversion.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: UserRegistrationActivity, userInfo: User) {

        //adds the user data to the firebase database
        mFireStore.collection(Constants.USERS).document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Registering the user failed", e)
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser


        var currentUserID = ""
        if(currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        //passes the collection name and document name to get the data on the user
        mFireStore.collection(Constants.USERS).document(getCurrentUserID()).get()
            .addOnSuccessListener { document ->
                //logs the details
                Log.i(activity.javaClass.simpleName, document.toString())

                //puts the data into a user
                val user = document.toObject(User::class.java)!!

                val sharedPrefrences = activity.getSharedPreferences(
                    Constants.USER_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                //stores the data on the device
                val editor: SharedPreferences.Editor = sharedPrefrences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.username}"
                )
                editor.apply()

                editor.putString(
                    Constants.LOGGED_IN_USER_IMAGE,
                    "${user.image}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is UserRegistrationActivity -> {
                        activity.userRegisterSuccess(user)
                    }
                    is HomePageActivity -> {
                        activity.refresh(user)
                    }
                }
            }
    }

    fun uploadImageToStorage(activity: Activity, imageFileURI: Uri?) {
        var namePrefix = ""
        //setting prefix
        when(activity) {
            is HomePageActivity -> {
                namePrefix = Constants.USER_PROFILE_IMAGE
            }
            is WriteReviewActivity -> {
                namePrefix = Constants.REVIEW_IMAGE
            }
        }
        //adds the image to the storage with the correct prefix based on what the image is used for
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            namePrefix + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity, imageFileURI)
        )

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            Log.e("Firebase Image URL",
            taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            //gets the image url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e("Downloadable Image URL", uri.toString())
                when(activity) {
                    is HomePageActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                    is WriteReviewActivity -> {
                        activity.reviewImageUploadSuccess(uri.toString())
                    }
                }
            }

        }.addOnFailureListener { exception ->
            Log.e(activity.javaClass.simpleName, exception.message, exception)
        }
    }

    fun updateProfilePic(imageURL: String) {
        val userRef = mFireStore.collection(Constants.USERS).document(getCurrentUserID())
        userRef.update(Constants.IMAGE, imageURL).addOnSuccessListener {
            Log.d("Update Profile Pic", "pfp successfully updated")
        }.addOnFailureListener { e ->
            Log.w("Update Profile Pic", "pfp NOT successfully updated")
        }
    }

}