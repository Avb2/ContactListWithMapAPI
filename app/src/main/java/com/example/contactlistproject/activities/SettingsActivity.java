package com.example.contactlistproject.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.contactlistproject.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact_settings);

        initListButton();
        initSettingsButton();
        initMapButton();

        initSortByClick();
        initSortOrderClick();
        initSettings();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    private void initListButton(){
        ImageButton ibList=findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, ContactListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initMapButton(){
        ImageButton ibList=findViewById(R.id.imageButtonMap);
        ibList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initSettingsButton(){
        ImageButton ibSettings = findViewById(R.id.imageButtonSettings);
        ibSettings.setEnabled(false);
    }


    private void initSettings() {
        // Get shared prefs
        String sortBy = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortfield", "contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences"
                , Context.MODE_PRIVATE).getString("sortorder", "ASC");

        // Get Radio Buttons
        RadioButton rbName = findViewById(R.id.radioName);
        RadioButton rbCity = findViewById(R.id.radioCity);
        RadioButton rbBirthDay = findViewById(R.id.radioBirthday);

        // Set Radio buttons checked
        if (sortBy.equalsIgnoreCase("contactname")) {
            rbName.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("city")) {
            rbCity.setChecked(true);
        } else {
            rbBirthDay.setChecked(true);
        }

        // Get  Ascending / descending radio buttons
        RadioButton rbAscending = findViewById(R.id.ascendingRadio);
        RadioButton rbDescending = findViewById(R.id.descendingRadio);
        if (sortOrder.equalsIgnoreCase("ASC")) {
            rbAscending.setChecked(true);
        } else {
            rbDescending.setChecked(true);
        }
    }


    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i){
                RadioButton rbName = findViewById(R.id.radioName);
                RadioButton rbCity = findViewById(R.id.radioCity);

                if (rbName.isChecked()){
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE)
                            .edit()
                            .putString("sortfield", "contactname").apply();
                } else if (rbCity.isChecked()) {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE)
                            .edit()
                            .putString("sortfield", "city")
                            .apply();
                } else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE)
                            .edit()
                            .putString("sortfield", "birthday")
                            .apply();
                }
            }
        });

    }


    private void initSortOrderClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortOrder);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rbAscending = findViewById(R.id.ascendingRadio);

                if (rbAscending.isChecked()){
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE)
                            .edit()
                            .putString("sortorder", "ASC")
                            .apply();
                } else {
                    getSharedPreferences("MyContactListPreferences", Context.MODE_PRIVATE)
                            .edit()
                            .putString("sortorder", "DESC")
                            .apply();
                }
            }
        });
    }
}