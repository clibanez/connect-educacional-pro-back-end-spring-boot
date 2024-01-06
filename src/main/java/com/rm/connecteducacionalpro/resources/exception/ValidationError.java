package com.rm.connecteducacionalpro.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{
    private static final long serialVersionUID = 1L;

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError() {
        super();
    }

    public ValidationError(long timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    //foi mudado para passar os parametros
    public void addError(String fieldName, String message) {
        this.errors.add(new FieldMessage(fieldName, message));
    }

}
