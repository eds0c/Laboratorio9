package com.example.lab9.Daos;

import com.example.lab9.Beans.Facultad;
import com.example.lab9.Beans.Rol;
import com.example.lab9.Beans.Universidad;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacultadDao extends DaoBase{

    public ArrayList<Facultad> listarFacultades() {
        ArrayList<Facultad> listarFacultades = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM departments d " +
                     "left join employees e on d.manager_id = e.employee_id " +
                     "left join locations l on d.location_id = l.location_id");) {
            Facultad facultad;
            while (rs.next()) {
                facultad = parseResultSet(rs);
                listarFacultades.add(facultad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listarFacultades;
    }

    public Facultad obtenerFacultad(int idFacultad) {

        Facultad facultad = null;

        String sql = "SELECT * FROM departments d " +
                "left join employees e on d.manager_id = e.employee_id " +
                "left join locations l on d.location_id = l.location_id " +
                "WHERE d.department_id = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, idFacultad);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    facultad = parseResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return facultad;
    }

    private Facultad parseResultSet(ResultSet rs) {
        Facultad facultad = new Facultad();
        try {
            facultad.setIdFacultad(rs.getInt(1));
            facultad.setNombre(rs.getString(2));
            facultad.setFechaRegistro(rs.getDate(3));
            facultad.setFechaEdicion(rs.getDate(4));

            if (rs.getInt("u.idUniversidad") != 0) {
                Universidad universidad = new Universidad();
                universidad.setIdUniversidad(rs.getInt("iduniversidad"));
                universidad.setNombre(rs.getString("nombre"));
                facultad.setIdUniversidad(universidad);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return facultad;
    }

    public void crearFacultad(int idFacultad, String nombre, int idUniversidad, Date fechaRegistro,Date fechaEdicion) {

        String sql = "INSERT INTO departments (`department_id`, `department_name`, `manager_id`, `location_id`) "
                + "VALUES (?,?,?,?)";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idUniversidad);
            pstmt.setString(2, nombre);

            if (idUniversidad == 0) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, idUniversidad);
            }

            pstmt.setDate(4, fechaRegistro);
            pstmt.setDate(5, fechaEdicion);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void actualizarFacultad(int idFacultad, String nombre, int idUniversidad, Date fechaRegistro,Date fechaEdicion) {

        String sql = "UPDATE departments SET department_name = ?, manager_id = ?, location_id = ? "
                + "WHERE department_id = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idFacultad);
            pstmt.setString(2, nombre);


            if (idUniversidad == 0) {
                pstmt.setNull(3, Types.INTEGER);
            } else {
                pstmt.setInt(3, idUniversidad);
            }

            pstmt.setDate(4, fechaRegistro);
            pstmt.setDate(5, fechaEdicion);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void borrarFactultad(int idFacultad) {

        String sql = "DELETE FROM facultad WHERE idfacultad = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idFacultad);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(FacultadDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Facultad> buscarFacultadPorNombre(String nombre) {

        ArrayList<Facultad> listaFacultad = new ArrayList<>();

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
                    Facultad facultad = new Facultad();
                    fetchFacultadData(facultad, rs);

                    listaFacultad.add(facultad);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listaFacultad;
    }

    private void fetchFacultadData(Facultad facultad, ResultSet rs) throws SQLException {
        facultad.setIdFacultad(rs.getInt(1));
        facultad.setNombre(rs.getString(2));


        Universidad universidad = new Universidad();
        universidad.setIdUniversidad(rs.getInt(4));
        universidad.setNombre(rs.getString("nombre"));
        facultad.setIdUniversidad(universidad);

        facultad.setFechaRegistro(rs.getDate(5));
        facultad.setFechaEdicion(rs.getDate(6));

    }

}
