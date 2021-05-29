package ru.mirea.koskin.sr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickShowDialog_Date(View view) {
        MyDateDialogFragment dialogFragment = new MyDateDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }
    public void onClickShowDialog_Prog(View view) {
        MyProgressDialogFragment dialogFragment = new MyProgressDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }
    public void onClickShowDialog_Time(View view) {
        MyTimeDialogFragment dialogFragment = new MyTimeDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }

    public void onRetDate(int year, int month, int day) {
        Toast.makeText(getApplicationContext(), day+" "+ month+" "+year,
                Toast.LENGTH_LONG).show();
    }
    public void onRetTime(Calendar c) {
        Toast.makeText(getApplicationContext(), c.HOUR_OF_DAY+" "+ c.MINUTE,
                Toast.LENGTH_LONG).show();
    }
}