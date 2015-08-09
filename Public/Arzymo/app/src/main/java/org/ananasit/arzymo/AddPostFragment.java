package org.ananasit.arzymo;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import org.ananasit.arzymo.adapter.PlaceSlidesFragmentAdapter;
import org.ananasit.arzymo.lib.CirclePageIndicator;
import org.ananasit.arzymo.model.Category;
import org.ananasit.arzymo.util.ApiHelper;
import org.ananasit.arzymo.util.GlobalVar;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostFragment extends Fragment {


    private static final String TAG =  "[add/edit post response]";
    ViewPager mPager;
    PagerAdapter mAdapter;
    CirclePageIndicator mIndicator;
    String content = "";
    String price = "";
    String price_currency;
    String result;
    String url = "";
    String id = "";
    EditText etContent;
    EditText etPrice;
    boolean mode = true; // add mode = 1, edit mode = 0;
    Button categoryBtn;
    Button postBtn;
    Category category = null;
    View rootView;

    public AddPostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_post, container, false);

        etContent = (EditText) rootView.findViewById(R.id.content);
        etPrice = (EditText) rootView.findViewById(R.id.price);
        //spinner job
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.price_currencies, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        postBtn = (Button) rootView.findViewById(R.id.btnPost);
        categoryBtn = (Button) rootView.findViewById(R.id.btnCategory);

        postBtn.setText(R.string.add);

        etContent.setText(content);
        etPrice.setText(price);
        spinner.setSelection(adapter.getPosition(price_currency));

        spinner.setOnItemSelectedListener(
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

        //for edit
        if(GlobalVar._Post != null)
        {
            this.id = GlobalVar._Post.getId();
            this.url = ApiHelper.POST_URL + "/" + id + "/";
        }
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
                Intent in = new Intent(getActivity(), CategoriesActivity.class);
                startActivity(in);
            }
        });


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(GlobalVar.Category != null && GlobalVar.Category.getSubcats() == null)
        {
            categoryBtn.setText(GlobalVar.Category.getName());
            this.category = GlobalVar.Category;
        }
        else this.category = null;
        ViewPagerWork();

    }

    private void ViewPagerWork() {
        int color = getResources().getColor(R.color.blue_dark);
        mAdapter = new PlaceSlidesFragmentAdapter(getActivity());
        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator) rootView.findViewById(R.id.indicator);
        mIndicator.setFillColor(color);
        mIndicator.setStrokeColor(color);
        mIndicator.setRadius(5);
        mIndicator.setViewPager(mPager);
        mIndicator.setSnap(true);
    }

    private void postButton() {
        if(!validate())
        {
            Toast.makeText(getActivity(), "Заполните все поля", Toast.LENGTH_LONG).show();
        }
        else
        {
            if (mode)
            {
                AddPostTask task = new AddPostTask();
                task.execute(ApiHelper.POST_URL);
            }
            else
            {   //use volley or async
                //VolleyPut();
                //async task
                putHttpAsyncTask task = new putHttpAsyncTask();
                task.execute();
            }
        }
    }

    private boolean validate(){

        content = etContent.getText().toString().trim();
        price = etPrice.getText().toString().trim();

        return (!content.equals("") && !price.equals("") && category != null && !price_currency.equals(""));
    }

    // add mode
    private class AddPostTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "","Загрузка...", true);
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
                jsonObject.put("api_key", ApiHelper.API_KEY);
                jsonObject.put("city", "bishkek");
                jsonObject.put("country", "kg");

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
                Log.d("AddPostFragment", "Exception: " + exText);
                return "Ошибка";
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            dialog.dismiss();
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            // Intent in = new Intent(context, HomeActivity.class);
            //in.putExtra("case", 1);
            //startActivity(in);
            //clear images
            GlobalVar._bitmaps.clear();
            GlobalVar.image_paths.clear();
        }
    }

    private class putHttpAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdialog = ProgressDialog.show(getActivity(), "","Загрузка...", true);
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            try
            {
                ApiHelper api = new ApiHelper();

                JSONObject jsonObject = new JSONObject();
                if(category.getIdParent().equals(""))
                {
                    jsonObject.put("id_category", category.getId());
                    jsonObject.put("id_subcategory", 0);
                }
                else
                {
                    jsonObject.put("id_category", category.getIdParent());
                    jsonObject.put("id_subcategory", category.getId());
                }
                jsonObject.put("content", content);
                jsonObject.put("price", price);
                jsonObject.put("price_currency", price_currency);
                jsonObject.put("api_key", ApiHelper.API_KEY);

                JSONObject obj = api.editPost(url, jsonObject); // will be checked for status ok
            }
            catch (Exception ex)
            {
                Log.d("AddPostFragment", "Exception: " + ex.getMessage());
                return "Ошибка";
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            pdialog.dismiss();
            if(!result.equals(""))
            {
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
            else
            {
                if(GlobalVar.image_paths.size() > 0)
                {
                    //Intent in = new Intent(context, EditImageActivity.class);
                    //startActivity(in);
                }
                else
                {
                    Toast.makeText(getActivity(), "Сохранено", Toast.LENGTH_SHORT).show();
                    //Intent in = new Intent(context, HomeActivity.class);
                    //in.putExtra("case", 1);
                    //startActivity(in);

                    //clear images
                    GlobalVar._bitmaps.clear();
                    GlobalVar.image_paths.clear();
                    GlobalVar._Post = null;
                }
            }
        }
    }


}
