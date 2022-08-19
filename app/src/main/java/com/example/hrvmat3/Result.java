package com.example.hrvmat3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Result extends AppCompatActivity {

    Button backToMenu,hrvButton;
    private String bpmValue;
    private int total, i=0, j=0, q, r, s,t,u,v,w,x,y,z,a,b,c,d,e,f,g,h,i1,j1,k,l,m,n,o,p,q1,q2,q3,q4;
    private int x1, x2, x3, x4,x5,x6,x7,x8,x9,x10,x11,x12,x13,x14,x15,x16,x17,x18,x19,x20,x21,x22,x23,x24,x25,x26,x27,x28,x29,x30;
    private int x1t, x2t, x3t, x4t,x5t,x6t,x7t,x8t,x9t,x10t,x11t,x12t,x13t,x14t,x15t,x16t,x17t,x18t,x19t,x20t,x21t,x22t,x23t,x24t,x25t,x26t,x27t,x28t,x29t,x30t;
    private int hrvFinal, hrvTotal;
    private double avg;
    Context context=this;
    String condition;
    private String finalValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        backToMenu=findViewById(R.id.backToMenu);
        hrvButton=findViewById(R.id.hrvButton);

        hrvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Analysis of HRV")
                        .setMessage("Mean RMSSD(ms) : " + avg +"\n"+"Condition: "+ condition)

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
//                        .setNegativeButton(android.R.string.no, null)
//                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        bpmValue = preferences.getString("bpmValue", "");
        condition = preferences.getString("condition", "");
        finalValue = preferences.getString("finalValue", "");


        Pattern p1 = Pattern.compile("[0-9]+");
        Matcher m1 = p1.matcher(bpmValue);
        while (m1.find()) {
            i = i + 1;

            // append n to list
        }
        total=i;
        i = i /30;
        q = i+i;
        r = q+i;
        s = r + i;
        t=s+i;
        u=t+i;
        v=u+i;
        w=v+i;
        x=w+i;
        y=x+i;
        z=y+i;
        a=z+i;
        b=a+i;
        c=b+i;
        d=c+i;
        e=d+i;
        f=e+i;
        g=f+i;
        h=g+i;
        i1=h+i;
        j1=i1+i;
        k=j1+i;
        l=k+i;
        m=l+i;
        n=m+i;
        o=n+i;
        p=o+i;
        q1=p+i;
        q2=q1+i;
        q3=q2+i;
        q4=q3+i;



        Pattern p2 = Pattern.compile("[0-9]+");
        Matcher m2 = p2.matcher(bpmValue);
        while (m2.find()) {
            j = j + 1;
            if(j<total){
                hrvTotal=Integer.parseInt(m2.group());
                hrvFinal+=hrvTotal;
            }
            if(j<i){
                x1 = Integer.parseInt(m2.group());
                x1t += x1;

            }else if(j<q && j>i){
                x2 = Integer.parseInt(m2.group());
                x2t += x2;
            }else if(j<r && j>q){
                x3 = Integer.parseInt(m2.group());
                x3t += x3;
            }else if(j<s && j>r){
                x4 = Integer.parseInt(m2.group());
                x4t += x4;
            }else if(j<t && j>s){
                x5 = Integer.parseInt(m2.group());
                x5t += x5;
            }else if(j<u && j>t){
                x6 = Integer.parseInt(m2.group());
                x6t += x6;
            }else if(j<v && j>u){
                x7 = Integer.parseInt(m2.group());
                x7t += x7;
            }else if(j<w && j>v){
                x8 = Integer.parseInt(m2.group());
                x8t += x8;
            }else if(j<x && j>w){
                x9 = Integer.parseInt(m2.group());
                x9t += x9;
            }else if(j<y && j>x){
                x10 = Integer.parseInt(m2.group());
                x10t += x10;
            }else if(j<z && j>y){
                x11 = Integer.parseInt(m2.group());
                x11t += x11;
            }else if(j<a && j>z){
                x12 = Integer.parseInt(m2.group());
                x12t += x12;
            }else if(j<b && j>a){
                x13 = Integer.parseInt(m2.group());
                x13t += x13;
            }else if(j<c && j>b){
                x14 = Integer.parseInt(m2.group());
                x14t += x14;
            }else if(j<d && j>c){
                x15 = Integer.parseInt(m2.group());
                x15t += x15;
            }else if(j<e && j>d){
                x16 = Integer.parseInt(m2.group());
                x16t += x16;
            }else if(j<f && j>e){
                x17 = Integer.parseInt(m2.group());
                x17t += x17;
            }else if(j<g && j>f){
                x18 = Integer.parseInt(m2.group());
                x18t += x18;
            }else if(j<h && j>g){
                x19 = Integer.parseInt(m2.group());
                x19t += x19;
            }else if(j<i1 && j>h){
                x20 = Integer.parseInt(m2.group());
                x20t += x20;
            }else if(j<j1 && j>i1){
                x21 = Integer.parseInt(m2.group());
                x21t += x21;
            }else if(j<k && j>j1){
                x22 = Integer.parseInt(m2.group());
                x22t += x22;
            }else if(j<l && j>k){
                x23 = Integer.parseInt(m2.group());
                x23t += x23;
            }else if(j<m && j>l){
                x24 = Integer.parseInt(m2.group());
                x24t += x24;
            }else if(j<n && j>m){
                x25 = Integer.parseInt(m2.group());
                x25t += x25;
            }else if(j<o && j>n){
                x26 = Integer.parseInt(m2.group());
                x26t += x26;
            }else if(j<p && j>o){
                x27 = Integer.parseInt(m2.group());
                x27t += x27;
            }else if(j<q1 && j>p){
                x28 = Integer.parseInt(m2.group());
                x28t += x28;
            }else if(j<q2 && j>q1){
                x29 = Integer.parseInt(m2.group());
                x29t += x29;
            }else if(j<q3 && j>q2){
                x30 = Integer.parseInt(m2.group());
                x30t += x30;
            }



        }
        x1t = x1t/i;
        x2t = x2t/i;
        x3t = x3t/i;
        x4t = x4t/i;
        x5t = x5t/i;
        x6t = x6t/i;
        x7t = x7t/i;
        x8t = x8t/i;
        x9t = x9t/i;
        x10t = x10t/i;
        x11t = x11t/i;
        x12t = x12t/i;
        x13t = x13t/i;
        x14t = x14t/i;
        x15t = x15t/i;
        x16t = x16t/i;
        x17t = x17t/i;
        x18t = x18t/i;
        x19t = x19t/i;
        x20t = x20t/i;
        x21t = x21t/i;
        x22t = x22t/i;
        x23t = x23t/i;
        x24t = x24t/i;
        x25t = x25t/i;
        x26t = x26t/i;
        x27t = x27t/i;
        x28t = x28t/i;
        x29t = x29t/i;
        x30t = x30t/i;

        hrvFinal=hrvFinal/total;
        int temp = 300000/total-1;
        avg = Math.sqrt(temp);
        DecimalFormat df = new DecimalFormat("#.##");
        avg = Double.valueOf(df.format(avg));//        hrvFinal= temp/hrvFinal;




        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);


        cartesian.padding(20d, 50d, 50d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

//        cartesian.title("Trend of Sales of the Most Popular Products of ACME Corp.");
        cartesian.title("Result");
        cartesian.yAxis(0).title("Heart Rate");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);
        cartesian.xAxis(0).title("Time(sec)");

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("0", 0));
        seriesData.add(new CustomDataEntry("10", x1t));
        seriesData.add(new CustomDataEntry("20", x2t));
        seriesData.add(new CustomDataEntry("30", x3t));
        seriesData.add(new CustomDataEntry("40", x4t));
        seriesData.add(new CustomDataEntry("50", x5t));
        seriesData.add(new CustomDataEntry("60", x6t));
        seriesData.add(new CustomDataEntry("70", x7t));
        seriesData.add(new CustomDataEntry("80", x8t));
        seriesData.add(new CustomDataEntry("90", x9t));
        seriesData.add(new CustomDataEntry("100", x10t));
        seriesData.add(new CustomDataEntry("110", x11t));
        seriesData.add(new CustomDataEntry("120", x12t));
        seriesData.add(new CustomDataEntry("130", x13t));
        seriesData.add(new CustomDataEntry("140", x14t));
        seriesData.add(new CustomDataEntry("150", x15t));
        seriesData.add(new CustomDataEntry("160", x16t));
        seriesData.add(new CustomDataEntry("170", x17t));
        seriesData.add(new CustomDataEntry("180", x18t));
        seriesData.add(new CustomDataEntry("190", x19t));
        seriesData.add(new CustomDataEntry("200", x20t));
        seriesData.add(new CustomDataEntry("210", x21t));
        seriesData.add(new CustomDataEntry("220", x22t));
        seriesData.add(new CustomDataEntry("230", x23t));
        seriesData.add(new CustomDataEntry("240", x24t));
        seriesData.add(new CustomDataEntry("250", x25t));
        seriesData.add(new CustomDataEntry("260", x26t));
        seriesData.add(new CustomDataEntry("270", x27t));
        seriesData.add(new CustomDataEntry("280", x28t));
        seriesData.add(new CustomDataEntry("290", x29t));
        seriesData.add(new CustomDataEntry("300", x30t));


        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");



        Line series1 = cartesian.line(series1Mapping);
        series1.name("Heart Rate");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);



        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);

    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);

    }

    }


    public void openMenu(){
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}