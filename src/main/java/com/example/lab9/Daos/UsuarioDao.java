package com.example.lab9.Daos;

import com.example.lab9.Beans.Rol;
import com.example.lab9.Beans.Usuario;

import java.sql.*;
import java.util.ArrayList;

public class UsuarioDao extends DaoBase{
    public ArrayList<Usuario> listarUsuarios() {
        ArrayList<Usuario> listarUsuarios = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees e \n"
                     + "left join jobs j on (j.job_id = e.job_id) \n"
                     + "left join departments d on (d.department_id = e.department_id)\n"
                     + "left  join employees m on (e.manager_id = m.employee_id)");) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                fetchUsuarioData(usuario, rs);

                listarUsuarios.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listarUsuarios;
    }

    public ArrayList<Usuario> listarUsuariosDocentes() {
        ArrayList<Usuario> listarUsuariosDocentes = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuario u \n"
                     + "left join rol r ON (u.idrol = r.idrol) \n"
                     + "WHERE u.idrol = 4");) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                fetchUsuarioData(usuario, rs);

                listarUsuariosDocentes.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listarUsuariosDocentes;
    }

    public ArrayList<Usuario> listarUsuariosDecanos() {
        ArrayList<Usuario> listarUsuariosDecanos = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuario u \n"
                     + "left join rol r ON (u.idrol = r.idrol) \n"
                     + "WHERE u.idrol = 3");) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                fetchUsuarioData(usuario, rs);

                listarUsuariosDecanos.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listarUsuariosDecanos;
    }

    public ArrayList<Usuario> listarUsuariosRectores() {
        ArrayList<Usuario> listarUsuariosRectores = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuario u \n"
                     + "left join rol r ON (u.idrol = r.idrol) \n"
                     + "WHERE u.idrol = 2");) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                fetchUsuarioData(usuario, rs);

                listarUsuariosRectores.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listarUsuariosRectores;
    }

    public ArrayList<Usuario> listarUsuariosAdmins() {
        ArrayList<Usuario> listarUsuariosAdmins = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuario u \n"
                     + "left join rol r ON (u.idrol = r.idrol) \n"
                     + "WHERE u.idrol = 1");) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                fetchUsuarioData(usuario, rs);

                listarUsuariosAdmins.add(usuario);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listarUsuariosAdmins;
    }

    public Usuario obtenerUsuario(int idUsuario) {

        Usuario usuario = null;

        String sql = "SELECT * FROM usuario u \n"
                + "left join rol r ON (u.idrol = r.idrol) \n"
                + "WHERE u.idusuario = ?";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery();) {

                if (rs.next()) {
                    usuario = new Usuario();
                    fetchUsuarioData(usuario, rs);

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return usuario;
    }

    public Usuario obtenerUsuarioDocente(int idUsuario) {

        Usuario usuario = null;

        String sql = "SELECT * FROM usuario u \n"
                + "left join rol r ON (u.idrol = r.idrol) \n"
                + "WHERE u.idrol = 3 and u.idusuario = ?";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery();) {

                if (rs.next()) {
                    usuario = new Usuario();
                    fetchUsuarioData(usuario, rs);

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return usuario;
    }

    public void guardarUsuario(Usuario usuario) throws SQLException {

        String sql = "INSERT INTO employees (first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            setUsuarioParams(pstmt, usuario);
            pstmt.executeUpdate();
        }
    }

    public void actualizarUsuario(Usuario usuario) throws SQLException {

        String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone_number = ?, "
                + "hire_date = ?, job_id = ?, salary = ?, commission_pct = ?, "
                + "manager_id = ?, department_id = ? WHERE employee_id = ?";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            setUsuarioParams(pstmt, usuario);
            pstmt.setInt(11, usuario.getIdUsuario());
            pstmt.executeUpdate();
        }
    }

    public void crearUsuario(Usuario usuario) throws SQLException {

        String sql = "Insert into usuario (nombre, correo, password, idrol, ultimo_ingreso, cantidad_ingresos, fecha_registro, fecha_edicion) values (?,?,SHA2(?,256),?,?,?,?,?)";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1,usuario.getNombre());
            pstmt.setString(2,usuario.getCorreo());
            pstmt.setString(3,usuario.getPassword());
            pstmt.setInt(4,usuario.getRol().getIdRol());
            pstmt.setDate(5,usuario.getUltimoIngreso());
            pstmt.setInt(6,usuario.getCantidadIngreso());
            pstmt.setDate(7,usuario.getFechaRegistro());
            pstmt.setDate(8,usuario.getFechaEdicion());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void borrarUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM usuario WHERE idusuario = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        }
    }

    public Usuario validarUsuarioPasswordHashed(String username, String password) {

        Usuario usuario = null;

        String sql = "SELECT * FROM usuario WHERE correo = ? AND password = SHA2(?,256)";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery();) {
                if (rs.next()) {
                    int idUsuario = rs.getInt(1);
                    usuario = this.obtenerUsuario(idUsuario);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return usuario;
    }



    private void setUsuarioParams(PreparedStatement pstmt, Usuario usuario) throws SQLException {
        pstmt.setString(1, usuario.getNombre());
        pstmt.setString(2, usuario.getCorreo());
        pstmt.setString(3, usuario.getPassword());
        pstmt.setDate(4, usuario.getUltimoIngreso());
        pstmt.setInt(5, usuario.getCantidadIngreso());
        pstmt.setDate(6, usuario.getFechaRegistro());
        pstmt.setDate(7, usuario.getFechaEdicion());

        if (usuario.getRol() == null) {
            pstmt.setNull(8, Types.INTEGER);
        } else {
            pstmt.setInt(8, usuario.getRol().getIdRol());
        }
    }


    public ArrayList<Usuario> buscarUsuarioPorNombre(String nombre) {

        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        String sql = "SELECT * FROM employees e \n"
                + "left join jobs j ON (j.job_id = e.job_id) \n"
                + "left join departments d ON (d.department_id = e.department_id)\n"
                + "left  join employees m ON (e.manager_id = m.employee_id)\n"
                + "WHERE e.first_name = ? OR e.last_name = ?";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            pstmt.setString(2, nombre);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    fetchUsuarioData(usuario, rs);

                    listaUsuarios.add(usuario);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaUsuarios;
    }


    private void fetchUsuarioData(Usuario usuario, ResultSet rs) throws SQLException {
        usuario.setIdUsuario(rs.getInt(1));
        usuario.setNombre(rs.getString(2));
        usuario.setCorreo(rs.getString(3));
        usuario.setPassword(rs.getString(4));
        usuario.setUltimoIngreso(rs.getDate(5));
        usuario.setCantidadIngreso(rs.getInt(6));
        usuario.setFechaRegistro(rs.getDate(7));
        usuario.setFechaEdicion(rs.getDate(8));

        Rol rol = new Rol();
        rol.setIdRol(rs.getInt(7));
        rol.setNombre(rs.getString("nombre"));
        usuario.setRol(rol);

    }
}
