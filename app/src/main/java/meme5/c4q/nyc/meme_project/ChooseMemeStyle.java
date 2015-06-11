package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class ChooseMemeStyle extends Activity {

    public static final String TAG = "MEME_ACTIVITY";
    Button small, medium, large;
    protected Button nextButton;
    protected boolean vanilla;
    ImageView img, thumbnail;
    AnimationDrawable frameAnimation;
    String imgFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_meme_style);

        //Get Image Chosen and place it on the thumbnail ImageView
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("imgFilePath") != null)
        {
            imgFilePath = bundle.getString("imgFilePath");
        }
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        Bitmap bmp2 = BitmapFactory.decodeFile(imgFilePath);
        thumbnail.setImageBitmap(bmp2);

        //apply fonts -

        TextView title = (TextView) findViewById(R.id.title);
        small=(Button) findViewById(R.id.small);
        medium=(Button) findViewById(R.id.medium);
        large=(Button) findViewById(R.id.large);

        TextView demotivationalRadio = (TextView) findViewById(R.id.chooseDemotivational);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ubuntu.ttf");
        title.setTypeface(tf);

        demotivationalRadio.setTypeface(tf);

        RadioGroup group = (RadioGroup) findViewById(R.id.styleGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Log.d(TAG, "onCheckedChanged()");
                // Is the button now checked?
                //boolean checked = ((RadioButton) view).isChecked();
                // Check which radio button was clicked
                switch (checkedId) {
                    case R.id.chooseVanilla:

                        Log.d(TAG, "onCheckedChanged() -- chooseVanilla");
                        //determine if user chose vanilla or demotivational

                        vanilla = true;
                        img = (ImageView) findViewById(R.id.sampleImageHolder);
                        img.setImageResource(R.drawable.vanilla_animation);

                        frameAnimation = (AnimationDrawable) img.getDrawable();

                        frameAnimation.start();
                        break;
                    case R.id.chooseDemotivational:

                        Log.d(TAG, "onCheckedChanged() -- chooseDemotivational");
                        //determine if user chose vanilla or demotivational
                        vanilla = false;
                        img = (ImageView) findViewById(R.id.sampleImageHolder);
                        img.setImageResource(R.drawable.demotivational_animation);

                        frameAnimation = (AnimationDrawable) img.getDrawable();

                        frameAnimation.start();
                        break;
                }
            }
        });

        //Intents to go to corresponding activity

        group.check(R.id.chooseDemotivational);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (vanilla) {
                    Intent vanillameme = new Intent(ChooseMemeStyle.this,VanillaMeme.class);
                    vanillameme.putExtra("imgFilePath",imgFilePath);
                    startActivity(vanillameme);
                }
                else {
                    Intent demotivationalMeme = new Intent(ChooseMemeStyle.this, DemotivationalMemeActivity.class);
                    demotivationalMeme.putExtra("imgFilePath",imgFilePath);
                    startActivity(demotivationalMeme);
                }
            }
        });
    }
}

