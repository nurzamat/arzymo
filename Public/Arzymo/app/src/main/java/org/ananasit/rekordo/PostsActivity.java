package org.ananasit.rekordo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import org.ananasit.rekordo.adapter.PostListAdapter;
import org.ananasit.rekordo.model.Param;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.model.User;
import org.ananasit.rekordo.util.ActionType;
import org.ananasit.rekordo.util.ApiHelper;
import org.ananasit.rekordo.util.CategoryType;
import org.ananasit.rekordo.util.EndlessRecyclerOnScrollListener;
import org.ananasit.rekordo.util.GlobalVar;
import org.ananasit.rekordo.util.JsonObjectRequest;
import org.ananasit.rekordo.util.MyPreferenceManager;
import org.ananasit.rekordo.util.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity implements DialogFilter.SearchListener{

    private static final String TAG =  "[category_posts response]";
    private List<Post> postList = new ArrayList<Post>();
    private PostListAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView emptyText;
    AppController appcon = null;
    public int next = 1;
    public String params;
    ProgressBar spin;
    String query = "";
    static PostsActivity _postActivity = null;
    Param param = null;

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

        //init recycler
        recyclerView = (RecyclerView) findViewById(R.id.list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(PostsActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        emptyText = (TextView) findViewById(android.R.id.empty);

        // specify an adapter (see also next example)
        adapter =  new PostListAdapter(PostsActivity.this, postList);
        recyclerView.setAdapter(adapter);
        //end

        spin = (ProgressBar) findViewById(R.id.loading);

        params = getParams();
        VolleyRequest(ApiHelper.getCategoryPostsUrl(1, params));

        /*
        if (postList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
        */

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(int current_page) {
                VolleyRequest(ApiHelper.getCategoryPostsUrl(current_page, params));
            }
        });
    }

    @NonNull
    private String getParams()
    {
        param = new Param();
        param.setQuery(query);
        int _actionType = 0;
        CategoryType categoryType = Utils.getCategoryType(GlobalVar.Category);
        if(categoryType != null)
        {
            if(categoryType.equals(CategoryType.SELL_BUY))
                _actionType = Utils.getActionTypeValue(ActionType.SELL);
            if(categoryType.equals(CategoryType.RENT))
                _actionType = Utils.getActionTypeValue(ActionType.RENT_GIVE);
            if(categoryType.equals(CategoryType.WORK))
                _actionType = Utils.getActionTypeValue(ActionType.VACANCY);

            if(categoryType.equals(CategoryType.DATING))
            {
                MyPreferenceManager myPreferenceManager = AppController.getInstance().getPrefManager();
                param.setSex(myPreferenceManager.getDatingSex());
                param.setAge_from(myPreferenceManager.getDatingAgeFrom());
                param.setAge_to(myPreferenceManager.getDatingAgeTo());
            }
        }
        param.setActionType(_actionType);

        return Utils.getParams(param);
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
            DialogFragment newFragment = DialogFilter.newInstance(param);
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
    public void onSearch(Param _p) {
        // User touched the dialog's Search button
        param = _p;
        params = Utils.getParams(_p);
        postList.clear();
        adapter.notifyDataSetChanged();
        VolleyRequest(ApiHelper.getCategoryPostsUrl(1, params));
    }
}
