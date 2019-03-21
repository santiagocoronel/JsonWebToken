package com.zencom.auth;

import java.security.Principal;

public class Account implements Principal {

    private Long id;
    private String nombre;
    private String appelido;
    private String email;

    public Account(Long id, String nombre, String appelido, String email) {
        this.id = id;
        this.nombre = nombre;
        this.appelido = appelido;
        this.email = email;
    }

    @Override
    public String getName() {
        return nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAppelido() {
        return appelido;
    }

    public void setAppelido(String appelido) {
        this.appelido = appelido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
