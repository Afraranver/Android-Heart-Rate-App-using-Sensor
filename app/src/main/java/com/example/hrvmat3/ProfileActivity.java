package com.example.hrvmat3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {

    private static final int GALLERY_INTENT_CODE = 1023 ;
    TextView fullName,email,age,height,weight,gender,nationality;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    Button changeProfileImage;
    FirebaseUser user;
    Context context = this;

    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private static final String TAG = "ProfileActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);
        age    = findViewById(R.id.profileAge);
        height    = findViewById(R.id.profileHeight);
        weight    = findViewById(R.id.profileWeight);
        gender    = findViewById(R.id.profileGender);
        nationality    = findViewById(R.id.profileNationality);
        changeProfileImage = findViewById(R.id.changeProfile);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                assert documentSnapshot != null;
                if(e == null){
                    if(documentSnapshot.exists()){
                        Log.e("------------", "Firebase exception", e);
                        fullName.setText(documentSnapshot.getString("fName"));
                        email.setText(documentSnapshot.getString("email"));
                        age.setText(documentSnapshot.getString("age"));
                        height.setText(documentSnapshot.getString("height"));
                        weight.setText(documentSnapshot.getString("weight"));
                        gender.setText(documentSnapshot.getString("gender"));
                        nationality.setText(documentSnapshot.getString("nationality"));

                    }else {
                        Log.d("tag", "onEvent: Document do not exists");
                    }
                }else{
//                    Toast.makeText(context, "fail to retrieve", Toast.LENGTH_SHORT).show();
                }

            }
        });


        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(context,EditProfile.class);
                i.putExtra("fullName",fullName.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("age",age.getText().toString());
                i.putExtra("height",height.getText().toString());
                i.putExtra("weight",weight.getText().toString());
                i.putExtra("gender",gender.getText().toString());
                i.putExtra("nationality",nationality.getText().toString());
                startActivity(i);
                finish();


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
                    case  R.id.nav_home:

                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
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
                    case  R.id.nav_Policy:{

                        startActivity(new Intent(getApplicationContext(),Privacy.class));
                        drawerLayout.closeDrawers();

                    }
                    break;

                    case R.id.nav_drawer2:{

                        startActivity(new Intent(getApplicationContext(),HistoryActivity.class));
                        drawerLayout.closeDrawers();
                        finish();
                    }
                    break;

                    case  R.id.nav_logout:{

                        FirebaseAuth.getInstance().signOut();//logout
                        revokeAccess();
                        Toast.makeText(getApplicationContext(), "You have Successfully Logged Out!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
                        drawerLayout.closeDrawers();
                        finishAffinity();
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
        drawerLayout = findViewById(R.id.drawerLayoutProfile);
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