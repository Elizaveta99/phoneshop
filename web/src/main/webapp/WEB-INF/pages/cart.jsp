<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<tags:master pageTitle="Cart">

  <script type="text/javascript">
    $(document).ready(function() {
        $.get({
          url : '${pageContext.servletContext.contextPath}/ajaxCart',
          dataType : 'json',
          success : function(res) {
            var json = '<h1><a href="${pageContext.servletContext.contextPath}/cart">Cart: ' + JSON.stringify(res.totalQuantity) + ' items, ' + JSON.stringify(res.totalCost) + '$' +  '</a></h1>';
            $('#resultContainer').html(json);
          }
        });
    });
  </script>

  <script type="text/javascript">
    $(document).ready(function() {
      $(document).on("click",".deleteItem",function() {
          var rowId = this.id;
          var workingObject = $(this);
          ajaxDelete(rowId, workingObject);

          function ajaxDelete(rowId, workingObject) {
            $.ajax({
              type: 'delete',
              url : '${pageContext.servletContext.contextPath}/cart/delete/' + rowId,
              success : function(res) {
                var json = '<p class="text-success">Phone '+rowId+' deleted successfully</p>';
                $('#messageContainer').html(json);

                workingObject.closest("tr").remove();
                ajaxGet();
              }
            });
          }

        function ajaxGet() {
          $.get({
            url : '${pageContext.servletContext.contextPath}/ajaxCart',
            dataType : 'json',
            success : function(res) {
              var json = '<h1><a href="${pageContext.servletContext.contextPath}/cart">Cart: ' + JSON.stringify(res.totalQuantity) + ' items, ' + JSON.stringify(res.totalCost) + '$' +  '</a></h1>';
              $('#resultContainer').html(json);
            }
          });
        }
      });

    });
  </script>

  <h2>Cart</h2>
  <div>
    <div class="float-left">
      <button><a href="${pageContext.request.contextPath}/productList">Back to product list</a></button>
      &nbsp
      <div class="float-right" id="messageContainer">${message}</div>
    </div>
    <div class="float-right">
      <button>Order</button>
    </div>
  </div>
  <br>
  <br>
  <p>
    <form:form method="post" action="${pageContext.servletContext.contextPath}/cart" modelAttribute="updateCartForm">
      <table id="cartTableId" class="table table-bordered table-striped">
      <thead>
      <tr>
        <th><b>Brand</b>
        </th>
        <th><b>Model</b>
        </th>
        <th><b>Color</b></th>
        <th><b>Display size</b>
        </th>
        <th><b>Price</b>
        </th>
        <th><b>Quantity</b></th>
        <th><b>Action</b></th>
      </tr>
      </thead>
      <c:forEach var="item" items="${updateCartForm.updateCartList}" varStatus="status">
        <tr>
            <td>${item.phone.brand}</td>
            <td>
              <a href="${pageContext.servletContext.contextPath}/productDetails/${item.phone.id}">
                  ${item.phone.model}
              </a>
            </td>
            <td>
              <c:forEach var="color" items="${item.phone.colors}">
                ${color.code}
              </c:forEach>
            </td>
            <td>${item.phone.displaySizeInches}"</td>
            <td>${item.phone.price}$</td>
            <td>
              <input type="hidden" name="updateCartList[${status.index}].phone.id" value="${item.phone.id}" />
              <c:set var="error" value="${errors[item.phone.id]}" />
              <input name="updateCartList[${status.index}].quantity" value="${item.quantity}" />
              <div id="error${item.phone.id}" class="text-danger">${error}</div>
              <c:set var="key" value="updateCartList[${status.index}].quantity" />
              <div id="updateCartList[${status.index}].quantity" class="text-danger">${validErrors[key]}</div>
            </td>
            <td>
              <a class="deleteItem" id="${item.phone.id}">Delete</a>
            </td>
        </tr>
      </c:forEach>
    </table>
      <p class="float-right">
        <input type="submit" value="Update" />
        <button>Order</button>
      </p>
    </form:form>
  </p>
</tags:master>
