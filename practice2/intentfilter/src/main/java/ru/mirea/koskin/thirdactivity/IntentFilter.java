package ru.mirea.koskin.thirdactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class IntentFilter extends AppCompatActivity {
    private Button buttonBrowser;
    private Button buttonSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_filter);
        buttonBrowser = (Button) findViewById(R.id.button);
        buttonSend = (Button) findViewById(R.id.button2);

        View.OnClickListener newBrowser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri address = Uri.parse("https://www.mirea.ru/");
                Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
                if (openLinkIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(openLinkIntent);
                } else {
                    Log.d("Intent", "Не получается обработать намерение!");

                }
            }
        };
        View.OnClickListener newSend = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MIREA");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Коськин Семён Игоревич");
                startActivity(Intent.createChooser(shareIntent, "МОИ ФИО"));
            }
        };

        buttonBrowser.setOnClickListener(newBrowser);
        buttonSend.setOnClickListener(newSend);

    }
}