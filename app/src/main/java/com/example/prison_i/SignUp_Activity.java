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

import java.util.UUID;

public class SignUp_Activity extends AppCompatActivity {


DatabaseReference assignAdminnUidPrisoner;

        EditText usernameEditText;
        EditText passwordEditText;
        EditText nameEditText;
        private FirebaseAuth mAuth;
        private String TAG = "TAG";
        boolean selectUserIsTrue;
        boolean jailorisTrue;
        String admin_uId;
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
    FirebaseUser user;

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        selectUserIsTrue = true;

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.jailorRadioB:
                if (checked)
                    Toast.makeText(SignUp_Activity.this, "jailor", Toast.LENGTH_SHORT).show();
                    jailorisTrue = true;
                break;
            case R.id.prisonerRadioB:
                if (checked)
                    jailorisTrue = false;
                    Toast.makeText(SignUp_Activity.this, "prisoner", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void UpdateFireBase(boolean jailorisTrue , String email , String name , String uuid) {

        DatabaseReference dataRef;
if(jailorisTrue)
{dataRef = databaseReference.child(admin_uId).child("jailorData");}
else
{dataRef = databaseReference.child(admin_uId).child("prisonerData");
    assignAdminnUidPrisoner=firebaseDatabase.getReference("ADMIN").child("prisonersAdminUId").child(user.getUid());
    assignAdminnUidPrisoner.child("AdminUid").setValue(admin_uId);
}
        //dataRef.child("UID").setValue(uuid);

        DatabaseReference uidRef = dataRef.child(uuid);
        uidRef.child("Name").setValue(name);
        uidRef.child("Email").setValue(email);

        Log.i("info","success");

    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up_);

            usernameEditText = (EditText)findViewById(R.id.usernameEditText);
            passwordEditText = (EditText)findViewById(R.id.passwordEditText);
            nameEditText= (EditText) findViewById(R.id.nameEditText);

            mAuth = FirebaseAuth.getInstance();
            Intent intent=getIntent();
            admin_uId=intent.getStringExtra("UId");
            Log.i("adminID",admin_uId+"44");
             //admin_uId="ADMIN";
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("ADMIN");

        }
        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();
            // updateUI(currentUser);
        }



        public void onClickLogin(View view) {

            if(selectUserIsTrue){

            if(usernameEditText.getText().toString().contains("@") && usernameEditText.getText().toString().length() > 5 && passwordEditText.getText().toString().length() > 5) {
                mAuth.createUserWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    user = mAuth.getCurrentUser();
                                    UpdateFireBase(jailorisTrue , usernameEditText.getText().toString() , nameEditText.getText().toString(), user.getUid() );
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


        }else {
                Toast.makeText(SignUp_Activity.this, "Select Prisoner OR Jailor",
                        Toast.LENGTH_LONG).show();
            }
    }





    }