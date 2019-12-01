package com.example.booker;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import com.example.booker.databinding.FragmentAddBookBinding;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {
    FragmentAddBookBinding addBookBinding;
    String title,author,isbn,publisher,genre,edition,language,numberOfPage,securityMoney,ownerId,CoverLink;
    String fileName;
    private StorageReference mStorageRef;
    private int IMAGE_REQUEST_CODE=122;

    public AddBookFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         addBookBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_book, container, false);

         return addBookBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        addBookBinding.chooserBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            createChooserForImg();
            }
        });


    }

    private void createChooserForImg() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"select an image"),IMAGE_REQUEST_CODE);

    }

    private String getExtensionFromFile(Uri selectedImageURI) {
        ContentResolver contentResolver=getActivity().getContentResolver();
        MimeTypeMap typeMap=MimeTypeMap.getSingleton();
        String ext=typeMap.getExtensionFromMimeType(contentResolver.getType(selectedImageURI));
        return ext;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==IMAGE_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            Uri selectedImageURI = data.getData();
           String extension= getExtensionFromFile(selectedImageURI);
           fileName=System.currentTimeMillis()+extension;


        }
    }


}
