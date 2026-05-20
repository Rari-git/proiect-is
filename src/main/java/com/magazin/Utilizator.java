package com.magazin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Administrator.class, name = "admin"),
        @JsonSubTypes.Type(value = Vanzator.class, name = "vanzator"),
        @JsonSubTypes.Type(value = Cumparator.class, name = "cumparator")
})
public abstract class Utilizator {
    private String email;
    private String parola;

    // Constructor gol necesar pentru deserializarea JSON
    public Utilizator() {
    }

    public Utilizator(String email, String parola) {
        this.email = email;
        this.parola = parola;
    }

    public String getEmail() {
        return email;
    }

    public String getParola() {
        return parola;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}