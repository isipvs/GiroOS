package ru.mpt.p50_4_19.giroos;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private SensorManager sysmanager;
    private Sensor sensor;
    private ImageView img;
    private TextView txt;
    private TextView txty;
    private SensorEventListener sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txt);
        txty = findViewById(R.id.txty);
        img = findViewById(R.id.img);

        sysmanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sysmanager != null)
            sensor = sysmanager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sv = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, event.values
                );
                float[] remappedRotationmatrix = new float[16];
                {

                    SensorManager.remapCoordinateSystem(rotationMatrix,
                            SensorManager.AXIS_X,
                            SensorManager.AXIS_Z,
                            remappedRotationmatrix);

                    float[] orientation = new float[3];
                    SensorManager.getOrientation(remappedRotationmatrix, orientation);

                    for (int i = 0; i < 3; i++) {
                        orientation[i] = (float) (Math.toDegrees(orientation[i]));
                    }

                    txt.setText("x:  " + String.valueOf((int) orientation[2]));

                    img.setRotation(-orientation[2]);
                }

                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_Y,
                        SensorManager.AXIS_Z,
                        remappedRotationmatrix);

                float[] orientation = new float[3];
                SensorManager.getOrientation(remappedRotationmatrix, orientation);

                for (int i = 0; i < 3; i++) {
                    orientation[i] = (float) (Math.toDegrees(orientation[i]));
                }

                txty.setText( "y: " + String.valueOf((int) orientation[2]));

                img.setRotation(-orientation[2]);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sysmanager.registerListener(sv, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sysmanager.unregisterListener(sv);
    }
}