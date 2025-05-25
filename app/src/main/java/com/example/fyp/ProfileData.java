package com.example.fyp;

import java.util.ArrayList;
import java.util.List;

public class ProfileData {
    public int user_id;
    public String name;
    public String email;
    public String contact_number;
    public String address;
    public String dob;
    public List<String> skills;
    public List<String> interests;

    // Constructor (optional)
    public ProfileData() {
        // You can initialize lists here if needed
        this.skills = new ArrayList<>();
        this.interests = new ArrayList<>();
    }
}

