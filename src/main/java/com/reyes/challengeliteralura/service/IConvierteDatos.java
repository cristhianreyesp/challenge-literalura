package com.reyes.challengeliteralura.service;

public interface IConvierteDatos {
    <T> T convertirDatos(String json, Class<T> clase);
}
