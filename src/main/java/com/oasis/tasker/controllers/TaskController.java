package com.oasis.tasker.controllers;

import java.util.List;
import java.util.stream.Collectors;

import com.oasis.tasker.dtos.FilterCommand;
import com.oasis.tasker.dtos.TaskListResponseCommand;
import com.oasis.tasker.converters.task.TaskToTaskCommand;
import com.oasis.tasker.dtos.TaskCommand;
import com.oasis.tasker.entities.Task;
import com.oasis.tasker.services.AuthService;
import com.oasis.tasker.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

  private final TaskToTaskCommand taskToTaskCommand;
  private final TaskService taskService;

  @Autowired
  private AuthService authService;

  public TaskController(TaskToTaskCommand taskToTaskCommand,
                        TaskService taskService) {
    this.taskToTaskCommand = taskToTaskCommand;
    this.taskService = taskService;
  }

    @GetMapping("/{pageIndex}")
    public ResponseEntity<TaskListResponseCommand> myTasks(
            @RequestParam(name = "priority", required = false) String priority,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "due_date", required = false) String due_date,
            @PathVariable Long pageIndex,
            @RequestParam(name = "isSearch", required = false) Boolean isSearch,
            @RequestParam(name = "isFilter", required = false) Boolean isFilter,
            @RequestParam(required = false) String searchQuery) {

        Long userId = authService.getLoggedInUser().getId();

        Page<Task> taskPage;
        if(isFilter){
            taskPage = taskService.filterMyTask(
                    new FilterCommand(
                            due_date,
                            priority,
                            status
                    ),pageIndex,userId);
        }
        else if (isSearch){

            taskPage = taskService.searchMyTasks(searchQuery, pageIndex, userId);
        }
        else {
            taskPage = taskService.fetchAll(pageIndex, userId);
        }



        List<TaskCommand> taskCommands = taskPage.getContent()
                .stream()
                .map(task -> taskToTaskCommand.convert(task))
                .collect(Collectors.toList());

        TaskListResponseCommand responseCommand = new TaskListResponseCommand(
                taskCommands, taskService.userTotalTasks(userId)
        );

        return ResponseEntity.ok(responseCommand);
    }

  @PostMapping
  public ResponseEntity<TaskCommand> create(@RequestBody TaskCommand task) {
    return ResponseEntity.ok(
            taskToTaskCommand.convert(
                    taskService.saveTask(
                            task, authService.getLoggedInUser()
                    )
            )
    );
  }

  @DeleteMapping("/{id}")
  void deleteTask(@PathVariable Long id) {
    taskService.validateTaskOwnership(id,authService.getLoggedInUser());
    taskService.deleteTask(id);
  }

  @PutMapping("/{id}")
  TaskCommand replaceTask(@RequestBody TaskCommand taskUpdate,
                          @PathVariable Long id) {
    taskService.validateTaskOwnership(id,authService.getLoggedInUser());
    taskService.validateUpadate(taskUpdate, authService.getLoggedInUser().getId());
    taskUpdate.setId(id);
    return taskToTaskCommand.convert(taskService.updateTask(taskUpdate, authService.getLoggedInUser()));
  }

}