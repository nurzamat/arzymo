package org.ananasit.rekordo.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.ananasit.rekordo.AppController;
import org.ananasit.rekordo.R;
import org.ananasit.rekordo.model.Image;
import org.ananasit.rekordo.util.GlobalVar;

import java.util.ArrayList;

/**
 * Created by nurzamat on 8/21/16.
 */
public class PostViewPagerAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Image> images;
    ArrayList<Bitmap> bitmaps;
    private LayoutInflater inflater;
    private boolean isAddPost = false;
    ImageLoader imageLoader = null;


    // constructor
    public PostViewPagerAdapter(Activity activity, ArrayList<Image> _images) {
        this._activity = activity;
        this.images = _images;
        this.isAddPost = false;
        this.imageLoader = AppController.getInstance().getImageLoader();
    }

    public PostViewPagerAdapter(Activity activity) {
        this._activity = activity;
        this.bitmaps = GlobalVar._bitmaps;
        this.isAddPost = true;
    }

    @Override
    public int getCount()
    {
        if(isAddPost)
        {
            if(this.bitmaps.size() > 0)
            return this.bitmaps.size();
        }
        else
        {   if(this.images.size() > 0)
            return this.images.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View viewLayout;
        if(isAddPost)
        {
            viewLayout = inflater.inflate(R.layout.viewpager_item, container, false);

            ImageView imgflag = (ImageView) viewLayout.findViewById(R.id.flag);
            imgflag.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // Capture position and set to the ImageView
            try
            {
                if(this.bitmaps != null && this.bitmaps.size() > 0)
                {
                    imgflag.setImageBitmap(bitmaps.get(position));
                }
                else
                {
                    imgflag.setImageResource(R.drawable.default_img);
                }
            }
            catch (IndexOutOfBoundsException ex)
            {
                Log.d("exception:", ex.getMessage());
            }
        }
        else
        {
            viewLayout = inflater.inflate(R.layout.post_detail_image, container, false);

            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();

            NetworkImageView imgDisplay = (NetworkImageView) viewLayout.findViewById(R.id.imgDisplay);
            ProgressBar spin = (ProgressBar) viewLayout.findViewById(R.id.progressBar1);
            spin.setVisibility(View.VISIBLE);

            // thumbnail image
            //if(_images.get(position).getUrl().equals(""))
            //   imgDisplay.setDefaultImageResId(R.drawable.default_img);
            imgDisplay.setImageUrl(images.get(position).getUrl(),imageLoader);
            if(imgDisplay.getDrawable() != null)
                spin.setVisibility(View.GONE);
        }

        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

}
