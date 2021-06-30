package giet.group12.udaily;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Year;

public class Profile extends AppCompatActivity {
    TextView username ,temail;
    Button finish;
    TextInputLayout sem,fname,year,branch;
    String name, usernamee,phoneNo,password,email;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //hookups
        username = findViewById(R.id.username);
        finish = findViewById(R.id.finish);
        sem = findViewById(R.id.sem);
        fname = findViewById(R.id.fname);
        username = findViewById(R.id.usrnm);
        year = findViewById(R.id.year);
        branch = findViewById(R.id.branch);
        temail=findViewById(R.id.email);


        //set text and edit text from intent data
        showUserdata();
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("users");

                finish(v);
            }
        });

    }
    private void showUserdata() {
        name=getIntent().getStringExtra("name");
        usernamee = getIntent().getStringExtra("username");
        phoneNo = getIntent().getStringExtra("phoneNo");
        password = getIntent().getStringExtra("password");
        email = getIntent().getStringExtra("email");
        //change the text
        username.setText(usernamee);
        temail.setText(email);
       fname.getEditText().setText(name);

    }



    private Boolean validateSem() {
        String val = sem.getEditText().getText().toString();
        int v = Integer.parseInt(val);
        if (val.isEmpty()) {
            sem.setError("Field cannot be empty");
            return false;
        } else if (v >= 9) {
            sem.setError("invalid input");
            return false;
        } else {
            sem.setError(null);
            sem.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateYear() {
        String val = year.getEditText().getText().toString();
        int v = Integer.parseInt(val);
        if (val.isEmpty()) {
            year.setError("Field cannot be empty");
            return false;
        } else if (v >=4) {
            year.setError("invalid input");
            return false;
        } else {
            year.setError(null);
            year.setErrorEnabled(false);
            return true;
        }
    }



    private void finish(View v) {
        if (!validateSem() | !validateYear()) {
            return;
        }
        {
            String Sem = sem.getEditText().getText().toString();
            String Year = year.getEditText().getText().toString();
            String Branch = branch.getEditText().getText().toString();
            Update update = new Update(Sem,Year,Branch,name,usernamee, email, phoneNo, password);
            reference.child(phoneNo).setValue(update);
            Intent intent = new Intent(Profile.this,Dashboard.class);
           startActivity(intent);

        }


    }
}