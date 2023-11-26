<%@page import="java.util.ArrayList" %>
<%@ page import="com.example.lab9.Beans.Usuario" %>
<%@ page import="com.example.lab9.Beans.Usuario" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean id="listaDocentes" type="java.util.ArrayList<com.example.lab9.Beans.Usuario>" scope="request"/>
<jsp:useBean id="textoBusqueda" scope="request" type="java.lang.String" class="java.lang.String"/>
<!DOCTYPE html>
<html>
    <head>
        <title>Lista Docentes</title>
        <jsp:include page="../includes/headCss.jsp"/>
    </head>
    <body>
        <div class='container'>
            <jsp:include page="../includes/navbar.jsp">
                <jsp:param name="currentPage" value="emp"/>
            </jsp:include>
            <div class="row mb-5 mt-4">
                <div class="col-md-7">
                    <h1>Lista de docentes</h1>
                </div>

                <div class="col-md-5 col-lg-4 ms-auto my-auto text-md-end">
                    <a href="<%= request.getContextPath()%>/EmployeeServlet?action=agregar" class="btn btn-primary">
                        Agregar nuevo docente</a>
                </div>

            </div>
            <jsp:include page="../includes/infoMsgs.jsp"/>
            <form method="post" action="<%=request.getContextPath()%>/DecanoServlet?action=buscar">
                <div class="input-group mb-3">
                    <input type="text" class="form-control" placeholder="Buscar por nombre" name="textoBuscar"
                           value="<%=textoBusqueda%>"/>
                    <button class="input-group-text" type="submit">
                        <i class="bi bi-search"></i>
                    </button>
                    <a class="input-group-text" href="<%=request.getContextPath()%>/EmployeeServlet">
                        <i class="bi bi-x-circle"></i>
                    </a>
                </div>
            </form>
            <table class="table">
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Docente</th>
                        <th>Correo</th>
                        <th>Rol</th>
                        <th>Fecha registro</th>
                        <th>Ultimo ingreso</th>
                        <th>Curso Asignado</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int i = 1;
                        for (Usuario u : listaDocentes) {
                    %>
                    <tr>
                        <td><%= i%>
                        </td>
                        <td><%= u.getNombre()%>
                        </td>
                        <td><%= u.getCorreo()%>
                        </td>
                        <td><%= u.getRol().getNombre()%>
                        </td>
                        <td><%= u.getFechaRegistro()%>
                        </td>
                        <td><%= u.getUltimoIngreso()%>
                        </td>
                        <td>
                        </td>
                        </td>
                        <td>
                            <a href="<%=request.getContextPath()%>/DecanoServlet?action=editar&id=<%= u.getIdUsuario()%>"
                               type="button" class="btn btn-primary">
                                <i class="bi bi-pencil-square"></i>
                            </a>
                        </td>
                        <td>
                            <a onclick="return confirm('¿Estas seguro de borrar?');"
                               href="<%=request.getContextPath()%>/DecanoServlet?action=editar&id=<%= u.getIdUsuario()%>"
                               type="button" class="btn btn-danger">
                                <i class="bi bi-trash"></i>
                            </a>
                        </td>
                    </tr>
                    <%
                            i++;
                        }
                    %>
                </tbody>
            </table>
            <jsp:include page="../includes/footer.jsp"/>
        </div>
    </body>
</html>
