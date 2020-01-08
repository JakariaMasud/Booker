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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.booker.databinding.FragmentEditBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {
    FragmentEditBinding editBinding;
    Book recievedBook;
    String title,author,isbn,publisher,genre,edition,language,numberOfPage,securityMoney,ownerId,coverLink;
    String fileName;
    Uri selectedImageURI;
    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;
    private int IMAGE_REQUEST_CODE=120;
    SharedPreferences preferences;
    String user_key;
    Book book;
    NavController navController;
    StorageReference fileRef;
    String uniqueId;


    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        editBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_edit, container, false);
        return editBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        mStorageRef = FirebaseStorage.getInstance().getReference("Cover Pictures");
        databaseReference= FirebaseDatabase.getInstance().getReference();
        preferences=getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        user_key = preferences.getString("user_key", "");
        editBinding.editChooserBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChooserForImg();
            }
        });
        if(getArguments()!=null){
           recievedBook= EditFragmentArgs.fromBundle(getArguments()).getBook();
           settingUpUi();
        }

    }

    private void settingUpUi() {
        editBinding.editPageTV.setText("Update "+recievedBook.getTitle()+" Info");
        editBinding.editAuthorET.setText(recievedBook.getAuthor());
        editBinding.editTitleET.setText(recievedBook.getTitle());
        editBinding.editPublilsherET.setText(recievedBook.getPublisher());
        editBinding.editIsbnET.setText(recievedBook.getIsbn());
        editBinding.editGenreET.setText(recievedBook.getGenre());
        editBinding.editEditionET.setText(recievedBook.getEdition());
        editBinding.editLanguageET.setText(recievedBook.getLanguage());
        editBinding.editPageET.setText(recievedBook.getNumberOfPage());
        editBinding.editCautionMoneyET.setText(recievedBook.getSecurityMoney());
        editBinding.previousCB.setChecked(true);
        settingUpListener();
    }

    private void settingUpListener() {
        editBinding.editUpdateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkingAllInformation();

            }
        });
    }
    private void checkingAllInformation() {
        title=editBinding.editTitleET.getText().toString();
        author=editBinding.editAuthorET.getText().toString();
        isbn=editBinding.editIsbnET.getText().toString();
        publisher=editBinding.editPublilsherET.getText().toString();
        genre=editBinding.editGenreET.getText().toString();
        edition=editBinding.editEditionET.getText().toString();
        language=editBinding.editLanguageET.getText().toString();
        numberOfPage=editBinding.editPageET.getText().toString();
        securityMoney=editBinding.editCautionMoneyET.getText().toString();
        if (TextUtils.isEmpty(title)) {
            editBinding.editTitleET.setError("please enter the book name");
            return;
        }
        else if(TextUtils.isEmpty(author)){
            editBinding.editAuthorET.setError("enter the author name");
            return;
        }
        else if(TextUtils.isEmpty(isbn)){
            editBinding.editIsbnET.setError("enter isbn number of the book");
            return;

        }
        else if(TextUtils.isEmpty(publisher)){
            editBinding.editPublilsherET.setError("Enter Publisher name ");
            return;
        }
        else if(TextUtils.isEmpty(genre)){
            editBinding.editGenreET.setError("please enter genre of the book");
            return;
        }
        else if(TextUtils.isEmpty(edition)){
            editBinding.editEditionET.setError("enter edition number");
            return;

        }
        else if(TextUtils.isEmpty(language)){
            editBinding.editLanguageET.setError("enter the language ");
            return;
        }
        else if(TextUtils.isEmpty(numberOfPage)){
            editBinding.editPageET.setError("enter the total number of  page");
            return;
        }
        else if(TextUtils.isEmpty(securityMoney)){
            editBinding.editCautionMoneyET.setError("enter the security money");
            return;
        }
        else if(editBinding.previousCB.isChecked()){
            coverLink=recievedBook.getCoverLink();
            updateTheDatabse();
    }
        else if(!editBinding.previousCB.isChecked()){
            if(fileName==null){
                editBinding.editChooserBTN.setError("Please select an image");
                return;
            }
            else {
                UploadImage();
            }
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

    private void updateTheDatabse() {
        uniqueId=recievedBook.getBookId();
        book=new Book(title,author,isbn,publisher,genre,edition,language,numberOfPage,securityMoney,user_key,coverLink,"Available",uniqueId,"");
        Task task=databaseReference.child("Books").child(uniqueId).setValue(book);
        task.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Task task2= databaseReference.child("Users").child(user_key).child("Books").child("Available").child(uniqueId).setValue(book);
                    task2.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){

                                Toast.makeText(getContext(), "book successfully updated", Toast.LENGTH_SHORT).show();
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
}}

