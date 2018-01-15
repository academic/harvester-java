package io.academic.dao;


import javax.validation.constraints.NotNull;
import java.util.UUID;

public class OaiDao {

    @NotNull
    private UUID id;


    public UUID getId() {
        return id;
    }
}
