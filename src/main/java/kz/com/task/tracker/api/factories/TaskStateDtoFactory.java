package kz.com.task.tracker.api.factories;


import kz.com.task.tracker.api.dto.TaskStateDto;
import kz.com.task.tracker.store.entitites.TaskStateEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskStateDtoFactory {

    public TaskStateDto makeProjectDto(TaskStateEntity taskStateEntity) {

        return TaskStateDto.builder()
                .id(taskStateEntity.getId())
                .name(taskStateEntity.getName())
                .createdAt(taskStateEntity.getCreatedAt())
                .ordinal(taskStateEntity.getOrdinal())
                .build();
    }
}
