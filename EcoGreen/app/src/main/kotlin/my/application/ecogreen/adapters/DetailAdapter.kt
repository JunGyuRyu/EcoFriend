package my.application.ecogreen.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isInvisible
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.application.ecogreen.DBHelper
import my.application.ecogreen.DetailItemList
import my.application.ecogreen.R
import my.application.ecogreen.datas.DetailItem
import my.application.ecogreen.detailDB
import kotlin.collections.ArrayList

class  DetailAdapter(val mContext: Context, val mDetailList: ArrayList<DetailItem>) : BaseAdapter() {

    val database = Firebase.database
    val myRef = database.getReference("regtb")
    private val detailDB = detailDB(mContext, "DetailList.db", null, 1)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.detail_list_form, null)
        val date = view.findViewById<TextView>(R.id.date_simple)
        val item = view.findViewById<TextView>(R.id.item_title)
        val price = view.findViewById<TextView>(R.id.price)
        val state = view.findViewById<TextView>(R.id.state)
        val cancle = view.findViewById<TextView>(R.id.cacle)
        val list = mDetailList[position]

        date.text = list.todaydate
        item.text = "[${list.classification}/${list.item}...]"
        price.text = "(수수료: ${list.total})"

        if(list.state?.toInt() == 1){
            state.text = "입금대기중"
            cancle.text = "신청취소"
        }
        else if(list.state?.toInt() == 3){
            state.setBackgroundResource(R.drawable.button_cancle)
            state.text = "결제완료"
            cancle.text = "삭제하기"
        }

        cancle?.setOnClickListener {
            var key :String = list.todaydate.toString()
            key = key.replace(".","")
            key = key.replace(" ","_")

            mDetailList.removeAt(position)
            myRef.child(key).removeValue()
            detailDB.delete(list.todaydate)
            notifyDataSetChanged()
        }

        return view
    }

    override fun getCount(): Int {
        return mDetailList.size
    }

    override fun getItem(position: Int): Any {
        return mDetailList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    fun updateList():ArrayList<DetailItem>{
        return mDetailList
    }
}