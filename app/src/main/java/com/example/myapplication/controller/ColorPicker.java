package com.example.myapplication.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.R;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.flag.BubbleFlag;
import com.skydoves.colorpickerview.flag.FlagMode;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ColorPicker extends AppCompatActivity {

    private ColorPickerView colorPickerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_picker);

        Context context = this;

        colorPickerView = findViewById(R.id.colorPickerView);
        BubbleFlag bubbleFlag = new BubbleFlag(context);
        bubbleFlag.setFlagMode(FlagMode.FADE);
        colorPickerView.setFlagView(bubbleFlag);

        final BrightnessSlideBar brightnessSlideBar = findViewById(R.id.brightnessSlide);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);

        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                setLayoutColor(envelope);
            }
        });

        ImageButton confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            ColorEnvelope envelope = colorPickerView.getColorEnvelope();
            returnIntent.putExtra("color",envelope.getColor());
            returnIntent.putExtra("hexColor","#" + envelope.getHexCode().substring(2));
            returnIntent.putExtra("colorType", getIntent().getStringExtra("colorType"));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });

        ImageButton cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED,returnIntent);
            finish();
        });

        RadioButton logoRadioButton = findViewById(R.id.logoRadioButton);
        RadioButton colorWheelRadioButton = findViewById(R.id.colorWheelRadioButton);
        RadioButton galleryRadioButton = findViewById(R.id.galleryRadioButton);

        galleryRadioButton.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 1000);
        });

        colorWheelRadioButton.setOnClickListener(v -> colorPickerView.setHsvPaletteDrawable());

        logoRadioButton.setOnClickListener(v -> {
            try {
                colorPickerView.setPaletteDrawable(getDrawable(R.drawable.test_logo));
            } catch ( Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
            try {
                assert data != null;
                final Uri imageUri = data.getData();
                if (imageUri != null) {
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    Drawable drawable = new BitmapDrawable(getResources(), selectedImage);
                    colorPickerView.setPaletteDrawable(drawable);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setLayoutColor(ColorEnvelope envelope) {
        TextView textView = findViewById(R.id.textView);
        String hexColor = "#" + envelope.getHexCode().substring(2);
        textView.setText(hexColor);

        AlphaTileView alphaTileView = findViewById(R.id.alphaTileView);
        alphaTileView.setPaintColor(envelope.getColor());
    }
}
