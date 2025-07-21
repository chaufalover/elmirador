
package com.proyecto.elmirador.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Registro {
    private String idRegistro;
    private LocalDateTime tiempo;
    private String motivo;
    private String ocurrencia;
    private Relevo idRelevo;
    private Departamento idDpto;
    
}
