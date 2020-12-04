<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Order overview">
    <h2>Thank you for your order</h2>
    <h4>Order number: ${order.id}</h4>
    <table id="orderTableId" class="table table-bordered table-striped">
        <thead>
        <tr>
            <th><b>Brand</b>
            </th>
            <th><b>Model</b>
            </th>
            <th><b>Color</b></th>
            <th><b>Display size</b>
            </th>
            <th><b>Quantity</b></th>
            <th><b>Price</b>
            </th>
        </tr>
        </thead>
        <c:forEach var="item" items="${order.orderItems}" varStatus="status">
            <tr>
                <td>${item.phone.brand}</td>
                <td>
                        ${item.phone.model}
                </td>
                <td>
                    <c:forEach var="color" items="${item.phone.colors}">
                        ${color.code}
                    </c:forEach>
                </td>
                <td>${item.phone.displaySizeInches}"</td>
                <td>
                        ${item.quantity}
                </td>
                <td>${item.phone.price}$</td>
            </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>Subtotal</td>
            <td>${order.subtotal}$</td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>Delivery</td>
            <td>${order.deliveryPrice}$</td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>TOTAL</td>
            <td>${order.totalPrice}$</td>
        </tr>
    </table>

    <h2>Your details</h2>
      <table>
          <tags:orderOverviewRow name="firstName" label="First name" order="${order}"></tags:orderOverviewRow>
          <tags:orderOverviewRow name="lastName" label="Last name" order="${order}"></tags:orderOverviewRow>
          <tags:orderOverviewRow name="deliveryAddress" label="Delivery address" order="${order}"></tags:orderOverviewRow>
          <tags:orderOverviewRow name="contactPhoneNo" label="Phone " order="${order}"></tags:orderOverviewRow>
          <tags:orderOverviewRow name="additionalInformation" label="Info " order="${order}"></tags:orderOverviewRow>
      </table>
    <br>
    <div class="float-left">
        <button><a href="${pageContext.request.contextPath}/productList">Back to shopping</a></button>
    </div>
    <br>
    <br>
</tags:master>
