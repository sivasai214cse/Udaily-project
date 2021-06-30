package giet.group12.udaily;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

public class Dashboard extends AppCompatActivity {
    private Button upload;
    private Button view,about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        about=findViewById(R.id.about);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashboard.this,About.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        Boolean uploaded = intent.getBooleanExtra("uploaded",false);
        if(uploaded)
        {
            Toast.makeText(this,"uploaded successfully",Toast.LENGTH_LONG).show();
        }
        upload=findViewById(R.id.UPLOAD1);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashboard.this,Upload.class);
                startActivity(intent);
            }
        });

        view=(Button) findViewById(R.id.VIEW1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dashboard.this,Pviewr.class);
                startActivity(intent);
            }
        });
    }

}
