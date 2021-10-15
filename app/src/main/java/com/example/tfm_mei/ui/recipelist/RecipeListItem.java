package com.example.tfm_mei.ui.recipelist;

public class RecipeListItem {
    public  String id;
    public  String name;
    public  String url;

    public RecipeListItem(){

    }

    public RecipeListItem(String name, String url, String id) {
        this.name = name;
        this.url = url;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String id) {
        this.url = url;
    }

}

