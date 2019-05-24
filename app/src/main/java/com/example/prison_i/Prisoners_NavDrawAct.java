package com.example.prison_i;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;


public class Prisoners_NavDrawAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Intent signUpIntent;
//<<<<<<< AkanshA0
    String adminId;
//=======
    RecyclerView recyclerView;
    String[] NameArray = {"","","",""};
    String[] EmailArray = {"","","",""};
    String[] StepsArray = {"","","",""};
    String[] PrisonersLocationLAT = {"","","",""};
    String[] PrisonersLocationLONG = {"","","",""};
    String[] prisonerUid={"","","",""};
    Boolean[] WatchOnBodyisTrue = {false,false,false,false};
    int[] locBoundCheck = {0,0,0,0};
    List<PrisonerUID> PrisonerUIDlist;

    Intent intentPrisonerDetail;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int counter = 0;
  //  ArrayList<String> prisonerNames;
   // ArrayList<String> prisonerEmail;
   // ArrayList<String> prisonerStepCount;





//>>>>>>> master
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisoners__nav_draw);

        Intent intent=getIntent();
        adminId=intent.getStringExtra("UId");
      //  adminId = "oiOWeQSV5NSI2rI4vUjYQp1nvE52";   // for test purpose

        intentPrisonerDetail=new Intent(getApplicationContext(),prisonerDetailAct.class);
        setTitle("Prisoners");
        signUpIntent=new Intent(getApplicationContext(),SignUp_Activity.class);

        PrisonerUIDlist = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ADMIN/"+adminId+"/prisonerData");



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int NumberOfPrisoners = (int)dataSnapshot.getChildrenCount();

                Iterable<DataSnapshot> chlNames = dataSnapshot.getChildren();
                Log.i("No. of Prisoners", String.valueOf(NumberOfPrisoners) );
               // prisonerNames = new ArrayList<String>();
               // prisonerEmail = new ArrayList<String>();
               // prisonerStepCount = new ArrayList<String>();
                counter = 0;
                for (DataSnapshot contact : chlNames) {
                   // prisonerNames.add(dataSnapshot.child(contact.getKey()).child("Email").getValue().toString());
                    //prisonerEmail.add(dataSnapshot.child(contact.getKey()).child("Name").getValue().toString());

                    prisonerUid[counter]=contact.getKey();
                    NameArray[counter] = dataSnapshot.child(contact.getKey()).child("Name").getValue().toString();
                    EmailArray[counter] = dataSnapshot.child(contact.getKey()).child("Email").getValue().toString();
                    StepsArray[counter] =  dataSnapshot.child(contact.getKey()).child("StepCount").getValue().toString();
                    PrisonersLocationLAT[counter] = dataSnapshot.child(contact.getKey()).child("Location").child("latitude").getValue().toString();
                    PrisonersLocationLONG[counter] = dataSnapshot.child(contact.getKey()).child("Location").child("longitude").getValue().toString();
                    String WatchOnPrisoner =  dataSnapshot.child(contact.getKey()).child("WatchOnBodyisTrue").getValue().toString();

                    if(WatchOnPrisoner.equals("true")){WatchOnBodyisTrue[counter] = true; }
                    else{WatchOnBodyisTrue[counter] = false;}

                    if(Float.valueOf(PrisonersLocationLONG[counter]) > 77.94528644 || Float.valueOf(PrisonersLocationLONG[counter]) < 77.92528644 || Float.valueOf(PrisonersLocationLAT[counter]) < 30.39496298 || Float.valueOf(PrisonersLocationLAT[counter]) > 30.41496298 || !WatchOnBodyisTrue[counter] )
                    {
                        locBoundCheck[counter] = 1; // Alarm needed
                    }else{
                        locBoundCheck[counter] = 0;
                    }

                    Log.d("prisonersID :: ",  PrisonersLocationLAT[counter] +"      " + PrisonersLocationLONG[counter] );
                    counter++;

                }
                recyclerView.setAdapter(new AdapterProgram(NameArray,EmailArray,StepsArray,locBoundCheck));

              }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        recyclerView.setAdapter(new AdapterProgram(NameArray,EmailArray,StepsArray,locBoundCheck));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(Prisoners_NavDrawAct.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

                intentPrisonerDetail.putExtra("adminUid",adminId);
                intentPrisonerDetail.putExtra("prisonerUid",prisonerUid[position]);
                startActivity(intentPrisonerDetail);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.prisoners__nav_draw, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_addPrisoner) {
        signUpIntent.putExtra("UId",adminId);
            startActivity(signUpIntent);
            // Handle the camera action
        } else if (id == R.id.nav_contactJailor) {

        } else if (id == R.id.nav_alarm) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
