package my.application.ecofriend

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.get
import kotlinx.android.synthetic.main.direct_chooser.*
import kotlinx.android.synthetic.main.item_list.view.*
import my.application.ecofriend.adapters.ItemAdapter
import my.application.ecofriend.datas.Item

class GetDatabase : dbBaseActivity() {

    // 액티비티에서 실제 목록을 담아줄 ArrayList를 만들고 실제 데이터들을 담기
    var mItemList = ArrayList<Item>()

    // 만들어둔 Adapter 클래스를 액티비티에 있는 리스트뷰와 연결
    lateinit var mItemAdapter: ItemAdapter

    val dbHelper = DBHelper(this, "ItemList.db", null, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.direct_chooser)

        if(intent.hasExtra("mItemList")) {
            mItemList = intent.getSerializableExtra("mItemList") as ArrayList<Item>
        }

        setValues()
        setupEvents()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@GetDatabase, DisuseWasteActivity::class.java))
    }

    override fun setupEvents() {
        itemListView.choiceMode = ListView.CHOICE_MODE_SINGLE

        itemListView.setOnItemClickListener{ adapterView, view, i, l ->
            val first = itemListView.firstVisiblePosition

            mItemList[i].checked =
                mItemList[i].checked != true

            itemListView[i - first].checkBox.setChecked(mItemList[i].checked)
        }

        next.setOnClickListener {
            for (i in mItemAdapter.count - 1 downTo 0) {
                if (mItemList[i].checked) {
                    var count : Long = dbHelper.query(mItemList[i].dockey)
                    if(count >= 0){
                        dbHelper.update(mItemList[i].dockey, (count+1).toInt())
                    }
                    else{
                        dbHelper.insert(mItemList[i].classification,
                            mItemList[i].dockey,
                            mItemList[i].item,
                            mItemList[i].standard,
                            mItemList[i].levy_amt, 1)

                    }
                }
            }

            val myIntent = Intent(mContext, CheckItemList::class.java)

            if(dbHelper.size() < 0){
                Toast.makeText(this, "배출하고 싶은 품목을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                startActivity(myIntent)
            }

            for (i in mItemAdapter.count - 1 downTo 0) {
                mItemList[i].checked = false
            }
            itemListView.clearChoices()
            mItemAdapter.notifyDataSetChanged()
        }

        back.setOnClickListener {
            val myIntent = Intent(mContext, DisuseWasteActivity::class.java)
            startActivity(myIntent)
        }

        standard_AR.setOnClickListener{
            startActivity(Intent(this@GetDatabase, UnityPlayerActivity::class.java))
        }
    }

    override fun setValues() {

        mItemAdapter = ItemAdapter(mContext, R.layout.item_list, mItemList)

        // 객체화된 adapter변수를 리스트뷰의 어댑터로 지정
        // 실제 목록을 리스트뷰에 뿌려준다.
        itemListView.adapter = mItemAdapter
    }


}

