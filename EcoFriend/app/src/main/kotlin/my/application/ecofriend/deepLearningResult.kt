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

        if (intent.getStringExtra("result") == "drawer") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("가구·침구류")
                    for (data in test.children) {
                        if (data.child("item").value == "서랍") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "가구·침구류",
                                    data.child("dockey").value as String,
                                    "서랍",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
        else if (intent.getStringExtra("result") == "sofa") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("가구·침구류")
                    for (data in test.children) {
                        if (data.child("item").value == "소파") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "가구·침구류",
                                    data.child("dockey").value as String,
                                    "소파",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
        else if (intent.getStringExtra("result") == "desk") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("가구·침구류")
                    for (data in test.children) {
                        if (data.child("item").value == "책상") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "가구·침구류",
                                    data.child("dockey").value as String,
                                    "책상",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
                        if (data.child("item").value == "의자") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "가구·침구류",
                                    data.child("dockey").value as String,
                                    "의자",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
        else if (intent.getStringExtra("result") == "printer") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("학습·사무기기")
                    for (data in test.children) {
                        if (data.child("item").value == "프린터") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "학습·사무기기",
                                    data.child("dockey").value as String,
                                    "프린터",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
        else if (intent.getStringExtra("result") == "bicycle") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("생활용품")
                    for (data in test.children) {
                        if (data.child("item").value == "자전거") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "생활용품",
                                    data.child("dockey").value as String,
                                    "자전거",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
        else if (intent.getStringExtra("result") == "frypan") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("주방용품")
                    for (data in test.children) {
                        if (data.child("item").value == "프라이팬") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "주방용품",
                                    data.child("dockey").value as String,
                                    "프라이팬",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
        else if (intent.getStringExtra("result") == "ricecooker") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("가전제품")
                    for (data in test.children) {
                        if (data.child("item").value == "전기밥솥") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "가전제품",
                                    data.child("dockey").value as String,
                                    "전기밥솥",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
        else if (intent.getStringExtra("result") == "fan") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("냉난방기")
                    for (data in test.children) {
                        if (data.child("item").value == "선풍기") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "냉난방기",
                                    data.child("dockey").value as String,
                                    "선풍기",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
        else if (intent.getStringExtra("result") == "flowerpot") {
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val test = snapshot.child("기타")
                    for (data in test.children) {
                        if (data.child("item").value == "화분") {
                            mTempmList.add(
                                Item(
                                    false,
                                    "기타",
                                    data.child("dockey").value as String,
                                    "화분",
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
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
