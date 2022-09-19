package my.application.ecofriend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPreviousLogin()

    }

    private fun checkPreviousLogin(){
        val user = FirebaseAuth.getInstance().currentUser

        //login이 되어있지 않으면 로그인 페이지 호출
        if(user == null)showLoginWindow()

        //login이 되어있는 경우 바로 MainHomeActivity로 이동
        else moveToMainHome()
    }

    private fun moveToMainHome(){
        startActivity(Intent(this, MainHomeActivity::class.java))
    }

    private fun showLoginWindow(){
        startActivity(Intent(this, LoginActivity::class.java))

        // Choose authentication providers
//        val providers = arrayListOf(
//                AuthUI.IdpConfig.EmailBuilder().build(),
//                AuthUI.IdpConfig.GoogleBuilder().build())
//
//        // Create and launch sign-in intent
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                RC_SIGN_IN
//        )
    }


//    //data: Intent? -> 코틀린에서 ?는 'null'일 수도 있다는 것을 의미
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RC_SIGN_IN) {
//            val response = IdpResponse.fromResultIntent(data)
//
//            if (resultCode == Activity.RESULT_OK) {
//                // Successfully signed in
//                val user = FirebaseAuth.getInstance().currentUser
//                startActivity(Intent(this, MainHomeActivity::class.java))
//            } else {
//                Toast.makeText(this, "로그인 실패, 로그인을 다시 시도해주세요", Toast.LENGTH_LONG).show()
//            }
//        }
//    }

}