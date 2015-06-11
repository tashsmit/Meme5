package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static meme5.c4q.nyc.meme_project.VanillaMeme.*;

/**
 * Created by c4q-anthonyf on 6/5/15.
 */
public class DemotivationalMeme extends Activity {

    Bitmap image, memeImage;
    ImageView preview;
    EditText largeET, smallET;
    Button save, share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational_meme);

        preview = (ImageView) findViewById(R.id.preview);
        largeET = (EditText) findViewById(R.id.large);
        smallET = (EditText) findViewById(R.id.small);
        save = (Button) findViewById(R.id.save);
        share = (Button) findViewById(R.id.share);

        String imgFilePath;
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("imgFilePath") != null) {
            imgFilePath = bundle.getString("imgFilePath");
            image = Decode.decodeFile(image, imgFilePath, true);
            preview.setImageBitmap(image);
        }

        TextWatch tw = new TextWatch(largeET, smallET);
        largeET.addTextChangedListener(tw);
        smallET.addTextChangedListener(tw);

        save.setOnClickListener(new SaveListener());
        share.setOnClickListener(new ShareListener());
    }

    public class TextWatch implements TextWatcher {

        private EditText large, small;

        public TextWatch(EditText large, EditText small) {
            this.large = large;
            this.small = small;
        }

        @Override
        public void afterTextChanged(Editable s) {
            memeImage = drawTextToBitmap(image.copy(image.getConfig(), true), large.getText().toString().toUpperCase(), true);
            memeImage = drawTextToBitmap(memeImage, small.getText().toString(), false);
            preview.setImageBitmap(memeImage);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    public Bitmap drawTextToBitmap(Bitmap bitmap, String mText1, boolean largeText) {
        try {
            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

            // set default bitmap config if none
            if (bitmapConfig == null)
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;

            // resource bitmaps are imutable, so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);
            Canvas newCanvas = new Canvas(bitmap);

            newCanvas.drawBitmap(bitmap, 0, 0, null);
            int textSize;
            int heightOffset;
            if (largeText) {
                textSize = 70;
                heightOffset = 150;
                mText1 = mText1.toUpperCase();
            } else {
                textSize = 30;
                heightOffset = 210;
            }

            TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.WHITE);
            paint.setTextSize(textSize);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(10f, 10f, 10f, Color.BLACK);

            Rect rectText = new Rect();
            paint.getTextBounds(mText1, 0, mText1.length(), rectText);

            newCanvas.drawText(mText1,
                    newCanvas.getWidth() / 2 - rectText.width() / 2, newCanvas.getHeight() - 299 + rectText.height() + heightOffset, paint);


            return bitmap;
        } catch (Exception e) {
            return null;
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
