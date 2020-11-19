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
    <form:form action="${pageContext.servletContext.contextPath}/cart" modelAttribute="multipleAddToCartForm">
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
      <c:forEach var="item" items="${multipleAddToCartForm.addToCartFormList}" varStatus="status">
        <tr>
            <td>${phones[status.index].brand}</td>
            <td>
              <a href="${pageContext.servletContext.contextPath}/productDetails/${item.phoneId}">
                  ${phones[status.index].model}
              </a>
            </td>
            <td>
              <c:forEach var="color" items="${phones[status.index].colors}">
                ${color.code}
              </c:forEach>
            </td>
            <td>${phones[status.index].displaySizeInches}"</td>
            <td>${phones[status.index].price}$</td>
            <td>
              <input type="hidden" name="addToCartFormList[${status.index}].phoneId" value="${item.phoneId}" />
              <c:set var="error" value="${errors[item.phoneId]}" />
              <input name="addToCartFormList[${status.index}].quantity" value="${item.quantity}" />
              <div id="error${item.phoneId}" class="text-danger">${error}</div>
              <c:set var="key" value="addToCartFormList[${status.index}].quantity" />
              <div id="addToCartFormList[${status.index}].quantity" class="text-danger">${validationErrors[key]}</div>
            </td>
            <td>
              <button form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/delete/${item.phoneId}">
                Delete
              </button>
            </td>
        </tr>
      </c:forEach>
    </table>
      <p class="float-right">
        <input type="submit" value="Update" />
        <button>Order</button>
      </p>
    </form:form>
  <form:form method="delete" id="deleteCartItem" />
  </p>
</tags:master>
