package org.ananasit.arzymo.model;

import java.util.ArrayList;

/**
 * Created by nurzamat on 8/3/15.
 */
public class Post{
    private String id, content, username;
    private String thumbnailUrl = "";
    private String price, price_currency = "";
    private Category category = null;
    //private ArrayList<String> image_urls;
    private ArrayList<Image> images;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> _images)
    {
        this.images = _images;
    }

}
