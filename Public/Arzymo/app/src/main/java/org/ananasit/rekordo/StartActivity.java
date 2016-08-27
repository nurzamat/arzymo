package org.ananasit.rekordo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.ananasit.rekordo.model.Category;
import org.ananasit.rekordo.model.User;
import org.ananasit.rekordo.util.ApiHelper;
import org.ananasit.rekordo.util.GlobalVar;
import org.ananasit.rekordo.util.Utils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if(!Utils.isConnected(this)){
            Toast.makeText(StartActivity.this, "No Internet connection!", Toast.LENGTH_LONG).show();
            //return;
        }
        else
        {
            User user = AppController.getInstance().getUser();
            String url;
            if(user != null)
               url = ApiHelper.CATEGORIES_URL.replace("_ID_", user.getId());
            else url = ApiHelper.CATEGORIES_URL.replace("_ID_", "0");

            HttpAsyncTask task = new HttpAsyncTask();
            task.execute(url);
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        private ProgressDialog pDialog;
        private String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(StartActivity.this, "", "Загрузка...", true);
        }

        @Override
        protected String doInBackground(String... urls) {

            try
            {
                GlobalVar._categories.clear();
                ApiHelper api = new ApiHelper();
                JSONObject response = api.getCategories(urls[0]);
                if(response.getBoolean("error"))
                {
                   result = response.getString("message");
                }
                else
                {
                    JSONArray categories = response.getJSONArray("categories");
                    JSONArray jcats;
                    JSONObject obj;
                    Category category;
                    // Parsing json array
                    for (int i = 0; i < categories.length(); i++)
                    {
                        obj = categories.getJSONObject(i);
                        category = new Category();
                        category.setId(obj.getString("id"));
                        category.setName(obj.getString("name"));

                        jcats = obj.getJSONArray("subcategories");

                        if(jcats.length() > 0)
                        {
                            ArrayList<Category> subcats = new ArrayList<Category>();
                            JSONObject objCat;
                            Category cat;
                            for (int j = 0; j < jcats.length(); j++)
                            {
                                objCat = jcats.getJSONObject(j);
                                cat = new Category();
                                cat.setId(objCat.getString("id"));
                                cat.setName(objCat.getString("name"));
                                cat.setIdParent(objCat.getString("idCategory"));
                                subcats.add(cat);
                            }

                            category.setSubcats(subcats);
                        }
                        GlobalVar._categories.add(category);
                    }

                    //setting User
                    obj = response.getJSONObject("user");
                    if(obj.has("id"))
                    {
                      ApiHelper.initClientUserFromServer(obj);
                    }
                }
            }
            catch (Exception ex)
            {
                result = ex.getMessage();
                Log.d("Start activity", "Exception: " + result);
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            pDialog.dismiss();
            if(!result.equals(""))
            {
                Toast.makeText(StartActivity.this, result, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Intent in = new Intent(StartActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
