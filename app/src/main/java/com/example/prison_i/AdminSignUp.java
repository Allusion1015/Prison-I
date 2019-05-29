package com.example.prison_i;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminSignUp extends AppCompatActivity {

    Intent JailorLoginIntent;
    EditText usernameEditText;
    EditText passwordEditText;
    EditText nameEditText;
    private FirebaseAuth mAuth;
    private String TAG = "TAG";
    int variable;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Intent intent;




    public void UpdateFireBase(int var,String email , String name , String uuid) {

        DatabaseReference dataRef;
        dataRef = databaseReference.child("ADMIN");
       // dataRef.child(uuid).setValue(name);


        DatabaseReference uidRef = dataRef.child(uuid);
        uidRef.child("Name").setValue(name);
        uidRef.child("Email").setValue(email);
        uidRef.child("var").setValue(var);
        Log.i("info","success");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);
        JailorLoginIntent=new Intent(getApplicationContext(),Login_Activity.class);
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        nameEditText= (EditText) findViewById(R.id.nameEditText);

        setTitle("ADMIN SignUp");
       // intent =new Intent(AdminSignUp.this,Login_Activity.class);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }



    public void onClickSignUpAdmin(View view) {



        if(usernameEditText.getText().toString().contains("@") && usernameEditText.getText().toString().length() > 5 && passwordEditText.getText().toString().length() > 5) {
            mAuth.createUserWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                variable=1;
                                UpdateFireBase(variable,usernameEditText.getText().toString() , nameEditText.getText().toString(), user.getUid() );
                                Log.i("adminID", user.getUid());
                                JailorLoginIntent.putExtra("UId",user.getUid());

                                startActivity(JailorLoginIntent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(AdminSignUp.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });


        }else{
            Toast.makeText(AdminSignUp.this, "INVALID USERNAME OR PASSWORD !",
                    Toast.LENGTH_LONG).show();
        }
    }

}

