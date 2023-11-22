package com.example.co_opt3final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.util.Locale

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mGoogleMap:GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    // Sets default starting location
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap

        // Marker will appear at these coordinates when app launches
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("welcome to sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        setMapLongClick(googleMap)
        setPoiClick(googleMap)
    }

    // Marker appears after pressing and holding on map
    private fun setMapLongClick(map: GoogleMap) {
        // Notices any long clicks
        map.setOnMapLongClickListener {
            // Retrieves latitude and longitude
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Lng: %2$.5f",
                it.latitude,
                it.longitude
            )

            // Adds a marker after long click
            map.addMarker(MarkerOptions().position(it).title("Marker here").snippet(snippet))

            // Adds location into firebase
            val database = Firebase.database("https://co-opt3-bc1f4-default-rtdb.firebaseio.com/")
            val reference = database.reference
            val data = reference.push().child("location").setValue(it)
        }
    }

    // Marker appears when clicking on an existing point of interest
    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { point ->
            val poiMarker = map.addMarker(MarkerOptions().position(point.latLng).title(point.name))
            if (poiMarker != null) {
                poiMarker.showInfoWindow()
            }
        }
    }
}