package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileOutputStream;

public class VanillaMeme extends Activity {

    ImageView preview;
    EditText topET, bottomET;
    Bitmap memeImage, image;
    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme);

        topET = (EditText) findViewById(R.id.top);
        bottomET = (EditText) findViewById(R.id.bottom);
        preview = (ImageView) findViewById(R.id.testImage);
        nextButton = (Button) findViewById(R.id.next);

        //get image path from previous activity
        String imgFilePath = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("imgFilePath") != null) {
            imgFilePath = bundle.getString("imgFilePath");
            image = Decode.decodeFile(image, imgFilePath, false);
            preview.setImageBitmap(image);
        }

        TextWatch tw = new TextWatch(topET, bottomET);
        topET.addTextChangedListener(tw);
        bottomET.addTextChangedListener(tw);
    }

    // Method used to write text on image
    public Bitmap drawTextToBitmap(Bitmap bitmap, String mText1, int textSize, int strokeSize, boolean top) {
        try {
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

            // set default bitmap config if none
            if (bitmapConfig == null)
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;

            // resource bitmaps are imutable, so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);
            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.rgb(255, 255, 255));
            paint.setTextSize(textSize);
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/impact.ttf");
            paint.setTypeface(tf);
            paint.setTextAlign(Paint.Align.CENTER);

            // center text
            int xPos = (bitmap.getWidth() / 2) - 2;     //-2 is for regulating the x position offset
            int yPos = bitmap.getHeight()-50;

            // create a static layout for word wrapping
            StaticLayout mTextLayout = new StaticLayout(mText1, paint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (top)
                canvas.translate(xPos, 10);
            else
                canvas.translate(xPos, yPos);

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
            return null;
        }
    }

    // ??????
    public void launchLastActivity(View view){
        if(memeImage != null) {
            try {
                //Write file
                String filename = "meme.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                memeImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                memeImage.recycle();

                //Pop intent
                Intent lastActivity = new Intent(VanillaMeme.this, SaveShare.class);
                lastActivity.putExtra("memeImage", filename);
                startActivity(lastActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class TextWatch implements TextWatcher {

        private EditText top, bottom;

        public TextWatch(EditText top, EditText bottom) {
            this.top = top;
            this.bottom = bottom;
        }

        @Override
        public void afterTextChanged(Editable s) {
            memeImage = drawTextToBitmap(image.copy(image.getConfig(), true), top.getText().toString(), 40, 2, true);
            memeImage = drawTextToBitmap(memeImage, bottom.getText().toString(), 40, 2, false);
            preview.setImageBitmap(memeImage);
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}
