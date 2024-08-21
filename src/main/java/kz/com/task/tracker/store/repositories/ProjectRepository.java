package kz.com.task.tracker.store.repositories;

import kz.com.task.tracker.store.entitites.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);

    @Query("SELECT p FROM ProjectEntity p")  // Custom JPQL query to select all ProjectEntity records
    Stream<ProjectEntity> streamAll(); // Stream all records

    Stream<ProjectEntity> streamAllByNameStartsWithIgnoreCase(String name);  // Stream by 'name' property
}
