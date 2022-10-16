package my.application.ecofriend.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import my.application.ecofriend.R

class ClassAdapter(
    val mContext: Context,
    val resId: Int,
    val mList: List<String>

) : ArrayAdapter<String>(mContext, resId, mList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inf = LayoutInflater.from(mContext)

        var tempRow = convertView
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.class_list, null)
        }

        val row = tempRow!!

        val data = mList[position]
        val classifier = row.findViewById<TextView>(R.id.classTxt)

        classifier.text = data

        return row
    }
}
