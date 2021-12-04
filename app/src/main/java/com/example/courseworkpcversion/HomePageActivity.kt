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
import java.io.IOException

//this class is the main page of the app that will show the reviews and allow the user to navigate
//to other parts of the app
class HomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        val sharedPreferences = getSharedPreferences(Constants.USER_PREFRENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, getString(R.string.guest))!!

        val lemon: TextView = findViewById(R.id.lemon)
        lemon.text = username


        //setting up the toolbar
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)

        //setting the bottom navigation bar to change the activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.map -> {
                    startActivity(Intent(this@HomePageActivity, MapsActivity::class.java))
                }
                R.id.restaurant -> {
                    startActivity(Intent(this@HomePageActivity, RestaurantsActivity::class.java))
                }
            }
            true
        }

    }

    fun logout(view: View) {
        //logs the user out and takes them back to the sign in page
        FirebaseAuth.getInstance().signOut()

        startActivity(Intent(this@HomePageActivity, LoginActivity::class.java))
        finish()
    }

    fun button(view: View) {
        ////checks if storage permission has been granted to access image files on the phone
        //will ask you for permission if it does not already have it
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED)
        {
            Constants.showImageChooser(this)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            Constants.READ_STORAGE_PERMISSION_CODE)
        }

        //FirestoreClass().uploadImageToStorage(this, mSelectedImageFileUri)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val userIcon = findViewById<ImageView>(R.id.userIcon)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE)
                if (data != null) {
                    try {
                        val selectedImageFileUri = data.data!!

                        userIcon.setImageURI(selectedImageFileUri)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        val snackError =
                            Snackbar.make(findViewById(android.R.id.content), getString(R.string.imageSelectionFailed), Snackbar.LENGTH_LONG)
                        snackError.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
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
                    Snackbar.make(findViewById(android.R.id.content), getString(R.string.permissionDenied), Snackbar.LENGTH_LONG)
                snackError.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
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
            R.id.logoutBtn -> {
                logout(myView)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}