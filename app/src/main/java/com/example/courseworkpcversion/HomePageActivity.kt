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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.courseworkpcversion.models.Review
import com.example.courseworkpcversion.models.User
import com.example.courseworkpcversion.utils.GlideLoader
import com.google.firebase.firestore.*
import kotlinx.coroutines.*
import java.io.IOException


//this class is the main page of the app that will show the reviews and allow the user to navigate
//to other parts of the app
class HomePageActivity : AppCompatActivity() {

    //the image on the device
    private var mSelectedImageFileUri: Uri? = null

    //the image on the cloud storage
    private var mUserProfileImageURL: String = ""

    //initialize the lateinit variables
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewArrayList : ArrayList<Review>
    private lateinit var myAdapter: MyAdapter
    private lateinit var reviewDb : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        //setting up the recycler view of reviews
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)

        reviewArrayList = arrayListOf()

        myAdapter = MyAdapter(reviewArrayList)

        recyclerView.adapter = myAdapter

        eventChangeListener()

        //puts the user details into a shared prefrences so they can be refrenced
        FirestoreClass().getUserDetails(this)
        val sharedPreferences =
            getSharedPreferences(Constants.USER_PREFERENCES, Context.MODE_PRIVATE)
        val username =
            sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, getString(R.string.guest))!!
        var profilePic = sharedPreferences.getString(
            Constants.LOGGED_IN_USER_IMAGE,
            Constants.DEFAULT_PROFILE_PIC
        )!!

        //sets the profile pic to a default one if the user has not set one already
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

        //adding functionality of bottom navigation bar
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
        //loads the profile picture again
        updateProfilePicture(profilePic)

    }

    private fun eventChangeListener() {

        //gets the data of the reviews that the user made and sends them to the adapter
        reviewDb = FirebaseFirestore.getInstance()
        reviewDb.collection(Constants.REVIEWS).whereEqualTo(Constants.USER_ID, FirestoreClass().getCurrentUserID())
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {

                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!) {

                        if(dc.type == DocumentChange.Type.ADDED) {
                            reviewArrayList.add(dc.document.toObject(Review::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }

    fun makeNewReview(view: View) {
        //stops the user from going to the WriteReviewActivity if they are not signed in
        if(FirestoreClass().getCurrentUserID() == Constants.GUEST_ID) {
            guestError(view)
        } else {
            startActivity(Intent(this@HomePageActivity, WriteReviewActivity::class.java))
        }
    }

    fun updateProfilePicture(image: String) {
        //sets up the profile pic of the user
        var uriImage: Uri = Uri.parse(image)
        val profilePicIcon = findViewById<ImageView>(R.id.userIcon)

        GlideLoader(this@HomePageActivity).loadUserPicture(uriImage, profilePicIcon)
    }
    fun logout() {
        //logs the user out and takes them back to the sign in page
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@HomePageActivity, LoginActivity::class.java))
        finish()
    }

    fun guestError(view: View) {
        //tells the user to sign in so they can use this feature
        val snackGuestError =
            Snackbar.make(view, getString(R.string.SignInToAccess), Snackbar.LENGTH_LONG)
        snackGuestError.view.setBackgroundColor(ContextCompat.getColor(this, R.color.ColourSnackbarError))
        snackGuestError.show()
    }

    fun pickProfilePic(view: View) {
        //disables this feature if a guest is logged in
        if(FirestoreClass().getCurrentUserID() == Constants.GUEST_ID) {
            guestError(view)
        } else {

            //checks if storage permission has been granted to access image files on the phone
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
    }

    fun button(view: View) {
        if(FirestoreClass().getCurrentUserID() == Constants.GUEST_ID) {
            guestError(view)
        } else {
            //saves the new profile picture
            if (mSelectedImageFileUri != null) {
                FirestoreClass().uploadImageToStorage(this, mSelectedImageFileUri)
                val snackSuccessProfilePicUpdate =
                    Snackbar.make(
                        view,
                        getString(R.string.SuccessImageUpload),
                        Snackbar.LENGTH_LONG
                    )
                snackSuccessProfilePicUpdate.view.setBackgroundColor(
                    ContextCompat.getColor(
                        this,
                        R.color.ColourSnackbarSuccess
                    )
                )
                snackSuccessProfilePicUpdate.show()
            }
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
        //sets up the menuItem
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
        //called when getUserDetails is used so it updates the values and profile pic
        Log.i("Username: ", user.username)
        Log.i("Email: ", user.email)
        Log.i("image", user.image)

        updateProfilePicture(user.image)
    }
}
