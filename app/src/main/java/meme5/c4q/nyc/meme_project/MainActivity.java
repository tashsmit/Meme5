package meme5.c4q.nyc.meme_project;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.GridView;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private static final int RESULT_LOAD_IMG = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private String imgFilePath;
    ArrayList<Contact> imageArry = new ArrayList<Contact>();
    ContactImageAdapter adapter;
    int [] image_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayImages();
    }

    public void DisplayImages () {

//        image_array = getResources().getStringArray(R.array.meme_images);
//
//        for (int i = 0; i < image_array.length; i++) {
        
            DataBaseHandler db = new DataBaseHandler(this);
// get image from drawable
            Bitmap image = BitmapFactory.decodeResource(getResources(),
                    R.drawable.meme_db_2);

// convert bitmap to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte imageInByte[] = stream.toByteArray();
/**
 * CRUD Operations
 * */
// Inserting Contacts
            Log.d("Insert: ", "Inserting ..");
            db.addContact(new Contact("FaceBook", imageInByte));
// display main List view bcard and contact name

// Reading all contacts from database
            List<Contact> contacts = db.getAllContacts();
            for (Contact cn : contacts) {
                String log = "ID:" + cn.getID() + " Name: " + cn.getName()
                        + " ,Image: " + cn.getImage();

// Writing Contacts to log
                Log.d("Result: ", log);
//add contacts data in arrayList
                imageArry.add(cn);

            }
            adapter = new ContactImageAdapter(this, R.layout.screen_list,
                    imageArry);
            ListView dataList = (ListView) findViewById(R.id.list);
            dataList.setAdapter(adapter);

        }


    private void launchChooseMeme(){
        Intent chooseMeme = new Intent(this,ChooseMemeStyle.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

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

