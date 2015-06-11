package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by c4q-anthonyf on 6/5/15.
 */
public class DemotivationalMemeActivity extends Activity {

    ImageView image;
    EditText line1;
    EditText line2;
    Bitmap bmp2;
    String imgFilePath;
    Button share;
    Button save;
    RelativeLayout memeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotivational_meme);


        //get image path from previous activity
        Bundle bundle = getIntent().getExtras();

        try {
            if (bundle.getString("imgFilePath") != null) {
                imgFilePath = bundle.getString("imgFilePath");
                image = (ImageView) findViewById(R.id.image);
                bmp2 = BitmapFactory.decodeFile(imgFilePath);
                image.setImageBitmap(bmp2);
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "No Image Found", Toast.LENGTH_LONG).show();
        }

        save = (Button) findViewById(R.id.saveButton);
        share = (Button) findViewById(R.id.shareButton);
        line1 = (EditText) findViewById(R.id.title);
        line2 = (EditText) findViewById(R.id.description);
        image = (ImageView) findViewById(R.id.image);
        memeLayout = (RelativeLayout) findViewById(R.id.memeLayout);
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

        MediaStore.Images.Media.insertImage(DemotivationalMemeActivity.this.getContentResolver(), bmp2, "title.jpg", "some description");
        Toast.makeText(this, "Meme saved!", Toast.LENGTH_LONG).show();
    }


    //Method to save meme to ExternalPublicDirectory
    public void saveMemeToCacheDir(View v){

        bmp2 = getBitmapFromView(memeLayout);



        String timeStamp = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date());
        String imageName="Meme_"+timeStamp+".jpg";
        File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File myFile=new File(path,imageName);


        FileOutputStream fos=null;


        try{
            fos=new FileOutputStream(myFile);
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, fos);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "NO File Found",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                Toast.makeText(DemotivationalMemeActivity.this,"File Saved",Toast.LENGTH_LONG).show();
                try{
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(myFile);
                    mediaScanIntent.setData(uri);
                    sendBroadcast(mediaScanIntent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
                Toast.makeText(this, "No File Found To Save",Toast.LENGTH_LONG).show();
            }
        }


    }
}