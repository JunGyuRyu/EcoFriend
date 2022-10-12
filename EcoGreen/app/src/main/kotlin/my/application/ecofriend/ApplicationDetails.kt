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
import kotlinx.android.synthetic.main.detail_list.*
import kotlinx.android.synthetic.main.detail_list_form.*
import my.application.ecofriend.adapters.DetailAdapter
import my.application.ecofriend.databinding.DetailListBinding
import my.application.ecofriend.datas.DetailItem


class ApplicationDetails : dbBaseActivity() {
    private var mDetailList = ArrayList<DetailItem>()
    lateinit var mDetailAdapter: DetailAdapter
    val auth = FirebaseAuth.getInstance()

    private var phoneNum:String = ""
    val database = Firebase.database
    val myRef = database.getReference("regtb")
    private var mBinding: DetailListBinding? = null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.detail_list)
        mBinding = DetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("mDetailList")) {
            mDetailList = intent.getSerializableExtra("mDetailList") as ArrayList<DetailItem>
        }

        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        cacle?.setOnClickListener {
            mDetailList = mDetailAdapter.updateList()
            Log.d("test0101", mDetailList.toString())
            setValues()
        }
        swipe_layout.setOnRefreshListener {
            GetInfoDB()
            swipe_layout.isRefreshing = false
        }
        detail_view.setOnItemClickListener { adapterView, view, i, l ->
            val todaydate = mDetailList[i].todaydate
            val priceTotal = mDetailList[i].total
            val addressFinal = mDetailList[i].addr
            val state = mDetailList[i].state
            val selectdate = mDetailList[i].selectdate

            val myIntent = Intent(mContext, DetailItemList::class.java)
            myIntent.putExtra("requestDate", todaydate.toString())
            myIntent.putExtra("total", priceTotal)
            myIntent.putExtra("addressFinal", addressFinal.toString())
            myIntent.putExtra("stateUpdate", state)
            myIntent.putExtra("selectDate", selectdate.toString())
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mDetailAdapter = DetailAdapter(mContext, mDetailList)
        detail_view.adapter = mDetailAdapter
        mDetailAdapter.notifyDataSetChanged()
    }

    fun GetInfoDB(){
        phoneNum = auth.getCurrentUser()?.getPhoneNumber().toString()
        phoneNum = "0" + phoneNum.substring(3)
        var mdetail = ArrayList<DetailItem>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    if(data.child("phone").value as String? == phoneNum){
                        mdetail.add(
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
                mDetailList.clear()
                mDetailList = mdetail
                setValues()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}