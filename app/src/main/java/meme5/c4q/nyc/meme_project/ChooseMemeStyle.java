package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;


public class ChooseMemeStyle extends Activity {

    public static final String TAG = "MEME_ACTIVITY";
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

        // Get thumbnail of image chosed to be displayed in R.id.thumbnail
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("imgFilePath") != null)
            imgFilePath = bundle.getString("imgFilePath");

        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        Bitmap bmp2 = BitmapFactory.decodeFile(imgFilePath);
        thumbnail.setImageBitmap(bmp2);

        RadioGroup group = (RadioGroup) findViewById(R.id.styleGroup);
        img = (ImageView) findViewById(R.id.sampleImageHolder);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Log.d(TAG, "onCheckedChanged()");
                switch (checkedId) {
                    case R.id.chooseVanilla:

                        Log.d(TAG, "onCheckedChanged() -- chooseVanilla");
                        vanilla = true;
                        img.setImageResource(R.drawable.vanilla_sample);
                        break;

                    case R.id.chooseDemotivational:

                        Log.d(TAG, "onCheckedChanged() -- chooseDemotivational");
                        vanilla = false;
                        img.setImageResource(R.drawable.demotivational_sample);
                        break;
                }
            }
        });

        group.check(R.id.chooseVanilla);

        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (vanilla) {
                    Intent vanillameme = new Intent(ChooseMemeStyle.this, VanillaMeme.class);
                    vanillameme.putExtra("imgFilePath", imgFilePath);
                    startActivity(vanillameme);
                } else {
                    Intent demotivationalMeme = new Intent(ChooseMemeStyle.this, DemotivationalMeme.class);
                    demotivationalMeme.putExtra("imgFilePath", imgFilePath);
                    startActivity(demotivationalMeme);
                }
            }
        });
    }
}

