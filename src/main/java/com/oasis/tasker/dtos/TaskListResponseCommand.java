package com.oasis.tasker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskListResponseCommand {
    List<TaskCommand> tasks = new ArrayList<>();
    Long total;
}
