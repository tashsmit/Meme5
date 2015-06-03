package meme5.c4q.nyc.meme_project;

import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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


public class ChooseMemeStyle extends ActionBarActivity {

    protected RadioGroup styleGroup;
    protected RadioButton styleButton;
    protected Button nextButton;
    protected boolean vanilla;
    protected Random slideShow;
    ImageView img;
    AnimationDrawable frameAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_meme_style);

        //apply fonts -
        //TODO: need to find a way to add this font globally through style!
        TextView step1 = (TextView) findViewById(R.id.step1);
        TextView step2 = (TextView) findViewById(R.id.step2);
        TextView step3 = (TextView) findViewById(R.id.step3);
        TextView title = (TextView) findViewById(R.id.title);
        TextView vanillaRadio = (TextView) findViewById(R.id.chooseVanilla);
        TextView demotivationalRadio = (TextView) findViewById(R.id.chooseDemotivational);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ubuntu.ttf");
        step1.setTypeface(tf);
        step2.setTypeface(tf);
        step3.setTypeface(tf);
        title.setTypeface(tf);
        vanillaRadio.setTypeface(tf);
        demotivationalRadio.setTypeface(tf);

        // Load the ImageView that will host the animation and
        // set its image source to our AnimationDrawable XML resource.
        img = (ImageView) findViewById(R.id.sampleImageHolder);
        img.setImageResource(R.drawable.vanilla_animation);

        // Get the image, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) img.getDrawable();

        // Start the animation (looped playback by default).
        frameAnimation.start();
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.chooseVanilla:
                //determine if user chose vanilla or demotivational
                if (checked)
                    vanilla = true;
                img = (ImageView) findViewById(R.id.sampleImageHolder);
                img.setImageResource(R.drawable.vanilla_animation);

                frameAnimation = (AnimationDrawable) img.getDrawable();

                frameAnimation.start();
                break;
            case R.id.chooseDemotivational:
                //determine if user chose vanilla or demotivational
                if (checked)
                    vanilla = false;
                img = (ImageView) findViewById(R.id.sampleImageHolder);
                img.setImageResource(R.drawable.demotivational_animation);

                frameAnimation = (AnimationDrawable) img.getDrawable();

                frameAnimation.start();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_meme_style, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
