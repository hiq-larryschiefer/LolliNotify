package com.hiqes.lollinotify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



public class MainActivity extends ActionBarActivity {
    private static final String         TAG = MainActivity.class.getName();
    protected static final String       EXTRA_PAYLOAD = "_payload";

    private NotificationManager             mMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button                  curBtn;

        super.onCreate(savedInstanceState);

        String msg = getIntent().getStringExtra(EXTRA_PAYLOAD);
        if (msg != null) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        mMgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        curBtn = (Button)findViewById(R.id.btn_alt_action);
        curBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = MainActivity.this;

                Notification.Builder bldr =
                    createBuilder(R.string.alt_action_main_title,
                                  R.string.alt_action_main_content,
                                  android.R.drawable.ic_dialog_info);

                Intent altIntent =
                    new Intent(ctx, SubActivity.class);
                altIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent =
                    PendingIntent.getActivity(ctx,
                                              1,
                                              altIntent,
                                              PendingIntent.FLAG_UPDATE_CURRENT);
                bldr.addAction(android.R.drawable.ic_dialog_alert,
                               getString(R.string.alt_action_title),
                               pendingIntent);

                postNotification(bldr, R.id.btn_alt_action);
            }
        });

        curBtn = (Button)findViewById(R.id.btn_cat_status);
        curBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = MainActivity.this;

                Notification.Builder bldr =
                    createBuilder(R.string.cat_status_title,
                                  R.string.cat_status_content,
                                  android.R.drawable.ic_popup_disk_full);

                //  Categories are only available on Android >= API 21
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    bldr.setCategory(Notification.CATEGORY_STATUS);
                }

                postNotification(bldr, R.id.btn_cat_status);
            }
        });

        curBtn = (Button)findViewById(R.id.btn_cat_event);
        curBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context ctx = MainActivity.this;

                Notification.Builder bldr =
                        createBuilder(R.string.cat_event_title,
                                R.string.cat_event_content,
                                android.R.drawable.ic_popup_disk_full);

                //  Categories are only available on Android >= API 21
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    bldr.setCategory(Notification.CATEGORY_EVENT);
                }

                bldr.setPriority(Notification.PRIORITY_HIGH);
                postNotification(bldr, R.id.btn_cat_event);
            }
        });

        //  Last button - clear EVERYTHING
        curBtn = (Button)findViewById(R.id.btn_clear_all);
        curBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMgr.cancelAll();
            }
        });
    }

    @SuppressWarnings("deprecation")
    private void postNotification(Notification.Builder bldr, int id) {
        Notification            n;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            n = bldr.getNotification();
        } else {
            n = bldr.build();
        }

        n.flags = Notification.FLAG_AUTO_CANCEL;

        mMgr.notify(id, n);
    }

    private Notification.Builder createBuilder(int title, int content, int icon) {
        Notification.Builder bldr = new Notification.Builder(this);

        bldr.setContentTitle(getString(title));
        bldr.setContentText(getString(content));
        bldr.setSmallIcon(icon);

        Intent intent =
                new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.putExtra(EXTRA_PAYLOAD, getString(title));
        PendingIntent pendingIntent =
            PendingIntent.getActivity(this,
                                      0,
                                      intent,
                                      PendingIntent.FLAG_UPDATE_CURRENT);
        bldr.setContentIntent(pendingIntent);
        bldr.setDefaults(Notification.DEFAULT_VIBRATE);

        return bldr;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
