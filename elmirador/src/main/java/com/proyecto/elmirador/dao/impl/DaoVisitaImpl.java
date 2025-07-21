package com.proyecto.elmirador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.proyecto.elmirador.dao.ConexionBD;
import com.proyecto.elmirador.dao.DaoVIsita;
import com.proyecto.elmirador.model.Departamento;
import com.proyecto.elmirador.model.Visita;

public class DaoVisitaImpl implements DaoVIsita {

    private ConexionBD conexion;

    public DaoVisitaImpl() {
        conexion = ConexionBD.getInstance();
    }

    @Override
    public String insert(Visita dato) {
        String sql = "INSERT INTO visitas (nombres, apellidos, tipo_documento, numero_documento, fecha, id_departamento) "
                +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dato.getNombres());
            ps.setString(2, dato.getApellidos());
            ps.setString(3, dato.getTipoDoc());
            ps.setString(4, dato.getNumDoc());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(6, dato.getIdDpto().getIdDpto());
            ps.executeUpdate();
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    public List<Visita> selectAll(int dato) {
        List<Visita> lista = new ArrayList<>();
        String sql = "SELECT v.id_visita, v.nombres, v.apellidos, v.tipo_documento, v.numero_documento, v.fecha, " +
                "d.id_departamento, d.numero_dpto " +
                "FROM visitas v INNER JOIN departamentos d ON v.id_departamento = d.id_departamento";

        try (Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Visita v = new Visita();
                v.setId(rs.getInt("id_visita"));
                v.setNombres(rs.getString("nombres"));
                v.setApellidos(rs.getString("apellidos"));
                v.setTipoDoc(rs.getString("tipo_documento"));
                v.setNumDoc(rs.getString("numero_documento"));
                v.setTiempo(rs.getTimestamp("fecha").toLocalDateTime());

                Departamento d = new Departamento();
                d.setIdDpto(rs.getInt("id_departamento"));
                d.setNumDpto(rs.getString("numero_dpto"));

                v.setIdDpto(d);
                lista.add(v);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Visita update(Visita dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Visita delete(Integer id) {
        String sql = "DELETE FROM visitas WHERE id_visita = ?";

        try (Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                Visita v = new Visita();
                v.setId(id);
                return v; 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; 
    }

}
