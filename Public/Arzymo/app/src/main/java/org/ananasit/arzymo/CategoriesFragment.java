package org.ananasit.arzymo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.ananasit.arzymo.adapter.CategoriesRecyclerAdapter;
import org.ananasit.arzymo.util.GlobalVar;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment
{

    private View rootView;
    private RecyclerView recyclerView;
    private CategoriesRecyclerAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        recyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        adapter =  new CategoriesRecyclerAdapter(getActivity(), GlobalVar._categories);
        recyclerView.setAdapter(adapter);

        return rootView;
    }
}
