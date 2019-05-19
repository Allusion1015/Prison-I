package com.example.prison_i;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    private FirebaseAuth mAuth;
    private  String TAG ="TAG :";
    String prev_uId;
Intent navIntent;
Intent SignUpIntent;
    Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
setTitle("Jailor Login");
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
navIntent=new Intent(getApplicationContext(),Prisoners_NavDrawAct.class);
SignUpIntent=new Intent(getApplicationContext(),SignUp_Activity.class);
       mAuth = FirebaseAuth.getInstance();
        Intent intent=getIntent();
        prev_uId=intent.getStringExtra("UId");
        Log.i("prevUID :", prev_uId+"54");
       mIntent=new Intent(Login_Activity.this,SignUp_Activity.class);


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Toast.makeText(Login_Activity.this, mAuth.getCurrentUser().getUid() + "value" , Toast.LENGTH_SHORT).show();
        // updateUI(currentUser);
    }

    public void onClickLogin(View view) {

        if(usernameEditText.getText().toString().contains("@") && usernameEditText.getText().toString().length() > 5 && passwordEditText.getText().toString().length() > 5) {


            mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                navIntent.putExtra("UId",prev_uId);
                                startActivity(navIntent);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(Login_Activity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            Toast.makeText(Login_Activity.this, "INVALID USERNAME OR PASSWORD !",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void onClickSignUp(View view)
    {
        SignUpIntent.putExtra("UId",prev_uId);
        startActivity(SignUpIntent);
    }


}
