package com.example.courseworkpcversion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.courseworkpcversion.models.Review
import com.example.courseworkpcversion.utils.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class RestaurantsActivity: AppCompatActivity() {
    private var restaurant: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewArrayList : ArrayList<Review>
    private lateinit var myAdapter: MyAdapter
    private lateinit var reviewDb : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.restaurants)
        val spinner = findViewById<Spinner>(R.id.dropdownSpinner)

        val restaurantsList = readRestaurantNames()

        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, restaurantsList)

        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                restaurant = restaurantsList[position]
                eventChangeListener()
                reviewArrayList.clear()
                recyclerView?.adapter?.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //setting the bottom navigation bar to change the activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        //because I am using activities and not fragments, the naviagtion bar
        //resets to home unless I have this code to stop it
        val currentActivity: View = bottomNav.findViewById(R.id.restaurant)
        currentActivity.performClick()

        bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.map -> {
                    startActivity(Intent(this@RestaurantsActivity, MapsActivity::class.java))
                }
                R.id.home -> {
                    startActivity(Intent(this@RestaurantsActivity, HomePageActivity::class.java))
                }
            }
            true
        }
        //setting up the toolbar
        val mToolbar = findViewById<Toolbar>(R.id.toolbarRestaurant)
        setSupportActionBar(mToolbar)

        //setting up the recycler view of reviews
        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(false)

        reviewArrayList = arrayListOf()

        myAdapter = MyAdapter(reviewArrayList)

        recyclerView.adapter = myAdapter

        eventChangeListener()
    }

    private fun eventChangeListener() {
        //gets the data of the reviews that the user made and sends them to the adapter
        reviewDb = FirebaseFirestore.getInstance()
        reviewDb.collection(Constants.REVIEWS).whereEqualTo(Constants.RESTAURANT_NAME, restaurant)
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

    fun readRestaurantNames(): Array<String> {
        val restaurantsListTemp = arrayOf(Constants.BOUCHON_DE_ROSSI, Constants.NEW_ICHIBAN, Constants.NANDOS)
        var i: Int = 0
        val db = FirebaseFirestore.getInstance()
        db.collection(Constants.RESTAURANTS).get().addOnCompleteListener { task ->
            if(task.isSuccessful) {

                for (document in task.result!!) {
                    val name = document.data[Constants.NAME].toString()
                    restaurantsListTemp[i] = name
                    i++
                }
            }
        }

        return restaurantsListTemp
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)
        return true
    }

    fun logout() {
        //logs the user out and takes them back to the sign in page
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this@RestaurantsActivity, LoginActivity::class.java))
        finish()
    }

    //implements the toolbar
    override fun onOptionsItemSelected(itemRestaurant: MenuItem): Boolean {
        val myView = findViewById<View>(R.id.toolbarRestaurant)
        when (itemRestaurant.itemId) {
            //clicking search icon just makes a snackbar saying searching
            R.id.search -> {
                val snackbar = Snackbar.make(myView, "searching", Snackbar.LENGTH_LONG)
                snackbar.show()
                return true
            }
            //clicking logout logs the user out and takes them to login page
            R.id.logoutToolbar -> {
                logout()
            }

        }
        return super.onOptionsItemSelected(itemRestaurant)
    }
}