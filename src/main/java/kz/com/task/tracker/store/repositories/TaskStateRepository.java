package kz.com.task.tracker.store.repositories;

import kz.com.task.tracker.store.entitites.TaskStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {

}
