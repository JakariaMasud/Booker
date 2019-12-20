package com.example.booker;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.booker.databinding.FragmentAddBookBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddBookFragment extends Fragment {
    FragmentAddBookBinding addBookBinding;
    String title,author,isbn,publisher,genre,edition,language,numberOfPage,securityMoney,ownerId,coverLink;
    String fileName;
    Uri selectedImageURI;
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private int IMAGE_REQUEST_CODE=122;
    SharedPreferences preferences;
    String user_key;
    Book book;
    NavController navController;
    StorageReference fileRef;
    String uniqueId;




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
        navController= Navigation.findNavController(view);
        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads");
        databaseReference=FirebaseDatabase.getInstance().getReference();
        preferences=getActivity().getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        addBookBinding.chooserBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            createChooserForImg();
            }
        });

        addBookBinding.addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkingAllInformation();
            }
        });


    }

    private void checkingAllInformation() {
        title=addBookBinding.titleET.getText().toString();
        author=addBookBinding.authorET.getText().toString();
        isbn=addBookBinding.isbnET.getText().toString();
        publisher=addBookBinding.publilsherET.getText().toString();
        genre=addBookBinding.genreET.getText().toString();
        edition=addBookBinding.editionET.getText().toString();
        language=addBookBinding.languageET.getText().toString();
        numberOfPage=addBookBinding.pageET.getText().toString();
        securityMoney=addBookBinding.cautionMoneyET.getText().toString();
        if (TextUtils.isEmpty(title)) {
            addBookBinding.titleET.setError("please enter the book name");
            return;
        }
        else if(TextUtils.isEmpty(author)){
            addBookBinding.authorET.setError("enter the author name");
            return;
        }
        else if(TextUtils.isEmpty(isbn)){
            addBookBinding.isbnET.setError("enter isbn number of the book");
            return;

        }
        else if(TextUtils.isEmpty(publisher)){
            addBookBinding.publilsherET.setError("Enter Publisher name ");
            return;
        }
        else if(TextUtils.isEmpty(genre)){
            addBookBinding.genreET.setError("please enter genre of the book");
            return;
        }
        else if(TextUtils.isEmpty(edition)){
            addBookBinding.editionET.setError("enter edition number");
            return;

        }
        else if(TextUtils.isEmpty(language)){
            addBookBinding.languageET.setError("enter the language ");
            return;
        }
        else if(TextUtils.isEmpty(numberOfPage)){
            addBookBinding.pageET.setError("enter the total number of  page");
            return;
        }
        else if(TextUtils.isEmpty(securityMoney)){
            addBookBinding.cautionMoneyET.setError("enter the security money");
            return;
        }
        else if(fileName==null){
            addBookBinding.chooserBTN.setError("please select an image from Gallery");
            return;
        }
        Boolean hasData= preferences.contains("user_key");
        if(hasData){
            user_key = preferences.getString("user_key", "");
           UploadImage();

        }



    }

    private void UploadImage() {
         fileRef=mStorageRef.child(fileName);
        UploadTask uploadTask= fileRef.putFile(selectedImageURI);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        coverLink= uri.toString();
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

    private void updateTheDatabse(){
         uniqueId=databaseReference.child("Books").push().getKey();
        book=new Book(title,author,isbn,publisher,genre,edition,language,numberOfPage,securityMoney,user_key,coverLink,"Available",uniqueId);
        Task task=databaseReference.child("Books").child(uniqueId).setValue(book);
        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Task task2= databaseReference.child("Users").child(user_key).child("Books").child(uniqueId).setValue(book);
                    task2.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){

                                Toast.makeText(getContext(), "book successfully addded", Toast.LENGTH_SHORT).show();
                                navController.navigate(R.id.deskFragment);


                            }
                            else {
                                Toast.makeText(getContext(), " exception in operation : 2"+task.getException(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
                else {
                    Toast.makeText(getContext(), " exception in operation : 1"+task.getException(), Toast.LENGTH_SHORT).show();
                }

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
            selectedImageURI = data.getData();
            String extension= getExtensionFromFile(selectedImageURI);
            fileName=System.currentTimeMillis()+"."+extension;

        }
    }



}


