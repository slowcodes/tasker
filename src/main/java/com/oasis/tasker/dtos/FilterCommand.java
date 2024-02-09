package com.oasis.tasker.dtos;

import com.oasis.tasker.enums.Priority;
import com.oasis.tasker.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilterCommand {
    private String dueDate;
    private String priority;
    private String status;
}
