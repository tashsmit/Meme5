package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class VanillaMeme extends Activity {

    ImageView image;
    EditText line1;
    EditText line2;
    Bitmap bmp2;
    String imgFilePath;
    TextView title;
    Button share;
    Button save;
    RelativeLayout memeLayout;
    Button small, medium, large;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanilla_meme);


        //get image path from previous activity
        Bundle bundle = getIntent().getExtras();

        try {
            if (bundle.getString("imgFilePath") != null) {
                imgFilePath = bundle.getString("imgFilePath");
                image = (ImageView) findViewById(R.id.testImage);
                bmp2 = BitmapFactory.decodeFile(imgFilePath);
                image.setImageBitmap(bmp2);
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "No Image Found", Toast.LENGTH_LONG).show();
        }


        save = (Button) findViewById(R.id.saveButton);
        share = (Button) findViewById(R.id.shareButton);
        line1 = (EditText) findViewById(R.id.top);
        line2 = (EditText) findViewById(R.id.bottom);
        image = (ImageView) findViewById(R.id.testImage);
        title = (TextView) findViewById(R.id.title);
        small = (Button) findViewById(R.id.small);
        medium = (Button) findViewById(R.id.medium);
        large = (Button) findViewById(R.id.large);
        memeLayout = (RelativeLayout) findViewById(R.id.memeLayout);


        //apply Font Typeface
        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/ubuntu.ttf");
        title.setTypeface(tf);
        small.setTypeface(tf);
        medium.setTypeface(tf);
        large.setTypeface(tf);
        line1.setTypeface(tf);
        line2.setTypeface(tf);


    }

    //following three methods will change the font size for the textViews
    public void textSizeSmall(View v) {
        line1.setTextSize(15);
        line2.setTextSize(15);
    }

    public void textSizeMed(View v) {
        line1.setTextSize(25);
        line2.setTextSize(25);
    }

    public void textSizeLg(View v) {
        line1.setTextSize(35);
        line2.setTextSize(35);
    }


//Method to create a bitmap from a view, in our case from a linearLayout
    public Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);

        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

//Method to share the meme
    public void sharingM(View view) {

        bmp2 = getBitmapFromView(memeLayout);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp2.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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

//Method to Save the Meme into the camera roll
    public void savingM(View view) {
        bmp2 = getBitmapFromView(memeLayout);

        MediaStore.Images.Media.insertImage(VanillaMeme.this.getContentResolver(), bmp2, "title.jpg", "some description");
        Toast.makeText(this, "Meme saved!", Toast.LENGTH_LONG).show();
    }

}