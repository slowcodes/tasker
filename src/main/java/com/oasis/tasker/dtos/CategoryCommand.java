package com.oasis.tasker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCommand {
    private Long id;
    private String name;
    private Long ownerId;
}
