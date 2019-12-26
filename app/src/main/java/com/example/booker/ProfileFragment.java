package com.example.booker;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.booker.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final int PROFILE_IMAGE_REQUEST_CODE = 777;
    FragmentProfileBinding profileBinding;
    String user_key, fileName;
    SharedPreferences preferences;
    DatabaseReference databaseReference;
    User user;
    StorageReference storageReference;
    StorageReference fileRef;
    Uri selectedImageURI;
    String profileLink;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        profileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return profileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        preferences = this.getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Boolean hasData = preferences.contains("user_key");
        profileBinding.saveChangeBTN.setOnClickListener(saveChangeListener);
        user_key = preferences.getString("user_key", "");
        databaseReference.child("Users").child(user_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user.getProfilePicLink() == null) {

                } else {
                    Picasso.get().load(user.getProfilePicLink()).into(profileBinding.profileImage);
                }
                profileBinding.nameTV.setText(user.getName());
                profileBinding.occupationTV.setText("Works as a " + user.getProfession());
                profileBinding.emailTV.setText(user.getEmail());
                profileBinding.phoneTV.setText(user.getPhone());
                profileBinding.locationTV.setText(user.getUserAddress().getAddress());
                if (user.isOnline()) {
                    profileBinding.activeStatusTV.setText("Active Now");

                } else {
                    String timeAgo = GetTimeAgo.getTimeAgo(user.getLastSeenTimeStamp(), getContext());
                    profileBinding.activeStatusTV.setText("Active Since " + timeAgo);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        profileBinding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChooserForImg();


            }
        });
    }

    private void createChooserForImg() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "select an image"), PROFILE_IMAGE_REQUEST_CODE);

    }

    private String getExtensionFromFile(Uri selectedImageURI) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        String ext = typeMap.getExtensionFromMimeType(contentResolver.getType(selectedImageURI));
        return ext;
    }

    private void UploadImage() {
        fileRef = storageReference.child(fileName);
        UploadTask uploadTask = fileRef.putFile(selectedImageURI);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileLink = uri.toString();
                        updateTheDatabse();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void updateTheDatabse() {
        Map profilePicMap = new HashMap();
        profilePicMap.put("profilePicLink", profileLink);
        Log.e("profile pic", profileLink);
        Log.e("user key ", user_key);
        databaseReference.child("Users").child(user_key).updateChildren(profilePicMap);
        profileBinding.saveChangeBTN.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Profile Picture Successfully Updated", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageURI = data.getData();
            profileBinding.profileImage.setImageURI(selectedImageURI);
            String extension = getExtensionFromFile(selectedImageURI);
            fileName = System.currentTimeMillis() + "." + extension;
            profileBinding.saveChangeBTN.setVisibility(View.VISIBLE);

        }
    }

    View.OnClickListener saveChangeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UploadImage();

        }
    };
}
