package com.proyecto.elmirador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.proyecto.elmirador.dao.ConexionBD;
import com.proyecto.elmirador.dao.DaoHabitante;
import com.proyecto.elmirador.model.Departamento;
import com.proyecto.elmirador.model.Habitante;

public class DaoHabitantesImpl implements DaoHabitante {

    private ConexionBD conexion;

    public DaoHabitantesImpl() {
        conexion = ConexionBD.getInstance();
    }

    @Override
    public String insert(Habitante dato) {
        String sql = "INSERT INTO habitantes (tipo, apellidos, nombres, telefono, id_departamento) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dato.getTipo());
            ps.setString(2, dato.getApellidos());
            ps.setString(3, dato.getNombres());
            ps.setString(4, dato.getTelefono());
            ps.setInt(5, dato.getIdDpto().getIdDpto());

            ps.executeUpdate();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public List<Habitante> selectAll(int dato) {
        List<Habitante> lista = new ArrayList<>();
        String sql = """
            SELECT h.*, d.numero_dpto
            FROM habitantes h
            JOIN departamentos d ON h.id_departamento = d.id_departamento
        """;

        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Habitante h = new Habitante();
                h.setIdHabitante(rs.getInt("id_habitantes"));
                h.setTipo(rs.getString("tipo"));
                h.setApellidos(rs.getString("apellidos"));
                h.setNombres(rs.getString("nombres"));
                h.setTelefono(rs.getString("telefono"));

                Departamento d = new Departamento();
                d.setIdDpto(rs.getInt("id_departamento"));
                d.setNumDpto(rs.getString("numero_dpto"));

                h.setIdDpto(d); 

                lista.add(h);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Habitante update(Habitante dato) {
        String sql = "UPDATE habitantes SET tipo = ?, apellidos = ?, nombres = ?, telefono = ?, id_departamento = ? WHERE id_habitantes = ?";

        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dato.getTipo());
            ps.setString(2, dato.getApellidos());
            ps.setString(3, dato.getNombres());
            ps.setString(4, dato.getTelefono());
            ps.setInt(5, dato.getIdDpto().getIdDpto());
            ps.setInt(6, dato.getIdHabitante());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                return dato;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Habitante delete(Integer id) {
        String sql = "DELETE FROM habitantes WHERE id_habitantes = ?";

        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                Habitante h = new Habitante();
                h.setIdHabitante(id);
                return h;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
