package com.quizest.quizestapp.FirebasePackage;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import com.quizest.quizestapp.ActivityPackage.MainActivity;
import com.quizest.quizestapp.LocalStorage.Storage;
import com.quizest.quizestapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseMessangingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage != null)
            sendNotification(remoteMessage);

    }

    private void sendNotification(RemoteMessage remoteMessage) {

        Storage storage = new Storage(this);

        try {
            Gson gson = new Gson();
            String json = gson.toJson(remoteMessage.getData());
            JSONObject jsonObject = new JSONObject(json);
            int id = jsonObject.getInt("id");
            String title = jsonObject.getString("name");


            if (storage.getLogInState()) {
                Intent intent = new Intent(this, MainActivity.class);
                // Create the TaskStackBuilder and add the intent, which inflates the back stack
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(intent);
                PendingIntent pendingIntent = stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = null;

                // Turn on the screen for notification
                PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
                boolean result = false;
                if (powerManager != null) {
                    result = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && powerManager.isInteractive() || Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT_WATCH && powerManager.isScreenOn();
                    if (!result) {
                        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MH24_SCREENLOCK");
                        wl.acquire(10000);
                        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MH24_SCREENLOCK");
                        wl_cpu.acquire(10000);
                    }
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    notificationBuilder = new NotificationCompat.Builder(this, "C")
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_name))
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setContentTitle("New Quiz Category Available!")
                            .setContentText(title)
                            .setCategory(Notification.CATEGORY_MESSAGE)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_MAX)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
                }


                NotificationManager notificationManager =

                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String channelId = getString(R.string.default_notification_channel_id);
                    NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription(title);
                    notificationManager.createNotificationChannel(channel);
                    notificationBuilder.setChannelId(channelId);
                }

                if (notificationManager != null) {
                    if (notificationBuilder != null) {
                        notificationManager.notify(id, notificationBuilder.build());
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
