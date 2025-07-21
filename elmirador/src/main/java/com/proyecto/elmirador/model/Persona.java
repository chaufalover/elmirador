package com.proyecto.elmirador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Persona {
    private int id;
    private String nombres;
    private String apellidos;
    private String tipoDoc;
    private String numDoc;
}
