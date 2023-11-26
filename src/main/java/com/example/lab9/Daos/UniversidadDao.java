package com.example.lab9.Daos;

import com.example.lab9.Beans.Rol;
import com.example.lab9.Beans.Universidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UniversidadDao extends DaoBase{

    public ArrayList<Universidad> listarUniversidades() {
        ArrayList<Universidad> listarUniversidades = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM departments d " +
                     "left join employees e on d.manager_id = e.employee_id " +
                     "left join locations l on d.location_id = l.location_id");) {
            Universidad universidad;
            while (rs.next()) {
                universidad = parseResultSet(rs);
                listarUniversidades.add(universidad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listarUniversidades;
    }

    public Universidad obtenerUniversidad(int idUniversidad) {

        Universidad universidad = null;

        String sql = "SELECT * FROM departments d " +
                "left join employees e on d.manager_id = e.employee_id " +
                "left join locations l on d.location_id = l.location_id " +
                "WHERE d.department_id = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, idUniversidad);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    universidad = parseResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return universidad;
    }

    private Universidad parseResultSet(ResultSet rs) {
        Universidad universidad = new Universidad();
        try {
            universidad.setIdUniversidad(rs.getInt(1));
            universidad.setNombre(rs.getString(2));
            universidad.setLogoURL(rs.getString(3));
            universidad.setFechaRegistro(rs.getDate(4));
            universidad.setFechaEdicion(rs.getDate(5));

            if (rs.getInt("r.idAdministrador") != 0) {
                Rol administrador = new Rol();
                administrador.setIdRol(rs.getInt("idrol"));
                administrador.setNombre(rs.getString("nombre"));
                universidad.setIdAdministrador(administrador);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return universidad;
    }

    public void crearUniversidad(int idUniversidad, String nombre, String logoURL, int idAdministrador, Date fechaRegistro,Date fechaEdicion) {

        String sql = "INSERT INTO departments (`department_id`, `department_name`, `manager_id`, `location_id`) "
                + "VALUES (?,?,?,?)";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idUniversidad);
            pstmt.setString(2, nombre);
            pstmt.setString(3, logoURL);

            if (idAdministrador == 0) {
                pstmt.setNull(4, Types.INTEGER);
            } else {
                pstmt.setInt(4, idAdministrador);
            }

            pstmt.setDate(5, fechaRegistro);
            pstmt.setDate(6, fechaEdicion);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void actualizarUniversidad(int idUniversidad, String nombre, String logoURL, int idAdministrador, Date fechaRegistro,Date fechaEdicion) {

        String sql = "UPDATE departments SET department_name = ?, manager_id = ?, location_id = ? "
                + "WHERE department_id = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idUniversidad);
            pstmt.setString(2, nombre);
            pstmt.setString(3, logoURL);

            if (idAdministrador == 0) {
                pstmt.setNull(4, Types.INTEGER);
            } else {
                pstmt.setInt(4, idAdministrador);
            }

            pstmt.setDate(5, fechaRegistro);
            pstmt.setDate(6, fechaEdicion);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void borrarUniversidad(int idUniversidad) {

        String sql = "DELETE FROM universidad WHERE iduniversidad = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUniversidad);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UniversidadDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Universidad> buscarUniversidadPorNombre(String nombre) {

        ArrayList<Universidad> listaUniversidad = new ArrayList<>();

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
                    Universidad universidad = new Universidad();
                    fetchUniversidadData(universidad, rs);

                    listaUniversidad.add(universidad);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaUniversidad;
    }

    private void fetchUniversidadData(Universidad universidad, ResultSet rs) throws SQLException {
        universidad.setIdUniversidad(rs.getInt(1));
        universidad.setNombre(rs.getString(2));
        universidad.setLogoURL(rs.getString(3));

        Rol rol = new Rol();
        rol.setIdRol(rs.getInt(4));
        rol.setNombre(rs.getString("nombre"));
        universidad.setIdAdministrador(rol);

        universidad.setFechaRegistro(rs.getDate(5));
        universidad.setFechaEdicion(rs.getDate(6));

    }


}
