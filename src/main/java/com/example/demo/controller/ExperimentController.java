package com.example.demo.controller;

import com.example.demo.model.Experiment;
import com.example.demo.repository.ExperimentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/experiments")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ExperimentController {
    
    @Autowired
    private ExperimentRepository experimentRepository;
    
    // GET all experiments
    @GetMapping
    public ResponseEntity<?> getAllExperiments() {
        try {
            List<Experiment> experiments = experimentRepository.findAll();
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Experiments retrieved successfully", experiments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving experiments: " + e.getMessage(), null));
        }
    }
    
    // GET experiment by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getExperimentById(@PathVariable Long id) {
        try {
            Optional<Experiment> experiment = experimentRepository.findById(id);
            if (experiment.isPresent()) {
                return ResponseEntity.ok()
                        .body(new ApiResponse(true, "Experiment retrieved successfully", experiment.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Experiment not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving experiment: " + e.getMessage(), null));
        }
    }
    
    // CREATE new experiment
    @PostMapping
    public ResponseEntity<?> createExperiment(@RequestBody Experiment experiment) {
        try {
            if (experiment.getName() == null || experiment.getName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse(false, "Experiment name is required", null));
            }
            
            Experiment savedExperiment = experimentRepository.save(experiment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Experiment created successfully", savedExperiment));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error creating experiment: " + e.getMessage(), null));
        }
    }
    
    // UPDATE experiment
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExperiment(@PathVariable Long id, @RequestBody Experiment experimentDetails) {
        try {
            Optional<Experiment> existingExperiment = experimentRepository.findById(id);
            
            if (existingExperiment.isPresent()) {
                Experiment experiment = existingExperiment.get();
                
                if (experimentDetails.getName() != null) {
                    experiment.setName(experimentDetails.getName());
                }
                if (experimentDetails.getDescription() != null) {
                    experiment.setDescription(experimentDetails.getDescription());
                }
                if (experimentDetails.getStatus() != null) {
                    experiment.setStatus(experimentDetails.getStatus());
                }
                if (experimentDetails.getResearcherName() != null) {
                    experiment.setResearcherName(experimentDetails.getResearcherName());
                }
                if (experimentDetails.getExperimentType() != null) {
                    experiment.setExperimentType(experimentDetails.getExperimentType());
                }
                if (experimentDetails.getDurationHours() != null) {
                    experiment.setDurationHours(experimentDetails.getDurationHours());
                }
                
                Experiment updatedExperiment = experimentRepository.save(experiment);
                return ResponseEntity.ok()
                        .body(new ApiResponse(true, "Experiment updated successfully", updatedExperiment));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Experiment not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error updating experiment: " + e.getMessage(), null));
        }
    }
    
    // DELETE experiment
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExperiment(@PathVariable Long id) {
        try {
            Optional<Experiment> experiment = experimentRepository.findById(id);
            
            if (experiment.isPresent()) {
                experimentRepository.deleteById(id);
                return ResponseEntity.ok()
                        .body(new ApiResponse(true, "Experiment deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Experiment not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error deleting experiment: " + e.getMessage(), null));
        }
    }
    
    // FILTER by status
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getExperimentsByStatus(@PathVariable String status) {
        try {
            List<Experiment> experiments = experimentRepository.findByStatus(status);
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Experiments retrieved successfully", experiments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving experiments: " + e.getMessage(), null));
        }
    }
    
    // FILTER by researcher
    @GetMapping("/researcher/{researcherName}")
    public ResponseEntity<?> getExperimentsByResearcher(@PathVariable String researcherName) {
        try {
            List<Experiment> experiments = experimentRepository.findByResearcherName(researcherName);
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Experiments retrieved successfully", experiments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error retrieving experiments: " + e.getMessage(), null));
        }
    }
    
    // SEARCH by name
    @GetMapping("/search/{name}")
    public ResponseEntity<?> searchExperimentsByName(@PathVariable String name) {
        try {
            List<Experiment> experiments = experimentRepository.searchByName(name);
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "Experiments retrieved successfully", experiments));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Error searching experiments: " + e.getMessage(), null));
        }
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok()
                .body(new ApiResponse(true, "API is healthy", null));
    }
    
    // API Response wrapper class
    public static class ApiResponse {
        public boolean success;
        public String message;
        public Object data;
        
        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public Object getData() {
            return data;
        }
    }
}
