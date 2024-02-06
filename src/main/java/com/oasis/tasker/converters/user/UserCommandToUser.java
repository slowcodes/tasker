package com.oasis.tasker.converters.user;

import com.oasis.tasker.dtos.UserCommand;
import com.oasis.tasker.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class UserCommandToUser implements Converter<UserCommand, User> {
    @Override
    public User convert(UserCommand source) {

        if(source == null)
            return null;

        User user = new User();

        user.setId(source.getId());
        user.setEmail(source.getEmail());
        user.setFirst_name(source.getFirst_name());
        user.setLast_name(source.getLast_name());
        user.setPassword(source.getPassword());
        user.setRole(source.getRole());


        return user;
    }
}
