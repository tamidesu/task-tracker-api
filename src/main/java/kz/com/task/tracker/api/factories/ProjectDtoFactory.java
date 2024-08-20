package kz.com.task.tracker.api.factories;

import kz.com.task.tracker.api.dto.ProjectDto;
import kz.com.task.tracker.store.entitites.ProjectEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
public class ProjectDtoFactory {
    public ProjectDto makeProjectDto(ProjectEntity projectEntity) {
        return ProjectDto.builder()
                .id(projectEntity.getId())
                .name(projectEntity.getName())
                .createdAt(projectEntity.getCreatedAt())
                .updatedAt(projectEntity.getUpdatedAt())
                .build();
    }
}
