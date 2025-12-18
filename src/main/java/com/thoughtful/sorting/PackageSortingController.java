package com.thoughtful.sorting;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PackageSortingController {

    private final PackageSorter packageSorter;

    public PackageSortingController() {
        this.packageSorter = new PackageSorter();
    }

    //http://localhost:8080/api/sort?width=50&height=50
    @PostMapping("/sort")
    public ResponseEntity<PackageResponse> sortPackage(@Valid @RequestBody PackageRequest request) {
        String category = packageSorter.sort(
            request.getWidth(),
            request.getHeight(),
            request.getLength(),
            request.getMass()
        );
        
        PackageResponse response = new PackageResponse(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Package Sorting API is running");
    }
}

