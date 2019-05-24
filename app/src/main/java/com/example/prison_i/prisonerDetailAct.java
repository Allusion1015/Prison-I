package com.example.prison_i;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class prisonerDetailAct extends AppCompatActivity {

    String adminUid;
    String prisonerUid;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    TextView nameTxt;
    TextView emailTxt;
    TextView stepTxt;
    Intent liveLocIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisoner_detail);

        nameTxt = findViewById(R.id.txtName);
        emailTxt = findViewById(R.id.txtEmail);
        stepTxt = findViewById(R.id.txtStepCount);

        liveLocIntent = new Intent(getApplicationContext(), LivelocationOfprisoners.class);
        Intent intent = getIntent();
        adminUid = intent.getStringExtra("adminUid");
        prisonerUid = intent.getStringExtra("prisonerUid");
        Toast.makeText(prisonerDetailAct.this, adminUid + "  " + prisonerUid, Toast.LENGTH_SHORT).show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ADMIN/" + adminUid + "/prisonerData");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int NumberOfPrisoners = (int) dataSnapshot.getChildrenCount();

                Iterable<DataSnapshot> chlNames = dataSnapshot.getChildren();
                Log.i("No. of Prisoners", String.valueOf(NumberOfPrisoners));
                // prisonerNames = new ArrayList<String>();
                // prisonerEmail = new ArrayList<String>();
                // prisonerStepCount = new ArrayList<String>();


                // prisonerNames.add(dataSnapshot.child(contact.getKey()).child("Email").getValue().toString());
                //prisonerEmail.add(dataSnapshot.child(contact.getKey()).child("Name").getValue().toString());


                nameTxt.setText(dataSnapshot.child(prisonerUid).child("Name").getValue().toString());
                emailTxt.setText(dataSnapshot.child(prisonerUid).child("Email").getValue().toString());
                stepTxt.setText("Step Count : "+dataSnapshot.child(prisonerUid).child("StepCount").getValue().toString());
                // PrisonersLocationLAT = dataSnapshot.child(prisonerUid).child("Location").child("latitude").getValue().toString();
                // PrisonersLocationLONG = dataSnapshot.child(prisonerUid).child("Location").child("longitude").getValue().toString();

                   /* if(Float.valueOf(PrisonersLocationLONG[counter]) > 77.93539644 || Float.valueOf(PrisonersLocationLONG[counter]) < 77.93519482 || Float.valueOf(PrisonersLocationLAT[counter]) < 30.40486298 || Float.valueOf(PrisonersLocationLAT[counter]) > 30.40506298 )
                    {
                        locBoundCheck[counter] = 1; // Alarm needed
                    }else{
                        locBoundCheck[counter] = 0;
                    }
                    */

                //Log.d("prisonersID :: ",  PrisonersLocationLAT[counter] +"      " + PrisonersLocationLONG[counter] );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        findViewById(R.id.btnLiveLoc).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                liveLocIntent.putExtra("adminUid",adminUid);
                liveLocIntent.putExtra("prisonerUid",prisonerUid);
                startActivity(liveLocIntent);
            }
        });
    }
}