package org.ananasit.rekordo.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by nurzamat on 8/28/16.
 */
public class Param
{
    String query = "0";
    int actionType = 0;
    String region = "0";
    String location = "0";
    String price_from = "0";
    String price_to = "0";
    int sex = 2;
    String age_from = "0";
    String age_to = "0";

    public Param() {
    }

    public String getQuery()
    {
        if(query.equals("") || query.equals("0") || query.contains(";"))
            query = "0";
        else
        {
            try
            {
                query = URLEncoder.encode(query, "UTF-8");
            }
            catch (UnsupportedEncodingException ex)
            {
                ex.printStackTrace();
            }
        }
        return query;
    }

    public void setQuery(String _query) {
        this.query = _query;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int _actionType) {
        this.actionType = _actionType;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String _region) {
        this.region = _region;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String _location) {
        this.location = _location;
    }

    public String getPrice_from() {
        return price_from;
    }

    public void setPrice_from(String _price_from) {
        this.price_from = _price_from;
    }

    public String getPrice_to() {
        return price_to;
    }

    public void setPrice_to(String _price_to) {
        this.price_to = _price_to;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int _sex) {
        this.sex = _sex;
    }

    public String getAge_from() {
        return age_from;
    }

    public void setAge_from(String _age_from) {
        this.age_from = _age_from;
    }

    public String getAge_to() {
        return age_to;
    }

    public void setAge_to(String _age_to) {
        this.age_to = _age_to;
    }

}
