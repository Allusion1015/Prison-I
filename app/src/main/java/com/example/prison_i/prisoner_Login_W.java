package com.example.prison_i;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class prisoner_Login_W extends AppCompatActivity implements SensorEventListener{
    EditText usernameEditText;
    EditText passwordEditText;
    private FirebaseAuth mAuth;
    private  String TAG ="TAG :";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dataRef;
    String curUsrId;

    boolean loginSuccess;

    private SensorManager sensorManager;
    boolean activityRunning ;
    TextView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisoner__login__w);
        setTitle("Prisoner Login");
        usernameEditText = (EditText)findViewById(R.id.usernameEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepView = (TextView)findViewById(R.id.stepView);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener( this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(prisoner_Login_W.this, "SENSOR_NOT_AVAILABLE", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(activityRunning){
            stepView.setText(String.valueOf(event.values[0]));
            if(loginSuccess)
            {dataRef.child("StepCount").setValue(String.valueOf(event.values[0]));}
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void onClickLogin(View view) {

        if(usernameEditText.getText().toString().contains("@") && usernameEditText.getText().toString().length() > 5 && passwordEditText.getText().toString().length() > 5) {


            mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(prisoner_Login_W.this, "Authentication sucessful. "+user.getUid(), Toast.LENGTH_SHORT).show();
                                loginSuccess=true;
                                curUsrId=user.getUid();
                                DatabaseReference toAccessAdminUid=firebaseDatabase.getReference("ADMIN");
                                //DatabaseReference temp=toAccessAdminUid.child("prisonersAdminUId").child(curUsrId);
                                //String adminUidFromPrisoner=temp.child("AdminUid");

                                //databaseReference=firebaseDatabase.getReference(adminUidFromPrisoner);
                                dataRef = databaseReference.child("prisonerData").child("UID").child(curUsrId);


                            } else {
                                loginSuccess=false;

                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(prisoner_Login_W.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        }else{
            Toast.makeText(prisoner_Login_W.this, "INVALID USERNAME OR PASSWORD !",
                    Toast.LENGTH_LONG).show();
        }

    }



}
