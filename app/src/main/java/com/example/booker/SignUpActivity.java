package com.example.booker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.example.booker.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schibstedspain.leku.LocationPickerActivity;

import static com.schibstedspain.leku.LocationPickerActivityKt.LATITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.LOCATION_ADDRESS;
import static com.schibstedspain.leku.LocationPickerActivityKt.LONGITUDE;
import static com.schibstedspain.leku.LocationPickerActivityKt.ZIPCODE;


public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding signUpBinding;
    int LOCATION_REQUEST_CODE =123;
    Intent locationPickerIntent;
    Address userAddress;
   DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       signUpBinding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        signUpBinding.signupAddressET.setInputType(InputType.TYPE_NULL);
       signUpBinding.signupAddressET.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               locationPickerIntent =new LocationPickerActivity.Builder()
                       .withLocation(23.810331, 90.412521)
                       .withGeolocApiKey("AIzaSyAvHCTyXb0ef26I2i5SPwrygEnblHOweBs")
                       .withSearchZone("bn")
                       .shouldReturnOkOnBackPressed()
                       .withMapStyle(R.raw.my_style)
                       .withGooglePlacesEnabled()
                       .withGoogleTimeZoneEnabled()
                       .withUnnamedRoadHidden()
                       .build(getApplicationContext());

               startActivityForResult(locationPickerIntent, LOCATION_REQUEST_CODE);
           }
       });

       signUpBinding.signupBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final String phone= String.valueOf(signUpBinding.signupPhoneET.getText());
              final String name= String.valueOf(signUpBinding.signupNameET.getText());
              final String email= String.valueOf(signUpBinding.signupEmailET.getText());
               final String profession= String.valueOf(signUpBinding.signupProfessionlET.getText());
               final String password= String.valueOf(signUpBinding.signupPasswordET.getText());

               if(TextUtils.isEmpty(phone)){
                   signUpBinding.signupPhoneET.setError("please enter the phone number");
                   return;
               }


               else if(TextUtils.isEmpty(name)){
                   signUpBinding.signupNameET.setError("please enter the name ");
                   return;
               }
               else if(TextUtils.isEmpty(email)){
                   signUpBinding.signupEmailET.setError("email field can not be empty");
                   return;
               }
               else if(TextUtils.isEmpty(profession)){
                   signUpBinding.signupProfessionlET.setError("please enter your occupation");
                   return;
               }
               else if(userAddress==null){
                   signUpBinding.signupAddressET.setError("address must be select");
                   return;
               }
               else if(TextUtils.isEmpty(password)){
                   signUpBinding.signupPasswordET.setError("password field can not be empty");
                   return;
               }
               signUpBinding.progressBar.setVisibility(View.VISIBLE);
               databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       boolean isExist= dataSnapshot.child(phone).exists();
                       if(isExist){
                           signUpBinding.signupPhoneET.setError("this phone number already has been taken");
                           signUpBinding.progressBar.setVisibility(View.GONE);
                       }
                       else{
                            creatingAccount(phone,name,email,profession,password);
                       }

                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });



           }
       });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==LOCATION_REQUEST_CODE && data!=null){
            double latitude = data.getDoubleExtra(LATITUDE, 0.0);
            double longitude  = data.getDoubleExtra(LONGITUDE, 0.0);
            String address = data.getStringExtra(LOCATION_ADDRESS);
            String postalcode = data.getStringExtra(ZIPCODE);
            userAddress=new Address(longitude,latitude,address,postalcode);
            signUpBinding.signupAddressET.setText(address);
        }
    }
    void creatingAccount(String phone,String name,String email,String profession,String password){
        User user=new User(phone,name,email,profession,password,userAddress);
        databaseReference.child("Users").child(phone).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    signUpBinding.progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }
}
