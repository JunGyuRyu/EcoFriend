package my.application.ecofriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.upload_chooser.*

class UploadChooser : BottomSheetDialogFragment() {

    interface UploadChooserNotifierInterface{
        fun cameraOnClick() //카메라 버튼 클릭 시 호출
        fun galleryOnClick() //갤러리 버튼 클리 식 호출
    }

    var uploadChooserNotifierInterface : UploadChooserNotifierInterface? = null

    fun addNotifier(listener: UploadChooserNotifierInterface){ //main activity에서 호출 예정이라서 private 아님.
        uploadChooserNotifierInterface = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.upload_chooser, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()
    }

    private fun setupListener(){
        upload_camera.setOnClickListener {
            uploadChooserNotifierInterface?.cameraOnClick() //?는 단순히 "'null'이 아니면 ? 이후를 진행한다" >> uploadChooserNotifierInterface가 있다면 cameraOnClick() 호출
//            uploadChooserNotifierInterface!!.cameraOnClick() "!!"는 uploadChooserNotifierInterface가 무조건 있고 cameraOnClic() 호출
        }
        //main에서 처리하고 싶은 경우 Interface 활용

        upload_gallery.setOnClickListener {
            uploadChooserNotifierInterface?.galleryOnClick()
        }

    }
}