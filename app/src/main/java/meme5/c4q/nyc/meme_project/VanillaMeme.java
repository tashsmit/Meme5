package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VanillaMeme extends Activity {

    ImageView preview;
    EditText topET, bottomET;
    Bitmap memeImage, image;
    Button switch_btn, save, share;
    int textSize, strokeSize, textLength;
    String imgFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme);

        topET = (EditText) findViewById(R.id.top);
        bottomET = (EditText) findViewById(R.id.bottom);
        preview = (ImageView) findViewById(R.id.testImage);
        switch_btn = (Button) findViewById(R.id.switch_btn);
        save = (Button) findViewById(R.id.save);
        share = (Button) findViewById(R.id.share);

        //get image path from previous activity
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("imgFilePath") != null) {
            imgFilePath = bundle.getString("imgFilePath");
            image = Decode.decodeFile(image, imgFilePath, false);
            preview.setImageBitmap(image);
        }

        TextWatch tw = new TextWatch(topET, bottomET);
        topET.addTextChangedListener(tw);
        bottomET.addTextChangedListener(tw);

        switch_btn.setOnClickListener(new SwitchListener());
        save.setOnClickListener(new SaveListener());
        share.setOnClickListener(new ShareListener());
    }

    // custom TextWatcher updates preview when text is added
    public class TextWatch implements TextWatcher {

        private EditText top, bottom;

        public TextWatch(EditText top, EditText bottom) {
            this.top = top;
            this.bottom = bottom;
        }

        @Override
        public void afterTextChanged(Editable s) {
            memeImage = drawTextToBitmap(image.copy(image.getConfig(), true), top.getText().toString().toUpperCase(), true);
            memeImage = drawTextToBitmap(memeImage, bottom.getText().toString().toUpperCase(), false);
            preview.setImageBitmap(memeImage);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            textLength = s.length();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
    }


    // Method used to write text on image
    public Bitmap drawTextToBitmap(Bitmap bitmap, String mText1, boolean top) {
        try {
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

            // set default bitmap config if none
            if (bitmapConfig == null)
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;

            // resource bitmaps are imutable, so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas canvas = new Canvas(bitmap);

            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/impact.ttf");
            paint.setTypeface(tf);
            paint.setTextAlign(Paint.Align.CENTER);

            // create a static layout for word wrapping
            // checked for number of chars entered to move yPos of bottom text so new lines do not get cut off
            StaticLayout mTextLayout = new StaticLayout(mText1, paint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            int xPos = (bitmap.getWidth() / 2) - 2;
            int yPos = bitmap.getHeight() - 70;
            if (top)
                canvas.translate(xPos, 10);
            else {
                while ((textLength * 46) > preview.getWidth()) {
                    yPos -= 45;
                    textLength -= 14;
                }
                canvas.translate(xPos, yPos);
            }

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int width = preview.getWidth();
        int height = preview.getHeight();

        if (width < height)
            textSize = height / 20;
        else
            textSize = height / 13;

        strokeSize = textSize / 20;
    }

    public class SwitchListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent demotivationalMeme = new Intent(getApplicationContext(), DemotivationalMeme.class);
            demotivationalMeme.putExtra("imgFilePath", imgFilePath);
            demotivationalMeme.putExtra("top", topET.getText().toString());
            demotivationalMeme.putExtra("bottom", bottomET.getText().toString());
            startActivity(demotivationalMeme);
        }
    }

    public class SaveListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String timeStamp = "meme_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
            MediaStore.Images.Media.insertImage(getContentResolver(), memeImage, timeStamp, "Created with Meme5");
            Toast.makeText(getApplicationContext(), "Meme has been saved!", Toast.LENGTH_LONG).show();
        }
    }

    public class ShareListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            memeImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
            try {
                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
            startActivity(Intent.createChooser(share, "Share Image"));
        }
    }
}
