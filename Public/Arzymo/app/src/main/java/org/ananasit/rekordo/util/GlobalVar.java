package org.ananasit.rekordo.util;

import android.util.SparseBooleanArray;

import org.ananasit.rekordo.model.Category;
import org.ananasit.rekordo.model.Post;

import java.util.ArrayList;

/**
 * Created by User on 12.12.2014.
 */
public class GlobalVar {

    public static ArrayList<Category> _categories = new ArrayList<Category>();
    //public static ArrayList<Bitmap> _bitmaps = new ArrayList<Bitmap>();
    public static ArrayList<String> image_paths = new ArrayList<String>();
    public static SparseBooleanArray mSparseBooleanArray = new SparseBooleanArray();
    public static Post _Post = null;
    public static Category Category = null;
}
