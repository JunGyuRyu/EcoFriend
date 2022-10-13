package my.application.ecofriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.account_delete_dialog.*

class AccountDeleteDialog : DialogFragment(){

    interface AccountDeleteDialogInterface{
        fun delete()
        fun cancleDelete()
    }

    private var accountDeleteDialogInterface: AccountDeleteDialogInterface? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_delete_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()
    }

    fun addAccountDeleteDialogInterface(listener: AccountDeleteDialogInterface){
        accountDeleteDialogInterface = listener
    }

    private fun setupListener(){
        delete_no.setOnClickListener {
            accountDeleteDialogInterface?.cancleDelete()
            dismiss() //선택하면 dialog 닫기
        }
        delete_yes.setOnClickListener {
            accountDeleteDialogInterface?.delete()
            dismiss()
        }
    }
}