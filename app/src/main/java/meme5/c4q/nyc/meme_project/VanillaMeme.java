package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.FileOutputStream;


public class VanillaMeme extends Activity {

    ImageView image;
    EditText line1;
    Bitmap bmp, bmp2;
    String line1Text;
    String imgFilePath;
    Button nextButton;
    TextView title;
    RadioButton small, medium, large;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme);

        getMeme();

        line1 = (EditText) findViewById(R.id.top);
        image = (ImageView) findViewById(R.id.testImage);
        nextButton = (Button) findViewById(R.id.next);
        title = (TextView) findViewById(R.id.title);
        small = (RadioButton) findViewById(R.id.small);
        medium = (RadioButton) findViewById(R.id.medium);
        large = (RadioButton) findViewById(R.id.large);

        applyFont();

        //create on check listener to see which size is chosen
        RadioGroup group = (RadioGroup) findViewById(R.id.textSizes);
        group.setOnCheckedChangeListener(radioGroupChangeListener);
        group.check(R.id.small);

    }

    public void getMeme(){
        //get image path from previous activity
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("imgFilePath") != null) {
            imgFilePath = bundle.getString("imgFilePath");
            decodeFile(imgFilePath);
        }
    }

    public void applyFont(){
        //apply font
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ubuntu.ttf");
        title.setTypeface(tf);
        small.setTypeface(tf);
        medium.setTypeface(tf);
        large.setTypeface(tf);
        line1.setTypeface(tf);
        nextButton.setTypeface(tf);
    }

    public RadioGroup.OnCheckedChangeListener radioGroupChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            line1Text = line1.getText().toString();

            switch (checkedId) {
                case R.id.small:
                    bmp = drawTextToBitmap(bmp2, line1Text, 40, 2);
                    image.setImageBitmap(bmp);
                    break;
                case R.id.medium:
                    bmp = drawTextToBitmap(bmp2, line1Text, 55, 3);
                    image.setImageBitmap(bmp);
                    break;
                case R.id.large:
                    bmp = drawTextToBitmap(bmp2, line1Text, 80, 4);
                    image.setImageBitmap(bmp);
                    break;
            }
        }
    };

    //method used to write text on image
    public Bitmap drawTextToBitmap(Bitmap bitmap, String mText1, int textSize, int strokeSize) {
        try {
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(255, 255, 255));
            // text size in pixels
            paint.setTextSize(textSize);
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
            // make text font impact
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/impact.ttf");
            paint.setTypeface(tf);
            paint.setTextAlign(Paint.Align.CENTER);
            // center text
            int xPos = (bitmap.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

            // create a static layout for word wrapping
            StaticLayout mTextLayout = new StaticLayout(mText1, paint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            canvas.translate(xPos, 10); //position the text
            //draw text without stroke first
            mTextLayout.draw(canvas);

            //set options on paint for stroke
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeSize);

            //then redraw text for stroke
            mTextLayout.draw(canvas);

            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception


            return null;
        }

    }

    //method used to resize, rotate and set up bitmap
    private void decodeFile(String filePath) {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap b1 = BitmapFactory.decodeFile(filePath, o2);
        bmp2= ExifUtils.rotateBitmap(filePath, b1);
    }

    public void launchLastActivity(View view){

        if(bmp != null) {
            try {
                //Write file
                String filename = "meme.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                bmp.recycle();

                //Pop intent
                Intent lastActivity = new Intent(VanillaMeme.this, add_text.class);
                lastActivity.putExtra("memeImage", filename);
                startActivity(lastActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
