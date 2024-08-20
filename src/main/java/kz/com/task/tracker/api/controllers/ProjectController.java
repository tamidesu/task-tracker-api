package kz.com.task.tracker.api.controllers;


import jakarta.transaction.Transactional;
import kz.com.task.tracker.api.dto.AckDto;
import kz.com.task.tracker.api.dto.ProjectDto;
import kz.com.task.tracker.api.exceptions.BadRequestException;
import kz.com.task.tracker.api.exceptions.NotFoundException;
import kz.com.task.tracker.api.factories.ProjectDtoFactory;
import kz.com.task.tracker.store.entitites.ProjectEntity;
import kz.com.task.tracker.store.repositories.ProjectRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class ProjectController {

    ProjectRepository projectRepository;

    ProjectDtoFactory projectDtoFactory;

    public static final String FETCH_PROJECT = "api/projects";
    public static final String CREATE_PROJECT = "api/projects";
    public static final String EDIT_PROJECT = "api/projects/{projects_id}";
    public static final String DELETE_PROJECT = "api/projects/{projects_id}";

    public static final String CREATE_OR_UPDATE_PROJECT = "api/projects";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDto> getAllProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> prefixName
    ) {

        prefixName = prefixName.filter(optionalPrefixName -> !optionalPrefixName.trim().isEmpty());

        Stream<ProjectEntity> projectEntityStream = prefixName
                .map(projectRepository::StreamAllByName)
                .orElseGet(projectRepository::StreamAll);

        return projectEntityStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(
            @RequestParam String name
    ) {

        if(name.trim().isEmpty()) {
            throw new BadRequestException("Project projectName cannot be empty");
        }

        findByName(name).ifPresent(
                project -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exists", name));
                });

        ProjectEntity projectEntity = projectRepository.saveAndFlush(
                ProjectEntity.builder()
                        .name(name)
                        .build()
        );
        return projectDtoFactory.makeProjectDto(projectEntity);
    }

    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id" , required = false) Optional<Long> project_id,
            @RequestParam(value = "projectName",required = false) Optional<String> name
    ){
        name = name.filter(optionalPrefixName -> !optionalPrefixName.trim().isEmpty());

        boolean isCreate = !project_id.isPresent();

        final ProjectEntity projectEntity = project_id
                .map(this::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());

        if(isCreate && name.isEmpty()) {
            throw new BadRequestException("Project name cannot be empty");
        }

        name
                .ifPresent(projectName -> {

                    findByName(projectName)
                            .filter( anotherProject -> !Objects.equals(anotherProject.getId(), projectEntity.getId()) )
                            .ifPresent( project -> {
                                throw new BadRequestException(String.format("Project \"%s\" already exists", projectName));
                            });

                    projectEntity.setName(projectName);
                });

        final ProjectEntity savedProjectEntity = projectRepository.saveAndFlush(projectEntity);

        return ProjectDto.builder().build();
    }

    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(
            @PathVariable("project_id") Long projectId,
            @RequestParam(value = "projectName") String name
    ) {

        if(name.trim().isEmpty()) {
            throw new BadRequestException("Project projectName cannot be empty");
        }

        ProjectEntity projectEntity = getProjectOrThrowException(projectId);
//        projectRepository.findByName(projectName).ifPresent(project -> {
//            throw new BadRequestException(String.format("Project \"%s\" already exists", projectName));
//        });

        findByName(name)
                .filter( anotherProject -> !Objects.equals(anotherProject.getId(), projectEntity.getId()) )
                .ifPresent( project -> {
                    throw new BadRequestException(String.format("Project \"%s\" already exists", name));
                });

        projectEntity.setName(name);

        projectRepository.saveAndFlush(projectEntity);


        return projectDtoFactory.makeProjectDto(projectEntity);
    }

    private Optional<ProjectEntity> findByName(String name) {
        return projectRepository
                .findByName(name);
    }

    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(
            @PathVariable("project_id") Long projectId
    ) {

        ProjectEntity projectEntity = getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AckDto.makeDefault(true);

    }

    private ProjectEntity getProjectOrThrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Project \"%s\" not found",
                                        projectId
                                )
                        )
                );
    }

}
