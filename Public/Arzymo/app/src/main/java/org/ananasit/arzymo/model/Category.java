package org.ananasit.arzymo.model;

import java.util.ArrayList;

/**
 * Created by nurzamat on 8/3/15.
 */
public class Category
{
    private String id;
    private String name;
    private boolean hasSubcat = false;
    private ArrayList<Category> subcats = null;

    public Category()
    {
    }

    public Category(String _id, String _name, boolean _has)
    {
        this.id = _id;
        this.name = _name;
        this.hasSubcat = _has;
    }
    public String getId() {
        return id;
    }

    public void setId(String _id) {
        this.id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public boolean isHasSubcat() {
        return hasSubcat;
    }

    public void setHasSubcat(boolean _has) {
        this.hasSubcat = _has;
    }

    public ArrayList<Category> getSubcats() {
        return subcats;
    }

    public void setSubcats(ArrayList<Category> _subcats)
    {
        this.subcats = _subcats;
    }
}
