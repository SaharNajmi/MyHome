package com.example.myhome.feature.add

import android.location.Geocoder
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myhome.R
import com.example.myhome.common.Constants.LOCATION
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.layout_map.*
import java.io.IOException
import java.util.*


class MyMapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener {
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_map)

        if (serviceOk()) {
            //show map in layout
            setContentView(R.layout.layout_map)
            initMap()
        } else {
            Toast.makeText(
                this,
                "سرویس مپ در دسترس نیست اتصال خود را بررسی کنید!",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

    }

    private fun serviceOk(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val check = googleApiAvailability.isGooglePlayServicesAvailable(this)
        when {
            check == ConnectionResult.SUCCESS -> return true
            googleApiAvailability.isUserResolvableError(check) -> Toast.makeText(
                this,
                "error",
                Toast.LENGTH_SHORT
            ).show()
            else -> Toast.makeText(this, "no service", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    private fun initMap() {
        if (mMap == null) {
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    private fun goToLocation(lat: Double, lng: Double, zoom: Float) {
        // Add a marker and move the camera
        val latLang = LatLng(lat, lng)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))

    }

    private fun searchLocation() {
        if (!isMapReady)
            return
        val locationSearch: EditText = findViewById(R.id.et_search)
        val location = locationSearch.text.toString().trim()

        if (location == null || location == "") {
            Toast.makeText(this, "pls enter location", Toast.LENGTH_SHORT).show()
        } else {

            Thread {
                try {
                    val gCoder = Geocoder(this, Locale.getDefault())

                    val addressList = gCoder.getFromLocationName(location, 1)
                    if (addressList.size == 0)
                        Toast.makeText(this, "آدرس پیدا نشد", Toast.LENGTH_SHORT).show()
                    else {
                        val address = addressList!![0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        // mMap!!.addMarker(MarkerOptions().position(latLng).title(location))
                        runOnUiThread {
                            mMap!!.animateCamera(
                                CameraUpdateFactory.newLatLng(
                                    latLng
                                )
                            )
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }.start()


        }
    }

    var isMapReady = false;

    override fun onMapReady(googleMap: GoogleMap) {
        isMapReady = true;
        mMap = googleMap
        if (mMap != null) {
            Toast.makeText(this, "INIT", Toast.LENGTH_SHORT).show()
            //Go to Tehran location
            val lat = 35.690599
            val lng = 51.391692
            goToLocation(lat, lng, 11F)

            btn_search_location.setOnClickListener {
                searchLocation()
            }
        }

        mMap!!.setOnMapLongClickListener(this)
    }

    override fun onMapLongClick(latLng: LatLng) {
        var marker: Marker? = null

        if (marker == null) {
            //No locations selected
            marker = mMap!!.addMarker(
                MarkerOptions().position(latLng).title("user destination")
            )
        } else {
            //new location
            marker.position = latLng
        }

        mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng), 300, null)

        //get Address When MapLongClick
        val geocoder: Geocoder = Geocoder(this, Locale("fa", "IR"))
        //  val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val allAddresses = geocoder.getFromLocation(
                latLng.latitude, latLng.longitude, 1
            )

            //show address in textView
            val address = allAddresses[0].getAddressLine(0)
            tv_maps_address.text = address.toString()
            LOCATION = address.toString()
            btn_done_location.setOnClickListener {
                //send get address
                finish()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}