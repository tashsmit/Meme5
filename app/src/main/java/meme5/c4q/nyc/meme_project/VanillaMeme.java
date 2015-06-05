package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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
import android.widget.RadioGroup;


public class VanillaMeme extends Activity {

    ImageView image;
    EditText line1;
    EditText line2;
    EditText line3;
    Button previewButton;
    Bitmap bmp;
    String line1Text;
    String line2Text;
    String line3Text;
    String finalText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme);

        //TODO - need file path of picture that will come from first activity


        image = (ImageView) findViewById(R.id.testImage);
        line1 = (EditText) findViewById(R.id.top);
        line2 = (EditText) findViewById(R.id.middle);
        line3 = (EditText) findViewById(R.id.bottom);
        previewButton = (Button) findViewById(R.id.drawButton);

        RadioGroup group = (RadioGroup) findViewById(R.id.textSizes);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                line1Text = line1.getText().toString();
                line2Text = line2.getText().toString();
                line3Text = line3.getText().toString();

                switch (checkedId) {
                    case R.id.small:
                        bmp = drawTextToBitmap(VanillaMeme.this, R.drawable.vanilla_test, line1Text, 20, 2);
                        image.setImageBitmap(bmp);
                        break;
                    case R.id.medium:
                        bmp = drawTextToBitmap(VanillaMeme.this, R.drawable.vanilla_test, line1Text, 25, 3);
                        image.setImageBitmap(bmp);
                        break;
                    case R.id.large:
                        bmp = drawTextToBitmap(VanillaMeme.this, R.drawable.vanilla_test, line1Text, 30, 4);
                        image.setImageBitmap(bmp);
                        break;
                }
            }
        });

    }


    public Bitmap drawTextToBitmap(Context mContext, int resourceId, String mText1, int textSize, int strokeSize) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

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
            paint.setTextSize((int) (textSize * scale));
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


}
