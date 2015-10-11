package org.ananasit.arzymo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import org.ananasit.arzymo.adapter.PostDetailListAdapter;
import org.ananasit.arzymo.model.Post;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.FifthHeaderFragment;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.FirstHeaderFragment;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.FourthHeaderFragment;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.SecondHeaderFragment;
import org.ananasit.arzymo.pagedheader.fragments.headerfragments.ThirdHeaderFragment;
import org.ananasit.arzymo.pagedheader.library.PagedHeadListView;
import org.ananasit.arzymo.pagedheader.library.utils.PageTransformerTypes;
import org.ananasit.arzymo.util.GlobalVar;
import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity2 extends AppCompatActivity {

    private PagedHeadListView mPagedHeadList;
    private int indicatorBgColor;
    private int indicatorColor;
    private PageTransformerTypes pageTransformerType;
    private Post p = GlobalVar._Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail2);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        indicatorBgColor = getResources().getColor(R.color.material_teal);
        indicatorColor = getResources().getColor(R.color.material_light_teal);
        pageTransformerType = PageTransformerTypes.ACCORDION;

        initPagedHeadList();
    }


    /**
     * Initializes list with mock fragments for the header and plenty of mock items
     */
    private void initPagedHeadList() {
        mPagedHeadList = (PagedHeadListView) findViewById(R.id.pagedHeadListView);

        try
        {
            mPagedHeadList.addFragmentToHeader(FirstHeaderFragment.newInstance(p.getImages().get(0).getUrl()));
            mPagedHeadList.addFragmentToHeader(SecondHeaderFragment.newInstance(p.getImages().get(1).getUrl()));
            mPagedHeadList.addFragmentToHeader(ThirdHeaderFragment.newInstance(p.getImages().get(2).getUrl()));
            mPagedHeadList.addFragmentToHeader(FourthHeaderFragment.newInstance(p.getImages().get(3).getUrl()));
            mPagedHeadList.addFragmentToHeader(FifthHeaderFragment.newInstance(p.getImages().get(4).getUrl()));
        }
        catch (IndexOutOfBoundsException ex)
        {
            ex.printStackTrace();
        }

        mPagedHeadList.setHeaderOffScreenPageLimit(4);
        mPagedHeadList.setHeaderPageTransformer(pageTransformerType);

        mPagedHeadList.setIndicatorBgColor(indicatorBgColor);
        mPagedHeadList.setIndicatorColor(indicatorColor);

        List<Post> postList = new ArrayList<Post>();
        postList.add(p);
        postList.add(p);

        PostDetailListAdapter adapter = new PostDetailListAdapter(PostDetailActivity2.this, postList);
        mPagedHeadList.setAdapter(adapter);
    }
}
