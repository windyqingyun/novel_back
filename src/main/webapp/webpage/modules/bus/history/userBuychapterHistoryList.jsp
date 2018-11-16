<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>章节购买管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>章节购买列表 </h5>
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
	<form:form id="searchForm" modelAttribute="userBuychapterHistory" action="${ctx}/history/userBuychapterHistory/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>小说名称：</span>
				<form:input path="book.name" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
			<span>购买章节：</span>
				<form:input path="chapter" htmlEscape="false" maxlength="11"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="history:userBuychapterHistory:add">
				<table:addRow url="${ctx}/history/userBuychapterHistory/form" title="章节购买"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="history:userBuychapterHistory:edit">
			    <table:editRow url="${ctx}/history/userBuychapterHistory/form" title="章节购买" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="history:userBuychapterHistory:del">
				<table:delRow url="${ctx}/history/userBuychapterHistory/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="history:userBuychapterHistory:import">
				<table:importExcel url="${ctx}/history/userBuychapterHistory/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="history:userBuychapterHistory:export">
	       		<table:exportExcel url="${ctx}/history/userBuychapterHistory/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column book.id">小说名称</th>
				<th  class="sort-column chapter">购买章节</th>
				<th  class="sort-column originalprice">原价</th>
				<th  class="sort-column discount">折扣</th>
				<th  class="sort-column payCoin">支付阅读币</th>
				<th  class="sort-column bulkbuychapterHistoryId">是否是多章购买</th>
				<th  class="sort-column createBy.id">购买人</th>
				<th  class="sort-column createDate">购买时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userBuychapterHistory">
			<tr>
				<td> <input type="checkbox" id="${userBuychapterHistory.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看章节购买', '${ctx}/history/userBuychapterHistory/form?id=${userBuychapterHistory.id}','800px', '500px')">
					${userBuychapterHistory.book.name}
				</a></td>
				<td>
					${userBuychapterHistory.chapter}
				</td>
				<td>
					${userBuychapterHistory.originalprice}${fns:getCoinNameByOffice(userBuychapterHistory.book.office.id, '阅读币')}
				</td>
				<td>
					${userBuychapterHistory.discount}
				</td>
				<td>
					${userBuychapterHistory.payCoin}${fns:getCoinNameByOffice(userBuychapterHistory.book.office.id, '阅读币')}
				</td>
				<td>
					${not empty userBuychapterHistory.bulkbuychapterHistoryId ? '是':'否'}
				</td>
				<td>
					${userBuychapterHistory.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${userBuychapterHistory.createDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<shiro:hasPermission name="history:userBuychapterHistory:view">
						<a href="#" onclick="openDialogView('查看章节购买', '${ctx}/history/userBuychapterHistory/form?id=${userBuychapterHistory.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="history:userBuychapterHistory:edit">
    					<a href="#" onclick="openDialog('修改章节购买', '${ctx}/history/userBuychapterHistory/form?id=${userBuychapterHistory.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="history:userBuychapterHistory:del">
						<a href="${ctx}/history/userBuychapterHistory/delete?id=${userBuychapterHistory.id}" onclick="return confirmx('确认要删除该章节购买吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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