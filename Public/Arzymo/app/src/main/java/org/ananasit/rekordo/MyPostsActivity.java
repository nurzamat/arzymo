package org.ananasit.rekordo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import org.ananasit.rekordo.adapter.MyPostListAdapter;
import org.ananasit.rekordo.model.Category;
import org.ananasit.rekordo.model.Image;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.util.ApiHelper;
import org.ananasit.rekordo.util.JsonObjectRequest;
import org.ananasit.rekordo.util.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {

    private static final String TAG =  "[my_posts response]";
    private List<Post> postList = new ArrayList<Post>();
    private ListView listView;
    private MyPostListAdapter adapter;
    private TextView emptyText;
    AppController appcon;
    public int next = 1;
    public String user_id = "";
    ProgressBar spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //toolbar.setSubtitle("some");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_exit));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView = (ListView) findViewById(R.id.list);
        emptyText = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);
        adapter = new MyPostListAdapter(MyPostsActivity.this, postList);
        listView.setAdapter(adapter);
        spin = (ProgressBar) findViewById(R.id.loading);

        appcon = AppController.getInstance();
        spin.setVisibility(View.VISIBLE);
        if(appcon.getUser() != null)
            user_id = appcon.getUser().getId();
        VolleyRequest(ApiHelper.getMyPostsUrl(user_id, 1));
        listView.setOnScrollListener(new EndlessScrollListener(1));

    }

    public void VolleyRequest(String url) {
        spin.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(!response.getBoolean("error"))
                    {
                        JSONArray posts = response.getJSONArray("posts");
                        if(posts.length() > 0)
                        {
                            next = next + 1;
                            JSONArray jimages;
                            JSONObject obj;
                            Category category;
                            for (int i = 0; i < posts.length(); i++) {
                                try {

                                    obj = posts.getJSONObject(i);
                                    Post post = new Post();
                                    post.setId(obj.getString("id"));
                                    post.setContent(obj.getString("content"));
                                    post.setHitcount(obj.getString("hitcount"));
                                    post.setPrice(obj.getString("price"));
                                    post.setPriceCurrency(obj.getString("price_currency"));
                                    category = Utils.getCategoryByIds(obj.getString("id_category"), obj.getString("id_subcategory"));
                                    post.setCategory(category);
                                    post.setUser(appcon.getUser());
                                    jimages = obj.getJSONArray("images");
                                    if(jimages.length() > 0)
                                    {
                                        post.setThumbnailUrl(ApiHelper.MEDIA_URL + "/" + jimages.getJSONObject(0).getString("original_image"));
                                        // Images
                                        ArrayList<Image> images = new ArrayList<Image>();
                                        JSONObject img;
                                        Image image;
                                        for (int j = 0; j < jimages.length(); j++)
                                        {
                                            img = jimages.getJSONObject(j);
                                            image = new Image(img.getString("id"), ApiHelper.MEDIA_URL + "/" + img.getString("original_image"));
                                            images.add(image);
                                        }
                                        post.setImages(images);
                                    }
                                    postList.add(post);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else next = 0;
                    }
                    // notifying list adapter about data changes
                    // so that it renders the list view with updated data
                    adapter.notifyDataSetChanged();
                    if(!(postList.size() > 0))
                        emptyText.setText(R.string.no_posts);

                    spin.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                spin.setVisibility(View.GONE);
            }
        });
        // Adding request to request queue
        appcon.addToRequestQueue(jsonObjReq);
    }

    private class EndlessScrollListener implements AbsListView.OnScrollListener {

        private int visibleThreshold = 5;
        private int currentPage = 0;
        private int previousTotal = 0;
        private boolean loading = true;

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                    currentPage++;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                if (next > 0)
                    VolleyRequest(ApiHelper.getMyPostsUrl(user_id, next));
                loading = true;
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_posts, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
