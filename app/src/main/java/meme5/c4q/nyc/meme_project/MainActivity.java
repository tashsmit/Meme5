package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;

public class MainActivity extends Activity {

    //ELEMENTS
    private String imgFilePath, popMemes;
    private ImageView popular_memes;

    // KEY VALUE PAIRS
    private static final int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popular_memes = (ImageView) findViewById(R.id.ivPreset);
//        popular_memes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent popular = new Intent(MainActivity.this, PopularMemes.class);
//                startActivity(popular);
//            }
//        });

    }
    private void launchChooseMeme(){
        Intent chooseMeme = new Intent(this, ChooseMemeStyle.class);
        chooseMeme.putExtra("imgFilePath",imgFilePath);
        startActivity(chooseMeme);
    }

    public void takePicture(View view){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void loadImagefromGallery(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }

    }

    public void presetsMemes (View view) {
        Intent popular_memes = new Intent(this, PopularMemes.class);
        startActivity(popular_memes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                //file path of captured image
                imgFilePath = cursor.getString(columnIndex);
                cursor.close();
                launchChooseMeme();

            } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //file path of gallery image
                imgFilePath = cursor.getString(columnIndex);

                cursor.close();
                launchChooseMeme();

            } else if (requestCode == PopularMemes.RESULT_OK){

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                //file path of gallery image
                imgFilePath = cursor.getString(columnIndex);
                cursor.close();
                launchChooseMeme();

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

}

