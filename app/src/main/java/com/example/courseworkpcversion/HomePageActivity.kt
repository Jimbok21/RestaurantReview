package com.example.courseworkpcversion

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.courseworkpcversion.firestore.FirestoreClass
import com.example.courseworkpcversion.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import android.util.Log
import android.widget.Toast
import com.example.courseworkpcversion.models.User
import com.example.courseworkpcversion.utils.GlideLoader
import kotlinx.coroutines.*
import java.io.IOException


//this class is the main page of the app that will show the reviews and allow the user to navigate
//to other parts of the app
class HomePageActivity : AppCompatActivity() {

    //the image on the device
    private var mSelectedImageFileUri: Uri? = null

    //the image on the cloud storage
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        FirestoreClass().getUserDetails(this)
        val sharedPreferences =
            getSharedPreferences(Constants.USER_PREFRENCES, Context.MODE_PRIVATE)
        val username =
            sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, getString(R.string.guest))!!
        var profilePic = sharedPreferences.getString(
            Constants.LOGGED_IN_USER_IMAGE,
            Constants.DEFAULT_PROFILE_PIC
        )!!

        //the program will crash if the user has not set an image without this if statement
        if(profilePic == "") {
            profilePic = Constants.DEFAULT_PROFILE_PIC
        }


        //sets the username text
        val userText: TextView = findViewById(R.id.username)
        userText.text = username


        //setting up the toolbar
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)

        //setting the bottom navigation bar to change the activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.map -> {
                    startActivity(Intent(this@HomePageActivity, MapsActivity::class.java))
                }
                R.id.restaurant -> {
                    startActivity(Intent(this@HomePageActivity, RestaurantsActivity::class.java))
                }
            }
            true
        }

        updateProfilePicture(profilePic)

    }

    fun updateProfilePicture(image: String) {
        //sets up the profile pic of the user
        var uriImage: Uri = Uri.parse(image)
        val profilePicIcon = findViewById<ImageView>(R.id.userIcon)
        /*var lemon = findViewById<TextView>(R.id.lemon)
        lemon.text = uriImage.toString()*/

        GlideLoader(this@HomePageActivity).loadUserPicture(uriImage, profilePicIcon)
    }
    fun logout() {
        //logs the user out and takes them back to the sign in page
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@HomePageActivity, LoginActivity::class.java))
        finish()
    }

    fun pickProfilePic(view: View) {
        ////checks if storage permission has been granted to access image files on the phone
        //will ask you for permission if it does not already have it
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            Constants.showImageChooser(this)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                Constants.READ_STORAGE_PERMISSION_CODE
            )
        }
    }

    fun button(view: View) {
        //saves the new profile picture
        if (mSelectedImageFileUri != null) {
            FirestoreClass().uploadImageToStorage(this, mSelectedImageFileUri)
            val snackSuccessLogin =
                Snackbar.make(
                    view,
                    getString(R.string.SuccessImageUpload),
                    Snackbar.LENGTH_LONG
                )
            snackSuccessLogin.view.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.ColourSnackbarSuccess
                )
            )
            snackSuccessLogin.show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //sets the profile picture to the file selected locally
        super.onActivityResult(requestCode, resultCode, data)
        val userIcon = findViewById<ImageView>(R.id.userIcon)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE)
                if (data != null) {
                    try {
                        mSelectedImageFileUri = data.data!!
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!, userIcon)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        val snackError =
                            Snackbar.make(
                                findViewById(android.R.id.content),
                                getString(R.string.imageSelectionFailed),
                                Snackbar.LENGTH_LONG
                            )
                        snackError.view.setBackgroundColor(
                            ContextCompat.getColor(
                                this,
                                R.color.ColourSnackbarError
                            )
                        )
                        snackError.show()
                    }
                }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //asks you for permission to read files
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                val snackError =
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        getString(R.string.permissionDenied),
                        Snackbar.LENGTH_LONG
                    )
                snackError.view.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ColourSnackbarError
                    )
                )
                snackError.show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //inflates the top toolbar
        menuInflater.inflate(R.menu.toolbar_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val myView = findViewById<View>(R.id.toolbar)
        when (item.itemId) {
            R.id.search -> {
                val snackbar = Snackbar.make(myView, getString(R.string.searching), Snackbar.LENGTH_LONG)
                snackbar.show()
                return true
            }
            R.id.logoutToolbar -> {
                logout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun imageUploadSuccess(imageURL: String) {
        //updates the profile pic when the image is uploaded
        mUserProfileImageURL = imageURL
        FirestoreClass().updateProfilePic(mUserProfileImageURL)
    }

    fun refresh(user: User) {
        Log.i("Username: ", user.username)
        Log.i("Email: ", user.email)
        Log.i("image", user.image)

        updateProfilePicture(user.image)
    }
}
