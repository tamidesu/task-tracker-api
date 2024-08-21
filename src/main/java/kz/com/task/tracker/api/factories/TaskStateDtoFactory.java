package kz.com.task.tracker.api.factories;


import kz.com.task.tracker.api.dto.TaskStateDto;
import kz.com.task.tracker.store.entitites.TaskStateEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TaskStateDtoFactory {

    TaskDtoFactory taskDtoFactory;

    public TaskStateDto makeTaskStateDto(TaskStateEntity taskStateEntity) {

        return TaskStateDto.builder()
                .id(taskStateEntity.getId())
                .name(taskStateEntity.getName())
                .createdAt(taskStateEntity.getCreatedAt())
                .leftTaskStateId(taskStateEntity.getLeftTaskState().map(TaskStateEntity::getId).orElse(null))
                .rightTaskStateId(taskStateEntity.getRightTaskState().map(TaskStateEntity::getId).orElse(null))
                .tasks(
                        taskStateEntity
                                .getTasks()
                                .stream()
                                .map(TaskDtoFactory::makeTaskDto)
                                .collect(Collectors.toList())
                )
                .build();
    }
}
