package meme5.c4q.nyc.meme_project;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by sufeizhao on 6/12/15.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> imageList;

    public ImageAdapter(Context c, List<String> imageList) {
        mContext = c;
        this.imageList = imageList;
    }

    public int getCount() {
        return imageList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null)
            imageView = new ImageView(mContext);
        else
            imageView = (ImageView) convertView;

        Picasso.with(mContext).load(imageList.get(position)).resize(300,300).centerCrop().into(imageView);
        return imageView;
    }
}