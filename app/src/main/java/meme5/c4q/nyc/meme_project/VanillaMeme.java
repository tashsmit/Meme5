package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
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
    Button save, share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme);

        topET = (EditText) findViewById(R.id.top);
        bottomET = (EditText) findViewById(R.id.bottom);
        preview = (ImageView) findViewById(R.id.testImage);
        save = (Button) findViewById(R.id.save);
        share = (Button) findViewById(R.id.share);

        //get image path from previous activity
        String imgFilePath;
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("imgFilePath") != null) {
            imgFilePath = bundle.getString("imgFilePath");
            image = Decode.decodeFile(image, imgFilePath, false);
            preview.setImageBitmap(image);
        }

        TextWatch tw = new TextWatch(topET, bottomET);
        topET.addTextChangedListener(tw);
        bottomET.addTextChangedListener(tw);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = "meme_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
                MediaStore.Images.Media.insertImage(getContentResolver(), memeImage, timeStamp, "Created with Meme5");
                Toast.makeText(getApplicationContext(), "Meme saved!", Toast.LENGTH_LONG).show();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        });
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
            memeImage = drawTextToBitmap(image.copy(image.getConfig(), true), top.getText().toString(), true);
            memeImage = drawTextToBitmap(memeImage, bottom.getText().toString(), false);
            preview.setImageBitmap(memeImage);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
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

            int textSize = 40;
            int strokeSize = 2;

            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);
            Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/impact.ttf");
            paint.setTypeface(tf);
            paint.setTextAlign(Paint.Align.CENTER);

            // center text
            int xPos = (bitmap.getWidth() / 2) - 2;     //-2 is for regulating the x position offset
            int yPos = bitmap.getHeight() - 50;

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
}
