package com.example.lab9.Daos;

import com.example.lab9.Beans.Curso;
import com.example.lab9.Beans.Evaluaciones;
import com.example.lab9.Beans.Facultad;
import com.example.lab9.Beans.Semestre;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CursoDao extends DaoBase{
    public ArrayList<Curso> listarCursos() {

        ArrayList<Curso> lista = new ArrayList<>();

        try (Connection conn = this.getConection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM departments d " +
                     "left join employees e on d.manager_id = e.employee_id " +
                     "left join locations l on d.location_id = l.location_id");) {
            Curso curso;
            while (rs.next()) {
                curso = parseResultSet(rs);
                lista.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Curso parseResultSet(ResultSet rs) {
        Curso curso = new Curso();
        try {
            curso.setIdCurso(rs.getInt(1));
            curso.setCodigo(rs.getString(2));
            curso.setNombre(rs.getString(3));


            if (rs.getInt("f.idFacultad") != 0) {
                Facultad facultad = new Facultad();
                facultad.setIdFacultad(rs.getInt("idcurso"));
                facultad.setNombre(rs.getString("nombre"));
                curso.setIdFacultad(facultad);
            }

            curso.setFechaRegistro(rs.getDate(5));
            curso.setFechaEdicion(rs.getDate(6));

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return curso;
    }

}
