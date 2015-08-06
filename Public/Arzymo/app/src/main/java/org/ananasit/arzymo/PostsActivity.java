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
import org.ananasit.arzymo.adapter.PostListAdapter;
import org.ananasit.arzymo.model.Post;
import org.ananasit.arzymo.util.ApiHelper;
import org.ananasit.arzymo.util.GlobalVar;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends ActionBarActivity {

    private static final String TAG =  "[post response]";
    private static final String url = ApiHelper.POST_URL;
    private List<Post> postList = new ArrayList<Post>();
    private ListView listView;
    private PostListAdapter adapter;
    private TextView emptyText;
    AppController appcon;
    private int total;
    private String next = null;
    ProgressBar spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        if(GlobalVar.Category != null)
        toolbar.setSubtitle(GlobalVar.Category.getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_exit));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
