package com.example.kidsgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextInputEditText textInputEditText;
    ImageView animals, clothes, fruits, vegetables;

    TextToSpeech tts;
    SharedPreferences pref;
    boolean isSoundON = true;
    private ImageView selectedImageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputEditText = findViewById(R.id.textInputEditText);
        animals = findViewById(R.id.animals);
        clothes = findViewById(R.id.clothes);
        fruits = findViewById(R.id.fruits);
        vegetables = findViewById(R.id.vegetables);

        tts = new TextToSpeech(this, this);
        pref = getSharedPreferences("settings", MODE_PRIVATE);
        isSoundON = pref.getBoolean("sound", true);

        // category clicks
        animals.setOnClickListener(view -> {
            setSelectedImageView((ImageView) view);
            selectedImageView.setTag("animals");
            view.setSelected(true);

            clothes.setSelected(false);
            fruits.setSelected(false);
            vegetables.setSelected(false);
//            Toast.makeText(this, getSelectedImageTag(), Toast.LENGTH_SHORT).show();

        });
        clothes.setOnClickListener(view -> {
            setSelectedImageView((ImageView) view);
            selectedImageView.setTag("clothes");
            view.setSelected(true);

            animals.setSelected(false);
            fruits.setSelected(false);
            vegetables.setSelected(false);
//            Toast.makeText(this, getSelectedImageTag(), Toast.LENGTH_SHORT).show();
        });
        fruits.setOnClickListener(view -> {
            setSelectedImageView((ImageView) view);
            selectedImageView.setTag("fruits");
            view.setSelected(true);

            animals.setSelected(false);
            clothes.setSelected(false);
            vegetables.setSelected(false);
//            Toast.makeText(this, getSelectedImageTag(), Toast.LENGTH_SHORT).show();
        });
        vegetables.setOnClickListener(view -> {
            setSelectedImageView((ImageView) view);
            selectedImageView.setTag("vegetables");
            view.setSelected(true);

            animals.setSelected(false);
            fruits.setSelected(false);
            clothes.setSelected(false);
//            Toast.makeText(this, getSelectedImageTag(), Toast.LENGTH_SHORT).show();
        });
    }

    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("sound", isSoundON);
        editor.apply();

        super.onDestroy();
    }

    // Method to set the selected ImageView
    private void setSelectedImageView(ImageView imageView) {
        // Deselect previously selected ImageView
        if (selectedImageView != null) {
            selectedImageView.setSelected(false);
        }
        // Select the new ImageView
        selectedImageView = imageView;
        selectedImageView.setSelected(true);
    }

    // Method to get the image resource ID
    private String getSelectedImageTag() {
        if (selectedImageView != null) {
            String imageResourceId = (String) selectedImageView.getTag();
            return imageResourceId;
        }
        return "";
    }

    public void play(View view) {
        if (textInputEditText.getText().toString().isEmpty()){
            String writeName = "Please Write Your Name";
            Toast.makeText(this, writeName, Toast.LENGTH_SHORT).show();
            if (tts != null) {
                tts.speak(writeName, TextToSpeech.QUEUE_FLUSH, null, null);
            }
            return;
        }
        if (getSelectedImageTag().isEmpty()){
            String writeCategory = "Please Choose Category";
            Toast.makeText(this, writeCategory, Toast.LENGTH_SHORT).show();
            if (tts != null) {
                tts.speak(writeCategory, TextToSpeech.QUEUE_FLUSH, null, null);
            }
            return;
        }
        Intent intent = new Intent(this, PlayingActivity.class);
        intent.putExtra("name", textInputEditText.getText().toString());
        intent.putExtra("category", getSelectedImageTag());
        startActivity(intent);
    }

    @Override
    public void onInit(int i) {

    }
}