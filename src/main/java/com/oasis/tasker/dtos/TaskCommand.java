package com.oasis.tasker.dtos;

import com.oasis.tasker.enums.Priority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCommand {
    private Long id;
    private String name;
    private String description;
    private CategoryCommand category;
    private Long ownerId;
    private Date due_date;
    private Priority priority;
}
