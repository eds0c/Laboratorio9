package com.example.lab9.Daos;

import com.example.lab9.Beans.*;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EvaluacionesDao extends DaoBase{
    public ArrayList<Evaluaciones> listarEvaluaciones() {
        ArrayList<Evaluaciones> listarEvaluaciones = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM departments d " +
                     "left join employees e on d.manager_id = e.employee_id " +
                     "left join locations l on d.location_id = l.location_id");) {
            Evaluaciones evaluaciones;
            while (rs.next()) {
                evaluaciones = parseResultSet(rs);
                listarEvaluaciones.add(evaluaciones);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listarEvaluaciones;
    }

    public Evaluaciones obtenerEvaluacion(int idEvaluaciones) {

        Evaluaciones evaluaciones = null;

        String sql = "SELECT * FROM departments d " +
                "left join employees e on d.manager_id = e.employee_id " +
                "left join locations l on d.location_id = l.location_id " +
                "WHERE d.department_id = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, idEvaluaciones);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    evaluaciones = parseResultSet(rs);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return evaluaciones;
    }

    private Evaluaciones parseResultSet(ResultSet rs) {
        Evaluaciones evaluaciones = new Evaluaciones();
        try {
            evaluaciones.setIdEvaluaciones(rs.getInt(1));
            evaluaciones.setNombreEstudiantes(rs.getString(2));
            evaluaciones.setCodigoEstudiantes(rs.getString(3));
            evaluaciones.setCorreoEstudiantes(rs.getString(4));
            evaluaciones.setNota(rs.getInt(5));

            if (rs.getInt("c.idCurso") != 0) {
                Curso curso = new Curso();
                curso.setIdCurso(rs.getInt("idcurso"));
                curso.setNombre(rs.getString("nombre"));
                evaluaciones.setIdCurso(curso);
            }
            if (rs.getInt("s.idSemestre") != 0) {
                Semestre semestre = new Semestre();
                semestre.setIdSemestre(rs.getInt("idsemestre"));
                semestre.setNombre(rs.getString("nombre"));
                evaluaciones.setIdSemestre(semestre);
            }

            evaluaciones.setFechaRegistro(rs.getDate(8));
            evaluaciones.setFechaEdicion(rs.getDate(9));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return evaluaciones;
    }

    public void crearEvaluacion(int idEvaluaciones, String nombreEstudiantes, String codigoEstudiantes, String correoEstudiantes, int nota,  int idCurso, int idSemestre, Date fechaRegistro,Date fechaEdicion) {

        String sql = "INSERT INTO evaluaciones (`idevaluaciones`, `nombre_estudiantes`, `codigo_estudiantes`, `correo_estudiantes`, `nota`, `idcurso`, `idsemestre`, `fecha_registro`,`fecha_edicion` ) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idEvaluaciones);
            pstmt.setString(2, nombreEstudiantes);
            pstmt.setString(3, codigoEstudiantes);
            pstmt.setString(4, correoEstudiantes);
            pstmt.setInt(5, nota);

            if (idCurso == 0) {
                pstmt.setNull(6, Types.INTEGER);
            } else {
                pstmt.setInt(6, idCurso);
            }

            if (idSemestre == 0) {
                pstmt.setNull(7, Types.INTEGER);
            } else {
                pstmt.setInt(7, idSemestre);
            }


            pstmt.setDate(8, fechaRegistro);
            pstmt.setDate(9, fechaEdicion);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void actualizarEvaluacion(int idEvaluaciones, String nombreEstudiantes, String codigoEstudiantes, String correoEstudiantes, int nota,  int idCurso, int idSemestre, Date fechaRegistro,Date fechaEdicion) {

        String sql = "UPDATE evaluaciones SET nombre_estudiantes = ?, codigo_estudiantes = ?, correo_estudiantes = ?, nota = ?, idcurso = ?, idsemestre = ?, fecha_registro = ?, fecha_edicion = ? "
                + "WHERE idevaluaciones = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql);) {

            pstmt.setInt(1, idEvaluaciones);
            pstmt.setString(2, nombreEstudiantes);
            pstmt.setString(3, codigoEstudiantes);
            pstmt.setString(4, correoEstudiantes);
            pstmt.setInt(5, nota);

            if (idCurso == 0) {
                pstmt.setNull(6, Types.INTEGER);
            } else {
                pstmt.setInt(6, idCurso);
            }

            if (idSemestre == 0) {
                pstmt.setNull(7, Types.INTEGER);
            } else {
                pstmt.setInt(7, idSemestre);
            }


            pstmt.setDate(8, fechaRegistro);
            pstmt.setDate(9, fechaEdicion);

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void borrarEvaluacion(int idEvaluaciones) {

        String sql = "DELETE FROM evaluaciones WHERE idevaluaciones = ?";
        try (Connection conn = this.getConection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idEvaluaciones);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(EvaluacionesDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private void fetchEvaluacionData(Evaluaciones evaluaciones, ResultSet rs) throws SQLException {
        evaluaciones.setIdEvaluaciones(rs.getInt(1));
        evaluaciones.setNombreEstudiantes(rs.getString(2));
        evaluaciones.setCodigoEstudiantes(rs.getString(3));
        evaluaciones.setCorreoEstudiantes(rs.getString(4));
        evaluaciones.setNota(rs.getInt(5));


        Curso curso = new Curso();
        curso.setIdCurso(rs.getInt(6));
        curso.setNombre(rs.getString("nombre"));
        evaluaciones.setIdCurso(curso);

        Semestre semestre = new Semestre();
        semestre.setIdSemestre(rs.getInt(7));
        semestre.setNombre(rs.getString("nombre"));
        evaluaciones.setIdSemestre(semestre);

        evaluaciones.setFechaRegistro(rs.getDate(8));
        evaluaciones.setFechaEdicion(rs.getDate(9));

    }
}
