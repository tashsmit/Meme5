package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import org.w3c.dom.Text;

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
            image = Decode.decodeFile(image, imgFilePath, true);
            preview.setImageBitmap(image);
        }

        TextWatch tw = new TextWatch(largeET, smallET);
        largeET.addTextChangedListener(tw);
        smallET.addTextChangedListener(tw);
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

            Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintText.setColor(Color.WHITE);
            paintText.setTextSize(textSize);
            paintText.setStyle(Paint.Style.FILL);
            paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

            Rect rectText = new Rect();
            paintText.getTextBounds(mText1, 0, mText1.length(), rectText);

            newCanvas.drawText(mText1,
                    newCanvas.getWidth() / 2 - rectText.width() / 2, newCanvas.getHeight() - 299 + rectText.height() + heightOffset, paintText);


            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public class TextWatch implements TextWatcher {

        private EditText large, small;

        public TextWatch(EditText large, EditText small) {
            this.large = large;
            this.small = small;
        }

        @Override
        public void afterTextChanged(Editable s) {
            memeImage = drawTextToBitmap(image.copy(image.getConfig(), true), large.getText().toString(), true);
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
}
