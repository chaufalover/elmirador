package com.proyecto.elmirador.dao.impl;

import com.proyecto.elmirador.dao.ConexionBD;
import com.proyecto.elmirador.dao.DaoUsuario;
import com.proyecto.elmirador.model.Usuario;
import com.proyecto.elmirador.util.RolEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DaoUsuarioImpl implements DaoUsuario {
    private String msg;
    private ConexionBD conexion;

    public DaoUsuarioImpl() {
        conexion = ConexionBD.getInstance();
    }

    @Override
    public Usuario Login(String usuario, String clave) {
        Usuario user = null;
        String sql = """
                SELECT*FROM
                    usuarios
                WHERE
                    usuario = ?
                AND
                    clave = ?
                """;
        try (Connection cn = conexion.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, clave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new Usuario();
                    user.setId(rs.getInt("id"));
                    user.setNomUsuario(rs.getString("usuario"));
                    user.setClave(rs.getString("clave"));
                    user.setRol(RolEnum.valueOf(rs.getString("rol")));
                } else {
                    String msg = "Ingrese datos validos";
                }
            } catch (Exception e) {
                e.getMessage();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return user;
    }

    @Override
    public String insert(Usuario dato) {

        String sql = """
                INSERT INTO usuarios
                    (nombres, apellidos, numero_documento, tipo_documento, usuario, rol, clave)
                VALUES
                    (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection c = conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql);) {
            ps.setString(1, dato.getNombres());
            ps.setString(2, dato.getApellidos());
            ps.setString(3, dato.getNumDoc());
            ps.setString(4, dato.getTipoDoc());
            ps.setString(5, dato.getNomUsuario());
            ps.setString(6, dato.getRol().toString());
            ps.setString(7, dato.getClave());
            return (ps.executeUpdate() == 0) ? msg = "Error al insertar" : null;
        } catch (Exception e) {
            msg = e.getMessage();
        }
        System.out.println(msg);
        return msg;
    }

    @Override
    public List<Usuario> selectAll(int dato) {
        List<Usuario> lista = new ArrayList<>();
        String sql = """
                    SELECT id, nombres, apellidos, numero_documento, tipo_documento, usuario, rol
                    FROM usuarios
                """;

        try (Connection cn = conexion.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario user = new Usuario();
                    user.setId(rs.getInt("id"));
                    user.setNombres(rs.getString("nombres"));
                    user.setApellidos(rs.getString("apellidos"));
                    user.setNumDoc(rs.getString("numero_documento"));
                    user.setTipoDoc(rs.getString("tipo_documento"));
                    user.setNomUsuario(rs.getString("usuario"));
                    user.setRol(RolEnum.valueOf(rs.getString("rol")));
                    lista.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return lista;
    }

    @Override
    public Usuario update(Usuario dato) {
        boolean actualizarClave = dato.getClave() != null && !dato.getClave().isEmpty();

        String sql;
        if (actualizarClave) {
            sql = """
                        UPDATE usuarios
                        SET nombres = ?, apellidos = ?, tipo_documento = ?, numero_documento = ?, usuario = ?, rol = ?, clave = ?
                        WHERE id = ?
                    """;
        } else {
            sql = """
                        UPDATE usuarios
                        SET nombres = ?, apellidos = ?, tipo_documento = ?, numero_documento = ?, usuario = ?, rol = ?
                        WHERE id = ?
                    """;
        }

        try (Connection c = conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, dato.getNombres());
            ps.setString(2, dato.getApellidos());
            ps.setString(3, dato.getTipoDoc());
            ps.setString(4, dato.getNumDoc());
            ps.setString(5, dato.getNomUsuario());
            ps.setString(6, dato.getRol().toString());

            if (actualizarClave) {
                ps.setString(7, dato.getClave());
                ps.setInt(8, dato.getId());
            } else {
                ps.setInt(7, dato.getId());
            }

            int result = ps.executeUpdate();
            if (result > 0) {
                System.out.println("USUARIO ACTUALIZADO: ID = " + dato.getId());
                return dato;
            } else {
                System.out.println("NO SE ACTUALIZÓ NINGUNA FILA");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Usuario delete(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
