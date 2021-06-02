package ru.mirea.koskin.mireaproject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Practice5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Practice5 extends Fragment implements SensorEventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView azimuthTextView;
    private TextView pitchTextView;
    private TextView rollTextView;

    private TextView gravityX;
    private TextView gravityY;
    private TextView gravityZ;

    private TextView gyroX;
    private TextView gyroY;
    private TextView gyroZ;

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private Sensor gravitySensor;
    private Sensor gyroSensor;



    private Uri imageUri;
    static final int CAMERA_REQUEST = 1;
    private ImageView imageView;
    final String TAG = MainActivity.class.getSimpleName();
    private boolean isWork = false;
    private static final int REQUEST_CODE_PERMISSION_CAMERA = 100;


    private static final int REQUEST_CODE_PERMISSION = 100;
    private String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private boolean isWork_REC = false;

    private Button startRecordButton;
    private Button stopRecordButton;
    private MediaRecorder mediaRecorder;
    private File audioFile;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Practice5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Practice5.
     */
    // TODO: Rename and change types and number of parameters
    public static Practice5 newInstance(String param1, String param2) {
        Practice5 fragment = new Practice5();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sensorManager =
                (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravitySensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_GRAVITY);
        gyroSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        return inflater.inflate(R.layout.fragment_practice5, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        azimuthTextView = getView().findViewById(R.id.textViewAzimuth);
        pitchTextView = getView().findViewById(R.id.textViewPitch);
        rollTextView = getView().findViewById(R.id.textViewRoll);
        gravityX = getView().findViewById(R.id.gravityX);
        gravityY = getView().findViewById(R.id.gravityY);
        gravityZ = getView().findViewById(R.id.gravityZ);
        gyroX = getView().findViewById(R.id.gyroX);
        gyroY = getView().findViewById(R.id.gyroY);
        gyroZ = getView().findViewById(R.id.gyroZ);


        imageView = getView().findViewById(R.id.imageView_Camera);
        getView().findViewById(R.id.button_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("PRAC5", "1CAMERA GETSHOT: "+isWork);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.d("PRAC5", "CAMERA GETSHOT: "+isWork);
                if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null && isWork == true)
                {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // генерирование пути к файлу на основе authorities
                    String authorities = getActivity().getApplicationContext().getPackageName() + ".fileprovider";
                    imageUri = FileProvider.getUriForFile(getActivity(), authorities, photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        int cameraPermissionStatus =
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            // Выполняется запрос к пользователь на получение необходимых разрешений
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_CAMERA);
        }




        startRecordButton = getView().findViewById(R.id.btnRecStart);
        stopRecordButton = getView().findViewById(R.id.btnRecStop);

        mediaRecorder = new MediaRecorder();

        isWork_REC = hasPermissions(getActivity(), PERMISSIONS);
        if (!isWork_REC) {
            requestPermissions(PERMISSIONS,
                    REQUEST_CODE_PERMISSION);
        }
        getView().findViewById(R.id.btnRecStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startRecordButton.setEnabled(false);
                    stopRecordButton.setEnabled(true);
                    stopRecordButton.requestFocus();
                    startRecording();
                } catch (Exception e) {
                    Log.e(TAG, "Caught io exception " + e.getMessage());
                }
            }
        });
        getView().findViewById(R.id.btnRecStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecordButton.setEnabled(true);
                stopRecordButton.setEnabled(false);
                startRecordButton.requestFocus();
                stopRecording();
                processAudioFile();
            }
        });

        getView().findViewById(R.id.btnPlayStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Player","Start");
                Intent intent = new Intent(getActivity(), MyMediaPlayerService.class);
                intent.putExtra("audio", "prac5");
                getActivity().startService(intent);
            }
        });
        getView().findViewById(R.id.btnPlayStop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Player","Stop");
                getActivity().stopService(
                        new Intent(getActivity(), MyMediaPlayerService.class));
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accelerometerSensor);
        sensorManager.unregisterListener(this, gravitySensor);
        sensorManager.unregisterListener(this, gyroSensor);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gravitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float valueAzimuth = event.values[0];
            float valuePitch = event.values[1];
            float valueRoll = event.values[2];
            azimuthTextView.setText("Azimuth: " + valueAzimuth);
            pitchTextView.setText("Pitch: " + valuePitch);
            rollTextView.setText("Roll: " + valueRoll);
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float val_gravityX = event.values[0];
            float val_gravityY = event.values[1];
            float val_gravityZ = event.values[2];
            gravityX.setText("X: " + val_gravityX);
            gravityY.setText("Y: " + val_gravityY);
            gravityZ.setText("Z: " + val_gravityZ);
        }
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float val_gyroX = event.values[0];
            float val_gyroY = event.values[1];
            float val_gyroZ = event.values[2];
            gyroX.setText("X: " + val_gyroX);
            gyroY.setText("Y: " + val_gyroY);
            gyroZ.setText("Z: " + val_gyroZ);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("PRAC5", "PERMISSION");
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                isWork = true;
            } else {
                isWork = false;
            }
        }
        if (requestCode == REQUEST_CODE_PERMISSION) {
            // permission granted
            isWork_REC = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        else {
            isWork_REC = false;
        }
    }




    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    private void startRecording() throws IOException {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "sd-card success");

            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            if (audioFile == null) {
                audioFile = new File(getActivity().getExternalFilesDir(
                        Environment.DIRECTORY_MUSIC), "mirea.3gp");
            }
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(getActivity(), "Recording started!", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            Log.d(TAG, "stopRecording");
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            Toast.makeText(getActivity(), "You are not recording right now!",
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void processAudioFile() {
        Log.d(TAG, "processAudioFile");
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        // установка meta данных созданному файлу
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audioFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
        ContentResolver contentResolver = getActivity().getContentResolver();
        Log.d(TAG, "audioFile: " + audioFile.canRead());
        Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(baseUri, values);
        // оповещение системы о новом файле
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
    }
}