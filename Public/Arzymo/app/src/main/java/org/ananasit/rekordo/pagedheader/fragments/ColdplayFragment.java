package org.ananasit.rekordo.pagedheader.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ananasit.rekordo.R;
import org.ananasit.rekordo.pagedheader.adapters.MockListAdapter;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.ColdPlayHeaderFragment1;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.ColdPlayHeaderFragment2;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.ColdPlayHeaderFragment3;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.ColdPlayHeaderFragment4;
import org.ananasit.rekordo.pagedheader.fragments.headerfragments.ColdPlayHeaderFragment5;
import org.ananasit.rekordo.pagedheader.library.PagedHeadListView;
import org.ananasit.rekordo.pagedheader.library.utils.PageTransformerTypes;

import java.util.ArrayList;

/**
 * Created by jorge on 31/07/14.
 */
public class ColdplayFragment extends Fragment {

    private View rootView;
    private PagedHeadListView mPagedHeadList;
    private int indicatorBgColor;
    private int indicatorColor;
    private PageTransformerTypes pageTransformerType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null)
            return rootView;

        rootView = inflater.inflate(R.layout.fragment_top_indicator, container, false);

        indicatorBgColor = getResources().getColor(R.color.material_lighter_blue);
        indicatorColor = getResources().getColor(R.color.material_lighter_light_blue);
        pageTransformerType = PageTransformerTypes.DEPTH;

       // getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(indicatorBgColor));
        initPagedHeadList();

        return rootView;
    }

    /**
     * Initializes list with mock fragments for the header and plenty of mock items
     */
    private void initPagedHeadList() {
        mPagedHeadList = (PagedHeadListView) rootView.findViewById(R.id.pagedHeadListView);

        mPagedHeadList.addFragmentToHeader(new ColdPlayHeaderFragment1());
        mPagedHeadList.addFragmentToHeader(new ColdPlayHeaderFragment2());
        mPagedHeadList.addFragmentToHeader(new ColdPlayHeaderFragment3());
        mPagedHeadList.addFragmentToHeader(new ColdPlayHeaderFragment4());
        mPagedHeadList.addFragmentToHeader(new ColdPlayHeaderFragment5());

        mPagedHeadList.setHeaderOffScreenPageLimit(4);
        mPagedHeadList.setHeaderPageTransformer(pageTransformerType);

        mPagedHeadList.setIndicatorBgColor(indicatorBgColor);
        mPagedHeadList.setIndicatorColor(indicatorColor);

        ArrayList<String> mockItemList = new ArrayList<String>();

        String[] songNames = getResources().getStringArray(R.array.coldplay_songs);

        for (int i = 0; i < songNames.length; i++)
            mockItemList.add("" + (i+1) + ". " + songNames[i]);

        MockListAdapter mockListAdapter = new MockListAdapter(getActivity(), R.layout.cold_play_list_item, mockItemList);
        mPagedHeadList.setAdapter(mockListAdapter);
    }
}
