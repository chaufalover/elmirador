package com.proyecto.elmirador;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.proyecto.elmirador.controller.LoginController;
import com.proyecto.elmirador.dao.DaoUsuario;
import com.proyecto.elmirador.dao.impl.DaoUsuarioImpl;
import com.proyecto.elmirador.model.Usuario;
import com.proyecto.elmirador.util.Hash;
import com.proyecto.elmirador.util.RolEnum;
import com.proyecto.elmirador.view.FrmLogin;

public class Elmirador {

    public static void main(String[] args) {
         DaoUsuario usuarioDao = new DaoUsuarioImpl();
        String claveHasheada = Hash.hashPassword("12345678");
        if (usuarioDao.Login("admin", claveHasheada) == null) {
            Usuario usuario = new Usuario();
            usuario.setNombres("Carlos");
            usuario.setApellidos("Gomez");
            usuario.setNomUsuario("admin");
            usuario.setRol(RolEnum.ADMIN);
            usuario.setClave(claveHasheada);
            usuario.setTipoDoc("DNI");
            usuario.setNumDoc("77788822");
            usuarioDao.insert(usuario);
            
        }
        
        FlatDarculaLaf.setup();
       
        FrmLogin login = new FrmLogin();
       LoginController controller = new LoginController(usuarioDao, login);
       controller.iniciar();
    }
}
