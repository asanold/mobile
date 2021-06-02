package ru.mirea.koskin.mireaproject;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

public class MyMediaPlayerService extends Service {
    private MediaPlayer mediaPlayer;
    public MyMediaPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String audio = intent.getStringExtra("audio");
        Log.d("PLAYER", audio);
        if (audio.contains("main")) {
            Log.d("1PLAYER", audio);
            mediaPlayer = MediaPlayer.create(this, R.raw.bill_evans_a_time_for_love);
        }
        else if (audio.contains("prac5")){
            Uri uri = Uri.parse("file:///storage/self/primary/Android/data/ru.mirea.koskin.mireaproject/files/Music/mirea.3gp");
            mediaPlayer = MediaPlayer.create(this, uri);
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }
}