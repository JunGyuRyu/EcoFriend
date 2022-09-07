package my.application.ecogreen

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_preview_pdf.*
import my.application.ecogreen.adapters.PdfAdapter
import my.application.ecogreen.datas.MyData
import my.application.ecogreen.datas.PdfItem
import java.util.*
import kotlin.collections.ArrayList

class PreviewPdf : dbBaseActivity() {
    var mFinalItemList = ArrayList<PdfItem>()

    lateinit var mListAdapter: PdfAdapter

    var dateSetting: Int? = 0
    var selectedDate: String? = null
    var todayDate: String = ""
    var phoneNum:String = ""
    var total: Long = 0
    var addressFinal: String? = null

    val auth = FirebaseAuth.getInstance()
    val database = Firebase.database
    val myRef = database.getReference("regtb")

    val detailDB = detailDB(this, "DetailList.db", null, 1)
    val dbHelper = DBHelper(this, "ItemList.db", null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_pdf)

        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        button.setOnClickListener {
            addressFinal = address.text.toString() + detial_addr.text.toString()
            System.out.println(addressFinal)

            var key :String = todayDate
            key = key.replace(".","")
            key = key.replace(" ","_")

            if (dateSetting != 0) {
                myRef.child(key).child("phone").setValue(phoneNum)
                myRef.child(key).child("address").setValue(addressFinal)
                myRef.child(key).child("date").setValue(todayDate)
                myRef.child(key).child("selectdate").setValue(selectedDate)
                myRef.child(key).child("fee").setValue(total)
                myRef.child(key).child("state").setValue(1)

                for (i in mFinalItemList.size - 1 downTo 0) {
                    myRef.child(key).child("classification${i}").setValue(mFinalItemList[i].classification)
                    myRef.child(key).child("item${i}").setValue(mFinalItemList[i].item)
                    myRef.child(key).child("standard${i}").setValue(mFinalItemList[i].standard)
                    myRef.child(key).child("levy_amt${i}").setValue(mFinalItemList[i].levy_amt)
                    myRef.child(key).child("count${i}").setValue(mFinalItemList[i].count)

                    detailDB.insert(todayDate,
                        selectedDate,
                        mFinalItemList[i].classification,
                        mFinalItemList[i].item,
                        mFinalItemList[i].standard,
                        mFinalItemList[i].levy_amt,
                        mFinalItemList[i].count,
                        total, 1)
                }
                dbHelper.deleteAll()

                val myIntent = Intent(mContext, PayCheck::class.java)
                myIntent.putExtra("requestDate", todayDate)
                myIntent.putExtra("priceTotal", priceTotal.text)
//                myIntent.putExtra("selectedDate", selectedDate)
//                myIntent.putExtra("addressFinal", addressFinal)

                startActivity(myIntent)
            } else {
                Toast.makeText(this, "배출예정일을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setValues() {
        //date
        todayDate = intent.getStringExtra("date").toString()
        today.text = todayDate.substring(0, 11)

        //mPersonalData = detailDB.getData(todayDate)

        //price
        total = intent.getStringExtra("priceTotal")!!.toLong()
        priceTotal.text = total.toString()

        //phone
        phoneNum = auth.getCurrentUser()?.getPhoneNumber().toString()
        phoneNum = "0" + phoneNum.substring(3)
        phone.text = phoneNum

        //address
        address.text = MyData.prefs.getString("gps", "no gps") + " "

        //schedule spinner
        select_date.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                    if (month < 9 && day < 10) {
                        select_date.text = "$year.0${month + 1}.0$day"
                        selectedDate = "${year}년 0${month + 1}월 0${day}일"
                    } else if (month < 9 && day >= 10) {
                        select_date.text = "$year.0${month + 1}.$day"
                        selectedDate = "${year}년 0${month + 1}월 ${day}일"
                    } else if (month >= 9 && day < 10) {
                        select_date.text = "$year.${month + 1}.0$day"
                        selectedDate = "${year}년 ${month + 1}월 0${day}일"
                    } else {
                        select_date.text = "$year.${month + 1}.$day"
                        selectedDate = "${year}년 ${month + 1}월 ${day}일"
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE)).apply {
                datePicker.minDate = System.currentTimeMillis()
            }.show()

            dateSetting = 1
        }

        mFinalItemList = dbHelper.detail

        mListAdapter = PdfAdapter(mContext, mFinalItemList)

        pdf_list.adapter = mListAdapter
    }
}