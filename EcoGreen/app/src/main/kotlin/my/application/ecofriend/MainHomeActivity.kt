package my.application.ecofriend

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_reuse.*
import kotlinx.android.synthetic.main.main_home.*
import kotlinx.android.synthetic.main.main_home.change_addr
import kotlinx.android.synthetic.main.select_service_dialog.*
import my.application.ecofriend.datas.DetailItem
import my.application.ecofriend.datas.MyData
import java.io.IOException
import java.util.*

class MainHomeActivity : AppCompatActivity(){
    var locationManager : LocationManager? = null
    private val LOCATION_PERMISSION_REQUEST = 1005
    var currentLocation : String = ""
    var latitude : Double? = null
    var longitude : Double? = null
    private var setupChooser: SetUpChooser? = null

    val auth = FirebaseAuth.getInstance()
    private var phoneNum:String = ""
    val database = Firebase.database
    val myRef = database.getReference("regtb")

    private lateinit var progressDialog: AppCompatDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_home)

        getCurrentLoc()

        setupListener()

        reuse_waste.setOnClickListener {
            val myIntent = Intent(this, ReuseWasteActivity::class.java)
            myIntent.putExtra("gps", currentLocation)
            startActivity(myIntent)
        }
        disuse_waste.setOnClickListener {
            val myIntent = Intent(this, DisuseWasteActivity::class.java)
            startActivity(myIntent)
        }
        setting.setOnClickListener {
            setupChooser = SetUpChooser().apply {
                addNotifier(object : SetUpChooser.SetUpChooserNotifierInterface{
                    override fun applicationOnClick() {
                        progressON()
                        //startActivity(Intent(this@MainHomeActivity, ApplicationDetails::class.java))
                        setupChooser?.dismiss()
                    }

                    override fun deleteUserOnClick() {
                        startActivity(Intent(this@MainHomeActivity, AccountingSettingActivity::class.java))
                        setupChooser?.dismiss()
                    }
                })
            }
            setupChooser!!.show(supportFragmentManager, "")
        }
    }

    private fun setupListener() {
        var region = resources.getStringArray(R.array.seoul_region)
        var str = MyData.prefs.getString("gps", "no gps")
        Log.d("str", str)

        region.set(0, currentLocation)

        change_addr.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, region)

        if(str != currentLocation && str != "no gps"){
            for(i in region.size -1 downTo 1){
                if(region[i] == str){
                    change_addr.setSelection(i)
                }
            }
        }
        else{
            MyData.prefs.setString("gps", currentLocation)
            change_addr.setSelection(0)
        }

        change_addr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (position != 0) {
                    //addr.text = region[position]
                    //addr.text = "변경 위치: "
                    MyData.prefs.setString("gps", "${region[position]}")
                    Log.d("region", region[position])
                }
                else{
                    MyData.prefs.setString("gps", currentLocation)
                }
            }
        }
    }

    private fun getCurrentLoc(){
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var userLocation: Location? = getLatLng()
        if(userLocation != null){
            latitude = userLocation.latitude
            longitude = userLocation.longitude
            Log.d("CheckCurrentLocation", "현재 내 위치 값 : $latitude, $longitude")

            var mGeocoder = Geocoder(applicationContext, Locale.KOREAN)
            var mResultList: List<Address>? = null
            try{
                mResultList = mGeocoder.getFromLocation(
                    latitude!!, longitude!!, 1
                )
            } catch (e: IOException){
                e.printStackTrace()
            }
            if(mResultList != null){
                Log.d("CheckCurrentLocation", mResultList[0].getAddressLine(0))
                currentLocation = mResultList[0].getAddressLine(0)
                currentLocation = currentLocation.substring(5)
//                currentLocation = currentLocation.substring(11)
            }
        }
    }

    private fun checkGpsPermission(){
        if(PermissionUtil().requestPermission(
                this,
                LOCATION_PERMISSION_REQUEST,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ){
            return
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLatLng() : Location? {
        var currentLatLng: Location? = null
        val locationProvider = LocationManager.GPS_PROVIDER
        checkGpsPermission()
        currentLatLng = locationManager?.getLastKnownLocation(locationProvider)

        if(currentLatLng == null){
            currentLatLng = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i("NETWORK_PROVIDER", "NETWORK_PROVIDER");
        }else{
            Log.i("GPS_PROVIDER", "GPS_PROVIDER");
        }

        return  currentLatLng
    }

    fun GetInfoDB(){
        phoneNum = auth.getCurrentUser()?.getPhoneNumber().toString()
        phoneNum = "0" + phoneNum.substring(3)
        val myIntent = Intent(this, ApplicationDetails::class.java)
        var mDetail = ArrayList<DetailItem>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if(data.child("phone").value as String? == phoneNum){
                        mDetail.add(
                            DetailItem(
                                data.child("date").value as String?,
                                data.child("selectdate").value as String?,
                                data.child("address").value as String?,
                                data.child("classification0").value as String?,
                                data.child("item0").value as String?,
                                data.child("fee").value as Long?,
                                data.child("state").value as Long?
                            ))
                    }
                }
                myIntent.putExtra("mDetailList", mDetail)

                startActivity(myIntent)
                progressOFF()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun progressON(){
        progressDialog = AppCompatDialog(this)
        progressDialog.setCancelable(true)
        progressDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.progress_dbwait)
        progressDialog.show()
        var img_loading_framge = progressDialog.findViewById<ImageView>(R.id.iv_frame_loading)
        var frameAnimation = img_loading_framge?.getBackground() as AnimationDrawable
        img_loading_framge.post(object : Runnable {
            override fun run() {
                frameAnimation.start()
                GetInfoDB()
            }
        })
    }

    fun progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss()
        }
    }

}