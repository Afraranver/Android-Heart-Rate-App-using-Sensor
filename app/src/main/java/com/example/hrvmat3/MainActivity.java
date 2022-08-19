package com.example.hrvmat3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String ARG_NAME = "username";
    private static Context context;
    Button measureHrv;
    Button stressRelief;

    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        measureHrv= findViewById(R.id.measureHrv);
        stressRelief=findViewById(R.id.stressRelief);

        stressRelief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityOption();
            }
        });

        measureHrv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMeasureActivity();
            }
        });


        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        firebaseAuth = FirebaseAuth.getInstance();

        setUpToolbar();
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {

//Paste your privacy policy link

                    case  R.id.nav_Policy:{

                        startActivity(new Intent(getApplicationContext(),Privacy.class));
                        drawerLayout.closeDrawers();

                    }
                           break;

                    case  R.id.nav_share:{
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody =  "http://play.google.com/store/apps/detail?id=" + getPackageName();
                        String shareSub = "Try now";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
                        drawerLayout.closeDrawers();
                    }
                           break;

                    case R.id.nav_drawer1:{

                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        drawerLayout.closeDrawers();
                    }
                    break;

                    case R.id.nav_drawer2:{

                        startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                        drawerLayout.closeDrawers();
                    }
                    break;

                    case  R.id.nav_logout:{

                        FirebaseAuth.getInstance().signOut();//logout
                         revokeAccess();
                         startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                         Toast.makeText(getApplicationContext(), "You have Successfully Logged Out!!", Toast.LENGTH_SHORT).show();
                         finish();
                         revokeAccess();
                        drawerLayout.closeDrawers();
                    }
                    break;

                    case R.id.nav_about:{

                        startActivity(new Intent(getApplicationContext(),AboutActivity.class));
                        drawerLayout.closeDrawers();
                    }
                }
                return false;
            }
        });
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    public static void startActivity(Context context, String username) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(ARG_NAME, username);
        context.startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



    private void signOut() {
        // Firebase sign out
        firebaseAuth.signOut();
        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Signed out of google");
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        firebaseAuth.signOut();
        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Revoked Access");
                    }
                });
    }

    public void openMeasureActivity(){
        Intent intent= new Intent(this, MeasureActivity.class);
        startActivity(intent);
    }

    public void openActivityOption(){
        Intent intent= new Intent(this, ActivityOption.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing the Application")
                .setMessage("Are you sure you want to exit HRVMAT?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


}