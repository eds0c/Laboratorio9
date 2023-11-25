package com.example.lab9.Controllers;

import com.example.lab9.Beans.Usuario;
import com.example.lab9.Daos.UsuarioDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action") == null ? "loginform" : request.getParameter("action");
        HttpSession session = request.getSession();

        switch (action) {
            case "loginform":
                Usuario usuario = (Usuario) session.getAttribute("usuario");
                if (usuario != null && usuario.getIdUsuario() > 0) {
                    response.sendRedirect(request.getContextPath() + "/UsuarioServlet");
                } else {
                    RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
                    view.forward(request, response);
                }
                break;
            case "logout":
                session.invalidate();
                response.sendRedirect(request.getContextPath());
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String username = request.getParameter("inputEmail");
        String password = request.getParameter("inputPassword");

        if (username == null || password == null) {
            request.setAttribute("err", "El usuario o password no pueden ser vacíos");
            RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
            view.forward(request, response);
        }else{
            UsuarioDao usuarioDao = new UsuarioDao();
            Usuario usuario = usuarioDao.validarUsuarioPasswordHashed(username, password);

            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                session.setMaxInactiveInterval(10 * 60); // 10 minutos

                response.sendRedirect(request.getContextPath() + "/UsuarioServlet");
            } else {
                request.setAttribute("err", "El usuario o password no existen");
                RequestDispatcher view = request.getRequestDispatcher("login/loginForm.jsp");
                view.forward(request, response);
            }
        }

    }
}
