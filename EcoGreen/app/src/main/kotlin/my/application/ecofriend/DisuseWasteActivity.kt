package my.application.ecofriend

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.FileProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_direct_fill_waste.*
import kotlinx.android.synthetic.main.activity_reuse.*
import kotlinx.android.synthetic.main.item_big_class.*
import kotlinx.android.synthetic.main.main_analyze_view.*
import kotlinx.android.synthetic.main.pdf_list.*
import my.application.ecofriend.adapters.ClassAdapter
import my.application.ecofriend.datas.Item
import my.application.ecofriend.datas.MyData
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

class DisuseWasteActivity : AppCompatActivity() {
    private val CAMERA_PERMISSION_REQUEST = 1000
    private val GALLERY_PERMISSION_REQUEST = 1001
    private val EXTERNAL_PERMISSION_REQUEST = 1002
    private val FILE_NAME = "picture.jpg"
    private var uploadChooser: UploadChooser? = null
    private lateinit var progressDialog: AppCompatDialog

    private val mClassList = ArrayList<String>()
    private var mItem = ArrayList<Item>()

    lateinit var mClassAdapter: ClassAdapter

    val database = Firebase.database
    val myRef = database.getReference("gangbuk")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_direct_fill_waste)

        setupListener()
        setupValue()

    }

    override fun onBackPressed() {
        startActivity(Intent(this@DisuseWasteActivity, MainHomeActivity::class.java))
    }

    private fun setupValue() {

        mClassList.add("가구·침구류")
        mClassList.add("학습·사무기기")
        mClassList.add("생활용품")
        mClassList.add("주방용품")
        mClassList.add("가전제품")
        mClassList.add("냉난방기")
        mClassList.add("기타")

        mClassAdapter = ClassAdapter(this, R.layout.class_list, mClassList)

        classListView.adapter = mClassAdapter
    }

    private fun setupListener() {
        classListView.setOnItemClickListener { adapterView, view, i, l ->
            // 눌린 위치에 해당하는 목록이 어떤 목록인지 가져오기
            val clickedClass = mClassList[i]
            // 선택된 목록정보를 가져왔으면 이제 화면 이동
            val myIntent = Intent(this, GetDatabase::class.java)

            if(MyData.prefs.getString("gps", "no gps") == "서울특별시 강북구") {
                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val test = snapshot.child(clickedClass)

                        for (data in test.children) {
                            mItem.add(
                                Item(
                                    false,
                                    clickedClass,
                                    data.child("dockey").value as String,
                                    data.child("item").value as String?,
                                    data.child("standard").value as String?,
                                    data.child("levy_amt").value as Long
                                ))

                        }

                        // 정보를 담아주기
                        myIntent.putExtra("mItemList", mItem)

                        // 화면 전환
                        startActivity(myIntent)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
            else{
                Toast.makeText(this, "현재 연결된 데이터가 없습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        search_item.setOnClickListener{
            //apply함수는 앞에 있는 부분 초기 설정할 때 활용하기 좋은 함수.
            uploadChooser = UploadChooser().apply {
                addNotifier(object : UploadChooser.UploadChooserNotifierInterface{
                    override fun cameraOnClick() {
                        Log.d("upload", "CameraOnClick")
                        // 카메라 권한 요청
                        checkCameraPermission()
                    }

                    override fun galleryOnClick() {
                        Log.d("upload", "galleryOnClick")
                        // 사진첩 권한 요청
                        checkGalleryPermission()
                    }
                })
            }
            uploadChooser!!.show(supportFragmentManager, "")
        }
    }

    private fun checkCameraPermission() {
        if (PermissionUtil().requestPermission(
                this,
                CAMERA_PERMISSION_REQUEST,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            openCamera()
        }
    }

    private fun checkGalleryPermission() {
        if (PermissionUtil().requestPermission(
                this,
                GALLERY_PERMISSION_REQUEST,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            openGallery()
        }
    }

    private fun checkExternalPermission(bitmap: Bitmap) {
        if (PermissionUtil().requestPermission(
                this,
                EXTERNAL_PERMISSION_REQUEST,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            progressON(bitmap)
        }
    }


    private fun openGallery() {
        val intent = Intent().apply {
            //image 폴더의 전체 다
            setType("image/*")
            setAction(Intent.ACTION_GET_CONTENT)
        }

        startActivityForResult(Intent.createChooser(intent, "Select a photo"),
            GALLERY_PERMISSION_REQUEST)
    }

    private fun openCamera() {
        //Uri는 경로 전체를 포함하는 의미(url은 인터넷 경로) -> photoUri: 사진을 저장할 uri 생성
        //getUriForFile(file을 위한 uri를 만들어주는 함수)
        val photoUri =
            FileProvider.getUriForFile(this, "my.application.ecofriend.provider", createCameraFile())
//        val photoUri = FileProvider.getUriForFile(this,  applicationContext.packageName + ".provider", createCameraFile())

        //사진 찍은 후 결과값 이미지를 올려야 하므로 startActivityForResult 사용
        startActivityForResult(
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                //찍은 결과물(EXTRA_OUTPUT)을 photoUri에 저장
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }, CAMERA_PERMISSION_REQUEST
        )
    }

    //data: Intent? -> 코틀린에서 ?는 'null'일 수도 있다는 것을 의미
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                //작업의 결과물을 떠나서 작업이 잘 되었는지 확인해주는 게 RESULT_OK. 잘못된 작업이면 굳이 결과물 가져오지 않아도 되니까
                if (resultCode != Activity.RESULT_OK) return
                val photoUri = FileProvider.getUriForFile(this,
                    "my.application.ecofriend.provider",
                    createCameraFile())
                uploadImage(photoUri)

                //찍은 사진을 사진첩에 저장하고 싶으면 이 위치에 코드 추가하면 됨.

            }
            GALLERY_PERMISSION_REQUEST -> {
                //data가 null이 아니면 let 이하를 시작
                data?.let {
                    it.data?.let { it1 -> uploadImage(it1) }
                }
            }
        }
    }


    //실질적으로 사진첩이나 카메라에서 이미지를 받아오고, 마지막으로 받아온 이미지를 이미지뷰에 올려주는 것.(업로드할 이미지가 준비가 되어 있는 상황)
    private fun uploadImage(imageUri: Uri) {
        //넘겨준 경로로 가서 사진을 받아옴. 사진은 비트맵으로 넘어옴.
        var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        bitmap = resizeBitmap(bitmap)
        //uploaded_image.setImageBitmap(bitmap)
        uploadChooser?.dismiss()

        checkExternalPermission(bitmap)
    }

    private fun createCameraFile(): File {
        //dir은 핸드폰 사집첩 폴더
        val dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(dir, FILE_NAME)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GALLERY_PERMISSION_REQUEST -> {
                if (PermissionUtil().permissionGranted(requestCode,
                        GALLERY_PERMISSION_REQUEST,
                        grantResults)
                ) {
                    openGallery()
                }
            }
            CAMERA_PERMISSION_REQUEST -> {
                //내가 보낸 요청에 대한 답이 왔는지 확인하고, 허락받은 권한이 하나라도 있는지 확인(grantResults size로), 0보다 크다면 그 중 0번째를 꺼내서 grant 됐는지 확인
                if (PermissionUtil().permissionGranted(requestCode,
                        CAMERA_PERMISSION_REQUEST,
                        grantResults)
                ) {
                    openCamera()
                }
            }
        }
    }

    fun resizeBitmap(original: Bitmap) : Bitmap {
        val resizeWidth = 416;
        val resizeHeight = 416;
        val result: Bitmap = Bitmap.createScaledBitmap(original, resizeWidth, resizeHeight, false);
        return result;
    }

    fun send2Server(bitmap: Bitmap) {
        val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File(dirPath, "/Baro/${UUID.randomUUID()}.jpg")
        var str : String? = null
        var indexName: String? = null
        var indexConf: String? = null
        var os: OutputStream? = null

        try {
            if (!imageFile.isDirectory) {
                imageFile.parentFile.mkdirs()
            }
            imageFile.createNewFile()
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("photo",
                imageFile.name,                                          //"/${UUID.randomUUID()}.jpg"
                RequestBody.create(MultipartBody.FORM, imageFile))
            .build()

        val request: Request = Request.Builder()
            .url("https://yolo-310016.du.r.appspot.com/yolo") // 애뮬레이터 실행 시 http://10.0.2.2:5000/dnn/yolo
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                System.out.println("Failure")
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                str = response.body!!.string()
                //str = str.toString()

                //Log.d("TEST : ", response.body!!.string())
                Log.d("TEST : ", str.toString())

                var token = str!!.split('"')
                if(token.size <= 1){
                    indexName = "notwardrobe"
                }
                else{
                    indexName = token[9]
                    indexConf = token[12]
                    indexConf = indexConf!!.substring(1)
                    indexConf = indexConf!!.split('}')[0]
                }

                progressOFF()
                val intent = Intent(this@DisuseWasteActivity, deepLearningResult::class.java)
                intent.putExtra("result", indexName.toString())
                System.out.println(indexName)
                startActivity(intent)
            }
        })
    }

    fun progressON(bitmap: Bitmap){
        progressDialog = AppCompatDialog(this)
        progressDialog.setCancelable(true)
        progressDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.progress_dialog)
        progressDialog.show()
        var img_loading_framge = progressDialog.findViewById<ImageView>(R.id.iv_frame_loading)
        var frameAnimation = img_loading_framge?.getBackground() as AnimationDrawable
        img_loading_framge.post(object : Runnable {
            override fun run() {
                frameAnimation.start()
                send2Server(bitmap)
            }
        })
    }

    fun progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss()
        }
    }

}

