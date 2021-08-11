package feature.add

import android.location.Geocoder
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myhome.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import data.LOCATION
import kotlinx.android.synthetic.main.layout_map.*
import java.io.IOException
import java.util.*


class MyMapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMapLongClickListener {

    private var mMap: GoogleMap? = null
    internal var marker: Marker? = null

    //latLng tehran
    val lat = 35.690599
    val lng = 51.391692
    val ZOOM: Float = 11F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_map)

        if (serviceOk()) {
            //اگه مپمون موجوده و به مشکلی نخوردیم توسط لایه فرگمنت مپ رو نمایش بده
            // Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
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

    fun serviceOk(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val check = googleApiAvailability.isGooglePlayServicesAvailable(this)
        if (check == ConnectionResult.SUCCESS) return true
        else if (googleApiAvailability.isUserResolvableError(check))
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(this, "no service", Toast.LENGTH_SHORT).show()
        return false
    }

    fun initMap() {
        if (mMap == null) {
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
    }

    fun goToLocation(lat: Double, lng: Double, zoom: Float) {
        // Add a marker and move the camera
        val latLang = LatLng(lat, lng)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLang, zoom))

    }

    fun searchLocation() {
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
            //رفتن به یک موقعیت خاص
            goToLocation(lat, lng, ZOOM)

            btn_search_location.setOnClickListener {
                searchLocation()
            }
        }

        mMap!!.setOnMapLongClickListener(this)
    }

    //با نگه داشتن روی نقشه لوکیشن آنجا قرار بگیرد و آدرس آنجا را به ما بدهد
    override fun onMapLongClick(latLng: LatLng) {
        if (marker == null) {
            //اگه کاربر هیچ لوکیشنی انتخاب نکرده باشه
            marker = mMap!!.addMarker(
                MarkerOptions().position(latLng).title("user destination")
            )
        } else {
            //مارکری که انتخاب شده روی لوکیشن جدید قرار بگیره
            marker!!.position = latLng
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