<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>小说管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			laydate({
	            elem: '#publishDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>小说列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="book" action="${ctx}/bus/book/" method="post" class="form-inline">
		<div class="col-sm-12">
			<div class="pull-left">
				<shiro:hasPermission name="bus:book:add">
					<table:addRow url="${ctx}/bus/book/form" title="小说"></table:addRow><!-- 增加按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:book:edit">
					<table:editRow url="${ctx}/bus/book/form" title="小说" id="contentTable"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:book:del">
					<table:delRow url="${ctx}/bus/book/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:book:import">
					<table:importExcel url="${ctx}/bus/book/import"></table:importExcel><!-- 导入按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:book:export">
					<table:exportExcel url="${ctx}/bus/book/export"></table:exportExcel><!-- 导出按钮 -->
				</shiro:hasPermission>
			   <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			
			</div>
			<div class="pull-right">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
				<div class="form-group">
					<span>名称：</span>
						<form:input path="name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
					<span>作者：</span>
						<form:input path="author" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
					<shiro:hasRole name="system"><span>创建机构：</span>
							<sys:treeselect id="office" name="office.id" value="${book.office.id}" labelName="office.name" labelValue="${book.office.name}"
								title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/></shiro:hasRole>
				 </div>
				<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
				<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
			</div>
		</div>
	</form:form>
	
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column name">名称</th>
				<th  class="sort-column author">作者</th>
				<th  class="sort-column tags">标签</th>
				<th  class="sort-column office.name">来源机构</th>
				<th  class="sort-column viewcount">浏览次数</th>
				<th  class="sort-column originalId">原始编号</th>
				<th  class="sort-column createDate">同步时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="book">
			<tr>
				<td> <input type="checkbox" id="${book.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看小说', '${ctx}/bus/book/form?id=${book.id}','800px', '500px')">
					${book.name}
				</a></td>
				<td>
					${book.author}
				</td>
				<td>
					${book.tags}
				</td>
				<td>
					${book.office.name}
				</td>
				<td>
					${book.viewcount}
				</td>
				<td>
					${book.originalId}
				</td>
				<td>
					<fmt:formatDate value="${book.publishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="bus:book:view">
						<a href="#" onclick="openDialogView('查看小说', '${ctx}/bus/book/form?id=${book.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="bus:book:edit">
    					<a href="#" onclick="openDialog('修改小说', '${ctx}/bus/book/form?id=${book.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="bus:book:del">
						<a href="${ctx}/bus/book/delete?id=${book.id}" onclick="return confirmx('确认要删除该小说吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>