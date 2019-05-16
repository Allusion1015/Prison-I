package com.example.prison_i;

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

public class SignUp_Activity extends AppCompatActivity {




        EditText usernameEditText;
        EditText passwordEditText;
        private FirebaseAuth mAuth;
        private String TAG = "TAG";



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login_);

            usernameEditText = (EditText)findViewById(R.id.usernameEditText);
            passwordEditText = (EditText)findViewById(R.id.passwordEditText);

            mAuth = FirebaseAuth.getInstance();



        }
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            // updateUI(currentUser);
        }

        public void onClickLogin(View view) {

            if(usernameEditText.getText().toString().contains("@") && usernameEditText.getText().toString().length() > 5 && passwordEditText.getText().toString().length() > 5) {
                mAuth.createUserWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUp_Activity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }else{
                Toast.makeText(SignUp_Activity.this, "INVALID USERNAME OR PASSWORD !",
                        Toast.LENGTH_LONG).show();
            }


        }





    }