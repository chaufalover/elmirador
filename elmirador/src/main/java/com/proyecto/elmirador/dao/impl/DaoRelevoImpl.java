package com.proyecto.elmirador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import com.proyecto.elmirador.dao.ConexionBD;
import com.proyecto.elmirador.dao.DaoRelevo;
import com.proyecto.elmirador.model.Relevo;

public class DaoRelevoImpl implements DaoRelevo {

    private ConexionBD conexion;

    public DaoRelevoImpl() {
        conexion = ConexionBD.getInstance();
    }

    @Override
    public String insert(Relevo dato) {
        String sql = "INSERT INTO relevos (turno, fecha, id_usuario) VALUES (?, ?, ?)";

        try (Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, dato.getTurno());
            ps.setTimestamp(2, Timestamp.valueOf(dato.getFecha().atStartOfDay()));
            ps.setInt(3, dato.getIdPersonal().getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        dato.setIdRelevo(idGenerado); 
                    }
                }
                return null; // todo ok
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return "No se pudo insertar el relevo.";
    }

    @Override
    public List<Relevo> selectAll(int dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAll'");
    }

    @Override
    public Relevo update(Relevo dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Relevo delete(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
