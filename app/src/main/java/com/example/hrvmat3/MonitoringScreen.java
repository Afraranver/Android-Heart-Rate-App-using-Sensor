package com.example.hrvmat3;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MonitoringScreen extends Activity {

    private static final String TAG = "BlueTest5-MainActivity";
    private int mMaxChars = 50000;//Default
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;
    private  CountDownTimer yourCountDownTimer;
    private String strInput = "";
    private String bpmValue="";
    private String mydate;
    List<String> list;
    private String listTemp=null;
    private String preValue;
    private double finalValue= 0.0;
    String tempbpm=null;

    FirebaseFirestore fStore;
    String UserID;


    private static final int RC_SIGN_IN = 1001;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;


    private boolean mIsUserInitiatedDisconnect = false;

    // All controls here
    private TextView mTxtReceive;
    private TextView txtTimer;
//    private Button mBtnClearInput;
    private ScrollView scrollView;
//    private CheckBox chkScroll;
//    private CheckBox chkReceiveText;


    private boolean mIsBluetoothConnected = false;

    private BluetoothDevice mDevice;

    private ProgressDialog progressDialog;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";


    SharedPreferences sharedpreferences;


    private int total, i=0, j=0, q, r, s,t,u,v,w,x,y,z,a,b,c,d,e,f,g,h,i1,j1,k,l,m,n,o,p,q1,q2,q3,q4;
    private int x1, x2, x3, x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22,x23,x24,x25,x26,x27,x28,x29,x30;
    private int x1t, x2t, x3t, x4t,x5t,x6t,x7t,x8t,x9t,x10t,x11t,x12t,x13t,x14t,x15t,x16t,x17t,x18t,x19t,x20t,x21t,x22t,x23t,x24t,x25t,x26t,x27t,x28t,x29t,x30t;
    private int hrvFinal, hrvTotal;
    private double avg;
    Context context=this;
    String condition = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_screen);
        ActivityHelper.initialize(this);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(MeasureActivity.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(MeasureActivity.DEVICE_UUID));
        mMaxChars = b.getInt(MeasureActivity.BUFFER_SIZE);
        Log.d(TAG, "Ready");
        mTxtReceive = (TextView) findViewById(R.id.txtReceive);
//        chkScroll = (CheckBox) findViewById(R.id.chkScroll);
//        chkReceiveText = (CheckBox) findViewById(R.id.chkReceiveText);
        scrollView = (ScrollView) findViewById(R.id.viewScroll);
//        mBtnClearInput = (Button) findViewById(R.id.btnClearInput);
        mTxtReceive.setMovementMethod(new ScrollingMovementMethod());
        txtTimer=findViewById(R.id.txtTimer);

        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        yourCountDownTimer = new CountDownTimer(5*60000, 1000) { // adjust the milli seconds here



            public void onTick(long millisUntilFinished) {
                txtTimer.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                yourCountDownTimer.cancel();
                txtTimer.setText("done!");
                saveToDB();


            }

        }.start();

        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        mydate = df.format(Calendar.getInstance().getTime());

    }

    public void onDestroy() {
        super.onDestroy();

    }

    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;

            try {
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i = 0;
                        /*
                         * This is needed because new String(buffer) is taking the entire buffer i.e. 256 chars on Android 2.3.4 http://stackoverflow.com/a/8843462/1287554
                         */
                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        strInput = new String(buffer, 0, i);

                        /*
                         * If checked then receive text, better design would probably be to stop thread if unchecked and free resources, but this is a quick fix
                         */

//                        if (chkReceiveText.isChecked()) {
                            mTxtReceive.post(new Runnable() {
                                @Override
                                public void run() {
                                    mTxtReceive.append(strInput);
                                    ArrayList<String> numbersList = new ArrayList<>();
                                    numbersList.add(strInput);
                                    bpmValue = bpmValue + strInput;


                                    int txtLength = mTxtReceive.getEditableText().length();
                                    if(txtLength > mMaxChars){
                                        mTxtReceive.getEditableText().delete(0, txtLength - mMaxChars);
                                    }

//                                    if (chkScroll.isChecked()) { // Scroll only if this is checked
                                        scrollView.post(new Runnable() { // Snippet from http://stackoverflow.com/a/4612082/1287554
                                            @Override
                                            public void run() {
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });
//                                    }
                                }
                            });
//                        }

                    }
                    Thread.sleep(500);
                }
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        public void stop() {
            bStop = true;
        }

    }

    private void saveToDB(){
            // Sign in success, update UI with the signed-in user's information
            bpmValue = bpmValue.replaceAll("BPM ♥:", "");
           tempbpm = bpmValue;
        Pattern p1 = Pattern.compile("[0-9]+");
        Matcher m1 = p1.matcher(bpmValue);
        while (m1.find()) {
            i = i + 1;

            // append n to list
        }
        total=i;

        Pattern p2 = Pattern.compile("[0-9]+");
        Matcher m2 = p2.matcher(bpmValue);
        while (m2.find()) {
            j = j + 1;
            if(j<total){
                hrvTotal=Integer.parseInt(m2.group());
                hrvFinal+=hrvTotal;
            }

        }

        hrvFinal=hrvFinal/total;
        int temp = 300000/total-1;
        avg = Math.sqrt(temp);
        DecimalFormat df = new DecimalFormat("#.##");
        avg = Double.valueOf(df.format(avg));//
//        hrvFinal= temp/hrvFinal;

        UserID = firebaseAuth.getCurrentUser().getUid();


        final DocumentReference documentReference = fStore.collection("HeartRate").document(UserID);


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference codesRef = rootRef.collection("HeartRate").document(UserID);


        codesRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();

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
                        listTemp = s;
                        break;
                    }
                    Map<String, Object> map2 = document.getData();
                    if (map2 != null) {
                        for (Map.Entry<String, Object> entry : map2.entrySet()) {
                            if(listTemp.equals(entry.getKey())){
                                preValue= entry.getValue().toString();
                            }
//                                list.add(entry.getKey());
                        }

                    }
                }
                if(preValue == null){
                    preValue = "0.0";
                }else{
                    preValue = preValue.substring(17, 22);
                }
                finalValue = avg-Double.parseDouble(preValue);
                if(finalValue>=0.5){
                    condition="Relaxed";
                }else if(finalValue<=0.5){
                    condition="Stressed";
                }else if(finalValue<0.5 && finalValue>-0.5 ){
                    condition="Neutral";
                };

                bpmValue = "Mean RMSSD(ms) : "+avg+" Condition: "+condition+" "+bpmValue;


                final Map<String,Object> user1 = new HashMap<>();
                user1.put(mydate.toString(), bpmValue);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                documentReference.update(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for "+ UserID);
                                        Toast.makeText(MonitoringScreen.this, "Saved", Toast.LENGTH_SHORT).show();
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MonitoringScreen.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("bpmValue",tempbpm);
                                        editor.putString("finalValue", String.valueOf(finalValue));
                                        editor.putString("condition",condition);
                                        editor.apply();
                                        startActivity(new Intent(MonitoringScreen.this,Result.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                        Toast.makeText(MonitoringScreen.this, "Failed", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } else {
                                Log.d(TAG, "No such document");
                                documentReference.set(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for "+ UserID);
                                        Toast.makeText(MonitoringScreen.this, "Saved", Toast.LENGTH_SHORT).show();
                                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MonitoringScreen.this);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("bpmValue",tempbpm);
                                        editor.apply();
                                        startActivity(new Intent(MonitoringScreen.this,Result.class));
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MonitoringScreen.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }

                    }
                });

            }

        });
        codesRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Log.d("Why fail", e);
            }
        });


    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ; // Wait until it stops
                mReadThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
// TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
        }
        Log.d(TAG, "Resumed");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopped");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
// TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MonitoringScreen.this, "Hold on", "Connecting");// http://stackoverflow.com/a/11130220/1287554
        }

        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
// Unable to connect to device
                e.printStackTrace();
                mConnectSuccessful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), "Could not connect to device. Is it a Serial device? Also check if the UUID is correct in the settings", Toast.LENGTH_LONG).show();
                finish();
            } else {
                msg("Connected to device");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput(); // Kick off input reader
            }

            progressDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Do you want to Stop Measuring?")
                .setMessage("The readings will not be saved.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        yourCountDownTimer.cancel();
                        startActivity(new Intent(MonitoringScreen.this,MeasureActivity.class));
                        finish();

                    }

                })
                .setNegativeButton("No", null)
                .show();

    }




}