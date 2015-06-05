package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap thumbnail;
    private String imgFilePath;
    private String thumbFileName;
    private boolean imageSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageSelected = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(imageSelected){
            launchChooseMeme();
        }
    }

    private void launchChooseMeme(){
        Intent chooseMeme = new Intent(this,ChooseMemeStyle.class);
        chooseMeme.putExtra("thumbnailFileName", thumbFileName);
        chooseMeme.putExtra("imgFilePath",imgFilePath);
        startActivity(chooseMeme);
    }

    public void takePicture(View view){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            imageSelected = true;
            try {
                //Write file
                thumbFileName = "thumbnail.png";
                FileOutputStream stream = this.openFileOutput(thumbFileName, Context.MODE_PRIVATE);
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                thumbnail.recycle();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            thumbnail = (Bitmap) extras.get("data");
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            //file path of captured image
            imgFilePath = cursor.getString(columnIndex);
            
            cursor.close();
        }
    }

}

