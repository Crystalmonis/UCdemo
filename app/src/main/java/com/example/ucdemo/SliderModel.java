package com.example.ucdemo;

public class SliderModel {
    private String banner;
    private String background_colour;

    public SliderModel(String banner, String background_colour) {
        this.banner = banner;
        this.background_colour = background_colour;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackground_colour() {
        return background_colour;
    }

    public void setBackground_colour(String background_colour) {
        this.background_colour = background_colour;
    }
}
