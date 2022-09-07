package my.application.ecogreen.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import my.application.ecogreen.R
import my.application.ecogreen.datas.CheckedItem
import my.application.ecogreen.datas.PdfItem

class  PdfAdapter (val mContext: Context, val mPdfList: ArrayList<PdfItem>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.pdf_list, null)
        val classification = view.findViewById<TextView>(R.id.classification)
        val item = view.findViewById<TextView>(R.id.item)
        val standard = view.findViewById<TextView>(R.id.standard)
        val count = view.findViewById<TextView>(R.id.count)

        val list = mPdfList[position]

        classification.text = list.classification
        item.text = list.item
        standard.text = list.standard
        count.text = list.count.toString()

        return view
    }

    override fun getItem(position: Int): Any {
        return mPdfList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mPdfList.size
    }
}