package com.example.hrvmat3;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.NonNull;


import android.content.Intent;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;


import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;
import java.util.Map;


public class EditProfile extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileFullName,profileEmail,profileAge,profileHeight,profileWeight;

    Button saveBtn;
    Spinner profileGender, profileNationality;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent data = getIntent();
        final String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String age = data.getStringExtra("age");
        String height = data.getStringExtra("height");
        String weight = data.getStringExtra("weight");
        String genderpa = data.getStringExtra("gender");
        String nationalitypa = data.getStringExtra("nationality");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        profileFullName = findViewById(R.id.edtProfileName);
        profileEmail = findViewById(R.id.edtProfileEmail);
        profileAge = findViewById(R.id.edtProfileAge);
        profileHeight= findViewById(R.id.edtProfileHeight);
        profileWeight= findViewById(R.id.edtProfileWeight);
        profileGender= findViewById(R.id.edtGender);
        profileNationality= findViewById(R.id.edtNationality);

        profileEmail.setFocusable(false);



        saveBtn = findViewById(R.id.saveProfileInfo);


        String[] gender=getResources().getStringArray(R.array.gender_arrays);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, gender);

        profileGender.setAdapter(adapter);


        String[] country=getResources().getStringArray(R.array.country_arrays);

        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, country);

        profileNationality.setAdapter(adapter2);






        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profileFullName.getText().toString().isEmpty() || profileEmail.getText().toString().isEmpty()){
                    Toast.makeText(EditProfile.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",email);
                        edited.put("fName",profileFullName.getText().toString());
                        edited.put("age",profileAge.getText().toString());
                        edited.put("height",profileHeight.getText().toString());
                        edited.put("weight",profileWeight.getText().toString());
                        edited.put("gender",profileGender.getSelectedItem().toString());
                        edited.put("nationality",profileNationality.getSelectedItem().toString());
//                        edited.put("phone",profilePhone.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                                finish();
                            }
                        });
//                        Toast.makeText(EditProfile.this, "Email is changed.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        profileEmail.setText(email);
        profileFullName.setText(fullName);
        profileAge.setText(age);
        profileHeight.setText(height);
        profileWeight.setText(weight);

        if (genderpa != null) {
            int spinnerPosition = adapter.getPosition(genderpa);
            profileGender.setSelection(spinnerPosition);
        }

        if (nationalitypa != null) {
            int spinnerPosition = adapter2.getPosition(nationalitypa);
            profileNationality.setSelection(spinnerPosition);
        }


        Log.d(TAG, "onCreate: " + fullName + " " + email );
    }



}