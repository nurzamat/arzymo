package org.ananasit.rekordo;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ananasit.rekordo.adapter.CategoriesRecyclerAdapter;
import org.ananasit.rekordo.adapter.PostImageAdapter;
import org.ananasit.rekordo.lib.CirclePageIndicator;
import org.ananasit.rekordo.model.Post;
import org.ananasit.rekordo.util.GlobalVar;

public class PostDetailActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbar;
    RecyclerView recyclerView;
    CategoriesRecyclerAdapter adapter;

    ViewPager mPager;
    PostImageAdapter mAdapter;
    LinearLayout layout;
    CirclePageIndicator mIndicator;
    private Post p = GlobalVar._Post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout = (LinearLayout) findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailActivity.this, "layout clicked ", Toast.LENGTH_SHORT).show();
            }
        });

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailActivity.this, "layout clicked ", Toast.LENGTH_SHORT).show();
            }
        });
        collapsingToolbar.setTitle(" ");
        recyclerView = (RecyclerView) findViewById(R.id.contentPostDetail);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //todo
        // specify an adapter (see also next example)
        adapter =  new CategoriesRecyclerAdapter(this, GlobalVar._categories);
        recyclerView.setAdapter(adapter);

        ViewPagerWork();

    }

    private void ViewPagerWork() {
        int color = ContextCompat.getColor(this, R.color.white);
        mAdapter = new PostImageAdapter(PostDetailActivity.this, p);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailActivity.this, "clicked ", Toast.LENGTH_SHORT).show();
            }
        });

        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setFillColor(color);
        mIndicator.setStrokeColor(color);
        mIndicator.setRadius(15);
        mIndicator.setViewPager(mPager);
        mIndicator.setSnap(true);
    }

}
