package com.proyecto.elmirador.controller;

import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.proyecto.elmirador.dao.DaoDepartamento;
import com.proyecto.elmirador.dao.DaoHabitante;
import com.proyecto.elmirador.dao.DaoRegistro;
import com.proyecto.elmirador.dao.DaoUsuario;
import com.proyecto.elmirador.dao.impl.DaoDptoImpl;
import com.proyecto.elmirador.dao.impl.DaoHabitantesImpl;
import com.proyecto.elmirador.dao.impl.DaoRegistroImpl;
import com.proyecto.elmirador.dao.impl.DaoUsuarioImpl;
import com.proyecto.elmirador.model.Departamento;
import com.proyecto.elmirador.model.Habitante;
import com.proyecto.elmirador.model.Registro;
import com.proyecto.elmirador.model.Usuario;
import com.proyecto.elmirador.util.Hash;
import com.proyecto.elmirador.util.RolEnum;
import com.proyecto.elmirador.view.FrmAdmin;
import com.proyecto.elmirador.view.FrmAdminDpto;
import com.proyecto.elmirador.view.FrmLogin;
import com.proyecto.elmirador.view.FrmRegistros;
import com.proyecto.elmirador.view.FrmUsuarios;

public class AdminController implements Controller {

    private FrmAdmin admin;
    private Usuario usuario;
    private FrmUsuarios usuarios;
    private FrmAdminDpto dpto;
    private Usuario usuarioAEditar = null;
    private Habitante habitanteAEditar = null;
    private FrmRegistros registros;
    private FrmLogin login;
    private LoginController loginController;

    public AdminController(Usuario usuario) {
        this.usuario = usuario;
        this.admin = new FrmAdmin();
        this.usuarios = new FrmUsuarios();
        this.dpto = new FrmAdminDpto();
        this.registros = new FrmRegistros();
        this.login = new FrmLogin();

    }

    private void limpiarFormulario() {
        usuarios.txtNombres.setText("");
        usuarios.txtApellidos.setText("");
        usuarios.txtContra.setText("");
        usuarios.txtUsuario.setText("");
        usuarios.txtNum.setText("");
        usuarios.cboTipo.setSelectedIndex(0);
    }

    private void limpiarActionListeners(JButton boton) {
        for (var al : boton.getActionListeners()) {
            boton.removeActionListener(al);
        }
    }

    @Override
    public void iniciar() {
        admin.setLocationRelativeTo(null);
        admin.setTitle("El Mirador");
         usuarios.setLocationRelativeTo(null);
         usuarios.setTitle("El Mirador");
          dpto.setLocationRelativeTo(null);
          dpto.setTitle("El Mirador");
           registros.setLocationRelativeTo(null);
           registros.setTitle("El Mirador");
        
        if (admin == null) {
            admin = new FrmAdmin();
        }
        admin.setVisible(true);
        admin.btnUsuarios.addActionListener(e -> registroUsuarios());
        admin.btnDptos.addActionListener(e -> registroHabitantes());
        admin.BtnRegistro.addActionListener(e -> verRegistros());
        admin.btnSalir.addActionListener(e-> {
              if (loginController == null) {
                loginController = new LoginController(new DaoUsuarioImpl(), new FrmLogin());
            }
            loginController.iniciar();
            admin.dispose();

        });
    }

    public void registroUsuarios() {
        usuarios.setVisible(true);
        admin.setVisible(false);
        cargarUsuariosEnTabla();
        limpiarFormulario();

        limpiarActionListeners(usuarios.btnRegistrar);

        usuarios.btnRegistrar.addActionListener(e -> {
            DaoUsuario daoUsuario = new DaoUsuarioImpl();

            String nombres = usuarios.txtNombres.getText();
            String apellidos = usuarios.txtApellidos.getText();
            String tipoDoc = usuarios.cboTipo.getSelectedItem().toString();
            String numDoc = usuarios.txtNum.getText();
            String nomUsuario = usuarios.txtUsuario.getText();
            String clave = new String(usuarios.txtContra.getPassword());

            if (usuarioAEditar == null) {
                String claveHash = Hash.hashPassword(clave);
                Usuario nuevoUsuario = new Usuario();
                nuevoUsuario.setNombres(nombres);
                nuevoUsuario.setApellidos(apellidos);
                nuevoUsuario.setTipoDoc(tipoDoc);
                nuevoUsuario.setNumDoc(numDoc);
                nuevoUsuario.setNomUsuario(nomUsuario);
                nuevoUsuario.setClave(claveHash);
                nuevoUsuario.setRol(RolEnum.EMPLEADO);

                String resultado = daoUsuario.insert(nuevoUsuario);
                if (resultado == null) {
                    JOptionPane.showMessageDialog(usuarios, "Usuario registrado correctamente");
                    limpiarFormulario();
                    cargarUsuariosEnTabla();
                } else {
                    JOptionPane.showMessageDialog(usuarios, "Error: " + resultado);
                }
            } else {
                usuarioAEditar.setNombres(nombres);
                usuarioAEditar.setApellidos(apellidos);
                usuarioAEditar.setTipoDoc(tipoDoc);
                usuarioAEditar.setNumDoc(numDoc);
                usuarioAEditar.setNomUsuario(nomUsuario);
                usuarioAEditar.setRol(RolEnum.EMPLEADO);

                if (!clave.isEmpty()) {
                    String claveHash = Hash.hashPassword(clave);
                    usuarioAEditar.setClave(claveHash);
                }

                Usuario actualizado = daoUsuario.update(usuarioAEditar);
                if (actualizado != null) {
                    JOptionPane.showMessageDialog(usuarios, "Usuario actualizado con éxito");
                    limpiarFormulario();
                    cargarUsuariosEnTabla();
                    usuarioAEditar = null;
                } else {
                    JOptionPane.showMessageDialog(usuarios, "Error al actualizar");
                }
            }
        });

        usuarios.btnActualizar.addActionListener(e -> {
            if (usuarioAEditar == null) {
                JOptionPane.showMessageDialog(usuarios, "Primero seleccione un usuario de la tabla para actualizar.");
                return;
            }

            DaoUsuario daoUsuario = new DaoUsuarioImpl();

            String nombres = usuarios.txtNombres.getText();
            String apellidos = usuarios.txtApellidos.getText();
            String tipoDoc = usuarios.cboTipo.getSelectedItem().toString();
            String numDoc = usuarios.txtNum.getText();
            String nomUsuario = usuarios.txtUsuario.getText();
            String clave = new String(usuarios.txtContra.getPassword());

            usuarioAEditar.setNombres(nombres);
            usuarioAEditar.setApellidos(apellidos);
            usuarioAEditar.setTipoDoc(tipoDoc);
            usuarioAEditar.setNumDoc(numDoc);
            usuarioAEditar.setNomUsuario(nomUsuario);
            usuarioAEditar.setRol(RolEnum.EMPLEADO);

            if (!clave.isEmpty()) {
                usuarioAEditar.setClave(Hash.hashPassword(clave));
            }

            Usuario actualizado = daoUsuario.update(usuarioAEditar);
            if (actualizado != null) {
                JOptionPane.showMessageDialog(usuarios, "Usuario actualizado con éxito");
                limpiarFormulario();
                cargarUsuariosEnTabla();
                usuarioAEditar = null;
                usuarios.tblUsuarios.clearSelection();
            } else {
                JOptionPane.showMessageDialog(usuarios, "Error al actualizar el usuario.");
            }
        });

        usuarios.tblUsuarios.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int fila = usuarios.tblUsuarios.getSelectedRow();
                if (fila >= 0) {
                    int id = Integer.parseInt(usuarios.tblUsuarios.getValueAt(fila, 0).toString());

                    DaoUsuario dao = new DaoUsuarioImpl();
                    List<Usuario> lista = dao.selectAll(0);
                    usuarioAEditar = lista.stream()
                            .filter(u -> u.getId() == id)
                            .findFirst()
                            .orElse(null);

                    if (usuarioAEditar != null) {
                        usuarios.txtNombres.setText(usuarioAEditar.getNombres());
                        usuarios.txtApellidos.setText(usuarioAEditar.getApellidos());
                        usuarios.cboTipo.setSelectedItem(usuarioAEditar.getTipoDoc());
                        usuarios.txtNum.setText(usuarioAEditar.getNumDoc());
                        usuarios.txtUsuario.setText(usuarioAEditar.getNomUsuario());
                        usuarios.txtContra.setText(""); // No mostrar clave
                    }
                }
            }
        });

        usuarios.btnCerrar.addActionListener(e -> {
            admin.setVisible(true);
            usuarios.setVisible(false);
            limpiarFormulario();
            usuarioAEditar = null;
        });
    }

    private void cargarUsuariosEnTabla() {
        DaoUsuario daoUsuario = new DaoUsuarioImpl();
        List<Usuario> lista = daoUsuario.selectAll(0);

        String[] columnas = { "ID", "Nombres", "Apellidos", "Tipo Documento", "Número Documento", "Usuario", "Rol" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Usuario u : lista) {
            Object[] fila = {
                    u.getId(),
                    u.getNombres(),
                    u.getApellidos(),
                    u.getTipoDoc(),
                    u.getNumDoc(),
                    u.getNomUsuario(),
                    u.getRol().toString()
            };
            modelo.addRow(fila);
        }

        usuarios.tblUsuarios.setModel(modelo);
        usuarios.tblUsuarios.getColumnModel().getColumn(0).setMinWidth(0);
        usuarios.tblUsuarios.getColumnModel().getColumn(0).setMaxWidth(0);
        usuarios.tblUsuarios.getColumnModel().getColumn(0).setWidth(0);
    }

    public void registroHabitantes() {
        dpto.setVisible(true);
        admin.setVisible(false);
        cargarHabitantesEnTabla();
        cargarDepartamentosEnCombo();
        limpiarFormularioHabitante();

        limpiarActionListeners(dpto.btnRegistrar);

        dpto.btnRegistrar.addActionListener(e -> {
            DaoHabitante dao = new DaoHabitantesImpl();

            String tipo = dpto.txtTipo.getText();
            String nombres = dpto.txtNombres.getText();
            String apellidos = dpto.txtApe.getText();
            String telefono = dpto.txtTelefonos.getText();
            Departamento dptoSeleccionado = (Departamento) dpto.cboDpto.getSelectedItem();

            if (tipo.isBlank() || nombres.isBlank() || apellidos.isBlank() || telefono.isBlank() || dptoSeleccionado == null) {
                JOptionPane.showMessageDialog(dpto, "Complete todos los campos.");
                return;
            }

            if (habitanteAEditar == null) {
                Habitante h = new Habitante();
                h.setTipo(tipo);
                h.setNombres(nombres);
                h.setApellidos(apellidos);
                h.setTelefono(telefono);
                h.setIdDpto(dptoSeleccionado);

                String res = dao.insert(h);
                if (res == null) {
                    JOptionPane.showMessageDialog(dpto, "Habitante registrado correctamente");
                    limpiarFormularioHabitante();
                    cargarHabitantesEnTabla();
                } else {
                    JOptionPane.showMessageDialog(dpto, "Error: " + res);
                }
            } else {
                habitanteAEditar.setTipo(tipo);
                habitanteAEditar.setNombres(nombres);
                habitanteAEditar.setApellidos(apellidos);
                habitanteAEditar.setTelefono(telefono);
                habitanteAEditar.setIdDpto(dptoSeleccionado);

                Habitante actualizado = dao.update(habitanteAEditar);
                if (actualizado != null) {
                    JOptionPane.showMessageDialog(dpto, "Habitante actualizado con éxito");
                    limpiarFormularioHabitante();
                    cargarHabitantesEnTabla();
                    habitanteAEditar = null;
                } else {
                    JOptionPane.showMessageDialog(dpto, "Error al actualizar");
                }
            }
        });

        dpto.btnActualizar.addActionListener(e -> {
            if (habitanteAEditar == null) {
                JOptionPane.showMessageDialog(dpto, "Seleccione un habitante para actualizar.");
                return;
            }

            String tipo = dpto.txtTipo.getText();
            String nombres = dpto.txtNombres.getText();
            String telefono = dpto.txtTelefonos.getText();
            Departamento dptoSeleccionado = (Departamento) dpto.cboDpto.getSelectedItem();

            habitanteAEditar.setTipo(tipo);
            habitanteAEditar.setNombres(nombres);
            habitanteAEditar.setTelefono(telefono);
            habitanteAEditar.setIdDpto(dptoSeleccionado);

            DaoHabitante dao = new DaoHabitantesImpl();
            Habitante actualizado = dao.update(habitanteAEditar);

            if (actualizado != null) {
                JOptionPane.showMessageDialog(dpto, "Habitante actualizado");
                limpiarFormularioHabitante();
                cargarHabitantesEnTabla();
                habitanteAEditar = null;
                dpto.tblDptos.clearSelection();
            } else {
                JOptionPane.showMessageDialog(dpto, "Error al actualizar el habitante.");
            }
        });

        dpto.tblDptos.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int fila = dpto.tblDptos.getSelectedRow();
                if (fila >= 0) {
                    int id = Integer.parseInt(dpto.tblDptos.getValueAt(fila, 0).toString());

                    DaoHabitante dao = new DaoHabitantesImpl();
                    List<Habitante> lista = dao.selectAll(0);
                    habitanteAEditar = lista.stream()
                            .filter(h -> h.getIdHabitante() == id)
                            .findFirst()
                            .orElse(null);

                    if (habitanteAEditar != null) {
                        dpto.txtTipo.setText(habitanteAEditar.getTipo());
                        dpto.txtNombres.setText(habitanteAEditar.getNombres());
                        dpto.txtTelefonos.setText(habitanteAEditar.getTelefono());
                        dpto.cboDpto.setSelectedItem(habitanteAEditar.getIdDpto());
                    }
                }
            }
        });

        dpto.btnCerrar.addActionListener(e -> {
            admin.setVisible(true);
            dpto.setVisible(false);
            limpiarFormularioHabitante();
            habitanteAEditar = null;
        });

        dpto.btnElimnar.addActionListener(e -> {
            int fila = dpto.tblDptos.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(dpto, "Seleccione un habitante para eliminar.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(dpto, "¿Está seguro de eliminar este habitante?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            int id = Integer.parseInt(dpto.tblDptos.getValueAt(fila, 0).toString());

            DaoHabitante dao = new DaoHabitantesImpl();
            Habitante eliminado = dao.delete(id);

            if (eliminado != null) {
                JOptionPane.showMessageDialog(dpto, "Habitante eliminado correctamente.");
                cargarHabitantesEnTabla();
                limpiarFormularioHabitante();
                habitanteAEditar = null;
            } else {
                JOptionPane.showMessageDialog(dpto, "Error al eliminar el habitante.");
            }
        });

    }

    private void limpiarFormularioHabitante() {
        dpto.txtTipo.setText("");
        dpto.txtNombres.setText("");
        dpto.txtTelefonos.setText("");
        dpto.cboDpto.setSelectedIndex(0);
    }

    private void cargarDepartamentosEnCombo() {
        DaoDepartamento dao = new DaoDptoImpl();
        List<Departamento> lista = dao.selectAll(0);
        dpto.cboDpto.removeAllItems();
        for (Departamento d : lista) {
            dpto.cboDpto.addItem(d);
        }
    }

    private void cargarHabitantesEnTabla() {
        DaoHabitante dao = new DaoHabitantesImpl();
        List<Habitante> lista = dao.selectAll(0);

        String[] columnas = { "ID", "Tipo", "Apellidos", "Nombres", "Teléfono", "N° Dpto" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Habitante h : lista) {
            Object[] fila = {
                    h.getIdHabitante(),
                    h.getTipo(),
                    h.getApellidos(),
                    h.getNombres(),
                    h.getTelefono(),
                    h.getIdDpto().getNumDpto()
            };
            modelo.addRow(fila);
        }

        dpto.tblDptos.setModel(modelo);
        dpto.tblDptos.getColumnModel().getColumn(0).setMinWidth(0);
        dpto.tblDptos.getColumnModel().getColumn(0).setMaxWidth(0);
        dpto.tblDptos.getColumnModel().getColumn(0).setWidth(0);
    }

    public void verRegistros(){
        admin.setVisible(false);
        registros.setVisible(true);
        DaoRegistro dao = new DaoRegistroImpl();
        List<Registro> lista = dao.selectAll(0);

        String[] columnas = { "Tiempo", "Departamento", "Motivo", "Ocurrencia", "Usuario" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Registro r : lista) {
            Object[] fila = {
                    r.getTiempo().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    r.getIdDpto().getNumDpto(),
                    r.getMotivo(),
                    r.getOcurrencia(),
                    r.getIdRelevo().getIdPersonal().getNombres()
            };
            modelo.addRow(fila);
        }

        registros.tblRegistros.setModel(modelo);
        registros.btnCerrar.addActionListener(e->{
            registros.dispose();
            admin.setVisible(true);
        });
    }

}
