<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Order">
  <br>
    <h2>Order</h2>
    <div>
        <div class="float-left">
            <button><a href="${pageContext.request.contextPath}/cart">Back to cart</a></button>
            &nbsp
            <div class="float-right" id="messageContainer">${message}</div>
        </div>
    </div>
    <br>
    <br>
    <p>
        <form:form method="post" modelAttribute="orderDataForm">
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
            <tags:orderFormRow name="firstName" label="First name" errors="${validationErrors}"></tags:orderFormRow>
            <tags:orderFormRow name="lastName" label="Last name" errors="${validationErrors}"></tags:orderFormRow>
            <tags:orderFormRow name="deliveryAddress" label="Address" errors="${validationErrors}"></tags:orderFormRow>
            <tags:orderFormRow name="contactPhoneNo" label="Phone" errors="${validationErrors}"></tags:orderFormRow>
            <tags:orderFormRow name="additionalInformation" label="Info" errors="${validationErrors}"></tags:orderFormRow>
        </table>
        <br>
        <p class="float-left">
            <input type="submit" value="Place order" />
        </p>
    </form:form>
        <br>
    </p>
</tags:master>
