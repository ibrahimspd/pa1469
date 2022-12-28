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
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.example.myapplication.controller.MainActivity;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TeamInfoModel extends ViewModel {

    private Context context;

    private MainActivity activity;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public TeamInfoModel(Context context, MainActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void handleResult(ActivityResult result, String imageName, ImageView imageView) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent imageData = result.getData();
            if (imageData != null) {

                loadImageFromUrl(Glide.with(context).load(imageData.getData()), imageView);

                StoreImageToFirabaseStorage(imageData, imageName);
            }
        }
    }

    private void loadImageFromUrl(RequestBuilder<Drawable> context, ImageView teamKit) {
        context.into(teamKit);
    }

    public void loadImage(String team, ImageView teamGkKit) {
        String teamGkKitUrl = team;
        if (!teamGkKitUrl.contains(".")){
            loadImageFromFiresbase(teamGkKitUrl, teamGkKit);
        }
        else
            loadImageFromUrl(Glide.with(context).load(teamGkKitUrl), teamGkKit);
    }

    private void loadImageFromFiresbase(String teamKitUrl, ImageView teamKit) {
        StorageReference storageRef = storage.getReference();
        StorageReference kitRef = storageRef.child(teamKitUrl);
        kitRef.getDownloadUrl().addOnSuccessListener(
                uri -> Glide.with(context).load(uri).into(teamKit));
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

    private void StoreImageToFirabaseStorage(Intent data, String imageName) {
        Uri uri = data.getData();
        InputStream imageStream = getInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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

}