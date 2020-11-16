<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<tags:master pageTitle="Product Details">

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

    <div class="float-left">
        <button><a href="${pageContext.request.contextPath}/productList">Back to product list</a></button>
        &nbsp
        <div class="float-right" id="messageContainer"></div>
    </div>
    <br>
    <br>
        <div class="col-xs-6">
            <h2 class="sub-header">${phone.model}</h2>
            <div class="table-responsive">
                <table id="phonesTableId">
                    <tbody>
                        <tr>
                            <td style="width:50%">
                                <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                            </td>
                        </tr>
                        <tr>
                            <td style="width:50%">${phone.description}</td>
                        </tr>
                        <tr>
                            <td><h3>Price: ${phone.price}$</h3></td>
                        </tr>
                        <tr>
                            <form:form action="${pageContext.servletContext.contextPath}/ajaxCart" method="post" id="phoneForm${phone.id}" modelAttribute="phoneToCart">

                                    <td style="width:50%">
                                        <div>
                                            <form:hidden path="phoneId" value="${phone.id}" id="phoneId${phone.id}" />
                                            <form:input path="quantity" id="quantity${phone.id}" />
                                            <input type="submit" value="Add to cart" class="button" id="${phone.id}" />
                                            <div id="error${phone.id}"></div>
                                        </div>
                                    </td>

                            </form:form>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="col-xs-6">
            <h2 class="sub-header"></h2>
            <div class="table-responsive">
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <td><b>Display</b></td>
                    </tr>
                    <tr>
                        <td>Size</td>
                        <td>${phone.displaySizeInches}"</td>
                    </tr>
                    <tr>
                        <td>Resolution</td>
                        <td>${phone.displayResolution}</td>
                    </tr>
                    <tr>
                        <td>Technology</td>
                        <td>${phone.displayTechnology}</td>
                    </tr>
                    <tr>
                        <td>Pixel density</td>
                        <td>${phone.pixelDensity}</td>
                    </tr>
                    <tr>
                        <td><b>Dimensions & weight</b></td>
                    </tr>
                    <tr>
                        <td>Length</td>
                        <td>${phone.lengthMm}mm</td>
                    </tr>
                    <tr>
                        <td>Width</td>
                        <td>${phone.widthMm}mm</td>
                    </tr>
                    <tr>
                        <td>Color</td>
                            <td>${phone.colors.iterator().next().code}</td>
                    </tr>
                    <tr>
                        <td>Weight</td>
                        <td>${phone.weightGr}</td>
                    </tr>
                    <tr>
                        <td><b>Camera</b></td>
                    </tr>
                    <tr>
                        <td>Front</td>
                        <td>${phone.frontCameraMegapixels} megapixels</td>
                    </tr>
                    <tr>
                        <td>Back</td>
                        <td>${phone.backCameraMegapixels} megapixels</td>
                    </tr>
                    <tr>
                        <td><b>Battery</b></td>
                    </tr>
                    <tr>
                        <td>Talk time</td>
                        <td>${phone.talkTimeHours} hours</td>
                    </tr>
                    <tr>
                        <td>Stand by time</td>
                        <td>${phone.standByTimeHours} hours</td>
                    </tr>
                    <tr>
                        <td>Battery capacity</td>
                        <td>${phone.batteryCapacityMah}mAh</td>
                    </tr>
                    <tr>
                        <td><b>Other</b></td>
                    </tr>
                    <tr>
                        <td>Colors</td>
                        <td>
                            <c:forEach var="color" items="${phone.colors}">
                                ${color.code}
                            </c:forEach>
                        </td>
                    </tr>
                    <tr>
                        <td>Device type</td>
                        <td>${phone.deviceType}</td>
                    </tr>
                    <tr>
                        <td>Bluetooth</td>
                        <td>${phone.bluetooth}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
</tags:master>