package com.example.courseworkpcversion

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
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
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))



        //gets the restaurant names
        val restaurantsList = RestaurantsActivity().readRestaurantNames()
        //gets the restaurant locations
        val restaurantLongLats = RestaurantsActivity().ReadRestaurantLongLat(0)
        val bouchonDeRossi = LatLng(restaurantLongLats[1], restaurantLongLats[0])
        val newIchiban = LatLng(restaurantLongLats[3], restaurantLongLats[2])
        val nandos = LatLng(restaurantLongLats[5], restaurantLongLats[4])

        for (i in 0..5) {
            Log.e("longlat list :", restaurantLongLats[i].toString())
        }
        /*Log.e("bouchonDeRossi Lat", restaurantLongLats[1].toString())
        Log.e("bouchonDeRossi long", restaurantLongLats[0].toString())*/

        //sets the map start point to swansea (nandos)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(nandos))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15F))


        //adds the locations to the map with markers
        val restaurantLocations = arrayOf(bouchonDeRossi, newIchiban, nandos)
        for (i in restaurantsList.indices) {
            googleMap.addMarker(MarkerOptions().position(restaurantLocations[i]).title(restaurantsList[i]))
        }
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