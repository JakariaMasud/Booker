package com.example.booker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.booker.databinding.ActivityLoginBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    DatabaseReference databaseReference;
    String phone,password;
    SharedPreferences preferences;
    SharedPreferences.Editor sfEditor;
    boolean isChecked=false;
    boolean isHide=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        preferences = getSharedPreferences("MyPref",MODE_PRIVATE);
        sfEditor=preferences.edit();
        loginBinding.signupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        loginBinding.loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isHide){
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(loginBinding.phoneET.getWindowToken(), 0);
                }

                phone= String.valueOf(loginBinding.phoneET.getText());
                password= String.valueOf(loginBinding.passwordET.getText());


                if(TextUtils.isEmpty(phone)){
                    loginBinding.phoneET.setError("you must enter the phone number");
                    return;
                }
                else if(TextUtils.isEmpty(password)){
                    loginBinding.passwordET.setError("you must enter the phone number");
                    return;
                }
               isChecked= loginBinding.rememberCB.isChecked();
                checkingUserCredential();


            }
        });
        loginBinding.rememberCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(loginBinding.phoneET.getWindowToken(), 0);
                isHide=true;
            }
        });

    }

    void checkingUserCredential(){
        loginBinding.loginpb.setVisibility(View.VISIBLE);


        databaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean isUser=dataSnapshot.hasChild(phone);
                if(isUser){
                   User user= dataSnapshot.child(phone).getValue(User.class);
                    sfEditor.putString("user_key",user.getPhone());
                    sfEditor.putBoolean("isChecked",isChecked);
                    sfEditor.commit();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    loginBinding.loginpb.setVisibility(View.GONE);
                    startActivity(intent);
                    finish();


                }
                else {
                    Toast.makeText(LoginActivity.this, "you are not a registered user", Toast.LENGTH_SHORT).show();
                    loginBinding.loginpb.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
