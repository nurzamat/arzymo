package org.ananasit.arzymo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import org.ananasit.arzymo.adapter.MyPostListAdapter;
import org.ananasit.arzymo.model.Image;
import org.ananasit.arzymo.model.Post;
import org.ananasit.arzymo.model.User;
import org.ananasit.arzymo.util.ApiHelper;
import org.ananasit.arzymo.util.EndlessScrollListener;
import org.ananasit.arzymo.util.GlobalVar;
import org.ananasit.arzymo.util.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends ActionBarActivity {

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
        listView.setOnScrollListener(new EndlessScrollListener(this, 1));

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
                            User user;
                            for (int i = 0; i < posts.length(); i++) {
                                try {

                                    obj = posts.getJSONObject(i);
                                    Post post = new Post();
                                    post.setId(obj.getString("id"));
                                    post.setContent(obj.getString("content"));
                                    post.setHitcount(obj.getString("hitcount"));
                                    post.setPrice(obj.getString("price"));
                                    post.setPriceCurrency(obj.getString("price_currency"));

                                    user = new User();
                                    user.setId(obj.getString("user_id"));
                                    user.setName(obj.getString("user_name"));
                                    user.setPhone(obj.getString("user_phone"));
                                    user.setAvatarUrl("");//todo
                                    post.setUser(user);

                                    post.setCategory(GlobalVar.Category);
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
}
