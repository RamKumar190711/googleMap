package com.toqsoft.googlemap.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.toqsoft.googlemap.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import android.widget.Toast
import androidx.annotation.RequiresPermission
import com.google.firebase.database.*
import com.google.android.gms.location.*
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory

import com.toqsoft.googlemap.service.LocationUpdatesService

class MapFragment : Fragment(), OnMapReadyCallback {

    private val LOCATION_PERMISSION_REQUEST = 1
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    // Firebase references
    private lateinit var database: FirebaseDatabase
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = childFragmentManager.findFragmentById(R.id.map_container)
        if (fragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            childFragmentManager.beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit()
        } else {
            mapFragment = fragment as SupportMapFragment
        }

        mapFragment.getMapAsync(this)

        FirebaseApp.initializeApp(requireContext())
        FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        // Initialize Firebase database reference for "user1"
        database = FirebaseDatabase.getInstance()
        ref = database.getReference("users/user1/location")
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        enableMyLocation()
        listenToFirebaseLocationUpdates()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            googleMap.isMyLocationEnabled = true
            startLocationService()
        }
    }

    private fun listenToFirebaseLocationUpdates() {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lat = snapshot.child("lat").getValue(Double::class.java)
                val lng = snapshot.child("lng").getValue(Double::class.java)

                if (lat != null && lng != null && ::googleMap.isInitialized) {
                    val user1Location = LatLng(lat, lng)
                    googleMap.clear()
                    googleMap.addMarker(
                        MarkerOptions().position(user1Location).title("Mobile 1 Location")
                    )
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user1Location, 15f))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load location", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startLocationService() {
        val intent = Intent(requireContext(), LocationUpdatesService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            startLocationService()  // Start the foreground service here
        } else {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
