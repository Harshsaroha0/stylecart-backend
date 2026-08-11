package com.microservices.stylecartbackend.exception;

public class NameAlreadyExistsException  extends  RuntimeException{

    public NameAlreadyExistsException(String name) {
        super("name already exists" + name);
    }
}
