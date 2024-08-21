package kz.com.task.tracker.api.controllers;


import jakarta.transaction.Transactional;
import kz.com.task.tracker.api.controllers.helpers.ControllerHelper;
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

    ControllerHelper controllerHelper;

    public static final String FETCH_PROJECT = "api/projects";
    public static final String CREATE_PROJECT = "api/projects";
    public static final String EDIT_PROJECT = "api/projects/{project_id}";
    public static final String DELETE_PROJECT = "api/projects/{project_id}";

    public static final String CREATE_OR_UPDATE_PROJECT = "api/projects";

    @GetMapping(FETCH_PROJECT)
    public List<ProjectDto> getAllProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> prefixName
    ) {

        prefixName = prefixName.filter(optionalPrefixName -> !optionalPrefixName.trim().isEmpty());

        Stream<ProjectEntity> projectEntityStream = prefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAll);

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
            @RequestParam(value = "project_id" , required = false) Optional<Long> projectId,
            @RequestParam(value = "project_name",required = false) Optional<String> projectName
    ){
        projectName = projectName.filter(optionalProjectName -> !optionalProjectName.trim().isEmpty());

        boolean isCreate = !projectId.isPresent();

        if(isCreate && !projectName.isPresent()) {
            throw new BadRequestException("Project name cannot be empty");
        }

        final ProjectEntity projectEntity = projectId
                .map(controllerHelper::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());


        projectName
                .ifPresent(optionalProjectName -> {

                    findByName(optionalProjectName)
                            .filter( anotherProject -> !Objects.equals(anotherProject.getId(), projectEntity.getId()) )
                            .ifPresent( project -> {
                                throw new BadRequestException(String.format("Project \"%s\" already exists", optionalProjectName));
                            });

                    projectEntity.setName(optionalProjectName);
                });

        final ProjectEntity savedProjectEntity = projectRepository.saveAndFlush(projectEntity);

        return projectDtoFactory.makeProjectDto(savedProjectEntity  );
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

        controllerHelper.getProjectOrThrowException(projectId);

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
