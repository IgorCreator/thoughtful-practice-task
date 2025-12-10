package com.thoughtful.sorting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for PackageSorter.
 */
class PackageSorterTest {
    
    private PackageSorter sorter;
    
    @BeforeEach
    void setUp() {
        sorter = new PackageSorter();
    }
    
    // ==================== STANDARD TESTS ====================
    
    @Test
    @DisplayName("Small package with light weight should be STANDARD")
    void testStandardPackage_SmallAndLight() {
        String result = sorter.sort(10, 10, 10, 5);
        assertEquals(PackageSorter.STANDARD, result);
    }
    
    @Test
    @DisplayName("Medium package just below thresholds should be STANDARD")
    void testStandardPackage_JustBelowThresholds() {
        // Volume: 149 * 149 * 44 = 976,844 cm³ (< 1,000,000)
        // All dimensions < 150 cm
        // Mass < 20 kg
        String result = sorter.sort(149, 149, 44, 19.9);
        assertEquals(PackageSorter.STANDARD, result);
    }
    
    // ==================== SPECIAL TESTS - BULKY ONLY ====================
    
    @Test
    @DisplayName("Package with one dimension exactly 150 cm should be SPECIAL")
    void testSpecialPackage_OneDimensionExactly150() {
        String result = sorter.sort(150, 10, 10, 5);
        assertEquals(PackageSorter.SPECIAL, result);
    }
    
    @Test
    @DisplayName("Package with one dimension over 150 cm should be SPECIAL")
    void testSpecialPackage_OneDimensionOver150() {
        String result = sorter.sort(200, 10, 10, 5);
        assertEquals(PackageSorter.SPECIAL, result);
    }
    
    @Test
    @DisplayName("Package with volume exactly 1,000,000 cm³ should be SPECIAL")
    void testSpecialPackage_VolumeExactly1Million() {
        // 100 * 100 * 100 = 1,000,000 cm³
        String result = sorter.sort(100, 100, 100, 5);
        assertEquals(PackageSorter.SPECIAL, result);
    }
    
    @Test
    @DisplayName("Package with volume over 1,000,000 cm³ should be SPECIAL")
    void testSpecialPackage_VolumeOver1Million() {
        // 101 * 100 * 100 = 1,010,000 cm³
        String result = sorter.sort(101, 100, 100, 5);
        assertEquals(PackageSorter.SPECIAL, result);
    }
    
    // ==================== SPECIAL TESTS - HEAVY ONLY ====================
    
    @Test
    @DisplayName("Package with mass exactly 20 kg should be SPECIAL")
    void testSpecialPackage_MassExactly20() {
        String result = sorter.sort(10, 10, 10, 20);
        assertEquals(PackageSorter.SPECIAL, result);
    }
    
    @Test
    @DisplayName("Package with mass over 20 kg should be SPECIAL")
    void testSpecialPackage_MassOver20() {
        String result = sorter.sort(10, 10, 10, 25);
        assertEquals(PackageSorter.SPECIAL, result);
    }
    
    @Test
    @DisplayName("Heavy but not bulky package should be SPECIAL")
    void testSpecialPackage_HeavyOnly() {
        String result = sorter.sort(50, 50, 50, 30);
        assertEquals(PackageSorter.SPECIAL, result);
    }
    
    // ==================== REJECTED TESTS ====================
    
    @Test
    @DisplayName("Package that is both bulky (dimension) and heavy should be REJECTED")
    void testRejectedPackage_BulkyByDimensionAndHeavy() {
        String result = sorter.sort(200, 50, 50, 25);
        assertEquals(PackageSorter.REJECTED, result);
    }
    
    @Test
    @DisplayName("Package that is both bulky (volume) and heavy should be REJECTED")
    void testRejectedPackage_BulkyByVolumeAndHeavy() {
        String result = sorter.sort(120, 120, 120, 25);
        assertEquals(PackageSorter.REJECTED, result);
    }
    
    @Test
    @DisplayName("Package at both thresholds should be REJECTED")
    void testRejectedPackage_AtBothThresholds() {
        String result = sorter.sort(150, 100, 100, 20);
        assertEquals(PackageSorter.REJECTED, result);
    }
    
    @Test
    @DisplayName("Large bulky and heavy package should be REJECTED")
    void testRejectedPackage_ExtremelyLargeAndHeavy() {
        String result = sorter.sort(200, 200, 200, 50);
        assertEquals(PackageSorter.REJECTED, result);
    }
    
    // ==================== EDGE CASES ====================
    
    @Test
    @DisplayName("Very small package with decimal dimensions should be STANDARD")
    void testEdgeCase_DecimalDimensions() {
        String result = sorter.sort(0.5, 0.5, 0.5, 0.1);
        assertEquals(PackageSorter.STANDARD, result);
    }
    
    @Test
    @DisplayName("Package with mass just below 20 kg should be STANDARD")
    void testEdgeCase_JustBelowMassThreshold() {
        String result = sorter.sort(10, 10, 10, 19.99);
        assertEquals(PackageSorter.STANDARD, result);
    }
    
    @Test
    @DisplayName("Package with dimension just below 150 cm should be STANDARD")
    void testEdgeCase_JustBelowDimensionThreshold() {
        String result = sorter.sort(149.99, 10, 10, 5);
        assertEquals(PackageSorter.STANDARD, result);
    }
    
    @Test
    @DisplayName("Package with volume just below 1,000,000 cm³ should be STANDARD")
    void testEdgeCase_JustBelowVolumeThreshold() {
        // 99.9 * 100 * 100 = 999,000 cm³
        String result = sorter.sort(99.9, 100, 100, 5);
        assertEquals(PackageSorter.STANDARD, result);
    }
    
    // ==================== COMPREHENSIVE BOUNDARY TESTS ====================
    
    @Test
    @DisplayName("Test all dimension boundaries for bulky classification")
    void testBoundaries_AllDimensions() {
        // Each dimension at 150 should trigger SPECIAL
        assertEquals(PackageSorter.SPECIAL, sorter.sort(150, 1, 1, 1));
        assertEquals(PackageSorter.SPECIAL, sorter.sort(1, 150, 1, 1));
        assertEquals(PackageSorter.SPECIAL, sorter.sort(1, 1, 150, 1));
    }
    
    @Test
    @DisplayName("Test volume calculation with different dimension combinations")
    void testBoundaries_VolumeCombinations() {
        // Different ways to achieve volume >= 1,000,000
        assertEquals(PackageSorter.SPECIAL, sorter.sort(100, 100, 100, 1)); // 1,000,000
        assertEquals(PackageSorter.SPECIAL, sorter.sort(200, 100, 50, 1));  // 1,000,000
        assertEquals(PackageSorter.SPECIAL, sorter.sort(125, 125, 64, 1));  // 1,000,000
    }
}

