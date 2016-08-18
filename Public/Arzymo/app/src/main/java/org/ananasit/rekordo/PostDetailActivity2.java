package org.ananasit.rekordo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.ananasit.rekordo.adapter.PostDetailListAdapter;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.model.User;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.FifthHeaderFragment;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.FirstHeaderFragment;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.FourthHeaderFragment;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.SecondHeaderFragment;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.ThirdHeaderFragment;
import org.ananasit.rekordo.pagedheader.library.PagedHeadListView;
import org.ananasit.rekordo.pagedheader.library.utils.PageTransformerTypes;
import org.ananasit.rekordo.util.ApiHelper;
import org.ananasit.rekordo.util.GlobalVar;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity2 extends AppCompatActivity {

    private PagedHeadListView mPagedHeadList;
    private int indicatorBgColor;
    private int indicatorColor;
    private PageTransformerTypes pageTransformerType;
    private Post p = GlobalVar._Post;
    private Menu mOptionsMenu;
    private boolean like = false;
    User client = null;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail2);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        indicatorBgColor = getResources().getColor(R.color.material_teal);
        indicatorColor = getResources().getColor(R.color.material_light_teal);
        pageTransformerType = PageTransformerTypes.ACCORDION;

        initPagedHeadList();

        client = AppController.getInstance().getUser();

        if(client != null && !p.getUser().getId().equals(client.getId()))
        {
            String url = ApiHelper.POST_URL + "/" + p.getId() + "/hitcount/" + client.getId();
            HitcountTask task = new HitcountTask();
            task.execute(url);
        }
    }

    /**
     * Initializes list with mock fragments for the header and plenty of mock items
     */
    private void initPagedHeadList() {
        mPagedHeadList = (PagedHeadListView) findViewById(R.id.pagedHeadListView);

        try
        {
            mPagedHeadList.addFragmentToHeader(FirstHeaderFragment.newInstance(p.getImages().get(0).getUrl()));
            mPagedHeadList.addFragmentToHeader(SecondHeaderFragment.newInstance(p.getImages().get(1).getUrl()));
            mPagedHeadList.addFragmentToHeader(ThirdHeaderFragment.newInstance(p.getImages().get(2).getUrl()));
            mPagedHeadList.addFragmentToHeader(FourthHeaderFragment.newInstance(p.getImages().get(3).getUrl()));
            mPagedHeadList.addFragmentToHeader(FifthHeaderFragment.newInstance(p.getImages().get(4).getUrl()));
        }
        catch (IndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
        }

        mPagedHeadList.setHeaderOffScreenPageLimit(4);
        mPagedHeadList.setHeaderPageTransformer(pageTransformerType);
        mPagedHeadList.setIndicatorBgColor(indicatorBgColor);
        mPagedHeadList.setIndicatorColor(indicatorColor);

        List<Post> postList = new ArrayList<Post>();
        postList.add(p);

        PostDetailListAdapter adapter = new PostDetailListAdapter(PostDetailActivity2.this, postList);
        mPagedHeadList.setAdapter(adapter);
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

            if(count < 10)
            {
                if(like)
                {
                    like = false;
                    mOptionsMenu.findItem(R.id.action_like).setIcon(R.drawable.ic_action_about);
                    url = ApiHelper.POST_URL + "/" + p.getId() + "/" + client.getId() + "/like/" + 0;
                }
                else
                {
                    like = true;
                    mOptionsMenu.findItem(R.id.action_like).setIcon(R.drawable.ic_action_like);
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

}
