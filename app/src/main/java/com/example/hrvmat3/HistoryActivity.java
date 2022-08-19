package com.example.hrvmat3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private static final String TAG = "HistoryActivity";

    ListView listView;
    TextView textView;

    List<String> list;
    ArrayAdapter<String> adapter;
    Context context = this;


    private String UserID;
    private String temp= "";
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        firebaseAuth = FirebaseAuth.getInstance();

        setUpToolbar();
        listView=(ListView)findViewById(R.id.listView);
        textView=(TextView)findViewById(R.id.txtMyList);
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.nav_home:

                        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
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

                    case R.id.nav_drawer1:{

                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        drawerLayout.closeDrawers();
                        finish();
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



        UserID = firebaseAuth.getCurrentUser().getUid();
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        rootRef.collection("HeartRate").orderBy("random", Query.Direction.ASCENDING);
        DocumentReference codesRef = rootRef.collection("HeartRate").document(UserID);

        codesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                final DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    list = new ArrayList<>();
                    Map<String, Object> map = document.getData();
                    if (map != null) {
                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                            list.add(entry.getKey());
                        }
                    }

                    Collections.sort(list, new Comparator<String>() {
                        DateFormat f = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                        @Override
                        public int compare(String o1, String o2) {
                            try {
                                return Objects.requireNonNull(f.parse(o2)).compareTo(f.parse(o1));
                            } catch (ParseException e) {
                                throw new IllegalArgumentException(e);
                            }
                        }
                    });

                    //So what you need to do with your list
                    for (String s : list) {
                        adapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
                        listView.setAdapter(adapter);


                        Log.d("TAG", s);
                    }



                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            // TODO Auto-generated method stub
                            String value=adapter.getItem(position);
                            temp = "";
                            Map<String, Object> map = document.getData();
                            if (map != null) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    if(value.equals(entry.getKey())){
                                        temp= entry.getValue().toString();
                                    }

//                                list.add(entry.getKey());
                                }
                                new AlertDialog.Builder(context)
                                        .setTitle("Heart Rate")
                                        .setMessage(temp)

                                        // Specifying a listener allows you to take an action before dismissing the dialog.
                                        // The dialog is automatically dismissed when a dialog button is clicked.
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Continue with delete operation
                                            }
                                        })
                                        .setNegativeButton("Export", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                StringBuilder csvList = new StringBuilder();

                                                if (!temp.isEmpty()) {
                                                    String state = Environment.getExternalStorageState();
                                                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                                                        if (Build.VERSION.SDK_INT >= 23) {
                                                            if (checkPermission()) {
                                                                File sdcard = Environment.getExternalStorageDirectory();
                                                                File dir = new File("sdcard/document/");
                                                                dir.mkdir();
                                                                File file = new File(dir, "HrvResult.txt");
                                                                FileOutputStream os = null;
                                                                try {
                                                                    os = new FileOutputStream(file);
                                                                    os.write(temp.getBytes());
                                                                    os.close();
                                                                    Toast.makeText(HistoryActivity.this, "Result is exported to /Internal Storage/document", Toast.LENGTH_SHORT).show();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            } else {
                                                                requestPermission(); // Code for permission
                                                            }
                                                        } else {
                                                            File sdcard = Environment.getExternalStorageDirectory();
                                                            File dir = new File("sdcard/document/");
                                                            dir.mkdir();
                                                            File file = new File(dir, "HrvResult.txt");
                                                            FileOutputStream os = null;
                                                            try {
                                                                os = new FileOutputStream(file);
                                                                os.write(temp.getBytes());
                                                                os.close();
                                                                Toast.makeText(HistoryActivity.this, "Result is exported to /Internal Storage/document", Toast.LENGTH_SHORT).show();
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                        .setNeutralButton("Share", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                                shareIntent.setType("text/plain");
                                                shareIntent.putExtra(Intent.EXTRA_TEXT, "HrvResult");
                                                shareIntent.putExtra (Intent.EXTRA_STREAM, Uri.parse("sdcard/document/HrvResult.txt"));
                                                startActivity(Intent.createChooser( shareIntent, "Share"));
                                            }
                                        })

                                        // A null listener allows the button to dismiss the dialog and take no further action.
//                                        .setNegativeButton(android.R.string.no, null)
                                        .show();
                            }

                            Toast.makeText(getApplicationContext(),value,Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }


        });

    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(HistoryActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HistoryActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(HistoryActivity.this, "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(HistoryActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
            break;
        }
    }


    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayoutHistory);
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