<%@page import="java.util.ArrayList" %>
<%@ page import="com.example.lab9.Beans.Curso" %>
<%@ page import="com.example.lab9.Beans.Usuario" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<jsp:useBean scope="request" id="listaCursos" type="java.util.ArrayList<com.example.lab9.Beans.Curso>"/>
<jsp:useBean id="listaDocente" type="java.util.ArrayList<com.example.webapphr3.Beans.Employee>" scope="request"/>
<!DOCTYPE html>
<html>
    <head>
        <title>Nuevo docente</title>
        <jsp:include page="../includes/headCss.jsp"/>
    </head>
    <body>
        <div class='container'>
            <jsp:include page="../includes/navbar.jsp">
                <jsp:param name="currentPage" value="emp"/>
            </jsp:include>
            <div class="row mb-4">
                <div class="col"></div>
                <div class="col-md-6">
                    <h1 class='mb-3'>Nuevo docente</h1>
                    <hr>
                    <jsp:include page="../includes/infoMsgs.jsp"/>
                    <form method="POST" action="EmployeeServlet">
                        <div class="mb-3">
                            <label class="form-label" for="first_name">Nombre</label>
                            <input type="text" class="form-control form-control-sm" id="first_name" name="first_name">
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="email">Correo</label>
                            <input type="text" class="form-control form-control-sm" id="email" name="email">
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="hire_date">Fecha</label>
                            <input type="text" class="form-control form-control-sm" id="hire_date" name="hire_date">
                        </div>
                        <div class="mb-3">
                            <label class="form-label" for="idcurso">Cursos</label>
                            <select name="job_id" id="idcurso" class="form-select form-select-sm">
                                <% for (Curso curso : listaCursos) {%>
                                <option value="<%=curso.getIdCurso()%>"><%=curso.getNombre()%>
                                </option>
                                <% }%>
                            </select>
                        </div>

                        <a href="<%= request.getContextPath()%>/EmployeeServlet" class="btn btn-danger">Cancelar</a>
                        <input type="submit" value="Guardar" class="btn btn-primary"/>
                    </form>
                </div>
                <div class="col"></div>
            </div>
        </div>
        <jsp:include page="../includes/footer.jsp"/>
    </body>
</html>
