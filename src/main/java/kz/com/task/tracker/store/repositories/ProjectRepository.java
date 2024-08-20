package kz.com.task.tracker.store.repositories;

import kz.com.task.tracker.store.entitites.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    Optional<ProjectEntity> findByName(String name);

    Stream<ProjectEntity> StreamAll();

    Stream<ProjectEntity> StreamAllByName(String name);
}
