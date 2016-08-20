package org.ananasit.rekordo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.ananasit.rekordo.AppController;
import org.ananasit.rekordo.R;
import org.ananasit.rekordo.model.Image;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.model.User;

import java.util.ArrayList;

/**
 * Created by nurzamat on 8/21/16.
 */
public class PostImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Image> _images;
    private LayoutInflater inflater;
    private Post post;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    User client = AppController.getInstance().getUser();

    // constructor
    public PostImageAdapter(Activity activity, Post _post) {
        this._activity = activity;
        this.post = _post;
        this._images = _post.getImages();
    }

    @Override
    public int getCount() {
        return this._images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        NetworkImageView imgDisplay;
        ImageButton btnClose;
        ImageButton btnDelete;
        TextView count;

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.post_detail_image, container, false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        imgDisplay = (NetworkImageView) viewLayout.findViewById(R.id.imgDisplay);
        ProgressBar spin = (ProgressBar) viewLayout.findViewById(R.id.progressBar1);
        spin.setVisibility(View.VISIBLE);

        // thumbnail image
        //if(_images.get(position).getUrl().equals(""))
        //   imgDisplay.setDefaultImageResId(R.drawable.default_img);
        imgDisplay.setImageUrl(_images.get(position).getUrl(),imageLoader);
        if(imgDisplay.getDrawable() != null)
            spin.setVisibility(View.GONE);

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }
}
