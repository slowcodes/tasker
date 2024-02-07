package com.oasis.tasker.services;


import com.oasis.tasker.dtos.TaskCommand;
import com.oasis.tasker.entities.Task;
import com.oasis.tasker.entities.User;
import com.oasis.tasker.repositories.CategoryRepository;
import com.oasis.tasker.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository repository;

    //@Autowired
    //private AuthService authService;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Task>  getUserTask(String searchQuery, Long pageIndex, Long ownerId){

        int pageSize = 10;
        System.out.println("We have been called");
        Pageable pageable = PageRequest.of(pageIndex.intValue(), pageSize);

        Page<Task> taskPage;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Perform search if searchQuery is provided
            System.out.println("Search Query ys found");
            taskPage = repository.findByOwnerIdAndNameContaining(ownerId, searchQuery, pageable);

        } else {
            System.out.println("No Search query");
            // Retrieve all tasks if no searchQuery is provided
            taskPage = repository.findByOwnerId(ownerId, pageable);
        }
        return taskPage;
    }

    public Long userTotalTasks(Long ownerId){
        return repository.countByOwnerId(ownerId);
    }

    public void deleteTask(Long id){
        repository.deleteById(id);
    }

    public Task saveTask(TaskCommand taskCommand, User user){
        return repository.save(
                        new Task(
                                taskCommand.getName(),
                                taskCommand.getDescription(),
                                categoryRepository.getCategoryById(taskCommand.getCategory().getId()),
                                user,
                                taskCommand.getDue_date(),
                                taskCommand.getPriority()
                        )

        );
    }

    public Task updateTask(TaskCommand taskUpdate, User user){

        return repository.findById(taskUpdate.getId())
                .map(task -> {
                    task.setName(taskUpdate.getName());
                    task.setDescription(taskUpdate.getDescription());
                    task.setDue_date(taskUpdate.getDue_date());
                    task.setPriority(taskUpdate.getPriority());
                    task.setCategory(categoryRepository.getCategoryById(taskUpdate.getCategory().getId()));

                    return repository.save(task);
                })
                .orElseGet(() -> {
                    return saveTask(taskUpdate, user);
                });
    }

    public void validateTaskOwnership(Long taskId, User user) throws ResponseStatusException {
        Optional<Task> taskOptional = repository.findById(taskId);
        if (taskOptional.get().getOwner().getId() != user.getId())
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this task");
    }


    public void validateUpadate(TaskCommand taskUpdate, Long userId) throws ResponseStatusException {
        if(categoryRepository.getCategoryById(
                taskUpdate.getCategory().getId()).getUser().getId()!=userId){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not authorized to use the selected category for update");
        }
    }
}
