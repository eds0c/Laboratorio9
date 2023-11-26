package com.example.lab9.Beans;

public class Curso_has_docente {
    private Curso idCurso;
    private Rol idDocente;

    public Curso getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Curso idCurso) {
        this.idCurso = idCurso;
    }

    public Rol getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(Rol idDocente) {
        this.idDocente = idDocente;
    }
}
