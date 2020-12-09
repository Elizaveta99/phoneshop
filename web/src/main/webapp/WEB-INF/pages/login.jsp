<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Login">
    <c:if test="${not empty param.error}">
        <div class="text-danger">Username or Password is incorrect</div>
    </c:if>
    <br>
    <form name='login' action="${pageContext.servletContext.contextPath}/login" method='post'>
        <input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
        <table>
            <tr>
                <td>Username:</td>
                <td><input type='text' name='username' value=''></td>
            </tr>
            <tr>
                <td>Password:</td>
                <td><input type='password' name='password' /></td>
            </tr>
            <tr>
                <td><input name="submit" type="submit" value="Login" /></td>
            </tr>
        </table>
    </form>
</tags:master>