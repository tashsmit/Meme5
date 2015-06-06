package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by c4q-anthonyf on 6/5/15.
 */
public class DemotivationalMemeActivity extends Activity {

    Bitmap image;
    ImageView preview;
    EditText largeText;
    EditText smallText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational_meme);

        preview = (ImageView) findViewById(R.id.preview);

        String imgFilePath = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("imgFilePath") != null)
        {
            imgFilePath = bundle.getString("imgFilePath");
            decodeFile(imgFilePath);
        }

        largeText = (EditText) findViewById(R.id.large);
        smallText = (EditText) findViewById(R.id.small);
        Button previewText = (Button) findViewById(R.id.previewText);
        previewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image = drawTextToBitmap(image,largeText.getText().toString(),true,45);
                image = drawTextToBitmap(image,smallText.getText().toString(),false,10);
                preview.setImageBitmap(image);
            }
        });
    }

    private Bitmap addBorder(Bitmap bmp,int color, int borderSize) {
        return addBorder(bmp, color,borderSize, borderSize, borderSize, borderSize);
    }

    private Bitmap addBorder(Bitmap bmp, int color, int left, int top, int right, int bottom){
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
        image= ExifUtils.rotateBitmap(filePath, b1);

        image = addBorder(image, Color.BLACK, 5);
        image = addBorder(image,Color.WHITE, 5);
        image = addBorder(image, Color.BLACK, 100, 150, 100, 300);
        preview.setImageBitmap(image);
    }

    public Bitmap drawTextToBitmap(Bitmap bitmapImage, String mText1, boolean largeText, int heightOffset) {
        try {

            Canvas newCanvas = new Canvas(bitmapImage);

            newCanvas.drawBitmap(bitmapImage, 0, 0, null);
            int textSize;
            if(largeText) {
                textSize = 175;
            }else{
                textSize = 50;
            }

            if(mText1 != null) {

                Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
                paintText.setColor(Color.WHITE);
                paintText.setTextSize(textSize);
                paintText.setStyle(Paint.Style.FILL);
                paintText.setShadowLayer(10f, 10f, 10f, Color.BLACK);

                Rect rectText = new Rect();
                paintText.getTextBounds(mText1, 0, mText1.length(), rectText);

                newCanvas.drawText(mText1,
                        newCanvas.getWidth() /2 - rectText.width() /2, newCanvas.getHeight() - rectText.height() - heightOffset, paintText);
            }

            return bitmapImage;
        } catch (Exception e) {
            // TODO: handle exception


            return null;
        }

    }


}
