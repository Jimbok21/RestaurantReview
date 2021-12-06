package com.example.courseworkpcversion

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */


        //gets the restaurant names
        val restaurantsList = RestaurantsActivity().readRestaurantNames()
        //sets the restaurant locations
        val bouchonDeRossi = LatLng(51.6189255, -3.9488759)
        val newIchiban = LatLng(51.6239331, -3.9409216)
        val nandos = LatLng(51.6191971, -3.9398177)




        //adds the locations to the map with markers
        val restaurantLocations = arrayOf(bouchonDeRossi, newIchiban, nandos)
        for (i in restaurantsList.indices) {
            googleMap.addMarker(MarkerOptions().position(restaurantLocations[i]).title(restaurantsList[i]))
        }

        //sets the map start point to swansea and zooms in
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLocations[2], 15F))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)



    }
}