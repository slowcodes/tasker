package com.oasis.tasker.converters.task;

import com.oasis.tasker.converters.category.CategoryToCategoryCommand;
import com.oasis.tasker.dtos.TaskCommand;
import com.oasis.tasker.entities.Task;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class TaskToTaskCommand implements Converter<Task, TaskCommand> {

    final CategoryToCategoryCommand ctcc = new CategoryToCategoryCommand();

    @Override
    public TaskCommand convert(Task source) {
        if (source == null)
            return null;

        TaskCommand taskCommand = new TaskCommand();

        taskCommand.setId(source.getId());
        taskCommand.setName(source.getName());
        taskCommand.setDescription(source.getDescription());
        taskCommand.setDueDate(source.getDueDate());
        taskCommand.setPriority(source.getPriority());
        taskCommand.setCategory(ctcc.convert(source.getCategory()));
        taskCommand.setStatus(source.getStatus());

        if(source.getOwner() !=null){
            taskCommand.setOwnerId(source.getOwner().getId());
        }

        return taskCommand;
    }
}
