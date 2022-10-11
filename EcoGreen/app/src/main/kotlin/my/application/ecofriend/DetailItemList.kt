package my.application.ecofriend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_list.*
import my.application.ecofriend.adapters.PdfAdapter
import my.application.ecofriend.datas.PdfItem
import kotlin.collections.ArrayList

class DetailItemList : dbBaseActivity() {
    var mPdfList = ArrayList<PdfItem>()

    var requestDate : String = ""
    var date_selected : String = ""
    var stateUpdate: Long = 1
    var total : Long = 0
    var addr : String = ""
    val auth = FirebaseAuth.getInstance()

    private lateinit var ListAdapter: PdfAdapter
    val database = Firebase.database
    val myRef = database.getReference("regtb")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_list)

        requestDate = intent.getSerializableExtra("requestDate").toString()
        total = intent.getSerializableExtra("total") as Long
        addr = intent.getSerializableExtra("addressFinal").toString()
        stateUpdate = intent.getSerializableExtra("stateUpdate") as Long
        date_selected = intent.getSerializableExtra("selectDate").toString()

        DetailList()
        setupEvents()

    }

    override fun setupEvents() {
        if(stateUpdate.toInt() == 3){
            issue.isClickable = true
            issue.isEnabled = true
            issue.setBackgroundResource(R.drawable.button_location)
            issue.text = "신고필증 발급하기"

            issue.setOnClickListener {
                val myIntent = Intent(mContext, PrintPdf::class.java)
                myIntent.putExtra("requestDate", requestDate)
                myIntent.putExtra("priceTotal", total.toString())
                myIntent.putExtra("selectedDate", date_selected)
                myIntent.putExtra("addressFinal", addr)
                myIntent.putExtra("mList", mPdfList)

                startActivity(myIntent)
            }
        }
    }

    fun DetailList(){
        var key :String = requestDate
        key = key.replace(".","")
        key = key.replace(" ","_")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("test0202", mPdfList.toString())
                val test = snapshot.child(key)
                val array = arrayOf(0,0,0,0,0)
                val cls = ArrayList<String>()
                val item = ArrayList<String>()
                val std = ArrayList<String>()
                val pri = ArrayList<Long>()
                val ct = ArrayList<Long>()
                for (data in test.children) {
                    Log.d("test0101", data.getValue().toString())
                    if(data.key == "classification${array[0]}") {
                        Log.d("test0202", data.getValue().toString())
                        cls.add(array[0],data.getValue().toString())
                        array[0]+=1
                        Log.d("array[0]", array[0].toString())
                    }
                    else if(data.key == "item${array[1]}") {
                        Log.d("test0303", data.getValue().toString())
                        item.add(array[1],data.getValue().toString())
                        array[1]+=1
                    }
                    else if(data.key =="standard${array[2]}") {
                        std.add(array[2],data.getValue().toString())
                        array[2]+=1
                    }
                    else if(data.key =="levy_amt${array[3]}") {
                        pri.add(array[3],data.getValue().toString().toLong())
                        array[3]+=1
                    }
                    else if(data.key =="count${array[4]}") {
                        ct.add(array[4],data.getValue().toString().toLong())
                        array[4]+=1
                    }
                }
                for(i in cls.size-1 downTo 0){
                    mPdfList.add(
                        PdfItem(
                            cls[i],
                            item[i],
                            std[i],
                            pri[i],
                            ct[i]
                        ))
                }
                setValues()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun setValues() {
        if(mPdfList!=null){
            ListAdapter = PdfAdapter(mContext, mPdfList)

            pdfList.adapter = ListAdapter
            ListAdapter.notifyDataSetChanged()
        }
    }

}