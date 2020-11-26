<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@attribute name="name" required="true" %>
<%@attribute name="label" required="true" %>
<%@attribute name="order" required="true" type="com.es.core.model.order.Order" %>

<tr>
    <td>${label}</td>
    <td>&nbsp&nbsp&nbsp</td>
    <td>
        <c:set var="orderName" value="${order[name]}" />
        ${orderName}
    </td>
</tr>