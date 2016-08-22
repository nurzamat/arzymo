package org.ananasit.rekordo.adapter;

/**
 * Created by User on 16.12.2014.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.ananasit.rekordo.AppController;
import org.ananasit.rekordo.PostDetailActivity;
import org.ananasit.rekordo.R;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.model.User;
import org.ananasit.rekordo.util.GlobalVar;
import java.util.List;

public class PostListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Post> postItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    User client = AppController.getInstance().getUser();

    //views
    private int thumbnail_id;
    private int menu_id;
    private int call_id;
    private int chat_id;

    public PostListAdapter(Activity activity, List<Post> postItems) {
        this.activity = activity;
        this.postItems = postItems;
    }

    @Override
    public int getCount() {
        return postItems.size();
    }

    @Override
    public Object getItem(int location) {
        return postItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.post_list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        NetworkImageView avatar = (NetworkImageView) convertView
                .findViewById(R.id.avatar);
        ProgressBar spin = (ProgressBar) convertView.findViewById(R.id.progressBar1);
        spin.setVisibility(View.VISIBLE);

        TextView content = (TextView) convertView.findViewById(R.id.content);
        TextView hitcount = (TextView) convertView.findViewById(R.id.hitcount);
        TextView displayed_name = (TextView) convertView.findViewById(R.id.displayed_name);
        TextView category_name = (TextView) convertView.findViewById(R.id.category);
        TextView price = (TextView) convertView.findViewById(R.id.price);
        ImageButton menu = (ImageButton) convertView.findViewById(R.id.btnMenu2);
        ImageButton call = (ImageButton) convertView.findViewById(R.id.show_phone);
        ImageButton chat = (ImageButton) convertView.findViewById(R.id.show_chat);

        // getting post data for the row
        Post p = postItems.get(position);
        String image_url = p.getThumbnailUrl();
        // thumbnail image
        if(image_url.equals(""))
            thumbNail.setDefaultImageResId(R.drawable.default_img);
        thumbNail.setImageUrl(image_url, imageLoader);
        if(thumbNail.getDrawable() != null)
            spin.setVisibility(View.GONE);

        if(p.getUser() != null)
        avatar.setImageUrl(p.getUser().getAvatarUrl(), imageLoader);
        // title
        content.setText(p.getContent());
        hitcount.setText(p.getHitcount());
        // username
        if(p.getUser() != null)
           displayed_name.setText(p.getUser().getUserName());

        if(p.getCategory() != null)
        category_name.setText(p.getCategory().getName());

        // price
        price.setText(String.valueOf(p.getPrice()));

        // image view click listener
        thumbnail_id = thumbNail.getId();
        menu_id = menu.getId();
        call_id = call.getId();
        chat_id = chat.getId();

        if(p.getUser() != null && p.getUser().getPhone().equals(client.getPhone()))
        {
            call.setVisibility(View.INVISIBLE);
            chat.setVisibility(View.INVISIBLE);
        }
        else
        {
            call.setVisibility(View.VISIBLE);
            chat.setVisibility(View.VISIBLE);
        }

        thumbNail.setOnClickListener(new OnImageClickListener(thumbnail_id, position, p));
        menu.setOnClickListener(new OnImageClickListener(menu_id, position, p));
        call.setOnClickListener(new OnImageClickListener(call_id, position, p));
        chat.setOnClickListener(new OnImageClickListener(chat_id, position, p));

        return convertView;
    }


    class OnImageClickListener implements View.OnClickListener {

        int _position;
        int _view_id = 0;
        Post _p;

        // constructors
        public OnImageClickListener(int view_id, int _position, Post m)
        {
            this._view_id = view_id;
            this._position = _position;
            this._p = m;
        }

        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
            if(_view_id == thumbnail_id)
            {
                if(_p.getImages() != null && _p.getImages().size() > 0)
                {
                    GlobalVar._Post = _p;
                    //Intent i = new Intent(activity, FullScreenViewActivity.class);
                    Intent i = new Intent(activity, PostDetailActivity.class);
                    activity.startActivity(i);
                }
                else Toast.makeText(activity, R.string.no_photo, Toast.LENGTH_SHORT).show();
            }
            if(_view_id == call_id)
            {
                try
                {
                    if (client != null)
                    {
                        String phone = _p.getUser().getPhone();
                        boolean isPhone = PhoneNumberUtils.isGlobalPhoneNumber("+"+phone);
                        if(isPhone)
                        {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:"+"+"+phone));
                            activity.startActivity(intent);
                        }
                        else Toast.makeText(activity, "call pressed /"+phone+"/", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        /*
                        Intent in;
                        if(GlobalVar.isCodeSent)
                            in = new Intent(activity, RegisterActivity.class);
                        else in = new Intent(activity, CodeActivity.class);
                        activity.startActivity(in);
                        */
                        Toast.makeText(activity, "You are not registered", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            if(_view_id == chat_id)
            {
               //todo
            }
            if(_view_id == menu_id)
            {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(activity, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.post_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String title = item.getTitle().toString();
                        if(title.equals("Сообщение"))
                        {
                            Toast.makeText(activity, "message pressed", Toast.LENGTH_SHORT).show();
                        }
                        if(title.equals("Позвонить"))
                        {
                            String phone = _p.getUser().getPhone();
                            boolean isPhone = PhoneNumberUtils.isGlobalPhoneNumber("+"+phone);
                            if(isPhone)
                            {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+"+"+phone));
                                activity.startActivity(intent);
                            }
                            else Toast.makeText(activity, "call pressed /"+phone+"/", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        }
    }
}
