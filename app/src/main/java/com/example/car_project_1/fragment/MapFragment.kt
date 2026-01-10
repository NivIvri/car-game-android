package com.example.car_project_1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.car_project_1.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class FragmentMap : Fragment(), OnMapReadyCallback {
    private var startLat: Double? = null
    private var startLon: Double? = null
    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        val lat = startLat ?: 32.0853
        val lon = startLon ?: 34.7818

        val pos = LatLng(lat, lon)
        googleMap.addMarker(MarkerOptions().position(pos))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14f))
    }


    fun moveToLocation(lat: Double, lon: Double) {
        startLat = lat
        startLon = lon

        if (::googleMap.isInitialized) {
            val pos = LatLng(lat, lon)
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(pos))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 14f))
        }
    }


}