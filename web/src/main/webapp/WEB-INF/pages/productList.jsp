<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Product List">
  <div>
    <div class="float-left">
      Phones
    </div>
    <div id="resultContainer"></div>
      <div class="float-right">
        <form>
          <input type="text" name="queryProduct" value="${param.queryProduct}" placeholder="Search by model...">
          <button>Search</button>
        </form>
      </div>
      <br>
  </div>
<p>
  Found <c:out value="${phones.size()}"/> phones.
  <table class="table table-bordered table-striped">
    <thead>
      <tr>
        <th><b>Image</b></th>
        <th><b>Brand</b>
            <tags:sortLink sort="brand"></tags:sortLink>
        </th>
        <th><b>Model</b>
            <tags:sortLink sort="model"></tags:sortLink>
        </th>
        <th><b>Color</b></th>
        <th><b>Display size</b>
            <tags:sortLink sort="displaySize"></tags:sortLink>
        </th>
        <th><b>Price</b>
            <tags:sortLink sort="price"></tags:sortLink>
        </th>
        <th><b>Quantity</b></th>
        <th><b>Action</b></th>
      </tr>
    </thead>
    <c:forEach var="phone" items="${phones}">
      <form:form action="${pageContext.servletContext.contextPath}/ajaxCart" method="post" id="phoneForm" name="phoneForm" modelAttribute="phoneToCart">
        <tr>
          <td>
            <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
          </td>
          <td>${phone.brand}</td>
          <td>${phone.model}</td>
          <td>
            <c:forEach var="color" items="${phone.colors}">
              ${color.code}
            </c:forEach>
          </td>
          <td>${phone.displaySizeInches}"</td>
          <td>${phone.price}$</td>
          <td>
              <form:input path="quantity" />
              <form:hidden path="phoneId" value="${phone.id}" />
          </td>
          <td>
            <input type="submit" value="Add to cart" />
          </td>
        </tr>
      </form:form>
    </c:forEach>
  </table>
</p>
</tags:master>
