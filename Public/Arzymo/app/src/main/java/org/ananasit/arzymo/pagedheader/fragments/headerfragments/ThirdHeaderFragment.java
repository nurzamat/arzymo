package org.ananasit.arzymo.pagedheader.fragments.headerfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.ananasit.arzymo.AppController;
import org.ananasit.arzymo.R;

/**
 * Created by jorge on 31/07/14.
 */
public class ThirdHeaderFragment extends Fragment {

    private static final String ARG_IMAGE = "image";
    private String image_url = "";
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private View rootView;

    public static ThirdHeaderFragment newInstance(String _url) {
        ThirdHeaderFragment fragment = new ThirdHeaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE, _url);
        fragment.setArguments(args);
        return fragment;
    }

    public ThirdHeaderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image_url = getArguments().getString(ARG_IMAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_header_third, container, false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView imgDisplay = (NetworkImageView) rootView.findViewById(R.id.imgDisplay);
        ProgressBar spin = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        spin.setVisibility(View.VISIBLE);
        imgDisplay.setImageUrl(image_url, imageLoader);
        if(imgDisplay.getDrawable() != null)
            spin.setVisibility(View.GONE);

        return rootView;
    }
}
