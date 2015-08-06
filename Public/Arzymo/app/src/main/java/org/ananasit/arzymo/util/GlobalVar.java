package org.ananasit.arzymo.util;

import android.graphics.Bitmap;
import android.util.SparseBooleanArray;

import org.ananasit.arzymo.model.Category;
import org.ananasit.arzymo.model.Post;

import java.util.ArrayList;

/**
 * Created by User on 12.12.2014.
 */
public class GlobalVar {

    public static String Phone = "996772143126";
    public static String Client_key = "c524ef7fb40e1f9b79f041dd8d47fcdb";//for testin
    public static ArrayList<Category> _categories = new ArrayList<Category>();
    public static ArrayList<Bitmap> _bitmaps = new ArrayList<Bitmap>();
    public static ArrayList<String> image_paths = new ArrayList<String>();
    public static String query = "";
    public static SparseBooleanArray mSparseBooleanArray = new SparseBooleanArray();
    public static Post _Post = null;
    public static Category Category = null;
}
