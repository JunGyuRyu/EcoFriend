package my.application.ecofriend

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import my.application.ecofriend.datas.Item

class deepLearningResult: AppCompatActivity() {
    var mTempmList = ArrayList<Item>()

    val database = Firebase.database
    val myRef = database.getReference("nowon")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.getStringExtra("result") == "wardrobe") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("가구·침구류")
                    for (data in test.children) {
                        if (data.child("item").value == "장롱") {   //if (data.child("item").value == "장롱")에서 바꿈
                            mTempmList.add(
                                Item(
                                    false,
                                    "가구·침구류",
                                    data.child("dockey").value as String, //data.child에서 바꿈
                                    "장롱",
                                    data.child("standard").value as String?, //data.child에서 바꿈
                                    data.child("levy_amt").value as Long //data.child에서 바꿈
                                ))
                        }
                    }
                    val myIntent = Intent(this@deepLearningResult, GetDatabase::class.java)

                    myIntent.putExtra("mItemList", mTempmList)

                    startActivity(myIntent)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        else if (intent.getStringExtra("result") == "chair") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("가구·침구류")
                    for (data in test.children) {
                        if (data.child("item").value == "의자") {   //if (data.child("item").value == "장롱")에서 바꿈
                            mTempmList.add(
                                Item(
                                    false,
                                    "가구·침구류",
                                    data.child("dockey").value as String, //data.child에서 바꿈
                                    "의자",
                                    data.child("standard").value as String?, //data.child에서 바꿈
                                    data.child("levy_amt").value as Long //data.child에서 바꿈
                                ))
                        }
                    }
                    val myIntent = Intent(this@deepLearningResult, GetDatabase::class.java)

                    myIntent.putExtra("mItemList", mTempmList)

                    startActivity(myIntent)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
        else if (intent.getStringExtra("result") == "drawer") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("가구·침구류")
                    for (data in test.children) {
                        if (data.child("item").value == "서랍") {   //if (data.child("item").value == "장롱")에서 바꿈
                            mTempmList.add(
                                Item(
                                    false,
                                    "가구·침구류",
                                    data.child("dockey").value as String, //data.child에서 바꿈
                                    "서랍",
                                    data.child("standard").value as String?, //data.child에서 바꿈
                                    data.child("levy_amt").value as Long //data.child에서 바꿈
                                ))
                        }
                    }
                    val myIntent = Intent(this@deepLearningResult, GetDatabase::class.java)

                    myIntent.putExtra("mItemList", mTempmList)

                    startActivity(myIntent)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        else{
            Toast.makeText(this, "대형페기물 목록에 존재하지 않는 품목입니다.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@deepLearningResult, DisuseWasteActivity::class.java))
        }

    }
}
