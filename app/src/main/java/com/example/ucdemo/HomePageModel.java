package com.example.ucdemo;

import java.util.List;

public class HomePageModel {

    private int type;
    private String backgroundColour;
    public static final int BANNER_SLIDER = 0;
    public static final int STRIP_AD_BANNER = 1;
    public static final int HORIZONTAL_PRODUCT_VIEW = 2;
    public static final int GRID_PRODUCT_VIEW = 3;

    ///////////////////////////Banner Slider

    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public HomePageModel(int type, List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }
    private List<SliderModel> sliderModelList;

    ///////////////////////////Banner Slider

    ///////////////////////Strip Ad
    private String resource;

    public HomePageModel(int type, String resource, String backgroundColour) {
        this.type = type;
        this.resource = resource;
        this.backgroundColour = backgroundColour;
    }
    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getBackgroundColour() {
        return backgroundColour;
    }
    public void setBackgroundColour(String backgroundColour) {
        this.backgroundColour = backgroundColour;
    }
    ///////////////////////Strip Ad


    private String title;
    private List<HorizontalProductScrollModel> horizontalProductScrollModelList;

    ///////////////////////Horizontal Product Layout
    private List<WishlistModel> viewAllProductList;

    public HomePageModel(int type, String title,String backgroundColour,List<HorizontalProductScrollModel> horizontalProductScrollModelList,List<WishlistModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.backgroundColour = backgroundColour;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
        this.viewAllProductList = viewAllProductList;
    }

    public List<WishlistModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishlistModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }
    ///////////////////////Horizontal Product Layout

    ///////////////////////Grid Product Layout
    public HomePageModel(int type, String title, String backgroundColour, List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.type = type;
        this.title = title;
        this.backgroundColour = backgroundColour;
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }
    ///////////////////////Grid Product Layout

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public List<HorizontalProductScrollModel> getHorizontalProductScrollModelList() {
        return horizontalProductScrollModelList;
    }
    public void setHorizontalProductScrollModelList(List<HorizontalProductScrollModel> horizontalProductScrollModelList) {
        this.horizontalProductScrollModelList = horizontalProductScrollModelList;
    }






}
