package kz.com.task.tracker.store.repositories;

import kz.com.task.tracker.store.entitites.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
}
