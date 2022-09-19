package my.application.ecofriend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_accounting_setting.*

class AccountingSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounting_setting)

        setUpListener()
    }

    private fun setUpListener() {
        account_setting_back.setOnClickListener { onBackPressed() }

        account_setting_logout.setOnClickListener { signoutAccount() }

        account_setting_delete.setOnClickListener { showDeleteDialog() }
    }

    private fun signoutAccount() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                moveToMainActivity()
                Toast.makeText(this, "로그아웃 하셨습니다", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteAccount() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                moveToMainActivity()
                Toast.makeText(this, "탈퇴 하셨습니다", Toast.LENGTH_SHORT).show()

            }
    }

    private fun showDeleteDialog() {
        AccountDeleteDialog().apply {
            addAccountDeleteDialogInterface(object :
                AccountDeleteDialog.AccountDeleteDialogInterface {
                override fun delete() {
                    deleteAccount()
                }

                override fun cancleDelete() {

                }
            })
        }.show(supportFragmentManager, "")

//        위와 같은 코드
//        val accountDeleteDialog = AccountDeleteDialog()
//        accountDeleteDialog.addAccountDeleteDialogInterface(object :
//            AccountDeleteDialog.AccountDeleteDialogInterface {
//            override fun delete() {
//                TODO("Not yet implemented")
//            }
//
//            override fun cancleDelete() {
//                TODO("Not yet implemented")
//            }
//        })
//        accountDeleteDialog.show(supportFragmentManager, "")
    }

    private fun moveToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}