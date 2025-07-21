package com.proyecto.elmirador.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.proyecto.elmirador.dao.ConexionBD;
import com.proyecto.elmirador.dao.DaoRegistro;
import com.proyecto.elmirador.model.Departamento;
import com.proyecto.elmirador.model.Registro;
import com.proyecto.elmirador.model.Relevo;
import com.proyecto.elmirador.model.Usuario;

public class DaoRegistroImpl implements DaoRegistro {

    private ConexionBD conexion;

    public DaoRegistroImpl() {
        conexion = ConexionBD.getInstance();
    }

    @Override
    public String insert(Registro dato) {
        String sql = "INSERT INTO registros (motivo, ocurrencia, id_departamento, id_relevo) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dato.getMotivo());
            ps.setString(2, dato.getOcurrencia());
            ps.setInt(3, dato.getIdDpto().getIdDpto());
            ps.setInt(4, dato.getIdRelevo().getIdRelevo());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                return null; 
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al insertar: " + e.getMessage();
        }

        return "No se pudo insertar el registro";
    }

    @Override
    public List<Registro> selectAll(int dato) {
        List<Registro> lista = new ArrayList<>();

        String sql = """
                    SELECT r.id_registro, r.tiempo, r.motivo, r.ocurrencia,
                           d.id_departamento, d.numero_dpto,
                           rel.id_relevo,
                           u.id, u.nombres
                    FROM registros r
                    JOIN departamentos d ON r.id_departamento = d.id_departamento
                    JOIN relevos rel ON r.id_relevo = rel.id_relevo
                    JOIN usuarios u ON rel.id_usuario = u.id
                    ORDER BY r.tiempo DESC
                """;

        try (Connection conn = conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Registro registro = new Registro();
                registro.setIdRegistro(rs.getString("id_registro"));
                registro.setTiempo(rs.getTimestamp("tiempo").toLocalDateTime());
                registro.setMotivo(rs.getString("motivo"));
                registro.setOcurrencia(rs.getString("ocurrencia"));

                Departamento dpto = new Departamento();
                dpto.setIdDpto(rs.getInt("id_departamento"));
                dpto.setNumDpto(rs.getString("numero_dpto"));
                registro.setIdDpto(dpto);

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNombres(rs.getString("nombres"));

                Relevo relevo = new Relevo();
                relevo.setIdRelevo(rs.getInt("id_relevo"));
                relevo.setIdPersonal(usuario);
                registro.setIdRelevo(relevo);

                lista.add(registro);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public Registro update(Registro dato) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Registro delete(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
