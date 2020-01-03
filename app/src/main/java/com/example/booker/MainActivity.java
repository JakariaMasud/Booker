package com.example.booker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.example.booker.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    boolean isUser = false;
    String user_key;
    ActivityMainBinding mainBinding;
    SharedPreferences preferences;
    boolean isChecked;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    Toolbar customToolbar,chatToolbar;
    String reciever_id;
    DatabaseReference databaseReference;
    TextView userName,userStatus;
    CircleImageView userImg;
    User user;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        customToolbar=(Toolbar) mainBinding.customAppbar;
        chatToolbar=(Toolbar)mainBinding.chatAppbar;
        setSupportActionBar(customToolbar);
        preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        boolean hasData = preferences.contains("user_key");
        if (hasData) {

            user_key = preferences.getString("user_key", "");
            isChecked = preferences.getBoolean("isChecked", false);
        } else {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(mainBinding.drawerLayout)
                        .build();


        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if(destination.getId() ==R.id.messageFragment){
                    mainBinding.customAppbar.setVisibility(View.GONE);
                    mainBinding.chatAppbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(chatToolbar);
                    NavigationUI.setupWithNavController(mainBinding.navigationView,navController);
                    NavigationUI.setupActionBarWithNavController(MainActivity.this,navController,appBarConfiguration);
                    getSupportActionBar().setDisplayShowTitleEnabled(false);
                    mainBinding.chatAppbar.findViewById(R.id.chat_profile_image);

                    if(arguments!= null){
                        userName=mainBinding.chatAppbar.findViewById(R.id.chat_username_TV);
                        userImg=mainBinding.chatAppbar.findViewById(R.id.chat_profile_image);
                         userStatus=mainBinding.chatAppbar.findViewById(R.id.chat_last_TV);
                        reciever_id=arguments.getString("reciever_id");
                        gettingDataForChatAppBar();

                    }




                }
                else {
                    mainBinding.chatAppbar.setVisibility(View.GONE);
                    mainBinding.customAppbar.setVisibility(View.VISIBLE);
                    setSupportActionBar(customToolbar);
                    NavigationUI.setupWithNavController(mainBinding.navigationView,navController);
                    NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);
                }


            }
        });


    }

    private void gettingDataForChatAppBar() {
        databaseReference.child("Users").child(reciever_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user =dataSnapshot.getValue(User.class);
                if(user.getProfilePicLink()==null){

                }
                else {
                    Picasso.get().load(user.getProfilePicLink()).placeholder(R.drawable.profile).into(userImg);
                }
                userName.setText(user.getName());
               if(user.isOnline()){
                   userStatus.setText("Online");
               }
               else {
                   String status=GetTimeAgo.getTimeAgo(user.getLastSeenTimeStamp(),getApplicationContext());
                   userStatus.setText(status);
               }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!isChecked){
            preferences.edit().clear().commit();

        }




    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void signout(MenuItem item) {
        if(user_key!=null){
            Map timeMap=new HashMap();
            timeMap.put("lastSeenTimeStamp", ServerValue.TIMESTAMP);
            timeMap.put("isOnline",false);
            databaseReference.child("Users").child(user_key).updateChildren(timeMap);
        }
        preferences.edit().clear().commit();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




}
