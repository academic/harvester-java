package io.academic.dao;


import org.hibernate.validator.constraints.NotBlank;

public class IdentifierDao {

    @NotBlank
    private String oai;

    public String getOai() {
        return oai;
    }
}
