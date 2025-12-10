package com.thoughtful.sorting;

public class PackageSorter {
    
    private static final int VOLUME_THRESHOLD = 1_000_000;
    private static final int DIMENSION_THRESHOLD = 150;
    private static final int MASS_THRESHOLD = 20;
    
    public static final String STANDARD = "STANDARD";
    public static final String SPECIAL = "SPECIAL";
    public static final String REJECTED = "REJECTED";
    
    public String sort(double width, double height, double length, double mass) {
        boolean isBulky = isBulky(width, height, length);
        boolean isHeavy = mass >= MASS_THRESHOLD;
        
        if (isBulky && isHeavy) {
            return REJECTED;
        } else if (isBulky || isHeavy) {
            return SPECIAL;
        } else {
            return STANDARD;
        }
    }
    
    private boolean isBulky(double width, double height, double length) {
        if (width >= DIMENSION_THRESHOLD || 
            height >= DIMENSION_THRESHOLD || 
            length >= DIMENSION_THRESHOLD) {
            return true;
        }
        
        double volume = width * height * length;
        return volume >= VOLUME_THRESHOLD;
    }
    
    public static void main(String[] args) {
        PackageSorter sorter = new PackageSorter();
        
        System.out.println("=".repeat(60));
        System.out.println("Package Sorting Demo");
        System.out.println("=".repeat(60));
        System.out.println();
        
        testPackage(sorter, 50, 50, 50, 10, "Standard package");
        testPackage(sorter, 150, 50, 50, 10, "Bulky by dimension");
        testPackage(sorter, 100, 100, 100, 10, "Bulky by volume");
        testPackage(sorter, 50, 50, 50, 20, "Heavy package");
        testPackage(sorter, 150, 100, 100, 25, "Bulky and heavy");
        
        System.out.println("=".repeat(60));
    }
    
    private static void testPackage(PackageSorter sorter, double width, double height, 
                                    double length, double mass, String description) {
        String result = sorter.sort(width, height, length, mass);
        double volume = width * height * length;
        
        System.out.println(description);
        System.out.printf("  Dimensions: %.0f x %.0f x %.0f cm%n", width, height, length);
        System.out.printf("  Volume: %.0f cm³%n", volume);
        System.out.printf("  Mass: %.0f kg%n", mass);
        System.out.printf("  Result: %s%n", result);
        System.out.println();
    }
}
