package org.ananasit.arzymo;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.ananasit.arzymo.adapter.FullScreenImageAdapter;
import org.ananasit.arzymo.model.Post;
import org.ananasit.arzymo.model.User;
import org.ananasit.arzymo.util.ApiHelper;
import org.ananasit.arzymo.util.GlobalVar;
import org.json.JSONObject;


public class FullScreenViewActivity extends ActionBarActivity {

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private Post p = GlobalVar._Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, p);

        viewPager.setAdapter(adapter);
        User client = AppController.getInstance().getUser();
        if(client != null && !p.getUser().getId().equals(client.getId()))
        {
            //HttpAsyncTask task = new HttpAsyncTask();
            //task.execute(ApiHelper.HITCOUNT_URL);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_full_screen_view, menu);
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

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            try
            {
                ApiHelper api = new ApiHelper();

                JSONObject jsonObject = new JSONObject();
                //jsonObject.put("hitcount_pk", p.getHitcountId());
                jsonObject.put("api_key", ApiHelper.API_KEY);

                JSONObject obj = api.sendHitcount(jsonObject);
                if(obj.getString("status").equals("success"))
                {
                    return "success";
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return "Ошибка";
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
