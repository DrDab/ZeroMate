package com.dokifusky.zeromate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private Switch angleUnitSwitch;
    private Switch lengthUnitSwitch;

    private EditText zeroingDistanceEditText;
    private EditText xDisplacementEditText;
    private EditText yDisplacementEditText;
    private EditText xClicksPerAngleEditText;
    private EditText yClicksPerAngleEditText;

    private TextView zeroDistanceTextView;
    private TextView xClicksPerAngleTextView;
    private TextView yClicksPerAngleTextView;
    private TextView xDisplacementTextView;
    private TextView yDisplacementTextView;
    private TextView calcuResultTextView;

    private boolean usingMils;
    private boolean usingMetricLength;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        angleUnitSwitch = (Switch) findViewById(R.id.angularUnitsSwitch);
        lengthUnitSwitch = (Switch) findViewById(R.id.lengthUnitSwitch);

        zeroingDistanceEditText = (EditText) findViewById(R.id.zeroingDistanceEditText);
        xDisplacementEditText = (EditText) findViewById(R.id.xDisplacementEditText);
        yDisplacementEditText = (EditText) findViewById(R.id.yDisplacementEditText);
        xClicksPerAngleEditText = (EditText) findViewById(R.id.XClicksPerAngleUnitEditText);
        yClicksPerAngleEditText = (EditText) findViewById(R.id.YClicksPerAngleUnitEditText);

        zeroDistanceTextView = (TextView) findViewById(R.id.zeroingDistanceTextView);
        xClicksPerAngleTextView = (TextView) findViewById(R.id.XClicksPerAngleTextView);
        yClicksPerAngleTextView = (TextView) findViewById(R.id.YClicksPerAngleUnitTextView);
        xDisplacementTextView = (TextView) findViewById(R.id.xDisplacementTextView);
        yDisplacementTextView = (TextView) findViewById(R.id.yDisplacementTextView);
        calcuResultTextView = (TextView) findViewById(R.id.calcResultsTextView);

        usingMils = false;
        usingMetricLength = false;
        updateUnitTexts();
    }

    public void updateUnitTexts()
    {
        if (usingMils)
        {
            xClicksPerAngleTextView.setText("X Clicks per mil");
            yClicksPerAngleTextView.setText("Y Clicks per mil");
            angleUnitSwitch.setText("Using mils");
        }
        else
        {
            xClicksPerAngleTextView.setText("X Clicks per MOA");
            yClicksPerAngleTextView.setText("Y Clicks per MOA");
            angleUnitSwitch.setText("Using MOA");
        }

        if (usingMetricLength)
        {
            xDisplacementTextView.setText("X Displacement (cm)");
            yDisplacementTextView.setText("Y Displacement (cm)");
            zeroDistanceTextView.setText("Zeroing Distance (m)");
            lengthUnitSwitch.setText("Units: Metric");
        }
        else
        {
            xDisplacementTextView.setText("X Displacement (in)");
            yDisplacementTextView.setText("Y Displacement (in)");
            zeroDistanceTextView.setText("Zeroing Distance (yd)");
            lengthUnitSwitch.setText("Units: Freedom");
        }
    }

    public void onAngularUnitsSwitchClicked(View view)
    {
        usingMils = angleUnitSwitch.isChecked();
        updateUnitTexts();
    }

    public void onLengthUnitsSwitchClicked(View view)
    {
        usingMetricLength = lengthUnitSwitch.isChecked();
        updateUnitTexts();
    }

    public void onCalculateButtonClicked(View view)
    {
        try
        {
            double zeroDist = (usingMetricLength ? 1.0 : .0254 * 36.0) * Double.parseDouble(zeroingDistanceEditText.getText().toString());
            double xDisp = (usingMetricLength ? 1.0 : 100.0 * .0254) * Double.parseDouble(xDisplacementEditText.getText().toString()) / 100.0;
            double yDisp = (usingMetricLength ? 1.0 : 100.0 * .0254) * Double.parseDouble(yDisplacementEditText.getText().toString()) / 100.0;
            double xClicksPerAng = Double.parseDouble(xClicksPerAngleEditText.getText().toString());
            double yClicksPerAng = Double.parseDouble(yClicksPerAngleEditText.getText().toString());
            double xAng = -Math.atan(xDisp / zeroDist) * (usingMils ? 1000.0 : 60.0 * 180.0 / Math.PI);
            double yAng = -Math.atan(yDisp / zeroDist) * (usingMils ? 1000.0 : 60.0 * 180.0 / Math.PI);
            double xClicks = xAng * xClicksPerAng;
            double yClicks = yAng * yClicksPerAng;
            String out = "";
            out += "Adjustments:\n";
            boolean adjustments = xClicks != 0 || yClicks != 0;
            if (xClicks != 0)
            {
                out += String.format("%.3f clicks %s\n", Math.abs(xClicks), xClicks > 0 ? "right" : "left");
            }
            if (yClicks != 0)
            {
                out += String.format("%.3f clicks %s\n", Math.abs(yClicks), yClicks > 0 ? "up" : "down");
            }
            out += !adjustments ? "None\n" : "";
            out += "---------------\n";
            String angUnit = usingMils ? "mrad" : "MOA";
            out += String.format("X angle offset: %.6f %s\nY angle offset: %.6f %s", xAng, angUnit, yAng, angUnit);
            calcuResultTextView.setText(out);
        }
        catch (Exception e)
        {
            calcuResultTextView.setText("Error: " + e);
        }
    }
}