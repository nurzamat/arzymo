package org.ananasit.rekordo;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import org.ananasit.rekordo.adapter.PostViewPagerAdapter;
import org.ananasit.rekordo.lib.CirclePageIndicator;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.model.User;
import org.ananasit.rekordo.util.ApiHelper;
import org.ananasit.rekordo.util.GlobalVar;
import org.ananasit.rekordo.util.Utils;
import org.json.JSONObject;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbar;
    ViewPager mPager;
    PostViewPagerAdapter mAdapter;
    LinearLayout layout;
    CirclePageIndicator mIndicator;
    private Post p = GlobalVar._Post;
    private Menu mOptionsMenu;
    private boolean like = false;
    User client = null;
    private int count = 0;
    //content params
    TextView hitcount, timestamp, location, title, price, price_currency, content, username, name;
    ImageButton chat, call;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(p.getImages() != null && p.getImages().size() > 0)
        {
            setContentView(R.layout.activity_post_detail);
            collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(" ");
        }
        else
        {
            setContentView(R.layout.activity_post_detail_no_image);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        hitcount = (TextView) findViewById(R.id.hitcount);
        timestamp = (TextView) findViewById(R.id.date);
        location = (TextView) findViewById(R.id.location);
        title = (TextView) findViewById(R.id.title);
        price = (TextView) findViewById(R.id.price);
        price_currency = (TextView) findViewById(R.id.price_currency);
        content = (TextView) findViewById(R.id.content);
        username = (TextView) findViewById(R.id.username);
        name = (TextView) findViewById(R.id.name);
        chat = (ImageButton) findViewById(R.id.action_chat);
        call = (ImageButton) findViewById(R.id.action_call);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        //sets
        hitcount.setText(p.getHitcount());
        timestamp.setText(Utils.getTimeAgo(p.getDate_created()));
        if(!p.getLocation().equals("null"))
        location.setText(p.getLocation());
        title.setText(p.getTitle());
        content.setText(p.getContent());
        //set price
        try
        {
            String _price = p.getPrice().trim();
            if(!_price.equals("0.00"))
            {
                double number = Double.parseDouble(_price);
                int res = (int)number; //целая часть
                double res2 = number - res; //дробная часть

                if(res2 > 0)
                    price.setText(_price);
                else price.setText(""+res);

                price_currency.setText(p.getPriceCurrency());
            }

            if(!p.getUser().getAvatarUrl().equals(""))
            {
                Glide.with(this).load(p.getUser().getAvatarUrl())
                        .thumbnail(0.5f)
                        .centerCrop()
                        .placeholder(R.drawable.aka)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profile_image);
            }

            username.setText(p.getUser().getUserName());
            name.setText(p.getUser().getName());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        client = AppController.getInstance().getUser();

        chat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(client != null)
                {
                    if(p != null && p.getUser() != null)
                    {
                        Intent intent = new Intent(PostDetailActivity.this, MessagesActivity.class);
                        intent.putExtra("chat_id", "0");
                        intent.putExtra("interlocutor_id", p.getUser().getId());
                        intent.putExtra("post_id", p.getId());
                        intent.putExtra("name", p.getUser().getUserName());
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent in = new Intent(PostDetailActivity.this, SignupActivity.class);
                    startActivity(in);
                }
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(client != null)
                {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + "+" + p.getPhone()));
                    startActivity(intent);
                }
                else
                {
                    Intent in = new Intent(PostDetailActivity.this, SignupActivity.class);
                    startActivity(in);
                }
            }
        });

        /*  // for image full screen logic
        layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailActivity.this, "layout clicked ", Toast.LENGTH_SHORT).show();
            }
        });
        */

        ViewPagerWork();

        if(client != null && !p.getUser().getId().equals(client.getId()))
        {
            String url = ApiHelper.POST_URL + "/" + p.getId() + "/hitcount/" + client.getId();
            HitcountTask task = new HitcountTask();
            task.execute(url);
        }
    }

    private void ViewPagerWork()
    {
        try
        {
            if(p.getImages() != null && p.getImages().size() > 0)
            {
                mAdapter = new PostViewPagerAdapter(PostDetailActivity.this, p.getImages());
                mPager = (ViewPager) findViewById(R.id.pager);
                mPager.setAdapter(mAdapter);

                int color = ContextCompat.getColor(this, R.color.color_primary_green_dark);
                mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
                mIndicator.setFillColor(color);
                mIndicator.setStrokeColor(color);
                mIndicator.setRadius(15);
                mIndicator.setViewPager(mPager);
                mIndicator.setSnap(true);
            }
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mOptionsMenu = menu;
        if(like)
            getMenuInflater().inflate(R.menu.menu_yes_like, menu);
        else getMenuInflater().inflate(R.menu.menu_no_like, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_like)
        {
            count++;
            String url;

            MenuItem menuItem = mOptionsMenu.findItem(R.id.action_like);

            if(count < 10)
            {
                if(like)
                {
                    like = false;
                    menuItem.setIcon(R.drawable.ic_heart_outline);
                    url = ApiHelper.POST_URL + "/" + p.getId() + "/" + client.getId() + "/like/" + 0;
                }
                else
                {
                    like = true;
                    menuItem.setIcon(R.drawable.ic_heart);
                    url = ApiHelper.POST_URL + "/" + p.getId() + "/" + client.getId() + "/like/" + 1;
                }

                LikeTask task = new LikeTask();
                task.execute(url);
            }

            /* will be used
            if(client != null && !p.getUser().getId().equals(client.getId()))
            {

            }
            */
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HitcountTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try
            {
                ApiHelper api = new ApiHelper();
                JSONObject obj = api.sendHitcount(urls[0]);
                if(obj.getBoolean("error"))
                {
                    return "error";
                }
                else
                {
                    if(obj.has("like"))
                    {
                        like = obj.getBoolean("like");
                    }
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return "error";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    private class LikeTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try
            {
                ApiHelper api = new ApiHelper();
                JSONObject obj = api.sendLike(urls[0]);
                if(obj.getBoolean("error"))
                {
                    return "error";
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return "error";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
