package com.example.myhome.feature.add

import android.location.Geocoder
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.myhome.R
import com.example.myhome.common.Constants.LOCATION
import com.example.myhome.common.showMessage
import com.example.myhome.databinding.ActivityMapBinding
import com.example.myhome.databinding.LayoutMapBinding
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.*


class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener {
    private var mMap: GoogleMap? = null
    private lateinit var binding: LayoutMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMapBinding.inflate(layoutInflater).root)

        if (serviceOk()) {
            //show map in layout
            binding = LayoutMapBinding.inflate(layoutInflater)
            setContentView(binding.root)
            initMap()
        } else {
            showMessage("سرویس مپ در دسترس نیست اتصال خود را بررسی کنید!")
            // finish()
        }

    }

    private fun serviceOk(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val check = googleApiAvailability.isGooglePlayServicesAvailable(this)
        when {
            check == ConnectionResult.SUCCESS -> return true
            googleApiAvailability.isUserResolvableError(check) -> showMessage("error")
            else -> showMessage("no service")
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
            showMessage("pls enter location")
        } else {

            Thread {
                try {
                    val gCoder = Geocoder(this, Locale.getDefault())

                    val addressList = gCoder.getFromLocationName(location, 1)
                    if (addressList.size == 0)
                        showMessage("آدرس پیدا نشد")
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
            // showMessage("INIT")
            //Go to Tehran location
            val lat = 35.690599
            val lng = 51.391692
            goToLocation(lat, lng, 11F)

            binding.btnSearchLocation.setOnClickListener {
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
            binding.tvMapsAddress.text = address.toString()
            binding.btnDoneLocation.setOnClickListener {
                LOCATION = address.toString()
                //send get address
                finish()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}