<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
  <title>${pageTitle}</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
    <script
            src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script
            src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
    <script
            src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
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
      <div id="resultContainer" class="float-right">
        <h1></h1>
      </div>
      <br>
      <br>
      <br>
    </div>
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