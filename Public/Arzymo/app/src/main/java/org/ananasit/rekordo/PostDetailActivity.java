package org.ananasit.rekordo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import org.ananasit.rekordo.adapter.CategoriesRecyclerAdapter;
import org.ananasit.rekordo.adapter.PostViewPagerAdapter;
import org.ananasit.rekordo.lib.CirclePageIndicator;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.model.User;
import org.ananasit.rekordo.util.ApiHelper;
import org.ananasit.rekordo.util.GlobalVar;
import org.json.JSONObject;

public class PostDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;
    CategoriesRecyclerAdapter adapter;
    ViewPager mPager;
    PostViewPagerAdapter mAdapter;
    LinearLayout layout;
    CirclePageIndicator mIndicator;
    private Post p = GlobalVar._Post;
    private Menu mOptionsMenu;
    private boolean like = false;
    User client = null;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(p.getImages() != null && p.getImages().size() > 0)
        {
            setContentView(R.layout.activity_post_detail);
            collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbar.setTitle(" ");
        }
        else setContentView(R.layout.activity_post_detail_no_image);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*
        layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailActivity.this, "layout clicked ", Toast.LENGTH_SHORT).show();
            }
        });
        */

        recyclerView = (RecyclerView) findViewById(R.id.contentPostDetail);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //todo
        // specify an adapter (see also next example)
        adapter =  new CategoriesRecyclerAdapter(this, GlobalVar._categories);
        recyclerView.setAdapter(adapter);

        ViewPagerWork();

        client = AppController.getInstance().getUser();

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
                    menuItem.setIcon(R.drawable.ic_action_about);
                    url = ApiHelper.POST_URL + "/" + p.getId() + "/" + client.getId() + "/like/" + 0;
                }
                else
                {
                    like = true;
                    menuItem.setIcon(R.drawable.ic_action_like);
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
