package com.oasis.tasker.services;


import com.oasis.tasker.dtos.FilterCommand;
import com.oasis.tasker.dtos.TaskCommand;
import com.oasis.tasker.entities.Task;
import com.oasis.tasker.entities.User;
import com.oasis.tasker.enums.Priority;
import com.oasis.tasker.enums.Status;
import com.oasis.tasker.repositories.CategoryRepository;
import com.oasis.tasker.repositories.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository repository;
    int pageSize = 10;

    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository repository, CategoryRepository categoryRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
    }


    public Page<Task> searchMyTasks(String searchText, Long pageIndex, Long ownerId){
        Pageable pageable = PageRequest.of(pageIndex.intValue(), pageSize);
        return repository.findByOwnerIdAndNameContaining(ownerId, searchText, pageable);

    }

    public Page<Task> fetchAll(Long pageIndex, Long ownerId){
        Pageable pageable = PageRequest.of(pageIndex.intValue(), pageSize);
        return repository.findByOwnerId(ownerId, pageable);
    }

    public Page<Task> filterMyTask(FilterCommand filter, Long pageIndex, Long ownerId){

        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageIndex.intValue(), pageSize);

        Page<Task> taskPage;
        if ((filter.getDueDate().length()>3) && Objects.nonNull(filter.getPriority()) && Objects.nonNull(filter.getStatus())) {
            System.out.println("All filter are set");

            taskPage = repository.findByOwnerIdAndDueDateAndPriorityAndStatus(
                    ownerId,
                    stringToLocalDateTime(filter.getDueDate()),
                    Priority.valueOf(filter.getPriority()),
                    Status.valueOf(filter.getStatus()),
                    pageable
            );

        }
        else {
            System.out.println("some filters or no filters are set");
            // Check each combination of fields and perform filtering accordingly
            if (filter.getDueDate() != null && filter.getPriority() == null && filter.getStatus() == null) {
                System.out.println("Only dueDate is set");
                taskPage = repository.findByOwnerIdAndDueDate(ownerId, stringToLocalDateTime(filter.getDueDate()), pageable);
            }
            else if (filter.getPriority() != null && (filter.getStatus() == null || filter.getStatus().isEmpty()) && filter.getDueDate().length()<3) {
                System.out.println("Only priority is set");
                taskPage = repository.findByOwnerIdAndPriority(ownerId, Priority.valueOf(filter.getPriority()), pageable);
            }
            else if (filter.getStatus() != null && (filter.getPriority() == null || filter.getPriority().isEmpty()) && filter.getDueDate() == null) {
                System.out.println("Only status is set");
                taskPage = repository.findByOwnerIdAndStatus(ownerId, Status.valueOf(filter.getStatus()), pageable);
            }
            else if (filter.getPriority() != null && filter.getStatus() != null && (filter.getDueDate() == null || filter.getDueDate().isEmpty())) {
                System.out.println("Only priority and status are set");
                taskPage = repository.findByOwnerIdAndPriorityAndStatus(ownerId, Priority.valueOf(filter.getPriority()), Status.valueOf(filter.getStatus()), pageable);
            }
            else if (filter.getPriority() != null && filter.getDueDate() != null && filter.getStatus() == null) {
                System.out.println("Only priority and dueDate are set");
                taskPage = repository.findByOwnerIdAndPriorityAndDueDate(ownerId, Priority.valueOf(filter.getPriority()), stringToLocalDateTime(filter.getDueDate()), pageable);
            } else if (filter.getDueDate() != null && filter.getStatus() != null && filter.getPriority() == null) {
                System.out.println("Only dueDate and status are set");
                taskPage = repository.findByOwnerIdAndDueDateAndStatus(ownerId, stringToLocalDateTime(filter.getDueDate()), Status.valueOf(filter.getStatus()), pageable);
            }
            else {
                //search text is empty
                taskPage = fetchAll(pageIndex,ownerId);
            }

        }
        return taskPage;
    }

    private LocalDateTime stringToLocalDateTime(String dateString){
        // Define the pattern of the input date string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse the date string into a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);

        return dateTime;

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
                                taskCommand.getDueDate(),
                                taskCommand.getPriority(),
                                taskCommand.getStatus()
                        )

        );
    }

    public Task updateTask(TaskCommand taskUpdate, User user){

        return repository.findById(taskUpdate.getId())
                .map(task -> {
                    task.setName(taskUpdate.getName());
                    task.setDescription(taskUpdate.getDescription());
                    task.setDueDate(taskUpdate.getDueDate());
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
