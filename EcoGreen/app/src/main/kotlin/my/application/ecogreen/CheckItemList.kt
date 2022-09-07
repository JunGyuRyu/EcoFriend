package my.application.ecogreen


import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.checked_list.*
import my.application.ecogreen.adapters.ListAdapter
import my.application.ecogreen.datas.CheckedItem
import my.application.ecogreen.datas.MyData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CheckItemList : dbBaseActivity() {
    var mCheckedItemList = ArrayList<CheckedItem>()
    var auth = FirebaseAuth.getInstance()

    lateinit var mListAdapter: ListAdapter
    val dbHelper = DBHelper(this, "ItemList.db", null, 1)
    val detailDB = detailDB(this, "DetailList.db", null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checked_list)

        setValues()
        setupEvents()
    }

    override fun setupEvents() {

        calculate_total.setOnClickListener {
            var value = calculateTotal()

            total.setTypeface(null, Typeface.BOLD)
            total.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            total.text = "= ${value.toString()}"
        }

        add_more.setOnClickListener {
            val myIntent = Intent(mContext, DisuseWasteActivity::class.java)

            startActivity(myIntent)
        }

        next.setOnClickListener {
            val currentDateTime = Calendar.getInstance().time
            val date_str = SimpleDateFormat("yyyy.MM.dd hh:mm:ss", Locale.KOREA).format(currentDateTime)
            val myIntent = Intent(mContext, PreviewPdf::class.java)

            var value = calculateTotal()

            myIntent.putExtra("priceTotal", value.toString())
            myIntent.putExtra("date", date_str)

            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mCheckedItemList = dbHelper.result
        mListAdapter = ListAdapter(mContext, mCheckedItemList)

        // 객체화된 adapter변수를 리스트뷰의 어댑터로 지정
        // 실제 목록을 리스트뷰에 뿌려준다.
        list_view.adapter = mListAdapter
    }

    fun calculateTotal() : Int{
        var price: Int = 0

        for (i in mCheckedItemList.size - 1 downTo 0) { //mListAdapter
            price += (mCheckedItemList[i].count!!.toInt()).times(mCheckedItemList[i].levy_amt!!.toInt())
        }
        return price
    }

}