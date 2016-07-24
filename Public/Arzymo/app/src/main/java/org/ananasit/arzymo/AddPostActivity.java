package org.ananasit.arzymo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.ananasit.arzymo.adapter.PlaceSlidesFragmentAdapter;
import org.ananasit.arzymo.lib.CirclePageIndicator;
import org.ananasit.arzymo.model.Category;
import org.ananasit.arzymo.util.ActionType;
import org.ananasit.arzymo.util.ApiHelper;
import org.ananasit.arzymo.util.CategoryType;
import org.ananasit.arzymo.util.GlobalVar;
import org.ananasit.arzymo.util.Utils;
import org.json.JSONObject;

public class AddPostActivity extends AppCompatActivity {

    private static final String TAG =  "[add post response]";
    ViewPager mPager;
    PagerAdapter mAdapter;
    CirclePageIndicator mIndicator;
    Toolbar toolbar;
    String content = "";
    String price = "";
    String price_currency = "";
    String result;
    EditText etContent, etPrice, etDisplayed_name;
    Button categoryBtn, postBtn;
    Category category = null;
    Spinner price_spinner, action_spinner, sex_spinner, birth_year_spinner;
    int actionType = 0;
    int actionPos = 0;
    int birthPos = 0;
    int sex = 2; //0 - female, 1 - male
    String birth_year = "";
    String displayed_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_post);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //toolbar.setSubtitle(GlobalVar.SubCategory.getName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_exit));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        etContent = (EditText) findViewById(R.id.content);
        etPrice = (EditText) findViewById(R.id.price);
        postBtn = (Button) findViewById(R.id.btnPost);
        categoryBtn = (Button) findViewById(R.id.btnCategory);
        etContent.setText(content);
        etPrice.setText(price);
        //spinner job
        action_spinner = (Spinner) findViewById(R.id.action_spinner);
        price_spinner = (Spinner) findViewById(R.id.price_spinner);
   
        //start Dating
        etDisplayed_name = (EditText) findViewById(R.id.displayed_name);
        etDisplayed_name.setText(displayed_name);
        sex_spinner = (Spinner) findViewById(R.id.sex_spinner);
        birth_year_spinner = (Spinner) findViewById(R.id.birth_year_spinner);
        //end Dating

        //ViewPagerWork();

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postButton();
            }
        });

        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(AddPostActivity.this, CategoriesActivity.class);
                startActivity(in);
            }
        });

    }

    private void ViewPagerWork() {
        int color = getResources().getColor(R.color.blue_dark);
        mAdapter = new PlaceSlidesFragmentAdapter(AddPostActivity.this);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setFillColor(color);
        mIndicator.setStrokeColor(color);
        mIndicator.setRadius(5);
        mIndicator.setViewPager(mPager);
        mIndicator.setSnap(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("AddPostActivity", "resumed");
        if(GlobalVar.Category != null && GlobalVar.Category.getSubcats() == null)
        {
            categoryBtn.setText(GlobalVar.Category.getName());
            this.category = GlobalVar.Category;
            CategoryType catType = Utils.getCategoryType(this.category);

            if(catType.equals(CategoryType.SELL_BUY)) //buy sell //куплю-продам
            {

                //action
                action_spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> action_adapter = ArrayAdapter.createFromResource(AddPostActivity.this,
                        R.array.buy_sell, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                action_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                action_spinner.setAdapter(action_adapter);
                action_spinner.setSelection(actionPos);
                action_spinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {

                                actionPos = pos;
                                // Showing selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected: " + pos, Toast.LENGTH_LONG).show();
                                if (pos == 0)
                                    actionType = Utils.getActionTypeValue(ActionType.SELL);
                                if (pos == 1)
                                    actionType = Utils.getActionTypeValue(ActionType.BUY);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        }
                );
                priceSpinner();

            }
            else if(catType.equals(CategoryType.RENT)) //Rent
            {
                etPrice.setVisibility(View.INVISIBLE);
                price_spinner.setVisibility(View.INVISIBLE);
                //action
                action_spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> action_adapter = ArrayAdapter.createFromResource(AddPostActivity.this,
                        R.array.rent_get_give, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                action_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                action_spinner.setAdapter(action_adapter);
                action_spinner.setSelection(actionPos);
                action_spinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {

                                actionPos = pos;
                                // Showing selected spinner item
                                // Toast.makeText(parent.getContext(), "Selected: " + pos, Toast.LENGTH_LONG).show();
                                if (pos == 0)
                                    actionType = Utils.getActionTypeValue(ActionType.RENT_GIVE);
                                if (pos == 1)
                                    actionType = Utils.getActionTypeValue(ActionType.RENT_GET);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        }
                );

                priceSpinner();
            }
            else if(catType.equals(CategoryType.WORK)) //Work
            {
                etPrice.setVisibility(View.INVISIBLE);
                price_spinner.setVisibility(View.INVISIBLE);
                //action
                action_spinner.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> action_adapter = ArrayAdapter.createFromResource(AddPostActivity.this,
                        R.array.work_resume_vacancy, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                action_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                action_spinner.setAdapter(action_adapter);
                action_spinner.setSelection(actionPos);
                action_spinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {

                                actionPos = pos;
                                // Showing selected spinner item
                                // Toast.makeText(parent.getContext(), "Selected: " + pos, Toast.LENGTH_LONG).show();
                                if (pos == 0)
                                    actionType = Utils.getActionTypeValue(ActionType.VACANCY);
                                if (pos == 1)
                                    actionType = Utils.getActionTypeValue(ActionType.RESUME);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        }
                );
            }
            else
            {
                action_spinner.setVisibility(View.INVISIBLE);
                etPrice.setVisibility(View.INVISIBLE);
                price_spinner.setVisibility(View.INVISIBLE);
            }

            //Dating
            if(catType.equals(CategoryType.DATING))
            {
                etDisplayed_name.setVisibility(View.VISIBLE);
                sex_spinner.setVisibility(View.VISIBLE);
                birth_year_spinner.setVisibility(View.VISIBLE);

                ArrayAdapter<CharSequence> sex_adapter = ArrayAdapter.createFromResource(AddPostActivity.this,
                        R.array.sex, android.R.layout.simple_spinner_item);

                ArrayAdapter<CharSequence> birth_year_adapter = ArrayAdapter.createFromResource(AddPostActivity.this,
                        R.array.birth_year, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
                sex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                birth_year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                sex_spinner.setAdapter(sex_adapter);
                sex_spinner.setSelection(actionPos);
                sex_spinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {

                                actionPos = pos;
                                // Showing selected spinner item
                                //Toast.makeText(parent.getContext(), "Selected: " + pos, Toast.LENGTH_LONG).show();
                                if (pos == 1)
                                    sex = 1;
                                if (pos == 2)
                                    sex = 0;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        }
                );

                birth_year_spinner.setAdapter(birth_year_adapter);
                birth_year_spinner.setSelection(birthPos);
                birth_year_spinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int pos, long id) {

                                birthPos = pos;

                                if (pos != 0)
                                    birth_year = parent.getItemAtPosition(pos).toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub
                            }
                        }
                );
            }
            else
            {
                etDisplayed_name.setVisibility(View.INVISIBLE);
                sex_spinner.setVisibility(View.INVISIBLE);
                birth_year_spinner.setVisibility(View.INVISIBLE);
            }

        }
        else this.category = null;
        ViewPagerWork();
    }

    private void priceSpinner() {
        //price
        etPrice.setVisibility(View.VISIBLE);
        // Create an ArrayAdapter using the string array and a default spinner layout
        price_spinner.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> price_adapter = ArrayAdapter.createFromResource(AddPostActivity.this,
                R.array.price_currencies, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        price_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        price_spinner.setAdapter(price_adapter);
        price_spinner.setSelection(price_adapter.getPosition(price_currency));
        price_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        // On selecting a spinner item
                        price_currency = parent.getItemAtPosition(pos).toString();

                        // Showing selected spinner item
                        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.d("AddPostActivity", "paused");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_post, menu);
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

    private void postButton() {
        if(!validate())
        {
            Toast.makeText(AddPostActivity.this, "Заполните все поля", Toast.LENGTH_LONG).show();
        }
        else
        {
            AddPostTask task = new AddPostTask();
            task.execute(ApiHelper.POST_URL);
        }
    }

    private boolean validate(){

        boolean val = true;
        content = etContent.getText().toString().trim();
        displayed_name = etDisplayed_name.getText().toString().trim();
        price = etPrice.getText().toString().trim();

        if(content.equals("") || category == null)
            val = false;

        if(etPrice.getVisibility() == View.VISIBLE && price.equals(""))
            val = false;

        if(price_spinner.getVisibility() == View.VISIBLE && price_currency.equals(""))
            val = false;

        //dating
        if(sex_spinner.getVisibility() == View.VISIBLE && sex == 2)
            val = false;

        if(birth_year_spinner.getVisibility() == View.VISIBLE && birth_year.equals(""))
            val = false;

        return val;
    }

    // add mode
    private class AddPostTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(AddPostActivity.this, "","Загрузка...", true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {

            try
            {
                result = "";
                ApiHelper api = new ApiHelper();

                JSONObject jsonObject = new JSONObject();
                if(category.getIdParent().equals(""))
                {
                    jsonObject.put("idCategory", category.getId());
                    jsonObject.put("idSubcategory", 0);
                }
                else
                {
                    jsonObject.put("idCategory", category.getIdParent());
                    jsonObject.put("idSubcategory", category.getId());
                }

                jsonObject.put("title", "test");
                jsonObject.put("content", content);
                jsonObject.put("price", price);
                jsonObject.put("price_currency", price_currency);
                jsonObject.put("api_key", ApiHelper.getClientKey());
                jsonObject.put("city", "bishkek");
                jsonObject.put("country", "kg");
                jsonObject.put("actionType", actionType);
                jsonObject.put("sex", sex);
                jsonObject.put("birth_year", birth_year);
                jsonObject.put("displayed_name", displayed_name);

                JSONObject obj = api.sendPost(jsonObject);
                if(obj.has("post_id"))
                {

                    String url = ApiHelper.POST_URL + "/" + obj.getString("post_id") + "/images";
                    int length = GlobalVar.image_paths.size();
                    if(length > 0)
                    {
                        JSONObject jobj;
                        for (int i = 0; i <length; i++) {
                            jobj = api.sendImage(url, GlobalVar.image_paths.get(i), true);
                            if(jobj.has("id"))
                                continue;
                        }
                    }

                    result = "Добавлено";
                }
                else result = "Ошибка";

            }
            catch (Exception ex)
            {
                String exText = ex.getMessage();
                Log.d("AddPostActivity", "Exception: " + exText);
                return "Ошибка";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            Toast.makeText(AddPostActivity.this, result, Toast.LENGTH_SHORT).show();
           // Intent in = new Intent(context, HomeActivity.class);
            //in.putExtra("case", 1);
            //startActivity(in);
            //clear images
            GlobalVar._bitmaps.clear();
            GlobalVar.image_paths.clear();
        }
    }
}
