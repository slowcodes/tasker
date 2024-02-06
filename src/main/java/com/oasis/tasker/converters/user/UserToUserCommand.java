package com.oasis.tasker.converters.user;

import com.oasis.tasker.dtos.UserCommand;
import com.oasis.tasker.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class UserToUserCommand implements Converter<User, UserCommand> {

    @Override
    public UserCommand convert(User source) {
        if(source == null)
            return null;

        UserCommand userCommand = new UserCommand();

        userCommand.setId(source.getId());
        userCommand.setEmail(source.getEmail());
        userCommand.setFirst_name(source.getFirst_name());
        userCommand.setLast_name(source.getLast_name());
        userCommand.setRole(source.getRole());

        return userCommand;
    }
}
