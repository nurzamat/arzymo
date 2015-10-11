package org.ananasit.arzymo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.ananasit.arzymo.pagedheader.fragments.BottomIndicatorFragment;

public class PostDetailActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail2);

        Fragment fragment = new BottomIndicatorFragment();
        Bundle args = new Bundle();
        args.putString("type", "depth");
        fragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
}
