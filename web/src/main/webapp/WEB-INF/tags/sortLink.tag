<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>

<div class="btn-group-vertical float-right">
    <a href="?sort=${sort}&order=asc&queryProduct=${param.queryProduct}" >
        <i class="fa fa-fw fa-sort-up"></i>
    </a>
    <a href="?sort=${sort}&order=desc&queryProduct=${param.queryProduct}">
        <i class="fa fa-fw fa-sort-down"></i>
    </a>
</div>