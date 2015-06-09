package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileOutputStream;

/**
 * Created by c4q-anthonyf on 6/5/15.
 */
public class DemotivationalMemeActivity extends Activity {

    Bitmap image, memeImage;
    ImageView preview;
    EditText largeET, smallET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational_meme);

        preview = (ImageView) findViewById(R.id.preview);
        largeET = (EditText) findViewById(R.id.large);
        smallET = (EditText) findViewById(R.id.small);

        String imgFilePath = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("imgFilePath") != null) {
            imgFilePath = bundle.getString("imgFilePath");
            decodeFile(imgFilePath);
        }

        largeET.addTextChangedListener(new TextWatch(largeET, smallET, true));
        smallET.addTextChangedListener(new TextWatch(smallET, smallET, false));
    }

    public void launchLastActivity(View view) {
        if (memeImage != null) {
            try {
                //Write file
                String filename = "meme.png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                memeImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                memeImage.recycle();

                //Pop intent
                Intent lastActivity = new Intent(this, SaveShare.class);
                lastActivity.putExtra("memeImage", filename);
                startActivity(lastActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap addBorder(Bitmap bmp, int color, int borderSize) {
        return addBorder(bmp, color, borderSize, borderSize, borderSize, borderSize);
    }

    private Bitmap addBorder(Bitmap bmp, int color, int left, int top, int right, int bottom) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + left + right, bmp.getHeight() + top + bottom, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(color);
        canvas.drawBitmap(bmp, left, top, null);

        return bmpWithBorder;
    }

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
        image = ExifUtils.rotateBitmap(filePath, b1);

        image = addBorder(image, Color.BLACK, 5);
        image = addBorder(image, Color.WHITE, 5);
        image = addBorder(image, Color.BLACK, 30, 30, 30, 150);
        preview.setImageBitmap(image);
    }

    public Bitmap drawTextToBitmap(Bitmap bitmapImage, String mText1, boolean largeText) {
        try {

            Canvas newCanvas = new Canvas(bitmapImage);

            newCanvas.drawBitmap(bitmapImage, 0, 0, null);/**/
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

            if (mText1 != null) {

                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setColor(Color.WHITE);
                paintText.setTextSize(textSize);
                paintText.setStyle(Paint.Style.FILL);
                paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

                Rect rectText = new Rect();
                paintText.getTextBounds(mText1, 0, mText1.length(), rectText);

                newCanvas.drawText(mText1,
                        newCanvas.getWidth() / 2 - rectText.width() / 2, newCanvas.getHeight() - 299 + rectText.height() + heightOffset, paintText);
            }

            return bitmapImage;
        } catch (Exception e) {
            return null;
        }
    }

    public class TextWatch implements TextWatcher {

        private EditText large, small;
        private boolean isLarge;

        public TextWatch(EditText large, EditText small, boolean isLarge) {
            this.large = large;
            this.small = small;
            this.isLarge = isLarge;
        }

        @Override
        public void afterTextChanged(Editable s) {
            memeImage = drawTextToBitmap(image.copy(image.getConfig(), true), large.getText().toString(), isLarge);
            memeImage = drawTextToBitmap(memeImage, small.getText().toString(), !isLarge);
            preview.setImageBitmap(memeImage);
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
    }
}
