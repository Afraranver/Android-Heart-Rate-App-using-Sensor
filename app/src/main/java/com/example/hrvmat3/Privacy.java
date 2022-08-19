package com.example.hrvmat3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Privacy extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private static final String TAG = "Privacy";
    private TextView aboutText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        firebaseAuth = FirebaseAuth.getInstance();

        aboutText = (TextView) findViewById(R.id.textView16);

        setUpToolbar();
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_home:

                        Intent intent = new Intent(Privacy.this, MainActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
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
        aboutText.setMovementMethod(new ScrollingMovementMethod());
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayoutPrivacy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

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

}