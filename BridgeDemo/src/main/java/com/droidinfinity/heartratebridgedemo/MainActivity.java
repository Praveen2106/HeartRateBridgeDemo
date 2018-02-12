package com.droidinfinity.heartratebridgedemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.droidinfinity.heartratemonitor.bridge.HeartRateBridge;

public class MainActivity extends AppCompatActivity {

    private HeartRateBridge heartRateBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!HeartRateBridge.isHeartRateMonitorInstalled(MainActivity.this)) {
                    Snackbar.make(view, "Heart Rate Monitor app is not installed. ", Snackbar.LENGTH_LONG)
                            .setAction("INSTALL", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    HeartRateBridge.startInstallIntent(MainActivity.this);
                                }
                            }).show();

                    return;
                }

                heartRateBridge.openHeartRateBridge(MainActivity.this);
            }
        });

        heartRateBridge = new HeartRateBridge();
        heartRateBridge.setHeartRateListener(new HeartRateBridge.HeartRateListener() {
            @Override
            public void onHeartRateMeasured(int heartRate) {
                if (heartRate > 0) {
                    Snackbar.make(fab, "Measured Heart Rate: " + heartRate, Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(fab, "Measurement is not accurate or incorrect. No data is returned", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (!heartRateBridge.onActivityResult(requestCode, resultCode, data)) {
            //Handle any other logic here.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_github) {
            final Uri uri = Uri.parse("https://github.com/Praveen2106/HeartRateBridgeDemo");
            final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
