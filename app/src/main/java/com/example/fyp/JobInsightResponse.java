package com.example.fyp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobInsightResponse {
    public String adref;
    public Category category;
    public Company company;
    public Date created;
    public String description;
    public String id;
    public double latitude;
    public Location location;
    public double longitude;
    public String redirect_url;
    public String salary_is_predicted;
    public double salary_max;
    public double salary_min;
    public String title;
}

class Company {
    public String display_name;
}

class Category {
    public String label;
    public String tag;
}

class Location {
    public ArrayList<String> area;
    public String display_name;
}