<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<tags:master pageTitle="Quick order">

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

  <h2>Quick order</h2>
  <div>
    <div class="float-left">
      <button><a href="${pageContext.request.contextPath}/productList">Back to product list</a></button>
      &nbsp
      <div class="text-success" >${successMessage}</div>
      <div class="text-danger">${errorMessage}</div>
    </div>
  </div>
  <br>
  <br>
  <p>
    <form:form action="${pageContext.servletContext.contextPath}/quickOrder" modelAttribute="multipleQuickAddToCartForm">
      <table id="orderTableId" class="table table-bordered table-striped">
      <thead>
      <tr>
        <th><b>Model</b>
        </th>
        <th><b>Quantity</b>
        </th>
      </tr>
      </thead>
      <c:forEach var="item" items="${multipleQuickAddToCartForm.addToCartFormList}" varStatus="status">
        <tr>
            <c:set var="num" value="${status.index}"/>
            <td>
              <form:input path="addToCartFormList[${num}].model" />
              <form:errors path="addToCartFormList[${num}].model" cssClass="error"/>
            </td>
            <td>
              <form:input path="addToCartFormList[${num}].quantity" />
              <form:errors path="addToCartFormList[${num}].quantity" cssClass="error"/>
            </td>
        </tr>
      </c:forEach>
    </table>
      <p class="float-right">
        <input type="submit" value="Add to cart" />
      </p>
    </form:form>
  </p>
</tags:master>
