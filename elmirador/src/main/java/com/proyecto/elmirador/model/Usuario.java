
package com.proyecto.elmirador.model;

import com.proyecto.elmirador.util.RolEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Usuario extends Persona {
    private String nomUsuario;
    private RolEnum rol;
    private String clave;
    
    public Usuario(int id, String nombres, String apellidos, String tipoDoc, String numDoc, String nomUsuario,
            RolEnum rol, String clave) {
        super(id, nombres, apellidos, tipoDoc, numDoc);
        this.nomUsuario = nomUsuario;
        this.rol = rol;
        this.clave = clave;
    }

   

}
