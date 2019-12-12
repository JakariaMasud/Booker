package com.example.booker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.booker.databinding.ActivityMainBinding;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    boolean isUser=false;
    String user_key;
    ActivityMainBinding mainBinding;
    SharedPreferences preferences;
    boolean isChecked;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        setSupportActionBar((Toolbar) mainBinding.customAppbar);
        preferences=getSharedPreferences("MyPref",MODE_PRIVATE);
        navController= Navigation.findNavController(this,R.id.nav_host_fragment);
        Boolean hasData= preferences.contains("user_key");
        if(hasData){

            String user_key = preferences.getString("user_key", "");
            isChecked=preferences.getBoolean("isChecked",false);
        }
        else {
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(mainBinding.drawerLayout)
                        .build();
        NavigationUI.setupWithNavController(mainBinding.navigationView,navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);




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
        preferences.edit().clear().commit();
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }
}
