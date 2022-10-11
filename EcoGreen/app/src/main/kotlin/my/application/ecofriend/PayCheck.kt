package my.application.ecofriend

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_paycheck.*
import my.application.ecofriend.datas.DetailItem
import java.util.*


class PayCheck :AppCompatActivity() {
    val auth = FirebaseAuth.getInstance()
    private var phoneNum:String = ""
    val database = Firebase.database
    val myRef = database.getReference("regtb")

    private lateinit var progressDialog: AppCompatDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paycheck)

        //date
        date.text = intent.getStringExtra("requestDate")

        //priceTotal
        deposit.text = intent.getStringExtra("priceTotal")

        setupListener()

    }

    private fun setupListener() {
        main.setOnClickListener {
            startActivity(Intent(this@PayCheck, MainHomeActivity::class.java))
        }
        complete_detail.setOnClickListener {
            progressON()
            //startActivity(Intent(this@PayCheck, ApplicationDetails::class.java))
        }
    }

    fun GetInfoDB(){
        phoneNum = auth.getCurrentUser()?.getPhoneNumber().toString()
        phoneNum = "0" + phoneNum.substring(3)
        val myIntent = Intent(this, ApplicationDetails::class.java)
        var mdetailList = ArrayList<DetailItem>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if(data.child("phone").value as String? == phoneNum){
                        mdetailList.add(
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
                myIntent.putExtra("mDetailList", mdetailList)
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