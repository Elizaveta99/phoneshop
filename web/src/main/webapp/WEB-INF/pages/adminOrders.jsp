<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<tags:adminMaster pageTitle="Orders List">
    <p>
        <table id="ordersTableId" class="table table-bordered table-striped">
            <thead>
            <tr>
                <th><b>Order number</b></th>
                <th><b>Customer</b></th>
                <th><b>Phone</b></th>
                <th><b>Address</b></th>
                <th><b>Date</b></th>
                <th><b>Total price</b></th>
                <th><b>Status</b></th>
            </tr>
            </thead>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/admin/orders/${order.secureId}">
                                ${order.id}
                        </a>
                    </td>
                    <td>${order.firstName} ${order.lastName}</td>
                    <td>${order.contactPhoneNo}</td>
                    <td>${order.deliveryAddress}</td>
                    <td>
                        <fmt:formatDate pattern="dd-MM-yyyy HH:mm:ss" value="${order.orderDate}"/>
                    </td>
                    <td>${order.totalPrice}$</td>
                    <td>${order.status}</td>
                </tr>
            </c:forEach>
        </table>
    </p>
</tags:adminMaster>
