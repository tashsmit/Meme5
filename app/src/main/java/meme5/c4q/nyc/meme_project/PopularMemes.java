package meme5.c4q.nyc.meme_project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class PopularMemes extends ActionBarActivity {

    public static String IMAGE_URI_KEY = "uri";
    private ListView memeListView;
    private String imageUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_memes);

        memeListView = (ListView) findViewById(R.id.meme_list_view);

        Integer[] memeResources = {
                R.drawable.popular0,
                R.drawable.popular1,
                R.drawable.popular2,
                R.drawable.popular3,
                R.drawable.popular4,
                R.drawable.popular5,
                R.drawable.popular6,
                R.drawable.popular7,
                R.drawable.popular8,
                R.drawable.popular9,
                R.drawable.popular10,
                R.drawable.popular11,

        };
        MemeAdapter memeAdapter = new MemeAdapter(this, memeResources);
        memeListView.setAdapter(memeAdapter);
        memeListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int meme = (Integer) parent.getItemAtPosition(position);
                Intent popularMemes = new Intent(PopularMemes.this, ChooseMemeStyle.class);
                imageUri += meme;
                popularMemes.putExtra(IMAGE_URI_KEY, imageUri);
                startActivity(popularMemes);
            }
        });

    }

    class MemeAdapter extends ArrayAdapter<Integer> {
        public MemeAdapter(Context context, Integer[] memeList) {
            super(context, 0, memeList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int singleMeme = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.imageholder, parent, false);
            }
            ImageView appIcon = (ImageView) convertView.findViewById(R.id.ivMemePopularHolder);
            appIcon.setImageResource(singleMeme);
            return convertView;
        }
    }
}