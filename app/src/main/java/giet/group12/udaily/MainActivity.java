package giet.group12.udaily;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button callsignUp , login;
    TextInputLayout regPhone ,regPassword;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    FirebaseAuth Auth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

       //creating hookups here
        callsignUp =  findViewById(R.id.signup_screen);
        login = findViewById(R.id.loign);
        regPhone = findViewById(R.id.phoneno);
        regPassword = findViewById(R.id.pass);
        Auth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

login.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       loginUser(v);

    }
});

        callsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,signUp.class);
                startActivity(intent);
                finish();
            }
        });



    }
    private Boolean validatePhoneNo() {
        String val = regPhone.getEditText().getText().toString();

        if (val.isEmpty()) {
            regPhone.setError("Field cannot be empty");
            return false;
        }
        else {
            regPhone.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString();
        if (val.isEmpty()) {
            regPassword.setError("feild cannot be empty");
            return false;
        } else {
            regPassword.setError(null);
            regPhone.setErrorEnabled(false);
            return true;
        }
    }
    public void loginUser(View view) {
        //Validate Login Info
        if (!validatePhoneNo() | !validatePassword()) {
            return;
        }else
            isUser();

    }

    private void isUser() {
        final String userEnteredPhone = regPhone.getEditText().getText().toString().trim();
        final String userEnteredPassword = regPassword.getEditText().getText().toString().trim();
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");
        Query checkUser = reference.orderByChild("phoneNo").equalTo(userEnteredPhone);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    regPhone.setError(null);
                    regPhone.setErrorEnabled(false);
                    String passwordFromDB = snapshot.child(userEnteredPhone).child("password").getValue(String.class);   //realtime data base do not take email special characters so use phone no insted.
                    if(passwordFromDB.equals(userEnteredPassword)) {
                        regPhone.setError(null);
                        regPhone.setErrorEnabled(false);
                        String userDB = snapshot.child(userEnteredPhone).child("name").getValue(String.class);
                        String phoneDB = snapshot.child(userEnteredPhone).child("phoneNo").getValue(String.class);
                        String emailDB = snapshot.child(userEnteredPhone).child("email").getValue(String.class);
                        String usernameDB = snapshot.child(userEnteredPhone).child("username").getValue(String.class);
                        progressBar.setVisibility(View.VISIBLE);

                        SharedPreferences sp =getSharedPreferences("login",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("phoneNO",userEnteredPhone);
                        editor.putString("password",userEnteredPassword);
                        editor.commit();
                        editor.apply();

                       Intent intent = new Intent(MainActivity.this,Dashboard.class);
//                        Intent intent = new Intent(getApplicationContext(),Profile.class);
//                        intent.putExtra("name", userDB);
//                        intent.putExtra("phoneNo", phoneDB);
//                        intent.putExtra("email", emailDB);
//                        intent.putExtra("username", usernameDB);
//                        intent.putExtra("password", passwordFromDB);
                       startActivity(intent);
                       finish();

                    }
                    else
                    {
                        regPassword.setError("wrong password");
                        regPassword.requestFocus();

                    }

                }
                else
                {
                    regPhone.setError("invalid id");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sp =getSharedPreferences("login",MODE_PRIVATE);
        if(sp.contains("phoneNO") && sp.contains("password"))
        {
         Intent intent = new Intent(MainActivity.this,Dashboard.class);
         startActivity(intent);
         finish();
        }


    }
}