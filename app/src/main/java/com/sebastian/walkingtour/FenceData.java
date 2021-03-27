package com.sebastian.walkingtour;

import java.io.Serializable;

public class FenceData implements Serializable {
    private final String id;
    private final String address;
    private final double latitude;
    private final double longitude;
    private final float radius;
    private final String description;
    private final String fenceColor;
    private final String imageUrl;

    public FenceData(String id, String address, double latitude, double longitude, float radius, String description, String fenceColor, String imageUrl) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.description = description;
        this.fenceColor = fenceColor;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getRadius() {
        return radius;
    }

    public String getDescription() {
        return description;
    }

    public String getFenceColor() {
        return fenceColor;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "FenceData{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", radius=" + radius +
                ", description='" + description + '\'' +
                ", fenceColor='" + fenceColor + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
