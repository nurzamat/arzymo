package org.ananasit.arzymo.pagedheader.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ananasit.arzymo.R;
import org.ananasit.arzymo.pagedheader.adapters.MockListAdapter;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.FifthHeaderFragment;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.FirstHeaderFragment;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.FourthHeaderFragment;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.SecondHeaderFragment;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.ThirdHeaderFragment;
import org.ananasit.arzymo.pagedheader.library.PagedHeadListView;
import org.ananasit.arzymo.pagedheader.library.utils.PageTransformerTypes;

import java.util.ArrayList;

/**
 * Created by jorge on 31/07/14.
 */
public class BottomIndicatorFragment extends Fragment {

    private View rootView;
    private PagedHeadListView mPagedHeadList;
    private String fragmentType;
    private int indicatorBgColor;
    private int indicatorColor;
    private PageTransformerTypes pageTransformerType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null)
            return rootView;

        fragmentType = getArguments().getString("type", "depth");

        rootView = inflater.inflate(R.layout.fragment_bottom_indicator, container, false);
        
        int actionBarColor;
        if (fragmentType.equals("flip"))
        {
            actionBarColor = indicatorBgColor = getResources().getColor(R.color.material_pink);
            indicatorColor = getResources().getColor(R.color.material_light_pink);
            pageTransformerType = PageTransformerTypes.FLIP;
        }
        else if (fragmentType.equals("scale")) {
            actionBarColor = indicatorBgColor = getResources().getColor(R.color.material_purple);
            indicatorColor = getResources().getColor(R.color.material_light_purple);
            pageTransformerType = PageTransformerTypes.SCALE;
        }
        else {
            actionBarColor = indicatorBgColor = getResources().getColor(R.color.material_teal);
            indicatorColor = getResources().getColor(R.color.material_light_teal);
            pageTransformerType = PageTransformerTypes.ACCORDION;
        }

        //getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(actionBarColor));
        initPagedHeadList();

        return rootView;
    }

    /**
     * Initializes list with mock fragments for the header and plenty of mock items
     */
    private void initPagedHeadList() {
        mPagedHeadList = (PagedHeadListView) rootView.findViewById(R.id.pagedHeadListView);

        mPagedHeadList.addFragmentToHeader(new FirstHeaderFragment());
        mPagedHeadList.addFragmentToHeader(new SecondHeaderFragment());
        mPagedHeadList.addFragmentToHeader(new ThirdHeaderFragment());
        mPagedHeadList.addFragmentToHeader(new FourthHeaderFragment());
        mPagedHeadList.addFragmentToHeader(new FifthHeaderFragment());

        mPagedHeadList.setHeaderOffScreenPageLimit(4);
        mPagedHeadList.setHeaderPageTransformer(pageTransformerType);

        mPagedHeadList.setIndicatorBgColor(indicatorBgColor);
        mPagedHeadList.setIndicatorColor(indicatorColor);

        ArrayList<String> mockItemList = new ArrayList<String>();

        for (int i = 0; i < 50; i++)
            mockItemList.add(getResources().getString(R.string.mock_item) + " " + (i+1));

        MockListAdapter mockListAdapter = new MockListAdapter(getActivity(), R.layout.mock_list_item, mockItemList);
        mPagedHeadList.setAdapter(mockListAdapter);
    }
}
