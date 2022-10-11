package my.application.ecofriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.set_up_chooser.*

class SetUpChooser : BottomSheetDialogFragment() {

    interface SetUpChooserNotifierInterface{
        fun applicationOnClick()
        fun deleteUserOnClick() //갤러리 버튼 클리 식 호출
    }

    var setUpChooserNotifierInterface : SetUpChooserNotifierInterface? = null

    fun addNotifier(listener: SetUpChooserNotifierInterface){ //disuse waste activity에서 호출 예정이라서 private 아님.
        setUpChooserNotifierInterface = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.set_up_chooser, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()
    }

    private fun setupListener(){
        applications.setOnClickListener {
            setUpChooserNotifierInterface?.applicationOnClick()
        }

        delete_user.setOnClickListener {
            setUpChooserNotifierInterface?.deleteUserOnClick()
        }

    }
}