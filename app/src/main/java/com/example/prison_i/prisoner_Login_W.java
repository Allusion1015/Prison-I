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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class prisoner_Login_W extends AppCompatActivity implements SensorEventListener{
    EditText usernameEditText;
    EditText passwordEditText;
    private FirebaseAuth mAuth;
    private  String TAG ="TAG :";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase2;
    DatabaseReference databaseReference2;
    ImageView imageView;
    String curUsrId;
    Button Loginbutton;

    boolean loginSuccess;

    private SensorManager sensorManager;
    boolean activityRunning ;
    TextView stepView;


    int counter = 0;
    ArrayList<String> prisonerUID;
    ArrayList<String> adminUID;
    String AdminOfPrisoner;


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

        firebaseDatabase2 = FirebaseDatabase.getInstance();

        imageView = findViewById(R.id.ImageViewPanel);
        Loginbutton = (Button)findViewById(R.id.loginButton);

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
            {databaseReference2.child("StepCount").setValue(String.valueOf(event.values[0]));}
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
                                final FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(prisoner_Login_W.this, "Authentication sucessful. "+user.getUid(), Toast.LENGTH_SHORT).show();

                                curUsrId=user.getUid();
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference("ADMIN/prisonersAdminUId");



                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        int NumberOfPrisoners = (int)dataSnapshot.getChildrenCount();

                                        Iterable<DataSnapshot> chlNames = dataSnapshot.getChildren();
                                        Log.i("No. of Prisoners", String.valueOf(NumberOfPrisoners) );
                                        prisonerUID = new ArrayList<String>();
                                        adminUID = new ArrayList<String>();
                                        counter = 0;
                                        for (DataSnapshot contact : chlNames) {
                                            prisonerUID.add(contact.getKey());
                                            adminUID.add(dataSnapshot.child(contact.getKey()).child("AdminUid").getValue().toString());

                                            Log.d("prisonersID :: ",  adminUID.get(0)+"      "  + prisonerUID.get(0) );
                                            counter++;

                                        }

                                        AdminOfPrisoner = FindAdminId(user,adminUID,prisonerUID);

                                        databaseReference2 = firebaseDatabase2.getReference("ADMIN/" + AdminOfPrisoner + "/prisonerData/" + curUsrId);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                loginSuccess=true;

                                imageView.setVisibility(View.VISIBLE);
                                Loginbutton.setVisibility(View.INVISIBLE);
                                usernameEditText.setVisibility(View.INVISIBLE);
                                passwordEditText.setVisibility(View.INVISIBLE);


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


    public String FindAdminId(FirebaseUser user , ArrayList<String> adminUID , ArrayList<String> PrisonerUID){
        String prisonerUid = user.getUid();
        int SIZE = adminUID.size();

        for(int i = 0 ; i<=SIZE ; i++ ){
            if(prisonerUid.equals(PrisonerUID.get(i))){
                return adminUID.get(i);
            }
        }

        return "ERROR : ADMIN NOT FOUND !";

    }

}
