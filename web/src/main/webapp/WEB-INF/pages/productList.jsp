<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tags:master pageTitle="Product List">

    <script>
        $(document).ready(function () {
            $('#phonesTableId').DataTable({
                "scrollY": "400px",
                "scrollCollapse": true,
                "searching": false
            });
            $('.dataTables_length').addClass('bs-select');
        });
    </script>

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
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            $("#phonesTableId").on("click", ".button", function(e){
                e.preventDefault();
                var rowId = this.id;
                ajaxPost(rowId);
            });

            function ajaxPost(rowId) {
                var formData = {
                    phoneId: $('#phoneId'+rowId).val(),
                    quantity: $('#quantity'+rowId).val(),
                }

                $.post({
                    contentType : 'application/json',
                    url : $('#phoneForm'+rowId).attr('action'),
                    data : JSON.stringify(formData),
                    dataType : 'json',
                    beforeSend: function(xhr) {
                        xhr.setRequestHeader("Accept", "application/json");
                        xhr.setRequestHeader("Content-Type", "application/json");
                        xhr.setRequestHeader(header, token);
                        $('#messageContainer').html('');
                    },
                    success : function(res) {
                        if (Object.keys(res.errorMessages).length === 0) {
                            var json = '<h1><a href="${pageContext.servletContext.contextPath}/cart">Cart: ' + JSON.stringify(res.totalQuantity) + ' items, ' + JSON.stringify(res.totalCost) + '$' +  '</a></h1>';
                            $('#resultContainer').html(json);
                            $('#messageContainer').html('<p class="text-success">Phone '+formData.phoneId+' successfully added</p>');
                            $('#error'+rowId).html('');
                        }
                        else {
                            $.each(res.errorMessages, function(key, value) {
                                $('#error'+key).html('<span class="text-danger">'+value+'</span>');
                                $('#messageContainer').html('<p class="text-danger">Error occurred while adding phone '+formData.phoneId+'</p>');
                            });
                        }
                    }
                });
            }
        });
    </script>

  <div>
    <div id="messageContainer" class="float-left"></div>
      <div class="float-right">
        <form>
          <input type="text" name="queryProduct" value="${param.queryProduct}" placeholder="Search by model...">
          <button>Search</button>
        </form>
      </div>
      <br>
  </div>
    <br>
<p>
  <table id="phonesTableId" class="table table-bordered table-striped">
    <thead>
      <tr>
        <th><b>Image</b></th>
        <th><b>Brand</b>
            <tags:sortLink sort="BRAND"></tags:sortLink>
        </th>
        <th><b>Model</b>
            <tags:sortLink sort="MODEL"></tags:sortLink>
        </th>
        <th><b>Color</b></th>
        <th><b>Display size</b>
            <tags:sortLink sort="DISPLAYSIZEINCHES"></tags:sortLink>
        </th>
        <th><b>Price</b>
            <tags:sortLink sort="PRICE"></tags:sortLink>
        </th>
        <th><b>Quantity</b></th>
        <th><b>Action</b></th>
      </tr>
    </thead>
    <c:forEach var="phone" items="${phones}">
        <tr>
            <form:form action="${pageContext.servletContext.contextPath}/ajaxCart" method="post" id="phoneForm${phone.id}" modelAttribute="addToCartForm">
              <td>
                <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
              </td>
              <td>${phone.brand}</td>
              <td>
                  <a href="${pageContext.servletContext.contextPath}/productDetails/${phone.id}">
                      ${phone.model}
                  </a>
              </td>
              <td>
                <c:forEach var="color" items="${phone.colors}">
                  ${color.code}
                </c:forEach>
              </td>
              <td>${phone.displaySizeInches}"</td>
              <td>${phone.price}$</td>
              <td>
                  <form:hidden path="phoneId" value="${phone.id}" id="phoneId${phone.id}" />
                  <form:input path="quantity" id="quantity${phone.id}" />
                  <div id="error${phone.id}"></div>
              </td>
              <td>
                  <input type="submit" value="Add to cart" class="button" id="${phone.id}" />
              </td>
            </form:form>
        </tr>
    </c:forEach>
  </table>
</p>
</tags:master>
