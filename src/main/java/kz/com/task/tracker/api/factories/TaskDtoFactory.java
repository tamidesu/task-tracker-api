package kz.com.task.tracker.api.factories;

import kz.com.task.tracker.api.dto.TaskDto;
import kz.com.task.tracker.store.entitites.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoFactory {

    public TaskDto makeProjectDto(TaskEntity taskEntity) {

        return TaskDto.builder()
                .id(taskEntity.getId())
                .name(taskEntity.getName())
                .description(taskEntity.getDescription())
                .createdAt(taskEntity.getCreatedAt())
                .build();
    }
}
