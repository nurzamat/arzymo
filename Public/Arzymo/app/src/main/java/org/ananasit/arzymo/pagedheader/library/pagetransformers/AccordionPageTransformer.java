package org.ananasit.arzymo.pagedheader.library.pagetransformers;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by jorge on 10/08/14.
 */
public class AccordionPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View page, float position) {

        page.setTranslationX(-1*page.getWidth()*position);

        if(position < 0) {
            page.setPivotX(0f);
        } else if(position > 0) {
            page.setPivotX(page.getWidth());
        }
        page.setScaleX(1-Math.abs(position));
    }
}
