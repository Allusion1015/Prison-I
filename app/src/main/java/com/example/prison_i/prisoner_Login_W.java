package com.example.prison_i;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.util.List;
import java.util.Locale;

public class prisoner_Login_W extends AppCompatActivity implements SensorEventListener {
    EditText usernameEditText;
    EditText passwordEditText;
    private FirebaseAuth mAuth;
    private String TAG = "TAG :";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase2;
    DatabaseReference databaseReference2;
    ImageView imageView;
    String curUsrId;
    Button Loginbutton;
    Location locationSet;
    LocationManager locationManager;
    LocationListener locationListener;

    private SensorManager mSensorManager;
    private Sensor mProximity;
    private static final int SENSOR_SENSITIVITY = 4;


    boolean loginSuccess;
    boolean WatchOnBodyisTrue;

    private SensorManager sensorManager;
    boolean activityRunning;
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
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        WatchOnBodyisTrue = true;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepView = (TextView) findViewById(R.id.stepView);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase2 = FirebaseDatabase.getInstance();

        imageView = findViewById(R.id.ImageViewPanel);
        Loginbutton = (Button) findViewById(R.id.loginButton);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i("info :", "location");
                locationSet = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
            locationSet = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(prisoner_Login_W.this, "SENSOR_NOT_AVAILABLE", Toast.LENGTH_SHORT).show();
        }

        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    protected void onPause() {
        super.onPause();
        activityRunning = false;


        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (activityRunning) {
            if (loginSuccess) {
                if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                    stepView.setText(String.valueOf(event.values[0]));
                    databaseReference2.child("StepCount").setValue(String.valueOf(event.values[0]));
                }
                databaseReference2.child("Location").setValue(locationSet);
               // Log.i("location ", locationSet.toString());
            }
        }


        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -SENSOR_SENSITIVITY && event.values[0] <= SENSOR_SENSITIVITY) {
                //near
                Toast.makeText(getApplicationContext(), "near", Toast.LENGTH_SHORT).show();
            } else {
                //far
                if(loginSuccess) {
                    WatchOnBodyisTrue = false;
                    databaseReference2.child("WatchOnBodyisTrue").setValue(WatchOnBodyisTrue);
                }
                Toast.makeText(getApplicationContext(), "far", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

            }
        }
    }


    public void onClickLogin(View view) {

        if (usernameEditText.getText().toString().contains("@") && usernameEditText.getText().toString().length() > 5 && passwordEditText.getText().toString().length() > 5) {


            mAuth.signInWithEmailAndPassword(usernameEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Log.d(TAG, "signInWithEmail:success");
                                final FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(prisoner_Login_W.this, "Authentication sucessful. " + user.getUid(), Toast.LENGTH_SHORT).show();

                                curUsrId = user.getUid();
                                firebaseDatabase = FirebaseDatabase.getInstance();
                                databaseReference = firebaseDatabase.getReference("ADMIN/prisonersAdminUId");


                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        int NumberOfPrisoners = (int) dataSnapshot.getChildrenCount();

                                        Iterable<DataSnapshot> chlNames = dataSnapshot.getChildren();
                                        Log.i("No. of Prisoners", String.valueOf(NumberOfPrisoners));
                                        prisonerUID = new ArrayList<String>();
                                        adminUID = new ArrayList<String>();
                                        counter = 0;
                                        for (DataSnapshot contact : chlNames) {
                                            prisonerUID.add(contact.getKey());
                                            adminUID.add(dataSnapshot.child(contact.getKey()).child("AdminUid").getValue().toString());

                                            Log.d("prisonersID :: ", adminUID.get(0) + "      " + prisonerUID.get(0));
                                            counter++;

                                        }

                                        AdminOfPrisoner = FindAdminId(user, adminUID, prisonerUID);

                                        databaseReference2 = firebaseDatabase2.getReference("ADMIN/" + AdminOfPrisoner + "/prisonerData/" + curUsrId);
                                        databaseReference2.child("WatchOnBodyisTrue").setValue(WatchOnBodyisTrue);

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                loginSuccess = true;

                                imageView.setVisibility(View.VISIBLE);
                                Loginbutton.setVisibility(View.INVISIBLE);
                                usernameEditText.setVisibility(View.INVISIBLE);
                                passwordEditText.setVisibility(View.INVISIBLE);


                            } else {
                                loginSuccess = false;

                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(prisoner_Login_W.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });
        } else {
            Toast.makeText(prisoner_Login_W.this, "INVALID USERNAME OR PASSWORD !",
                    Toast.LENGTH_LONG).show();
        }

    }


    public String FindAdminId(FirebaseUser user, ArrayList<String> adminUID, ArrayList<String> PrisonerUID) {
        String prisonerUid = user.getUid();
        int SIZE = adminUID.size();

        for (int i = 0; i <= SIZE; i++) {
            if (prisonerUid.equals(PrisonerUID.get(i))) {
                return adminUID.get(i);
            }
        }

        return "ERROR : ADMIN NOT FOUND !";

    }






}
