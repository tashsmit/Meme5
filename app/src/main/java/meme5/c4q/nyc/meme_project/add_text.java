package meme5.c4q.nyc.meme_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

//activity allows user to save or share meme created
public class add_text extends ActionBarActivity {

    Bitmap memeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        getMeme();

    }

    public void getMeme () {
        String filename = getIntent().getStringExtra("memeImage");
        try {
            FileInputStream is = this.openFileInput(filename);
            memeImage = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView previewMeme = (ImageView) findViewById(R.id.previewMeme);
        if(memeImage != null) {
            previewMeme.setImageBitmap(memeImage);
        }
    }

    public void shareImage(View view) {
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

    public void saveImage(View view) {
        MediaStore.Images.Media.insertImage(add_text.this.getContentResolver(), memeImage, "title.jpg", "some description");
        Toast.makeText(this, "Meme saved!", Toast.LENGTH_LONG).show();
    }

}
