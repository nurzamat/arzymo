package org.ananasit.rekordo.pagedheader.library.pagetransformers;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by jorge on 10/08/14.
 */
public class ScalePageTransformer implements  ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        final float normalizedposition = Math.abs(Math.abs(position) - 1);
        page.setScaleX(normalizedposition / 2 + 0.5f);
        page.setScaleY(normalizedposition / 2 + 0.5f);
    }
}
