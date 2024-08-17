package com.example.kidsgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

//import static nl.dionsegijn.konfetti.core.Position.Relative;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class PlayingActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView name, question;
    EditText answer;
    ImageView itemCard, mic, solid;
    ImageButton doneBtn;
    String category;
    TextToSpeech tts;
    MediaPlayer player;
    private KonfettiView konfettiView = null;
    private Shape drawableShape = null;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;

    ArrayList<CardItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.name);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.ans);
        mic = findViewById(R.id.mic);
        itemCard = findViewById(R.id.playingCard);
        solid = findViewById(R.id.solidShape);
        doneBtn = findViewById(R.id.doneBtn);
        konfettiView = findViewById(R.id.konfettiView);
        tts = new TextToSpeech(this, this);

        items.clear();

        String nameMain = getIntent().getStringExtra("name");
        name.setText("Hello " + nameMain + "\uD83D\uDC27");
        YoYo.with(Techniques.RollIn).duration(2000).playOn(name);

        category = getIntent().getStringExtra("category");

        question.setText("What is the name of this " + category.substring(0, category.length()-1) + "?");
        Toast.makeText(this, ""+ category, Toast.LENGTH_SHORT).show();

        switch (category){
            case "animals": {
                items.addAll(Constants.animals);
                break;
            }
            case "clothes": {
                items.addAll(Constants.clothes);
                break;
            }
            case "fruits": {
                items.addAll(Constants.fruits);
                break;
            }
            case "vegetables": {
                items.addAll(Constants.vegetables);
                break;
            }
        }
        for (CardItem item : items) {
            Log.i("items", item.getName());
        }
        Log.i("items---------------", "--------------");
        Collections.shuffle(items);
        itemCard.setImageResource(items.get(0).getImage());
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);


        mic.setOnClickListener(view ->  {
            checkAudioPermission();
            mic.setImageResource(R.drawable.ic_mic);
            startSpeechToText();
//            if (motionEvent.getAction() == MotionEvent.ACTION_UP){
//                speechRecognizer.stopListening();
//            }
//            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//                mic.setImageResource(R.drawable.ic_mic);
//                speechRecognizer.startListening(speechRecognizerIntent);
//            }
//            return false;
        });

        Thread timer = new Thread() {
            public void run() {
                try {
                    try {
                        sleep(300);
                        if (tts != null) {
                            tts.speak(question.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                finally {
//                        finish();
                }
            }

        };
        timer.start();
        if (tts.isSpeaking()) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (tts != null) {
            tts.speak(question.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
        }

    }

    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (player!=null)
            player.stop();

        speechRecognizer.destroy();
        items.clear();

        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSpeechToText() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                answer.setText("");
                answer.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {
                mic.setImageResource(R.drawable.ic_mic_off);
            }

            @Override
            public void onError(int i) {
                String message = "";
                switch (i){
                    case SpeechRecognizer.ERROR_AUDIO: { message = "Audio recording error"; }
                    case SpeechRecognizer.ERROR_CLIENT: { message = "Client side error"; }
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:{
                        message = "Insufficient permissions";
                    }
                    default: { message = "Unknown error"; }
                }
                mic.setImageResource(R.drawable.ic_mic_off);
                answer.setText("");
                Toast.makeText(PlayingActivity.this, "Error occurred "+message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResults(Bundle bundle) {
                mic.setImageResource(R.drawable.ic_mic_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (data != null)
                    answer.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        speechRecognizer.startListening(speechRecognizerIntent);
    }

    private void checkAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // M = 23
            if (ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.programmingtech.offlinespeechtotext"));
                startActivity(intent);
                Toast.makeText(this, "Allow Microphone Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onInit(int i) {

    }

    public void speak(View view) {
        ImageView iv = (ImageView) view;
        iv.setImageResource(R.drawable.ic_mic);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        }
        catch (Exception e) {
            Toast.makeText(this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
            iv.setImageResource(R.drawable.ic_mic_off);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                answer.setText(Objects.requireNonNull(result).get(0));
                mic.setImageResource(R.drawable.ic_mic_off);
            }
        }
    }

    int i = 1;
    boolean isDone = false;
    public void done(View view) {
        if (!isDone) {
            if (answer.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Answer", Toast.LENGTH_SHORT).show();
                tts.speak("Please Answer", TextToSpeech.QUEUE_FLUSH, null, null);
                return;
            }
            EmitterConfig emitterConfig = new Emitter(5L, TimeUnit.SECONDS).perSecond(50);
            Drawable drawable;

            if (answer.getText().toString().equalsIgnoreCase(items.get(i-1).getName())){
                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.party);
                if (drawable != null)
                    drawableShape = new Shape.DrawableShape(drawable, true, true);
                konfettiView.start(
                        new PartyFactory(emitterConfig)
                                .angle(270)
                                .spread(90)
                                .setSpeedBetween(1f, 5f)
                                .timeToLive(2000L)
                                .shapes(new Shape.Rectangle(0.2f), drawableShape)
                                .sizes(new Size(12, 5f, 0.2f))
                                .position(0.0, 0.0, 1.0, 0.0)
                                .build());
//                drawableShape = new Shape.DrawableShape(drawable, true, true);
                player = MediaPlayer.create(this,R.raw.right);
                player.start();
            }else {
//                drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_sad);
//                drawableShape = ImageUtil.loadDrawable(drawable, true, true);
                tts.speak("Try Next Time", TextToSpeech.QUEUE_FLUSH, null, null);
                player = MediaPlayer.create(this,R.raw.wrong);
                player.start();
            }
            YoYo.with(Techniques.FlipInX).duration(3000).playOn(itemCard);
            solid.setVisibility(View.GONE);
            doneBtn.setImageResource(R.drawable.ic_arrow_next);
            isDone = true;
        }else {
            isDone = false;
            if (i == items.size()) {
                tts.speak("Back and select another category", TextToSpeech.QUEUE_FLUSH, null, null);
                doneBtn.setEnabled(false);
                return;
            }
            if (player!=null)
                player.stop();

            if (tts.isSpeaking()) {
//                tts.stop();
                tts.shutdown();
            }

            solid.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceInRight).duration(500).playOn(itemCard);
            itemCard.setImageResource(items.get(i++).getImage());
            doneBtn.setImageResource(R.drawable.ic_done);
        }
    }
}