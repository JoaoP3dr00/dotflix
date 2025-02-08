package com.dotflix.domain.category;

import com.dotflix.domain.lixo.validation.Error;
import com.dotflix.domain.lixo.validation.ValidationHandler;
import com.dotflix.domain.lixo.validation.Validator;

public class CategoryValidator extends Validator {
    private final Category category;

    public CategoryValidator(final Category category, final ValidationHandler handler){
        super(handler);
        this.category = category;
    }

    @Override
    public void validate(){
        final String name = this.category.getName();

        if(name == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        if(name.trim().length() > 255 || name.trim().length() < 3){
            this.validationHandler().append(new Error("'name' should have between 3 and 255 characters"));
            return;
        }


    }
}
