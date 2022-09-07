package my.application.ecogreen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import my.application.ecogreen.DBHelper
import my.application.ecogreen.R
import my.application.ecogreen.datas.CheckedItem
import my.application.ecogreen.CheckItemList as CheckItemList1

class  ListAdapter (val mContext: Context, val mCheckedItemList: ArrayList<CheckedItem>) : BaseAdapter() {

    val dbHelper = DBHelper(mContext, "ItemList.db", null, 1)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.checked_list_form, null)
        val classTxt = view.findViewById<TextView>(R.id.classTxt)
        val itemTxt = view.findViewById<TextView>(R.id.itemTxt)
        val price = view.findViewById<TextView>(R.id.price)
        val count = view.findViewById<TextView>(R.id.count)
        val minus = view.findViewById<ImageView>(R.id.minus)
        val plus = view.findViewById<ImageView>(R.id.plus)
        val priceTxt = view.findViewById<TextView>(R.id.itemPriceTxt)
        val delete_item = view.findViewById<ImageView>(R.id.delete_item)

        val list = mCheckedItemList[position]
        var price_each = list.levy_amt.toString().toInt()
        var count_int = list.count.toString().toLong()
        var total = price_each.times(count_int)

        count.text = list.count.toString()
        priceTxt.text = total.toString()

        minus.setOnClickListener {
            count_int = count_int.minus(1)
            total = price_each.times(count_int)
            mCheckedItemList[position].count = count_int
            dbHelper.update(list.dockey, count_int.toInt())

            if(count_int.toInt() == -1){
                mCheckedItemList.removeAt(position)
                dbHelper.delete(list.dockey)
                notifyDataSetChanged()
            }

            count.text = count_int.toString()
            priceTxt.text = total.toString()

            if(count_int.toInt() == 0){
                Toast.makeText(mContext, "버튼을 한 번 더 누르시면 품목이 삭제됩니다.", Toast.LENGTH_SHORT).show()
            }
        }

        plus.setOnClickListener {
            count_int = count_int.plus(1)
            total = price_each.times(count_int)
            mCheckedItemList[position].count = count_int
            dbHelper.update(list.dockey, count_int.toInt())

            count.text = count_int.toString()
            priceTxt.text = total.toString()
        }

        delete_item?.setOnClickListener {
            mCheckedItemList.removeAt(position)
            dbHelper.delete(list.dockey)
            notifyDataSetChanged()
        }

        classTxt.text = list.classification
        itemTxt.text = "${list.item} / ${list.standard}"
        price.text = "(${list.levy_amt}/개)"

        return view
    }

    override fun getItem(position: Int): Any {
        return mCheckedItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mCheckedItemList.size
    }

}