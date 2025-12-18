package com.thoughtful.sorting;

public class PackageResponse {
    private String category;

    public PackageResponse() {
    }

    public PackageResponse(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "PackageResponse{" +
                "category='" + category + '\'' +
                '}';
    }
}

