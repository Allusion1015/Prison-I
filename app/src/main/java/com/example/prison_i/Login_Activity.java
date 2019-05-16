package com.example.prison_i;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login_Activity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;

    private String[] usernameStringArray = {"Admin"};
    private String[] passwordStringArray = {"123456"};
    private int[] varAdminOrNot = {1};
    private int keyValue = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
    }

    public void onClickLogin(View view) {
        if(usernameEditText.getText().length() != 0 && passwordEditText.getText().length() != 0){

           if(usernameEditText.getText().toString().equals(usernameStringArray[keyValue]) && passwordEditText.getText().toString().equals(passwordStringArray[keyValue])){
               Toast.makeText(Login_Activity.this, "valid username OR password !", Toast.LENGTH_SHORT).show();

           }else{
               Toast.makeText(Login_Activity.this, "Invalid username OR password !", Toast.LENGTH_SHORT).show();
           }

        }else{
            Toast.makeText(Login_Activity.this, "Enter username and Password !", Toast.LENGTH_SHORT).show();
        }
    }



}
