package org.ananasit.rekordo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import org.ananasit.rekordo.adapter.PostListAdapter;
import org.ananasit.rekordo.model.Image;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.model.User;
import org.ananasit.rekordo.util.ActionType;
import org.ananasit.rekordo.util.ApiHelper;
import org.ananasit.rekordo.util.CategoryType;
import org.ananasit.rekordo.util.GlobalVar;
import org.ananasit.rekordo.util.JsonObjectRequest;
import org.ananasit.rekordo.util.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity implements DialogFilter.SearchListener{

    private static final String TAG =  "[category_posts response]";
    private List<Post> postList = new ArrayList<Post>();
    private ListView listView;
    private PostListAdapter adapter;
    private TextView emptyText;
    AppController appcon = null;
    public int next = 1;
    public String params;
    ProgressBar spin;
    String query = "";
    static PostsActivity _postActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        IntentWork(getIntent());
        if(_postActivity != null)
            _postActivity.finish();
        _postActivity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_exit));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if(GlobalVar.Category != null)
        toolbar.setSubtitle(GlobalVar.Category.getName());

        listView = (ListView) findViewById(R.id.list);
        emptyText = (TextView) findViewById(android.R.id.empty);
        listView.setEmptyView(emptyText);
        adapter = new PostListAdapter(PostsActivity.this, postList);
        listView.setAdapter(adapter);
        spin = (ProgressBar) findViewById(R.id.loading);

        //by default
        int _actionType = 0;
        CategoryType categoryType = Utils.getCategoryType(GlobalVar.Category);
        if(categoryType != null && categoryType.equals(CategoryType.SELL_BUY))
            _actionType = Utils.getActionTypeValue(ActionType.SELL);
        if(categoryType != null && categoryType.equals(CategoryType.RENT))
            _actionType = Utils.getActionTypeValue(ActionType.RENT_GIVE);
        if(categoryType != null && categoryType.equals(CategoryType.WORK))
            _actionType = Utils.getActionTypeValue(ActionType.VACANCY);

        params = Utils.getParams(query, _actionType);
        VolleyRequest(ApiHelper.getCategoryPostsUrl(1, params));
        listView.setOnScrollListener(new EndlessScrollListener(1));
    }

    private void IntentWork(Intent intent)
    {
        if(intent.getStringExtra("query") != null && !intent.getStringExtra("query").equals(""))
        query = intent.getStringExtra("query");
    }

    public void VolleyRequest(String url) {

        if(appcon == null)
        appcon = AppController.getInstance();

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
                            JSONObject obj;
                            User user;
                            for (int i = 0; i < posts.length(); i++) {
                                try {
                                    obj = posts.getJSONObject(i);
                                    user = new User();
                                    user.setId(obj.getString("user_id"));
                                    user.setName(obj.getString("user_name"));
                                    user.setUserName(obj.getString("user_username"));
                                    user.setEmail(obj.getString("user_email"));
                                    user.setPhone(obj.getString("user_phone"));
                                    if(!obj.getString("user_image_name").equals(""))
                                        user.setAvatarUrl(ApiHelper.MEDIA_URL + "/profile/" + obj.getString("user_image_name"));

                                    Post post = ApiHelper.initPost(obj, GlobalVar.Category, user);
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
                    VolleyRequest(ApiHelper.getCategoryPostsUrl(next, params));
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
        getMenuInflater().inflate(R.menu.menu_posts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter)
        {
            // Create the fragment and show it as a dialog.
            FragmentManager fm = getSupportFragmentManager();
            DialogFragment newFragment = DialogFilter.newInstance(1);
            newFragment.show(fm, "dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        finish();
    }

    //DialogFilter interface
    @Override
    public void onSearch(String _params) {
        // User touched the dialog's Search button
        params = _params;
        postList.clear();
        adapter.notifyDataSetChanged();
        VolleyRequest(ApiHelper.getCategoryPostsUrl(1, _params));
    }
}
