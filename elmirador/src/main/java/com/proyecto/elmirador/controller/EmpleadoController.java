package com.proyecto.elmirador.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.proyecto.elmirador.dao.DaoDepartamento;
import com.proyecto.elmirador.dao.DaoHabitante;
import com.proyecto.elmirador.dao.DaoRegistro;
import com.proyecto.elmirador.dao.DaoRelevo;
import com.proyecto.elmirador.dao.DaoVIsita;
import com.proyecto.elmirador.dao.impl.DaoDptoImpl;
import com.proyecto.elmirador.dao.impl.DaoHabitantesImpl;
import com.proyecto.elmirador.dao.impl.DaoRegistroImpl;
import com.proyecto.elmirador.dao.impl.DaoRelevoImpl;
import com.proyecto.elmirador.dao.impl.DaoVisitaImpl;
import com.proyecto.elmirador.model.Departamento;
import com.proyecto.elmirador.model.Habitante;
import com.proyecto.elmirador.model.Registro;
import com.proyecto.elmirador.model.Relevo;
import com.proyecto.elmirador.model.Usuario;
import com.proyecto.elmirador.model.Visita;
import com.proyecto.elmirador.view.FrmLogin;
import com.proyecto.elmirador.view.FrmRegistro;
import com.proyecto.elmirador.view.FrmRelevo;
import com.proyecto.elmirador.view.FrmVerDpto;
import com.proyecto.elmirador.view.FrmVisitas;

public class EmpleadoController implements Controller {

    private Usuario usuario;
    private FrmRelevo relevo;
    private FrmRegistro registro;
    private FrmLogin login;
    private Relevo relevoActual;
    private List<Departamento> departamentos = new ArrayList<>();
    private FrmVerDpto verDpto;
    private FrmVisitas visita;

    public EmpleadoController(Usuario usuario) {
        this.usuario = usuario;
        this.relevo = new FrmRelevo();
        this.registro = new FrmRegistro();
        this.login = new FrmLogin();
        this.verDpto = new FrmVerDpto();
        this.visita = new FrmVisitas();
    }

    @Override
    public void iniciar() {
        relevo.setLocationRelativeTo(null);
        relevo.setTitle("El Mirador");
         registro.setLocationRelativeTo(null);
        registro.setTitle("El Mirador");
         verDpto.setLocationRelativeTo(null);
        verDpto.setTitle("El Mirador");
         visita.setLocationRelativeTo(null);
        visita.setTitle("El Mirador");
        relevo.setVisible(true);
        relevo.buttonGroup.add(relevo.radioDia);
        relevo.buttonGroup.add(relevo.radioNoche);

        relevo.btnContinuar.addActionListener(e -> {
            String turno = null;

            if (relevo.radioDia.isSelected()) {
                turno = "Día 7:00-19:00";
            } else if (relevo.radioNoche.isSelected()) {
                turno = "Noche 19:00-07:00";
            }

            if (turno == null) {
                javax.swing.JOptionPane.showMessageDialog(relevo, "Debe seleccionar un turno (Día o Noche).");
                return;
            }

            Relevo nuevoRelevo = new Relevo();
            nuevoRelevo.setTurno(turno);
            nuevoRelevo.setFecha(java.time.LocalDate.now());
            nuevoRelevo.setIdPersonal(usuario);

            DaoRelevo dao = new DaoRelevoImpl();
            String resultado = dao.insert(nuevoRelevo);

            if (resultado == null) {
                javax.swing.JOptionPane.showMessageDialog(relevo, "Relevo registrado con éxito.");
                this.relevoActual = nuevoRelevo;
                registro.setVisible(true);
                registrarEventos();
                cargarDepartamentosEnCombo();
                cargarRegistrosEnTabla();
                mostrarDptos();
                relevo.dispose();

            } else {
                javax.swing.JOptionPane.showMessageDialog(relevo, "Error al registrar relevo: " + resultado);
            }

        });

        relevo.btnCerrar.addActionListener(e -> {
            login.setVisible(true);
            relevo.dispose();
        });
    }

    private void registrarEventos() {
        registro.btnRegistrar.addActionListener(e -> {
            String motivo = registro.txtMotivos.getText().trim();
            String ocurrencia = registro.txtOcurrencias.getText().trim();
            int index = registro.cboDpto.getSelectedIndex();

            if (motivo.isEmpty() || ocurrencia.isEmpty() || index < 0) {
                JOptionPane.showMessageDialog(registro, "Complete todos los campos.");
                return;
            }

            Departamento dptoSeleccionado = departamentos.get(index);
            Registro reg = new Registro();
            reg.setMotivo(motivo);
            reg.setOcurrencia(ocurrencia);
            reg.setIdDpto(dptoSeleccionado);
            reg.setIdRelevo(relevoActual);

            DaoRegistro dao = new DaoRegistroImpl();
            String res = dao.insert(reg);

            if (res == null) {
                JOptionPane.showMessageDialog(registro, "Registro guardado correctamente.");
                registro.txtMotivos.setText("");
                registro.txtOcurrencias.setText("");
                registro.cboDpto.setSelectedIndex(0);
                cargarRegistrosEnTabla();

            } else {
                JOptionPane.showMessageDialog(registro, "Error al registrar: " + res);
            }
        });

        registro.btnDepartamentos.addActionListener(e -> {
            verDpto.setVisible(true);
            mostrarDptos();
        });

        registro.btnvisita.addActionListener(e -> {
            visita.setVisible(true);
            cargarDepartamentosEnCombo();
            cargarVisitasEnTabla();

            visita.btnRegistrar.addActionListener(ev -> registrarVisita());
            visita.btnCerrar.addActionListener(ev -> visita.dispose());
        });
        registro.btnFinalizar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    registro,
                    "¿Está seguro de que desea salir del sistema?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void cargarRegistrosEnTabla() {
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

        registro.tblRegistros.setModel(modelo);
    }

    private void cargarDepartamentosEnCombo() {
        DaoDepartamento dao = new DaoDptoImpl();
        departamentos = dao.selectAll(0);
        registro.cboDpto.removeAllItems();
        for (Departamento d : departamentos) {
            registro.cboDpto.addItem(d.getNumDpto());
        }
        visita.cboDpto.removeAllItems();
        for (Departamento d : departamentos) {
            visita.cboDpto.addItem(d.getNumDpto());
        }
    }

    private void mostrarDptos() {
        cargarHabitantesEnTabla();
        verDpto.btnCerrar.addActionListener(e -> verDpto.dispose());
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

        verDpto.tblDpto.setModel(modelo);
        verDpto.tblDpto.getColumnModel().getColumn(0).setMinWidth(0);
        verDpto.tblDpto.getColumnModel().getColumn(0).setMaxWidth(0);
        verDpto.tblDpto.getColumnModel().getColumn(0).setWidth(0);
    }

    private void registrarVisita() {
        String nombres = visita.txtNom.getText().trim();
        String apellidos = visita.txtApe.getText().trim();
        String numeroDoc = visita.txtNum.getText().trim();
        String tipoDoc = (String) visita.cboTipo.getSelectedItem();
        int indexDpto = visita.cboDpto.getSelectedIndex();

        if (nombres.isEmpty() || apellidos.isEmpty() || numeroDoc.isEmpty() || tipoDoc == null || indexDpto < 0) {
            JOptionPane.showMessageDialog(visita, "Complete todos los campos.");
            return;
        }

        Visita nuevaVisita = new Visita();
        nuevaVisita.setNombres(nombres);
        nuevaVisita.setApellidos(apellidos);
        nuevaVisita.setNumDoc(numeroDoc);
        nuevaVisita.setTipoDoc(tipoDoc);
        nuevaVisita.setTiempo(LocalDateTime.now());
        nuevaVisita.setIdDpto(departamentos.get(indexDpto));

        DaoVIsita dao = new DaoVisitaImpl();
        String res = dao.insert(nuevaVisita);

        if (res == null) {
            JOptionPane.showMessageDialog(visita, "Visita registrada.");
            visita.txtNom.setText("");
            visita.txtApe.setText("");
            visita.txtNum.setText("");
            visita.cboTipo.setSelectedIndex(0);
            visita.cboDpto.setSelectedIndex(0);
            cargarVisitasEnTabla();
            visita.btnEliminar.addActionListener(e -> {
                int fila = visita.tblVisitas.getSelectedRow();

                if (fila == -1) {
                    JOptionPane.showMessageDialog(visita, "Seleccione una visita de la tabla para eliminar.");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(
                        visita,
                        "¿Está seguro de que desea eliminar esta visita?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION)
                    return;

                int idVisita = (int) visita.tblVisitas.getValueAt(fila, 0);

                DaoVIsita daov = new DaoVisitaImpl();
                Visita v = daov.delete(idVisita);

                if (v != null) {
                    JOptionPane.showMessageDialog(visita, "Visita eliminada correctamente.");
                    cargarVisitasEnTabla();
                } else {
                    JOptionPane.showMessageDialog(visita, "Error al eliminar la visita.");
                }
            });
        } else {
            JOptionPane.showMessageDialog(visita, "Error: " + res);
        }

    }

    private void cargarVisitasEnTabla() {
        DaoVIsita dao = new DaoVisitaImpl();
        List<Visita> lista = dao.selectAll(0);

        String[] columnas = { "ID", "Nombres", "Apellidos", "Tipo Doc", "Número Doc", "Fecha", "N° Dpto" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

        for (Visita v : lista) {
            Object[] fila = {
                    v.getId(),
                    v.getNombres(),
                    v.getApellidos(),
                    v.getTipoDoc(),
                    v.getNumDoc(),
                    v.getTiempo().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    v.getIdDpto().getNumDpto()
            };
            modelo.addRow(fila);
        }

        visita.tblVisitas.setModel(modelo);
    }

}
