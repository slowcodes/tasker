package com.oasis.tasker.dtos;

import com.oasis.tasker.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCommand {
    private Long id;
    private String email;
    private String last_name;
    private String first_name;
    private String password;
    private String password_confirm;
    private UserRole role;
}
