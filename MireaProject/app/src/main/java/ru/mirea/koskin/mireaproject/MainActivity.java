package ru.mirea.koskin.mireaproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ru.mirea.koskin.mireaproject.ui.HistoryDialogFragment;

public class MainActivity extends AppCompatActivity implements HistoryDialogFragment.ExampleDialogListener {

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Player","Stop");
                stopService(
                        new Intent(MainActivity.this, MyMediaPlayerService.class));
            }
        });
        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Player","Start");
                Intent intent = new Intent(MainActivity.this, MyMediaPlayerService.class);
                intent.putExtra("audio", "main");
                startService(intent);
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.calculateFragment, R.id.webViewFragment,
                R.id.practice5,
                R.id.settings, R.id.history)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        String username = getPreferences(MODE_PRIVATE).getString("username", "Empty");
        Toast.makeText(this, "Hello, "+username+"!",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onClickPlayMusic(View view) {
        Log.d("Player","Start");
        Log.d("Player","Start");
        Intent intent = new Intent(MainActivity.this, MyMediaPlayerService.class);
        intent.putExtra("audio", "main");
        startService(intent);
    }
    public void onClickStopMusic(View view) {
        Log.d("Player","Stop");
        stopService(
                new Intent(MainActivity.this, MyMediaPlayerService.class));
    }






    @Override
    public void applyTexts(String multitext) {
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //Log.d("MainActivity", "Name: "+getSupportFragmentManager().getFragments().get(1).getActivity().getClass().getSimpleName());
        /*for (Fragment f : this.getSupportFragmentManager().getFragments()) {
            Log.d("MainActivity", "Name: "+f.getId());
            if (f.getClass().getSimpleName().contains("NavHostFragment")) {
                for (Fragment i : f.getChildFragmentManager().getFragments()) {
                    Log.d("MainActivity", "1Name: " + i.getClass().getSimpleName());
                }
            }
        }*/
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        Log.d("MainActivity", "Name: " + navHostFragment.getClass().getSimpleName());
        Log.d("MainActivity", "fragments: " + navHostFragment.getChildFragmentManager().getFragments());
        Log.d("MainActivity", "fragmentHis: " + navHostFragment.getChildFragmentManager().findFragmentById(R.id.history));

        History frag = null;
        for (Fragment f : navHostFragment.getChildFragmentManager().getFragments()) {
            if (f.getClass().getSimpleName().contains("History")) {
                frag = (History)f;
                break;
            }
        }

        //History frag = (History) navHostFragment.getChildFragmentManager().findFragmentById(R.id.history);
        //History frag = (History)(navHostFragment.getChildFragmentManager().findFragmentById(R.id.history));
        //History frag = ((History) getSupportFragmentManager().findFragmentById(R.id.history));
        if (frag == null) {
            Log.d("Main", "NULL");
        }
        frag.applyTexts(multitext);
    }
}