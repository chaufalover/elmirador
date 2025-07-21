package com.proyecto.elmirador.model;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Visita extends Persona{

     private LocalDateTime tiempo;
     private Departamento idDpto;
    
     public Visita(int id, String nombres, String apellidos, String tipoDoc, String numDoc, LocalDateTime tiempo, Departamento idDpto) {
          super(id, nombres, apellidos, tipoDoc, numDoc);
          this.tiempo = tiempo;
          this.idDpto = idDpto;
     }
     
}
