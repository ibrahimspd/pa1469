package com.example.myapplication.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.myapplication.controller.ColorPicker;
import com.example.myapplication.controller.MainActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileModel extends ViewModel {

    private Context context;

    private MainActivity activity;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public ProfileModel(Context context, MainActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public boolean handleResult(ActivityResult result, String imageName, ImageView imageView) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent imageData = result.getData();
            if (imageData != null) {

                loadImageFromUrl(Glide.with(context).load(imageData.getData()), imageView);

                return storeImageToFirebaseStorage(imageData, imageName);
            }
        }
        return false;
    }

    private boolean loadImageFromUrl(RequestBuilder<Drawable> image, ImageView imageView) {
        try {
            image.into(imageView);
        } catch (Exception e) {
            Toast.makeText(this.context, "Error loading image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean loadImage(String imageString, ImageView imageView) {
        if (!imageString.contains(".")){
            loadImageFromFiresbase(imageString, imageView);
        }
        else
            return loadImageFromUrl(Glide.with(context).load(imageString), imageView);
        return true;
    }

    private void loadImageFromFiresbase(String imageString, ImageView imageView) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(imageString);
        imageRef.getDownloadUrl().addOnSuccessListener(
                uri -> Glide.with(context).load(uri).into(imageView));
    }

    @Nullable
    private InputStream getInputStream(Uri uri) {
        InputStream imageStream = null;
        try {
            imageStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return imageStream;
    }

    private boolean storeImageToFirebaseStorage(Intent data, String imageName) {
        if(data == null || imageName == null)
            return false;

        try {
            Uri uri = data.getData();
            InputStream imageStream = getInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] data2 = baos.toByteArray();

            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child(imageName);
            UploadTask uploadTask = imageRef.putBytes(data2);
            uploadTask
                    .addOnFailureListener(exception ->
                            Toast.makeText(context, "Image could not save", Toast.LENGTH_SHORT).show())
                    .addOnSuccessListener(taskSnapshot ->
                            Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show());
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @NonNull
    public Intent getColorPickerIntent(String color, String type) {
        Intent intent = new Intent(context, ColorPicker.class);
        intent.putExtra("colorType", color);
        intent.putExtra("color", type);
        return intent;
    }
}

