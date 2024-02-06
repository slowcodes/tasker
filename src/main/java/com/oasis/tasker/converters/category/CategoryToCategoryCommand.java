package com.oasis.tasker.converters.category;

import com.oasis.tasker.dtos.CategoryCommand;
import com.oasis.tasker.entities.Category;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {


    @Override
    public CategoryCommand convert(Category source) {
        if(source == null)
            return null;

        CategoryCommand categoryCommand = new CategoryCommand();

        categoryCommand.setId(source.getId());
        categoryCommand.setName(source.getName());
        if(source.getUser().getId() != null)
            categoryCommand.setOwnerId(source.getUser().getId());

        return categoryCommand;
    }
}
