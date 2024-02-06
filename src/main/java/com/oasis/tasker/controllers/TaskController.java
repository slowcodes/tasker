package com.oasis.tasker.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.oasis.tasker.dtos.TaskListResponseCommand;
import com.oasis.tasker.converters.category.CategoryCommandToCategory;
import com.oasis.tasker.converters.task.TaskToTaskCommand;
import com.oasis.tasker.dtos.TaskCommand;
import com.oasis.tasker.entities.Task;
import com.oasis.tasker.repositories.CategoryRepository;
import com.oasis.tasker.repositories.TaskRepository;
import com.oasis.tasker.services.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

  private final TaskRepository taskRepository;
  private final CategoryRepository categoryRepository;
  private final AuthService authService;
  private final TaskToTaskCommand taskToTaskCommand;

  public TaskController(TaskRepository taskRepository, AuthService authService,
                        TaskToTaskCommand taskToTaskCommand,
                        CategoryRepository categoryRepository,
                        CategoryCommandToCategory categoryToCategoryCommand) {
    this.taskRepository = taskRepository;
    this.authService = authService;
    this.categoryRepository = categoryRepository;
    this.taskToTaskCommand = taskToTaskCommand;
  }

//  @GetMapping("/{pageIndex}")
//  public ResponseEntity<TaskListResponseCommand> myTasks(@PathVariable Long pageIndex) {
//      int pageSize = 10;
//      Pageable pageable = PageRequest.of(pageIndex.intValue(), pageSize);
//
//
//      List<TaskCommand> taskCommands = new ArrayList<>();
//      Long ownerId = authService.getLoggedInUser().getId();
//      Page<Task> taskPage = taskRepository.findByOwnerId(ownerId, pageable);
//
//      taskPage.getContent().forEach(task -> {
//          taskCommands.add(this.taskToTaskCommand.convert(task));
//      });
//
//
//      return ResponseEntity.ok(
//              new TaskListResponseCommand(
//                      taskCommands, taskRepository.countByOwnerId(ownerId)
//              )
//      );
//  }

    @GetMapping("/{pageIndex}")
    public ResponseEntity<TaskListResponseCommand> myTasks(
            @PathVariable Long pageIndex,
            @RequestParam(required = false) String searchQuery
    ) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageIndex.intValue(), pageSize);

        Long ownerId = authService.getLoggedInUser().getId();
        Page<Task> taskPage;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Perform search if searchQuery is provided
            System.out.println("Search Query ys found");
            taskPage = taskRepository.findByOwnerIdAndNameContaining(ownerId, searchQuery, pageable);

        } else {
            // Retrieve all tasks if no searchQuery is provided
            taskPage = taskRepository.findByOwnerId(ownerId, pageable);
        }

        List<TaskCommand> taskCommands = taskPage.getContent()
                .stream()
                .map(task -> taskToTaskCommand.convert(task))
                .collect(Collectors.toList());

        TaskListResponseCommand responseCommand = new TaskListResponseCommand(
                taskCommands, taskRepository.countByOwnerId(ownerId)
        );

        return ResponseEntity.ok(responseCommand);
    }
  @PostMapping
  public ResponseEntity<TaskCommand> create(@RequestBody TaskCommand task) {
    return ResponseEntity.ok(
            newTask(task)
    );
  }



  @DeleteMapping("/{id}")
  void deleteTask(@PathVariable Long id) {
    validateTaskOwnership(id);
    taskRepository.deleteById(id);
  }



  @PutMapping("/{id}")
  TaskCommand replaceTask(@RequestBody TaskCommand taskUpdate, @PathVariable Long id) {

    validateTaskOwnership(id);

    validateUpadate(taskUpdate);

    return taskRepository.findById(id)
      .map(task -> {
        task.setName(taskUpdate.getName());
        task.setDescription(taskUpdate.getDescription());
        task.setDue_date(taskUpdate.getDue_date());
        task.setPriority(taskUpdate.getPriority());
        task.setCategory(categoryRepository.getCategoryById(taskUpdate.getCategory().getId()));

        return taskToTaskCommand.convert(taskRepository.save(task));
      })
      .orElseGet(() -> {
        return newTask(taskUpdate);
      });

  }

  private TaskCommand newTask(TaskCommand taskCommand){
      return taskToTaskCommand.convert(
              taskRepository.save(
                      new Task(
                              taskCommand.getName(),
                              taskCommand.getDescription(),
                              categoryRepository.getCategoryById(taskCommand.getCategory().getId()),
                              authService.getLoggedInUser(),
                              taskCommand.getDue_date(),
                              taskCommand.getPriority()
                      )
              )
      );
  }

  private void validateTaskOwnership(Long taskId) throws ResponseStatusException{
    Optional<Task> taskOptional = taskRepository.findById(taskId);
    if (taskOptional.get().getOwner().getId() != authService.getLoggedInUser().getId())
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this task");
  }

    private void validateUpadate(TaskCommand taskUpdate) throws ResponseStatusException {
        if(categoryRepository.getCategoryById(
                taskUpdate.getCategory().getId()).getUser().getId()!=authService.getLoggedInUser().getId()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not authorized to use the selected category for update");
        }

    }
}