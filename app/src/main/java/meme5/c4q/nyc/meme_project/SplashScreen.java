package meme5.c4q.nyc.meme_project;

/**
 * Created by c4q-tashasmith on 6/7/15.
 * This activity will display a screen when the app is initially opened for 2.5 seconds
 */
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends Activity {


    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 100;//Time which activity will be displayed for in ms
    ImageView img;
    AnimationDrawable frameAnimation;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);

        img = (ImageView) findViewById(R.id.splashscreen);
        img.setImageResource(R.drawable.splash_animation);
        frameAnimation = (AnimationDrawable) img.getDrawable();
        frameAnimation.start();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}