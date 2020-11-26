<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@attribute name="name" required="true" %>
<%@attribute name="label" required="true" %>
<%@attribute name="errors" required="true" type="java.util.Map" %>

<tr>
    <td>
        <label>
            ${label}
                <c:if test="${name != 'additionalInformation'}">
                    <span style="color: red">*</span>
                </c:if>
        </label>
    </td>
    <td>
        <c:if test="${name != 'additionalInformation'}"><form:input path ="${name}"/></c:if>
        <c:if test="${name == 'additionalInformation'}"><form:textarea path="${name}" /></c:if>
        <div id="error" class="text-danger">${errors[name]}</div>
    </td>
</tr>