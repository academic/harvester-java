package io.academic.dao;


import org.hibernate.validator.constraints.NotBlank;

import java.util.UUID;

public class DcDao {

    public DcDao(){

        this.id = id;
    }

    @NotBlank
    private UUID id;

    public UUID getId() {
        return id;
    }
}
