package com.example.booker;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    SharedPreferences preferences;
    public static final String CHANNEL_ID = "Booker Channel";
    DatabaseReference databaseReference;
    String user_key;
    DatabaseReference connectedRef;
    Map presenceMap;

    @Override
    public void onCreate() {
        super.onCreate();
        presenceMap=new HashMap<>();

        preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        updateUserPresence();
        createNotificationChannels();

    }

    private void updateUserPresence() {
        boolean hasData = preferences.contains("user_key");
        if (hasData) {
            user_key = preferences.getString("user_key", "");
            if (user_key != null) {
                connectedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean connected = snapshot.getValue(Boolean.class);
                        if (connected) {
                            presenceMap.put("isOnline",true);
                            databaseReference.child("Users").child(user_key).updateChildren(presenceMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if(task.isSuccessful()){
                                        databaseReference.child("Users").child(user_key).child("lastSeenTimeStamp").onDisconnect().setValue(ServerValue.TIMESTAMP);
                                        databaseReference.child("Users").child(user_key).child("isOnline").onDisconnect().setValue(false);
                                    }
                                }
                            });

                        }


                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        }
    }

        private void createNotificationChannels () {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Booker Channel", importance);
                channel.setDescription("Booker Channel");
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

        }


    }

