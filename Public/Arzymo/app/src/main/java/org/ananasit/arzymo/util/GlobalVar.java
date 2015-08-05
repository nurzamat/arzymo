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

    public static String Phone = "";
    public static String Token = "";
    public static String Uid = "";
    public static ArrayList<Category> _categories = new ArrayList<Category>();
    public static ArrayList<Bitmap> _bitmaps = new ArrayList<Bitmap>();
    public static ArrayList<String> image_paths = new ArrayList<String>();
    public static boolean adv_position;
    public static String query = "";
    public static boolean isHomeFragment;
    public static boolean isCodeSent = false;
    public static SparseBooleanArray mSparseBooleanArray = new SparseBooleanArray();
    public static Post _Post = null;
    public static boolean Mode; // true = add mode, false = edit mode
    public static boolean profile_edit = false;
    public static String DisplayedName;
    public static Category Category = null;
}
