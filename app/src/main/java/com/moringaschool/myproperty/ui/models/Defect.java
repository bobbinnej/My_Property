package com.moringaschool.myproperty.ui.models;

import android.net.Uri;

public class Defect {
    private String description , building, houseNumber, imageUri;

    public Defect() {
    }

    public Defect(String description, String building, String houseNumber) {
        this.description = description;
        this.building = building;
        this.houseNumber = houseNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
