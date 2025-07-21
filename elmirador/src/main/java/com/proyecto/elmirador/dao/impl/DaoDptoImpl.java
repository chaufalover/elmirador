package com.proyecto.elmirador.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.proyecto.elmirador.dao.ConexionBD;
import com.proyecto.elmirador.dao.DaoDepartamento;
import com.proyecto.elmirador.model.Departamento;

public class DaoDptoImpl implements DaoDepartamento {

    private ConexionBD conexion;

    public DaoDptoImpl() {
        conexion = ConexionBD.getInstance();
    }

    @Override
    public String insert(Departamento dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public List<Departamento> selectAll(int dato) {
        List<Departamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM departamentos";
        try (Statement stmt = conexion.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Departamento d = new Departamento();
                d.setIdDpto(rs.getInt("id_departamento"));
                d.setNumDpto(rs.getString("numero_dpto"));
                lista.add(d);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener departamentos: " + e.getMessage());
        }
        return lista;
    }

    

    @Override
    public Departamento update(Departamento dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Departamento delete(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
