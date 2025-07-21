package com.proyecto.elmirador.controller;

import javax.swing.JOptionPane;

import com.proyecto.elmirador.dao.DaoUsuario;
import com.proyecto.elmirador.dao.impl.DaoUsuarioImpl;
import com.proyecto.elmirador.model.Usuario;
import com.proyecto.elmirador.util.Hash;
import com.proyecto.elmirador.util.RolEnum;
import com.proyecto.elmirador.view.FrmLogin;

public class LoginController implements Controller {

    private DaoUsuario daoUsuario;
    Usuario usuario;
    EmpleadoController empCtrl;
    AdminController adminCtrl;
    private FrmLogin login;

    public LoginController(DaoUsuario daoUsuario, FrmLogin login) {
        this.daoUsuario = daoUsuario;
        this.login = login;
        daoUsuario = new DaoUsuarioImpl();
    }

    @Override
    public void iniciar() {
        login.setTitle("El Mirador");
        login.setVisible(true);
        login.setLocationRelativeTo(null);
        this.usuario = null;
        login.btnCerrar.addActionListener(
                e -> {
                    System.exit(0);
                });
        login.btnIniciar.addActionListener(e -> iniciarSesion());
    }

   public void iniciarSesion() {
    String usuario = login.txtUsuario.getText();
    String contra = login.txtContra.getText();
    if (usuario.isEmpty() || contra.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Completa los campos");
    } else {
        String contraHash = Hash.hashPassword(contra);
        Usuario user = daoUsuario.Login(usuario, contraHash);
        if (user != null) {
            RolEnum rol = user.getRol();
            login.setVisible(false);
            if (rol == RolEnum.ADMIN) {
                adminCtrl = new AdminController(user);
                adminCtrl.iniciar();
            } else if (rol == RolEnum.EMPLEADO) {
                empCtrl = new EmpleadoController(user);
                empCtrl.iniciar();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Datos inválidos:)");
            login.txtUsuario.setText(null);
            login.txtContra.setText(null);
        }
    }
}
}
