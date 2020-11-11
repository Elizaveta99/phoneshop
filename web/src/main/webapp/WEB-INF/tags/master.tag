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

  <script type="text/javascript">
    $(document).ready(function() {
      /*  Submit form using Ajax */
      $('#phoneForm').submit(function(e) {

        //Prevent default submission of form
        e.preventDefault();
        ajaxPost();
      });

      function ajaxPost() {

        var formData = {
          quantity: $('#quantity').val(),
          phoneId: $('#phoneId').val(),
        }

        $.post({
          contentType : 'application/json',
          url : $('#phoneForm').attr( 'action'),
          data : JSON.stringify(formData),
          dataType : 'application/json',
          beforeSend: function(xhr) {
            xhr.setRequestHeader("Accept", "application/json");
            xhr.setRequestHeader("Content-Type", "application/json");
          },
          success : function(res) {
            if(res.validated) {
              $('#resultContainer').text(JSON.stringify(res.totalQuantity));
              $('#resultContainer').show();
            }
            else {
              //Set error messages
              $.each(res.errorMessages, function(key,value){
                $('input[name='+key+']').after('<span class="error">'+value+'</span>');
              });
            }
          }
        });
      }
    });
  </script>
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
      <div class="float-right">
        <h1>
          <a href="${pageContext.servletContext.contextPath}/cart">
            Cart:
          </a>
        </h1>
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