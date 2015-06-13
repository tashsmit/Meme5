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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileOutputStream;

/**
 * Created by c4q-anthonyf on 6/5/15.
 */
public class DemotivationalMemeActivity extends Activity {

    Bitmap image;
    Bitmap memeImage;
    ImageView preview;
    EditText largeText;
    EditText smallText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational_meme);

        preview = (ImageView) findViewById(R.id.preview);

        getMeme();

        largeText = (EditText) findViewById(R.id.large);
        smallText = (EditText) findViewById(R.id.small);

    }

    public void getMeme() {
        String imgFilePath = "";
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("imgFilePath") != null)
        {
            imgFilePath = bundle.getString("imgFilePath");
            decodeFile(imgFilePath);
        }
    }

    public void previewTextOnClick (View view) {
        memeImage = drawTextToBitmap(image.copy(image.getConfig(),true),largeText.getText().toString(),true);
        memeImage = drawTextToBitmap(memeImage,smallText.getText().toString(),false);
        preview.setImageBitmap(memeImage);
    }

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
                Intent lastActivity = new Intent(this, add_text.class);
                lastActivity.putExtra("memeImage", filename);
                startActivity(lastActivity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public Bitmap drawTextToBitmap(Bitmap bitmapImage, String mText1, boolean largeText) {
        try {

            Canvas newCanvas = new Canvas(bitmapImage);

            newCanvas.drawBitmap(bitmapImage, 0, 0, null);
            int textSize;
            int heightOffset;
            if(largeText) {
                textSize = 80;
                heightOffset = 30;
                mText1 = mText1.toUpperCase();
            }else{
                textSize = 40;
                heightOffset = 140;
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
                        newCanvas.getWidth() /2 - rectText.width() /2, newCanvas.getHeight() - 299 + rectText.height() + heightOffset, paintText);
            }

            return bitmapImage;
        } catch (Exception e) {
            // TODO: handle exception


            return null;
        }

    }


}
