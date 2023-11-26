<%@ page import="com.example.lab9.Beans.Usuario" %>
<% String currentPage = request.getParameter("currentPage"); %>
<jsp:useBean id="usuarios" type="com.example.lab9.Beans.Usuario" scope="session"
             class="com.example.lab9.Beans.Usuario"/>

<nav class="navbar navbar-expand-md navbar-light bg-light">
    <a class="navbar-brand" href="#">Gestion Universidades</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
        <ul class="navbar-nav">
            <% if (usuarios.getIdUsuario() > 0) { %>

            <li class="nav-item">
                <a class="nav-link <%=currentPage.equals("loc") ? "active" : ""%>"
                   href="<%=request.getContextPath()%>/DecanoServlet">
                    Docentes
                </a>
            </li>

            <li class="nav-item">
                <span class="nav-link text-dark">
                    Bienvenido <%=usuarios.getNombre()%> (<a
                        href="<%=request.getContextPath()%>/LoginServlet?action=logout">cerrar sesión</a>)
                </span>
            </li>
            <% } else { %>
            <a class="nav-link" style="color: #007bff;" href="<%=request.getContextPath()%>/LoginServlet">
                (Iniciar Sesión)
            </a>
            <% } %>
        </ul>
    </div>
</nav>
