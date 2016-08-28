package org.ananasit.rekordo;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import org.ananasit.rekordo.util.ActionType;
import org.ananasit.rekordo.util.CategoryType;
import org.ananasit.rekordo.util.GlobalVar;
import org.ananasit.rekordo.util.Utils;

/**
 * Created by nurzamat on 8/27/16.
 */
public class DialogFilter extends DialogFragment {

    int mNum;
    String params;
    int actionType = 0;
    int sex = 2; //0 - female, 1 - male
    int actionPos = 0;
    String region, location;
    CheckBox chkMale, chkFemale;
    Spinner region_spinner, city_spinner, action_spinner;
    EditText etAgeFrom, etAgeTo, etPriceFrom, etPriceTo;
    LinearLayout city_layout, action_layout, sex_layout, age_layout, price_layout;
    /**
     * Create a new instance of DialogFilter, providing "num"
     * as an argument.
     */
    static DialogFilter newInstance(int num) {
        DialogFilter f = new DialogFilter();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }
    public interface SearchListener {
        void onSearch(String params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments().getInt("num");
        //working with num logic num - образно говоря
        //// TODO: 8/27/16
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_filter, container, false);
        region_spinner = (Spinner) v.findViewById(R.id.region_spinner);
        city_spinner = (Spinner) v.findViewById(R.id.city_spinner);
        action_spinner = (Spinner) v.findViewById(R.id.action_spinner);
        chkMale = (CheckBox) v.findViewById(R.id.chkMale);
        chkFemale = (CheckBox) v.findViewById(R.id.chkFemale);
        //layouts
        city_layout = (LinearLayout)v.findViewById(R.id.city_layout);
        action_layout = (LinearLayout)v.findViewById(R.id.action_layout);
        sex_layout = (LinearLayout)v.findViewById(R.id.sex_layout);
        age_layout = (LinearLayout)v.findViewById(R.id.age_layout);
        price_layout = (LinearLayout)v.findViewById(R.id.price_layout);
        //
        etAgeFrom = (EditText) v.findViewById(R.id.age_from);
        etAgeTo = (EditText) v.findViewById(R.id.age_to);
        etPriceFrom = (EditText) v.findViewById(R.id.price_from);
        etPriceTo = (EditText) v.findViewById(R.id.price_to);

        initLocationSpinners();

        CategoryType categoryType = Utils.getCategoryType(GlobalVar.Category);
        actionLayout(categoryType);
        datingLayout(categoryType);
        priceLayout(categoryType);

        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.btnSearch);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // When button is clicked, call up to owning activity.
                params = Utils.getParams("", actionType);
                ((SearchListener)getActivity()).onSearch(params);
                dismiss();
            }
        });

        return v;
    }

    //init work
    private void initLocationSpinners()
    {
        ArrayAdapter<CharSequence> region_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.regions, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        region_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        region_spinner.setAdapter(region_adapter);
        region_spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {

                        if(pos != 0)
                            region = parent.getItemAtPosition(pos).toString();
                        initLocationCity(pos);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                    }
                }
        );
    }

    private void initLocationCity(int pos)
    {
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = city_layout.getLayoutParams();

        if(pos == 0)
        {
            city_spinner.setVisibility(View.INVISIBLE);
            params.height = 0;
        }
        else
        {
            // Changes the height and width to the specified *pixels*
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            city_spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> city_adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.chuy, android.R.layout.simple_spinner_item);

            if(pos == 2)
            {
                city_adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.issyk, android.R.layout.simple_spinner_item);
            }
            if(pos == 3)
            {
                city_adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.naryn, android.R.layout.simple_spinner_item);
            }
            if(pos == 4)
            {
                city_adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.talas, android.R.layout.simple_spinner_item);
            }
            if(pos == 5)
            {
                city_adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.jalalabad, android.R.layout.simple_spinner_item);
            }
            if(pos == 6)
            {
                city_adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.osh, android.R.layout.simple_spinner_item);
            }
            if(pos == 7)
            {
                city_adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.batken, android.R.layout.simple_spinner_item);
            }
            // Specify the layout to use when the list of choices appears
            city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            city_spinner.setAdapter(city_adapter);
            city_spinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int pos, long id) {

                            location = parent.getItemAtPosition(pos).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    }
            );
        }
        city_layout.setLayoutParams(params);
    }

    private void actionLayout(final CategoryType catType)
    {
        ViewGroup.LayoutParams params = action_layout.getLayoutParams();

        if(catType.equals(CategoryType.SELL_BUY) || catType.equals(CategoryType.RENT) || catType.equals(CategoryType.WORK))
        {
            //action
            action_layout.setVisibility(View.VISIBLE);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            ArrayAdapter<CharSequence> action_adapter = ArrayAdapter.createFromResource(getActivity(),
                    R.array.buy_sell, android.R.layout.simple_spinner_item);

            if(catType.equals(CategoryType.RENT))
            {
                action_adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.rent_get_give, android.R.layout.simple_spinner_item);
            }
            if(catType.equals(CategoryType.WORK))
            {
                action_adapter = ArrayAdapter.createFromResource(getActivity(),
                        R.array.work_resume_vacancy, android.R.layout.simple_spinner_item);
            }

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
                            if(catType.equals(CategoryType.SELL_BUY))
                            {
                                if (pos == 0)
                                    actionType = Utils.getActionTypeValue(ActionType.SELL);
                                if (pos == 1)
                                    actionType = Utils.getActionTypeValue(ActionType.BUY);
                            }
                            if(catType.equals(CategoryType.RENT))
                            {
                                if (pos == 0)
                                    actionType = Utils.getActionTypeValue(ActionType.RENT_GIVE);
                                if (pos == 1)
                                    actionType = Utils.getActionTypeValue(ActionType.RENT_GET);
                            }
                            if(catType.equals(CategoryType.WORK))
                            {
                                if (pos == 0)
                                    actionType = Utils.getActionTypeValue(ActionType.VACANCY);
                                if (pos == 1)
                                    actionType = Utils.getActionTypeValue(ActionType.RESUME);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            // TODO Auto-generated method stub
                        }
                    }
            );
        }
        else
        {   action_layout.setVisibility(View.INVISIBLE);
            params.height = 0;
        }

        action_layout.setLayoutParams(params);
    }

    private void datingLayout(final CategoryType catType)
    {
        ViewGroup.LayoutParams sex_params = sex_layout.getLayoutParams();
        ViewGroup.LayoutParams age_params = age_layout.getLayoutParams();

        if(catType.equals(CategoryType.DATING))
        {
            sex_layout.setVisibility(View.VISIBLE);
            age_layout.setVisibility(View.VISIBLE);

            sex_params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            age_params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            chkMale.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //is chkMale checked?
                    if (((CheckBox) v).isChecked()) {
                        sex = 1;
                        chkFemale.setChecked(false);
                    }
                }
            });

            chkFemale.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //is chkFemale checked?
                    if (((CheckBox) v).isChecked()) {
                        sex = 0;
                        chkMale.setChecked(false);
                    }
                }
            });

        }
        else
        {
            sex_layout.setVisibility(View.INVISIBLE);
            age_layout.setVisibility(View.INVISIBLE);
            sex_params.height = 0;
            age_params.height = 0;
        }
        sex_layout.setLayoutParams(sex_params);
        age_layout.setLayoutParams(age_params);
    }

    private void priceLayout(final CategoryType catType)
    {
        ViewGroup.LayoutParams params = price_layout.getLayoutParams();

        if(catType.equals(CategoryType.SELL_BUY) || catType.equals(CategoryType.RENT))
        {
            price_layout.setVisibility(View.VISIBLE);
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        else
        {
            price_layout.setVisibility(View.INVISIBLE);
            params.height = 0;
        }
        price_layout.setLayoutParams(params);
    }
}
