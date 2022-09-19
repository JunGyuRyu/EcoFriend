package my.application.ecofriend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/*
* Copyright 2020. supercoders(hackstarsj) all rights reserved.
*
* https://www.youtube.com/watch?v=_EgsrEmWwR8
* https://github.com/hackstarsj/FirebaseAuthAndroid
*/

public class LoginActivity extends AppCompatActivity {

    private static final int GOOGLE_SIGN_IN_REQUEST =112 ;
    FirebaseAuth auth;
    String verificationOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth=FirebaseAuth.getInstance();
        InitiallizeOTPLogin();
        // 기존 로그인 상태 확인 기능 위치
        // if(auth.getCurrentUser()!=null){
        //     startActivity(new Intent(LoginActivity.this,MainHomeActivity.class));
        //     finish();
        // }
    }

    private void InitiallizeOTPLogin(){
        Button sendOtp=findViewById(R.id.send_otp);
        Button verifyOtp=findViewById(R.id.verify_otp);
        final EditText phoneInput=findViewById(R.id.phone_input);
        final EditText otpInput=findViewById(R.id.otp_input);

        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOtpCode("+82"+phoneInput.getText().toString());
            }
        });

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerifyOtp(otpInput.getText().toString());
            }
        });
    }

    private void  sendOtpCode(String phone){
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("Success","Verified");
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("Success","Failed");
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(@NonNull String verifyToke, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationOtp=verifyToke;
                PhoneAuthProvider.ForceResendingToken  token=forceResendingToken;
            }
        };

        PhoneAuthProvider
                .getInstance().
                verifyPhoneNumber(phone,60, TimeUnit.SECONDS, LoginActivity.this,callbacks);
    }

    private void VerifyOtp(String otp){
        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationOtp,otp);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=task.getResult().getUser();
                            SendUserData(user);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check Result come from Google
        if(requestCode==GOOGLE_SIGN_IN_REQUEST){
            Task<GoogleSignInAccount> accountTask=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=accountTask.getResult(ApiException.class);
                processFirebaseLoginStep(account.getIdToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
//            callbackManager.onActivityResult(requestCode,resultCode,data);
        }
    }

    private void processFirebaseLoginStep(String token){
        AuthCredential authCredential= GoogleAuthProvider.getCredential(token,null);
        auth.signInWithCredential(authCredential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user=auth.getCurrentUser();
                            SendUserData(user);
                        }
                    }
                });
    }

    private void  SendUserData(FirebaseUser user){
        Log.d("Login Success","Success");
        Log.d("User ",user.getUid());
        // startActivity(new Intent(LoginActivity.this,MainHomeActivity.class));
        if(auth.getCurrentUser()!=null) {
            startActivity(new Intent(LoginActivity.this,MainHomeActivity.class));
            finish();
        }
    }
}
