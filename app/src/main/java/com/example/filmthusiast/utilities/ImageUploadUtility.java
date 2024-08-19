package com.example.filmthusiast.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ImageUploadUtility {

    public interface UploadCallback {
        void onUploadSuccess(String imageUrl);
        void onUploadFailure(Exception exception);
    }

    private static final int IMAGE_PICKER_REQUEST_CODE = 1001;

    private Context context;
    private UploadCallback callback;

    public ImageUploadUtility(Context context) {
        this.context = context;
    }

    public void pickImageAndUpload(Activity activity, UploadCallback callback) {
        this.callback = callback;
        ImagePicker.with(activity)
                .compress(1024) // compress image size to 1MB
                .maxResultSize(1080, 1080)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }

    public void pickImageAndUpload(Fragment fragment, UploadCallback callback) {
        this.callback = callback;
        ImagePicker.with(fragment)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(IMAGE_PICKER_REQUEST_CODE);
    }

    public void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                uploadImageToFirebase(fileUri);
            } else {
                callback.onUploadFailure(new Exception("Failed to get the image"));
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
            callback.onUploadFailure(new Exception(ImagePicker.getError(data)));
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show();
            callback.onUploadFailure(new Exception("Task Cancelled"));
        }
    }

    private void uploadImageToFirebase(Uri fileUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

        fileRef.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                callback.onUploadSuccess(task.getResult().toString());
                            } else {
                                callback.onUploadFailure(task.getException());
                            }
                        }
                    });
                } else {
                    callback.onUploadFailure(task.getException());
                }
            }
        });
    }
}
