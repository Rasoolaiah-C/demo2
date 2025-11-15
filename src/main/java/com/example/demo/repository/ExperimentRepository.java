package com.example.demo.repository;

import com.example.demo.model.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    
    List<Experiment> findByStatus(String status);
    
    List<Experiment> findByResearcherName(String researcherName);
    
    List<Experiment> findByExperimentType(String experimentType);
    
    @Query("SELECT e FROM Experiment e WHERE e.name LIKE CONCAT('%', :name, '%')")
    List<Experiment> searchByName(@Param("name") String name);
}
