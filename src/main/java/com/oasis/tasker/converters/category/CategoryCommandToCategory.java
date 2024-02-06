package com.oasis.tasker.converters.category;

import com.oasis.tasker.dtos.CategoryCommand;
import com.oasis.tasker.entities.Category;
import com.oasis.tasker.entities.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class CategoryCommandToCategory implements Converter<CategoryCommand, Category> {

    @Override
    public Category convert(CategoryCommand source) {
        if(source ==null)
            return null;

        Category category = new Category();

        category.setId(source.getId());
        category.setName(source.getName());

        if(source.getOwnerId()!=null){
            User user = new User();
            user.setId(source.getOwnerId());
            category.setUser(user);
        }

        return  category;
    }
}
