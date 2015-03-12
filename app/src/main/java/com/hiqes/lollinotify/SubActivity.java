package com.hiqes.lollinotify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SubActivity extends ActionBarActivity {
    private NotificationManager mMgr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String msg = getIntent().getStringExtra(MainActivity.EXTRA_PAYLOAD);
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_sub);

        mMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Button btn =
            (Button)findViewById(R.id.btn_simple_legacy);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder bldr =
                    createBuilder(R.string.priv_title_legacy,
                                  R.string.priv_content_legacy,
                                  android.R.drawable.ic_dialog_alert);

                //  Do NOT explicitly set the visibility, this is legacy
                mMgr.notify(R.id.btn_simple_legacy, bldr.build());
            }
        });

        btn = (Button)findViewById(R.id.btn_simple_public);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder bldr =
                        createBuilder(R.string.priv_title_public,
                                R.string.priv_content_public,
                                android.R.drawable.ic_dialog_info);

                bldr.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                mMgr.notify(R.id.btn_simple_public, bldr.build());
            }
        });

        btn = (Button)findViewById(R.id.btn_simple_secret);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder bldr =
                        createBuilder(R.string.priv_title_secret,
                                R.string.priv_content_secret,
                                android.R.drawable.ic_dialog_email);

                bldr.setVisibility(NotificationCompat.VISIBILITY_SECRET);
                mMgr.notify(R.id.btn_simple_secret, bldr.build());
            }
        });
    }

    private NotificationCompat.Builder createBuilder(int title, int content, int icon) {
        NotificationCompat.Builder bldr = new NotificationCompat.Builder(this);

        bldr.setContentTitle(getString(title));
        bldr.setContentText(getString(content));
        bldr.setSmallIcon(icon);

        Intent intent =
                new Intent(this, SubActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(MainActivity.EXTRA_PAYLOAD, getString(title));
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this,
                                          0,
                                          intent,
                                          PendingIntent.FLAG_UPDATE_CURRENT);
        bldr.setContentIntent(pendingIntent);
        bldr.setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        return bldr;
    }

}
