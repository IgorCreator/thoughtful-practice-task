package com.thoughtful.sorting;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PackageSortingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Package Sorting API is running"));
    }

    @Test
    public void testSortStandardPackage() throws Exception {
        PackageRequest request = new PackageRequest(50.0, 50.0, 50.0, 10.0);
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("STANDARD"));
    }

    @Test
    public void testSortBulkyPackageByDimension() throws Exception {
        PackageRequest request = new PackageRequest(150.0, 50.0, 50.0, 10.0);
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("SPECIAL"));
    }

    @Test
    public void testSortBulkyPackageByVolume() throws Exception {
        PackageRequest request = new PackageRequest(100.0, 100.0, 100.0, 10.0);
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("SPECIAL"));
    }

    @Test
    public void testSortHeavyPackage() throws Exception {
        PackageRequest request = new PackageRequest(50.0, 50.0, 50.0, 20.0);
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("SPECIAL"));
    }

    @Test
    public void testSortRejectedPackage() throws Exception {
        PackageRequest request = new PackageRequest(150.0, 100.0, 100.0, 25.0);
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("REJECTED"));
    }

    @Test
    public void testSortWithNullWidth() throws Exception {
        String jsonRequest = "{\"height\":50.0,\"length\":50.0,\"mass\":10.0}";
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.messages").isArray());
    }

    @Test
    public void testSortWithNegativeValue() throws Exception {
        PackageRequest request = new PackageRequest(-50.0, 50.0, 50.0, 10.0);
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.messages").isArray());
    }

    @Test
    public void testSortWithZeroValue() throws Exception {
        PackageRequest request = new PackageRequest(0.0, 50.0, 50.0, 10.0);
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    public void testSortWithInvalidJson() throws Exception {
        String invalidJson = "{\"width\":\"not-a-number\"}";
        
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Invalid Request"))
                .andExpect(jsonPath("$.messages[0]").value("Invalid value 'not-a-number' for field 'width'. Expected a valid number (e.g., 10.5, 150.0)."));
    }

    @Test
    public void testSortWithEmptyBody() throws Exception {
        mockMvc.perform(post("/api/sort")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
}

