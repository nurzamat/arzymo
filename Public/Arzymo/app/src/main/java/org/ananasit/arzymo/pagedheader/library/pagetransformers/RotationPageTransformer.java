package org.ananasit.arzymo.pagedheader.library.pagetransformers;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by jorge on 10/08/14.
 */
public class RotationPageTransformer implements  ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        page.setRotationY(position * -30);
    }
}
