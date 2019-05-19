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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    String[] PrisonersKeyValue;
    List<PrisonerUID> PrisonerUIDlist;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int counter = 0;





//>>>>>>> master
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prisoners__nav_draw);

        Intent intent=getIntent();
        adminId=intent.getStringExtra("UId");

        setTitle("Prisoners");
        signUpIntent=new Intent(getApplicationContext(),SignUp_Activity.class);

        PrisonerUIDlist = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("TqfQ8qsRDWZfiq177Tq3CZDXai62");

        DatabaseReference databaseReference2 = databaseReference.child("prisonerData").child("sTjfAltD4vf1gLp1dNiIxdz27Gs1");


        databaseReference.child("prisonerData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int NumberOfPrisoners = (int)dataSnapshot.getChildrenCount();
                Log.i("No. of Prisoners", String.valueOf(NumberOfPrisoners));
                PrisonersKeyValue = new String[NumberOfPrisoners];

                PrisonerUID prisonerUID = dataSnapshot.getValue(PrisonerUID.class);


                try {
                    JSONObject jsonObject = new JSONObject(dataSnapshot.getValue().toString());
                    Log.i ("pUID " , jsonObject.getString("sTjfAltD4vf1gLp1dNiIxdz27Gs1"));
                    JSONObject jsonObjectforEmail = new JSONObject(jsonObject.getString("sTjfAltD4vf1gLp1dNiIxdz27Gs1"));
                    Log.i ("Email  " , jsonObjectforEmail.getString("Email"));
                    Log.i ("Email  " , jsonObjectforEmail.getString("Name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
             /*   JSONArray weatherArray = new JSONArray(dataSnapshot.getValue().toString());
                for(int i = 0; i<=weatherArray.length() ; i++ ){
                    JSONObject weatherArrayPart = weatherArray.getJSONObject(i);
                    String main = weatherArrayPart.getString("main");
                    String description = weatherArrayPart.getString("description");
                    Log.i("info :",  main);
                    Log.i("info :", description );
                }
          */      Log.i ("pUID " , dataSnapshot.getValue().toString());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fromFirebaseToAdapterSender(databaseReference2);




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


        recyclerView.setAdapter(new AdapterProgram(NameArray,EmailArray));
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

    public  void fromFirebaseToAdapterSender(DatabaseReference databaseReference){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                NameArray[counter] =  dataSnapshot.child("Name").getValue().toString();
                EmailArray[counter] =  dataSnapshot.child("Email").getValue().toString();
                Log.d("TAG",  dataSnapshot.child("Name").getValue() + "null1234");
                recyclerView.setAdapter(new AdapterProgram(NameArray,EmailArray));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
