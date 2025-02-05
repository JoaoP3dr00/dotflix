package com.dotflix.domain.category;

import com.dotflix.domain.validation.Error;
import com.dotflix.domain.validation.ValidationHandler;
import com.dotflix.domain.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;

    public CategoryValidator(final Category category, final ValidationHandler handler){
        super(handler);
        this.category = category;
    }

    @Override
    public void validate(){
        if(this.category.getName() == null){
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
