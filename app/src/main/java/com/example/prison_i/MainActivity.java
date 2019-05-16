package com.example.prison_i;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Intent LoginIntent;
    Intent SignUPIntent;


    public void startActivity(View view){
        startActivity(LoginIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoginIntent = new Intent(getApplicationContext(),Login_Activity.class);
        SignUPIntent = new Intent(getApplicationContext(),SignUp_Activity.class);

        findViewById(R.id.IntentCallSignUP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SignUPIntent);
            }
        });
    }
}
