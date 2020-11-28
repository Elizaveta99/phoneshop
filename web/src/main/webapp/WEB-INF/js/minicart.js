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
