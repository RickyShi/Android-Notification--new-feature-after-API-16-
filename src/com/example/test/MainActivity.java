package com.example.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.widget.RemoteViews;

public class MainActivity extends Activity {

    private static final String TAG = "_-->>>>>>";

    private static final RemoteViews CurrentText = null;

    int notificationID = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayNotification();
    }

    //    public void onClick(final View view) {
    //        Log.d("test", "display");
    //        displayNotification();
    //    }
    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public void displayNotification() {

        final Intent i = new Intent(this, MainActivity.class);
        i.putExtra("notificationID", notificationID);
        // ******************************************一般情况下的做法******************************************
        /*
         * PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationID, i, 0);
         */

        // **********************************************设置点击后返回一定是home********************************
        // 当你点击notification后打开的界面是应用流程的一部分，需要这一块，能直接回到home,如果只是显示notification
        // 的详细信息，可以直接使用上面的一般情况下的做法
        final TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(i);
        final PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // ****************************************************************************************************
        // Another way to do it
        /*
         * final PendingIntent pendingIntent = PendingIntent.getActivity( this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT
         * );
         */
        // ***************************************************************************************************
        final NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // api 11是这种方法
        // Notification notif = new Notification(R.drawable.ic_launcher,
        // "Remingder:Meeting starts in 5 minutes",
        // System.currentTimeMillis());
        final CharSequence from = "Notification";
        final CharSequence message = getString(R.string.info);

        final NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(
                this).setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                // .setWhen(System.currentTimeMillis())
                .setContentTitle(from)
                .setContentText(message)
                // *******************************设置通知栏的大视图big view,android
                // 4.1之后支持**********************
                .setStyle(
                        new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.info)));
        // .setContentText(message).setAutoCancel(false);

        // ****************************************** Another way to load inboxStyle
        // final NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        // final String[] events = new
        // String[] { "Ricky", "is", "doing", "a big view", "notification", "test" };
        // inboxStyle.setBigContentTitle("Title: detail"); // 大视图的名称
        // inboxStyle.setSummaryText("TEST");
        // for (final String event : events) {
        // inboxStyle.addLine(event);
        // }
        // nBuilder.setStyle(inboxStyle);

        // *************************************************************************************************
        // *******************************************更新通知条数信息******************************************
        int numMessage = 0;
        nBuilder.setContent(CurrentText).setNumber(++numMessage);
        // ********************************************************************************************
        // ***********************明确进度时间的进度通知栏显示**********************
        //
        new Thread(new Runnable() {
            @Override
            public void run() {
                int incr;
                for (incr = 0; incr <= 100; incr += 5) {
                    nBuilder.setProgress(100, incr, false);
                    nm.notify(0, nBuilder.build());
                    try { // Sleep for 5 seconds
                        Thread.sleep(1 * 1000);
                    } catch (final InterruptedException e) {
                        Log.d(TAG, "sleep failure");
                    }
                }
                nBuilder.setContentText("开始开会");
                nBuilder.setProgress(0, 0, false);
                nm.notify(0, nBuilder.build());
            }

        }).start();

        // **********************************************************
        // ***********************不明确进度时间的进度通知栏************************
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int incr = 0; incr <= 100; incr += 5) {
                    nBuilder.setProgress(0, 0, true);
                    nm.notify(0, nBuilder.build());
                    try {
                        // Sleep for 5 seconds
                        Thread.sleep(1 * 1000);
                    } catch (final InterruptedException e) {
                        Log.d(TAG, "sleep failure");
                    }
                }
                nBuilder.setContentText("开始开会");
                nBuilder.setProgress(0, 0, false);
                nm.notify(0, nBuilder.build());
            }

        }).start();
         */
        // **************************************************************************
        nm.notify(notificationID, nBuilder.build());

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
