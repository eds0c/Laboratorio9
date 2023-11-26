package com.example.lab9.Controllers;

import com.example.lab9.Beans.Curso;
import com.example.lab9.Beans.Rol;
import com.example.lab9.Beans.Usuario;
import com.example.lab9.Daos.CursoDao;
import com.example.lab9.Daos.UsuarioDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;

@WebServlet(name = "DecanoServlet", value = "/DecanoServlet")
public class DecanoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action") == null ? "lista" : request.getParameter("action");

        RequestDispatcher view;

        UsuarioDao usuarioDao = new UsuarioDao();
        CursoDao cursoDao = new CursoDao();

        switch (action){
            case "listaDocentes":
                request.setAttribute("listaDocentes", usuarioDao.listarUsuariosDocentes());
                view = request.getRequestDispatcher("Decano/lista.jsp");
                view.forward(request, response);
                break;
            case "agregarDocentes":
                request.setAttribute("listaCursos", cursoDao.listarCursos());
                view = request.getRequestDispatcher("Decano/formularioNuevo.jsp");
                view.forward(request, response);
                break;
            case "editar":
                if (request.getParameter("id") != null) {
                    String usuarioIdString = request.getParameter("id");
                    int idUsuario = 0;
                    try {
                        idUsuario = Integer.parseInt(usuarioIdString);
                    } catch (NumberFormatException ex) {
                        response.sendRedirect("DecanoServlet");
                    }

                    Usuario usuario = usuarioDao.obtenerUsuarioDocente(idUsuario);

                    if (usuario != null) {
                        request.setAttribute("usuario", usuario);
                        request.setAttribute("listaCursos", cursoDao.listarCursos());
                        view = request.getRequestDispatcher("Decano/formularioEditar.jsp");
                        view.forward(request, response);
                    } else {
                        response.sendRedirect("DecanoServlet");
                    }

                } else {
                    response.sendRedirect("DecanoServlet");
                }

                break;
            case "borrar":
                if (request.getParameter("id") != null) {
                    String usuarioIdString = request.getParameter("id");
                    int idUsuario = 0;
                    try {
                        idUsuario = Integer.parseInt(usuarioIdString);
                    } catch (NumberFormatException ex) {
                        response.sendRedirect("DecanoServlet");
                    }

                    Usuario usuario = usuarioDao.obtenerUsuarioDocente(idUsuario);

                    if (usuario != null) {
                        try {
                            usuarioDao.borrarUsuario(idUsuario);
                            request.getSession().setAttribute("err", "Docente borrado exitosamente");
                        } catch (SQLException e) {
                            request.getSession().setAttribute("err", "Error al borrar el Docente");
                            e.printStackTrace();
                        }
                        response.sendRedirect(request.getContextPath() + "/DecanoServlet");
                    }
                } else {
                    response.sendRedirect("DecanoServlet");
                }

                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String action = request.getParameter("action") == null ? "guardar" : request.getParameter("action");
        UsuarioDao usuarioDao = new UsuarioDao();

        switch (action) {
            case "guardar":

                Usuario u = new Usuario();
                u.setNombre(request.getParameter("nombre"));
                u.setCorreo(request.getParameter("correo"));
                u.setPassword(request.getParameter("password"));

                /*int idRol = Integer.parseInt(request.getParameter("idrol"));
                Rol rol = new Rol(idRol);
                u.setRol(rol);*/

                if (request.getParameter("idusuario") == null) {
                    try {
                        usuarioDao.guardarUsuario(u);
                        session.setAttribute("msg", "Docente creado exitosamente");
                        response.sendRedirect(request.getContextPath() + "/EmployeeServlet");
                    } catch (SQLException exc) {
                        session.setAttribute("err", "Error al crear el Docente");
                        response.sendRedirect(request.getContextPath() + "/DecanoServlet?action=agregar");
                    }
                } else {
                    u.setIdUsuario(Integer.parseInt(request.getParameter("idusuario")));
                    try {
                        usuarioDao.actualizarUsuario(u);
                        session.setAttribute("msg", "Docente actualizado exitosamente");
                        response.sendRedirect(request.getContextPath() + "/DecanoServlet");
                    } catch (SQLException ex) {
                        session.setAttribute("err", "Error al actualizar el Docente");
                        response.sendRedirect(request.getContextPath() + "/DecanoServlet?action=editar");
                    }
                }
                break;
            case "buscar":
                String textoBuscar = request.getParameter("textoBuscar");
                if (textoBuscar == null) {
                    response.sendRedirect("DecanoServlet");
                } else {
                    request.setAttribute("textoBusqueda", textoBuscar);
                    request.setAttribute("listaDocentes", usuarioDao.buscarUsuarioPorNombre(textoBuscar));
                    RequestDispatcher view = request.getRequestDispatcher("Decano/lista.jsp");
                    view.forward(request, response);
                }
                break;
        }
    }
}

