package com.example.courseworkpcversion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.courseworkpcversion.databinding.ActivityMapsBinding

class MapsActivity2 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //gets the restaurant names
        val restaurantsList = RestaurantsActivity().readRestaurantNames()
        //gets the restaurant locations
        val bouchonDeRossi = LatLng(51.6189255, -3.9488759)
        val newIchiban = LatLng(51.6239331, -3.9409216)
        val nandos = LatLng(51.6191971, -3.9398177)

        //sets the map start point to swansea (nandos)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(nandos))


        //adds the locations to the map with markers
        val restuarantLocations = arrayOf(bouchonDeRossi, newIchiban, nandos)
        for (i in restaurantsList.indices) {
            mMap.addMarker(MarkerOptions().position(restuarantLocations[i]).title(restaurantsList[i]))
        }

    }
}