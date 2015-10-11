package org.ananasit.arzymo.pagedheader.library.pagetransformers;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Code by Kedzie @ github: https://github.com/kedzie/Support_v4_NineOldAndroids#pager
 */
public class FlipPageTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        page.setTranslationX(-1*page.getWidth()*position);

        if(position >= -.5 && position <= .5)
            page.setAlpha(1);
        else
            page.setAlpha(0);

        page.setRotationY(position*180);
    }
}
