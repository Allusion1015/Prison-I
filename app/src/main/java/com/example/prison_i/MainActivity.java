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

public class MainActivity extends AppCompatActivity {

    Intent JailorLoginIntent;
    Intent AdminSignUpIntent;
    Intent prisonerLoginIntent;

    EditText usernameEditText;
    EditText passwordEditText;
    private FirebaseAuth mAuth;
    private  String TAG ="TAG :";
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ADMIN LOGIN");
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);

//<<<<<<< AkanshA0
        prisonerLoginIntent=new Intent(getApplicationContext(),prisoner_Login_W.class);
  //      AdminSignUpIntent = new Intent(getApplicationContext(),AdminSignUp.class);
//=======
        AdminSignUpIntent = new Intent(getApplicationContext(),LivelocationOfprisoners.class);
//>>>>>>> master
        //SignUPIntent = new Intent(getApplicationContext(),SignUp_Activity.class);

        JailorLoginIntent=new Intent(getApplicationContext(),Login_Activity.class);


        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
    public void onClick(View v){
        startActivity(AdminSignUpIntent);
    }
});
        findViewById(R.id.btnPrisonerLogin).setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(prisonerLoginIntent);
            }
        });
mAuth = FirebaseAuth.getInstance();

       /* findViewById(R.id.IntentCallSignUP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SignUPIntent);
            }
        });
*/
        //intent=new Intent(getApplicationContext() , Login_Activity.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //Toast.makeText(MainActivity.this, mAuth.getCurrentUser().getUid() + "value" , Toast.LENGTH_SHORT).show();
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

                                String UId = user.getUid();
                                Log.i("adminID", UId + "null");

                                JailorLoginIntent.putExtra("UId",UId);

                                startActivity(JailorLoginIntent);
                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }

                            // ...
                        }
                    });
        }else{
            Toast.makeText(MainActivity.this, "INVALID USERNAME OR PASSWORD !",
                    Toast.LENGTH_LONG).show();
        }

    }





}
