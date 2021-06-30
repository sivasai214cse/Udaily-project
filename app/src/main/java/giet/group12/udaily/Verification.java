package giet.group12.udaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;


public class Verification extends AppCompatActivity {
    Button verify_btn;
    EditText otpentered;
    ProgressBar progressbar;
    String verificationcodebysystem;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    FirebaseDatabase rootnode;
    String name ,email ,username,password ,phoneNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verify_btn = findViewById(R.id.verifybtn);
        otpentered = findViewById(R.id.enter_otp);
        progressbar = findViewById(R.id.prog);
      //user information
        phoneNo = getIntent().getStringExtra("phoneNo");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");



        sendVerificationCodeToUser(phoneNo);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder()
                        .setPhoneNumber("+91" + phoneNo)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS)  // Timeout and unit
                        .setActivity(this)                        // Activity (for callback binding)
                        .setCallbacks(mCallbacks)                // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationcodebysystem = s;
        }
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressbar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        }


        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Verification.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcodebysystem, codeByUser);
        signInTheUserByCredentials(credential);
    }


    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Verification.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(Verification.this, "Your Account has been created successfully!", Toast.LENGTH_SHORT).show();
                            //Perform Your required action here to either let the user sign In or do something required
                            Intent intent = new Intent(getApplicationContext(), Profile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("name",name);
                            intent.putExtra("email",email);
                            intent.putExtra("password",password);
                            intent.putExtra("username",username);
                            intent.putExtra("phoneNo",phoneNo);
                            startActivity(intent);
                            storeNewUsersData();
                            finish();

                        } else {
                            Toast.makeText(Verification.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void storeNewUsersData() {

       rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("users");
        UserHelperClass userHelperClass = new UserHelperClass(name,username,email,phoneNo,password);
        reference.child(phoneNo).setValue(userHelperClass);


    }
    public void send(View view) {
        String otp =otpentered.getText().toString();
        this.verifyCode(otp);
    }
}