package org.ananasit.arzymo.model;

import java.util.ArrayList;

/**
 * Created by nurzamat on 8/3/15.
 */
public class Post{
    private String thumbnailUrl = "";
    private String id = "";
    private String content = "";
    private String price = "";
    private String price_currency = "";
    private String hitcount = "";
    private User user = null;
    private Category category = null;
    private ArrayList<Image> images = null;

    public Post() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String name) {
        this.content = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category _category) {
        this.category = _category;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceCurrency() {
        return price_currency;
    }

    public void setPriceCurrency(String price_currency) {
        this.price_currency = price_currency;
    }

    public String getHitcount() {
        return hitcount;
    }

    public void setHitcount(String _hitcount) {
        this.hitcount = _hitcount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User _user) {
        this.user = _user;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> _images)
    {
        this.images = _images;
    }

}
