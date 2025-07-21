
package com.proyecto.elmirador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Habitante {
    private int idHabitante;
    private String tipo;
    private String apellidos;
    private String nombres;     
    private String telefono;
    private Departamento idDpto;
    
}
