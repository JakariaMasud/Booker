package com.example.booker;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class BookNotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String type=String.valueOf(remoteMessage.getData().get("Type"));
        if(remoteMessage.getNotification()!=null){
            String title=remoteMessage.getNotification().getTitle();
            String description=remoteMessage.getNotification().getBody();
            NotificationHelper.displayNotification(getApplicationContext(),title,description,type);
        }
    }
}
