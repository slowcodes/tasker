package com.oasis.tasker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category extends BaseModel{

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
