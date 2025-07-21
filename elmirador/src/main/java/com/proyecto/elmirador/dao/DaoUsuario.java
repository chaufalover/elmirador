package com.proyecto.elmirador.dao;

import com.proyecto.elmirador.model.Usuario;

public interface DaoUsuario extends Dao<Usuario, Integer> {
    
    Usuario Login(String usuario, String clave);

}
