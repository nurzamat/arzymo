package org.ananasit.arzymo;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import org.ananasit.arzymo.util.GlobalVar;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class SearchResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            if(!query.equals(""))
            {
                try
                {
                    GlobalVar.query = URLEncoder.encode(query, "UTF-8");
                    SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                            MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
                    suggestions.saveRecentQuery(query, null);
                }
                catch (UnsupportedEncodingException ex)
                {
                    ex.printStackTrace();
                }
                //Intent in = new Intent(SearchResultsActivity.this, HomeActivity.class);
                //in.putExtra("case", 7);
                //startActivity(in);
            }
            // Toast.makeText(this, query, Toast.LENGTH_LONG).show();
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_results, menu);
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
