package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    String[] units = {"Feet", "Inches", "Centimeters", "Meters", "Yards"};
    Spinner fromSpinner, toSpinner;
    EditText inputValue;
    TextView resultText;
    Button convertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        fromSpinner = findViewById(R.id.fromUnit);
        toSpinner = findViewById(R.id.toUnit);
        resultText = findViewById(R.id.resultText);
        convertButton = findViewById(R.id.convertButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputStr = inputValue.getText().toString();
                if (inputStr.isEmpty()) {
                    resultText.setText("Please enter a value");
                    return;
                }

                double input = Double.parseDouble(inputStr);
                String fromUnit = fromSpinner.getSelectedItem().toString();
                String toUnit = toSpinner.getSelectedItem().toString();

                double meters = toMeters(input, fromUnit);
                double result = fromMeters(meters, toUnit);

                resultText.setText(String.format("%.4f %s", result, toUnit));
            }
        });

        Button settingsBtn = findViewById(R.id.settingsButton);
        settingsBtn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });

    }

    double toMeters(double value, String unit) {
        switch (unit) {
            case "Feet": return value * 0.3048;
            case "Inches": return value * 0.0254;
            case "Centimeters": return value * 0.01;
            case "Meters": return value;
            case "Yards": return value * 0.9144;
            default: return value;
        }
    }

    double fromMeters(double value, String unit) {
        switch (unit) {
            case "Feet": return value / 0.3048;
            case "Inches": return value / 0.0254;
            case "Centimeters": return value / 0.01;
            case "Meters": return value;
            case "Yards": return value / 0.9144;
            default: return value;
        }
    }
}