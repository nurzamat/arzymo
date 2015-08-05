package org.ananasit.arzymo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import org.ananasit.arzymo.adapter.ListAdapter;
import org.ananasit.arzymo.util.GlobalVar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment
{

    private View rootView;
    private ListView listView;
    private ListAdapter adapter;

    public CategoriesFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        }

        listView = (ListView) rootView.findViewById(R.id.list);

        adapter =  new ListAdapter(getActivity(), GlobalVar._categories);
        listView.setAdapter(adapter);

        // Click event for single list row
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                GlobalVar.Category = GlobalVar._categories.get(position);
                Intent in;
                if (GlobalVar.Category.getSubcats() != null && GlobalVar.Category.getSubcats().size() > 0)
                {
                    in = new Intent(getActivity(), SubcatsActivity.class);
                }
                else
                {
                    GlobalVar.SubCategory = null;
                    in = new Intent(getActivity(), PostsActivity.class);
                }
                getActivity().startActivity(in);

            }
        });

        return rootView;
    }
}
