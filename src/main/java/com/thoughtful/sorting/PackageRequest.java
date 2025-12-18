package com.thoughtful.sorting;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PackageRequest {
    @NotNull(message = "Width cannot be null")
    @Positive(message = "Width must be positive")
    private Double width;
    
    @NotNull(message = "Height cannot be null")
    @Positive(message = "Height must be positive")
    private Double height;
    
    @NotNull(message = "Length cannot be null")
    @Positive(message = "Length must be positive")
    private Double length;
    
    @NotNull(message = "Mass cannot be null")
    @Positive(message = "Mass must be positive")
    private Double mass;

    // Constructors
    public PackageRequest() {
    }

    public PackageRequest(Double width, Double height, Double length, Double mass) {
        this.width = width;
        this.height = height;
        this.length = length;
        this.mass = mass;
    }

    // Getters and Setters
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getMass() {
        return mass;
    }

    public void setMass(Double mass) {
        this.mass = mass;
    }

    @Override
    public String toString() {
        return "PackageRequest{" +
                "width=" + width +
                ", height=" + height +
                ", length=" + length +
                ", mass=" + mass +
                '}';
    }
}
