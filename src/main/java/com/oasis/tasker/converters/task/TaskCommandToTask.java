package com.oasis.tasker.converters.task;

import com.oasis.tasker.converters.category.CategoryCommandToCategory;
import com.oasis.tasker.dtos.TaskCommand;
import com.oasis.tasker.entities.Task;
import com.oasis.tasker.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class TaskCommandToTask implements Converter<TaskCommand, Task> {

    private final CategoryCommandToCategory cctc = new CategoryCommandToCategory();

    @Override
    public Task convert(TaskCommand source) {
        if (source==null)
            return null;

        Task task = new Task();
        task.setId(source.getId());
        task.setDescription(source.getDescription());
        task.setPriority(source.getPriority());
        task.setName(source.getName());
        task.setDueDate(source.getDueDate());
        task.setCategory(cctc.convert(source.getCategory()));
        task.setStatus(source.getStatus());

        if(source.getOwnerId() != null){
            User user = new User();
            user.setId(source.getOwnerId());
            task.setOwner(user);
        }
        return task;
    }
}
