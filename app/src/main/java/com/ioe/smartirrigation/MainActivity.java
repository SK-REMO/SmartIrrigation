package com.ioe.smartirrigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ToggleButton btn,btn1;
    TextView soildmois, soilMoistureValue, soilTemperatureValue, weatherHumidityValue;
    private DatabaseReference mDatabase;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.automanual);
        btn1 = findViewById(R.id.OnOff);
        soildmois = findViewById(R.id.soilMoisture);

        soilMoistureValue = findViewById(R.id.soilMoistureValue);
        soilTemperatureValue = findViewById(R.id.soilTemperatureValue);
        weatherHumidityValue = findViewById(R.id.weatherHumidityValue);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btn1.setVisibility(View.GONE);
                    //mDatabase.child("Irrigation_Data").child("pump_status").setValue("off");
                    mDatabase.child("Irrigation_Data").child("status").setValue("auto");
                    mDatabase.child("Irrigation_Data").child("pump_status").setValue("off");
                    Toast.makeText(MainActivity.this,"ON",Toast.LENGTH_SHORT).show();
                } else {
                    btn1.setVisibility(View.VISIBLE);
                    mDatabase.child("Irrigation_Data").child("status").setValue("manual");

                    btn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                //ON
                                mDatabase.child("Irrigation_Data").child("pump_status").setValue("on");
                            }
                            else{
                                //OFF
                                mDatabase.child("Irrigation_Data").child("pump_status").setValue("off");
                            }
                        }
                    });

                    Toast.makeText(MainActivity.this,"OFF",Toast.LENGTH_SHORT).show();
            }
        }




    });

        //Read Data
        mDatabase.child("Irrigation_Data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String soil_humidity = dataSnapshot.child("soil_humidity").getValue(String.class);
                        String soil_moisture = dataSnapshot.child("soil_moisture").getValue(String.class);
                        String soil_temp = dataSnapshot.child("soil_temp").getValue(String.class);

                        String status = dataSnapshot.child("status").getValue(String.class);
                        if(status.equals("auto")){
                            btn.setChecked(true);
                            btn1.setVisibility(View.GONE);
                        }
                        else{
                            String pump_status = dataSnapshot.child("pump_status").getValue(String.class);
                            btn.setChecked(false);
                            btn1.setVisibility(View.VISIBLE);
                            if(pump_status.equals("on")){
                                btn1.setChecked(true);
                            }
                            else{
                                btn1.setChecked(false);
                            }

                        }
                        System.out.println(soil_humidity + soil_moisture + soil_temp);

                        soilMoistureValue.setText(soil_moisture + " %");
                        soilTemperatureValue.setText(soil_temp + " \u2103");
                        weatherHumidityValue.setText(soil_humidity + " %");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
}
}
