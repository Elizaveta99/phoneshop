<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
  <title>${pageTitle}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
    <script src="https://cdn.datatables.net/1.10.22/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.22/js/dataTables.bootstrap4.min.js"></script>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
</head>

<body class="product-list">
  <header>
    <div>
      <div class="float-left">
        <h1>
          <a href="${pageContext.servletContext.contextPath}">
            <em>Phonify</em>
          </a>
        </h1>
      </div>
      <jsp:include page="${pageContext.servletContext.contextPath}/ajaxCart" />
      <div class="float-right">
        <br>
        <h6>
          <sec:authorize access="!isAuthenticated()">
            <a href="${pageContext.servletContext.contextPath}/login">Login &nbsp&nbsp</a>
          </sec:authorize>
          <a href="${pageContext.servletContext.contextPath}/admin/orders">Admin &nbsp&nbsp</a>
              <sec:authorize access="isAuthenticated()">
                <sec:authentication property="name"/>&nbsp&nbsp
                  <div class="float-right">
                      <form:form action="${pageContext.servletContext.contextPath}/logout" method="post">
                          <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
                          <input type="submit" value="Logout">
                      </form:form>
                  </div>
              </sec:authorize>

        </h6>
      </div>
      <br>
      <br>
      <br>
    </div>
    <div id="resultContainer" class="float-right">
      <h1></h1>
    </div>
    <br>
    <br>
    <br>
      <hr />
  </header>
  <main>
    <jsp:doBody/>
  </main>
<footer>
  <p>
    (c) Expert-Soft
  </p>
</footer>
</body>
</html>