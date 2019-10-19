package com.example.googlemaps

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener{
    private lateinit var locList:LocationListener
    override fun onProviderDisabled(p0: String?) {


    }

    override fun onProviderEnabled(p0: String?) {
        Log.i("Enabled","Location Found")
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

        Log.i("Status Changed","Location was changes")

    }

    override fun onLocationChanged(p0: Location) {
        val loc=LatLng(p0.latitude,p0.longitude)
        mMap.addMarker(MarkerOptions().position(loc).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc))
        if (mMap != null)
            mMap.addMarker(MarkerOptions().position(loc).title("Marker in Sydney"))
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(p0.latitude, p0.longitude),22f))
    }


    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locList=this

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), 1234
            )
        } else {
            startLocationUpdates()
        }
    }
    lateinit var locMan :LocationManager
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(){
      //  getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locMan=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0f,locList)
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
        mMap.uiSettings.apply {
            isZoomControlsEnabled=true
            isCompassEnabled=true
            isMyLocationButtonEnabled=true
        }
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.addPolyline(
            PolylineOptions()
                .add(sydney,LatLng(20.59,78.39))
                .color(ContextCompat.getColor(baseContext,R.color.colorPrimary))
        )
            .width=2f
    }

    override fun onDestroy() {
        super.onDestroy()
        locMan.removeUpdates(this)
    }
}
