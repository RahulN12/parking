package com.projects.parkingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.gms.maps.model.LatLng;

// This is our MainActivity (Backend) - on app launch
public class MainActivity extends AppCompatActivity {

    // Declare variables to access UI elements
    private SwitchCompat locationSwitch;
    private EditText customLocationLatitude;
    private EditText customLocationLongitude;
    private Spinner spinnerMenu;
    private Button findParkingButton;
    private LatLng customLatLng = null;

    // this function is executed on App launch
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind UI items in variables
        locationSwitch = findViewById(R.id.locationSwitch);
        customLocationLatitude = findViewById(R.id.customLocationLatitude);
        customLocationLongitude = findViewById(R.id.customLocationLongitude);
        spinnerMenu = findViewById(R.id.spinnerMenu);
        findParkingButton = findViewById(R.id.findParkingButton);

        // Set up TextWatchers for customLocationEditText1 and customLocationEditText2
        customLocationLatitude.addTextChangedListener(textWatcher);
        customLocationLongitude.addTextChangedListener(textWatcher);

        // disable find parking button
        findParkingButton.setEnabled(false);

        // display custom input fields if the switch is on
        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // When the switch changes, update the visibility of custom location EditTexts
            if (isChecked) {
                customLocationLatitude.setVisibility(EditText.VISIBLE);
                customLocationLongitude.setVisibility(EditText.VISIBLE);
            } else {
                customLocationLatitude.setVisibility(EditText.INVISIBLE);
                customLocationLongitude.setVisibility(EditText.INVISIBLE);
                // Reset the EditTexts and disable the button
                customLocationLatitude.setText("");
                customLocationLongitude.setText("");
                enableFindParkingButton();
            }
        });

        // get array values of vehicle_types and add them to spinner UI
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vehicle_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(adapter);

        // Set a default selection to the prompt item - Select vehicle type string in res/values/arrays.xml
        spinnerMenu.setSelection(0);

        // Enable parking button on spinner menu item selection
        spinnerMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On spinner item selection - enable find parking button
                enableFindParkingButton();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected - we wait for the user input
            }
        });

        // Open the next activity when the find Parking button is clicked
        findParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent is used to share data from one activity to another
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                // if custom location is set then share it to MapsActivity
                if(customLatLng != null) {
                    // Adding custom Lat Lng to intent
                    intent.putExtra("customLatLng", customLatLng);
                }
                startActivity(intent);
            }
        });
    }

    // Enable or disable the findParkingButton based on conditions
    private void enableFindParkingButton() {
        boolean isCustomLocationEnabled = locationSwitch.isChecked();
        boolean isLatitudeFilled = !customLocationLatitude.getText().toString().trim().isEmpty();
        boolean isLongitudeFilled = !customLocationLongitude.getText().toString().trim().isEmpty();
        // Use spinnerMenu.getSelectedItemPosition() to get the selected position
        boolean isSpinnerItemSelected = spinnerMenu.getSelectedItemPosition() > 0;

        if (isCustomLocationEnabled) {
            findParkingButton.setEnabled(isLatitudeFilled && isLongitudeFilled && isSpinnerItemSelected);
            customLatLng = new LatLng(Double.parseDouble(customLocationLatitude.getText().toString()), Double.parseDouble(customLocationLongitude.getText().toString()));
            if(isLatitudeFilled && isLongitudeFilled){
                customLatLng = new LatLng(Double.parseDouble(customLocationLatitude.getText().toString()), Double.parseDouble(customLocationLongitude.getText().toString()));
                System.out.println("Location in Main -- " + customLatLng);
            }
        } else {
            findParkingButton.setEnabled(isSpinnerItemSelected);
        }
    }

    // TextWatcher to enable/disable the button based on EditText input
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Do nothing
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Check if both EditTexts have non-empty text to enable the button
            boolean isEditText1Filled = !customLocationLatitude.getText().toString().trim().isEmpty();
            boolean isEditText2Filled = !customLocationLongitude.getText().toString().trim().isEmpty();
        }
    };
}