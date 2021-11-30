package com.example.courseworkpcversion.firestore

import android.app.Activity
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.courseworkpcversion.LoginActivity
import com.example.courseworkpcversion.R
import com.example.courseworkpcversion.UserRegistrationActivity
import com.example.courseworkpcversion.models.User
import com.example.courseworkpcversion.utils.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: UserRegistrationActivity, userInfo: User) {

        //adds the user data to the firebase database
        mFireStore.collection(Constants.USERS).document(userInfo.id)
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisterSuccess()
            }
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

            }
    }
}