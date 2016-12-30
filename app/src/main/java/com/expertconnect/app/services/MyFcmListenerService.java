package com.expertconnect.app.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.expertconnect.app.R;
import com.expertconnect.app.activities.Dashboard;
import com.expertconnect.app.constants.Constants;
import com.expertconnect.app.utils.Foreground;
import com.expertconnect.app.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;


public class MyFcmListenerService extends FirebaseMessagingService {


    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map data = message.getData();




        if(Foreground.get().isBackground()) {

            Utils.setNotificationBadgeCount((Utils.getNotificationBadgeCount(getApplicationContext())+1),getApplicationContext());
            Utils.setIsNotificationRead(false,this);

            try {

                String str = data.get("message").toString();
                sendNotification(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
//                    => $message, 'topic_id'=>$user_data['topic_id'], 'user_id'=>$user_data['from_id'], 'timestamp'=>$timestamp, 'profile_pic'=>$this->get_profile_pic($user_data['from_id'])),

            ShortcutBadger.applyCount(getApplicationContext(), Utils.getNotificationBadgeCount(getApplicationContext()));
        }else{

            String str = data.get("message").toString();
            sendNotification(str);
        }

    }


    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {



        Intent intent = new Intent(this, Dashboard.class);
        intent.putExtra("notification",true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                0);




// Build the notification, setting the group appropriately
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notif = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(messageBody)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setGroup(Constants.GROUP_NOTIFICATION)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();




// Issue the notification
//        NotificationManagerCompat notificationManager =
//                NotificationManagerCompat.from(this);

        if(notificationManager==null) {
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        int id1 = (int) Math.random() * 10;
        notificationManager.notify(1, notif);



    }
}
