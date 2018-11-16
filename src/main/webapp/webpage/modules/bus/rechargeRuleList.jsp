<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>充值规则管理</title>
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
		<h5>充值规则列表 </h5>
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
	
		<div class="col-sm-12">
			<div class="pull-left">
				<shiro:hasPermission name="bus:rechargeRule:add">
					<table:addRow url="${ctx}/bus/rechargeRule/form" title="充值规则"></table:addRow><!-- 增加按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:rechargeRule:edit">
					<table:editRow url="${ctx}/bus/rechargeRule/form" title="充值规则" id="contentTable"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:rechargeRule:del">
					<table:delRow url="${ctx}/bus/rechargeRule/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:rechargeRule:import">
					<table:importExcel url="${ctx}/bus/rechargeRule/import"></table:importExcel><!-- 导入按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:rechargeRule:export">
					<table:exportExcel url="${ctx}/bus/rechargeRule/export"></table:exportExcel><!-- 导出按钮 -->
				</shiro:hasPermission>
			   <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			
			</div>
			<div class="pull-right">
				<form:form id="searchForm" modelAttribute="rechargeRule" action="${ctx}/bus/rechargeRule/" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
					<div class="form-group">
						<shiro:hasRole name="system">
							<span>机构：</span>
							<sys:treeselect id="office" name="office.id" value="${rechargeRule.office.id}" labelName="office.name" labelValue="${rechargeRule.office.name}"
								title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
						</shiro:hasRole>
						<span>规则名称：</span>
							<form:input path="name" htmlEscape="false" maxlength="100"  class=" form-control input-sm"/>
					 </div>	
					<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
					<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
				</form:form>
			</div>
		</div>
	
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
				<th  class="sort-column office.name">机构</th>
				<th  class="sort-column name">规则名称</th>
				<th  class="sort-column price">充值金额</th>
				<th  class="sort-column rstCoin">返回阅读币</th>
				<th  class="sort-column enable">是否启用</th>
				<th  class="sort-column remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="rechargeRule">
			<tr>
				<td> <input type="checkbox" id="${rechargeRule.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看充值规则', '${ctx}/bus/rechargeRule/form?id=${rechargeRule.id}','800px', '500px')">
					${rechargeRule.office.name}
				</a></td>
				<td>
					${rechargeRule.name}
				</td>
				<td>
					${rechargeRule.price}
				</td>
				<td>
					${rechargeRule.rstCoin}
				</td>
				<td>
					${fns:getDictLabel(rechargeRule.enable, 'yes_no', '')}
				</td>
				<td>
					${rechargeRule.remarks}
				</td>
				<td>
					<shiro:hasPermission name="bus:rechargeRule:view">
						<a href="#" onclick="openDialogView('查看充值规则', '${ctx}/bus/rechargeRule/form?id=${rechargeRule.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="bus:rechargeRule:edit">
    					<a href="#" onclick="openDialog('修改充值规则', '${ctx}/bus/rechargeRule/form?id=${rechargeRule.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="bus:rechargeRule:del">
						<a href="${ctx}/bus/rechargeRule/delete?id=${rechargeRule.id}" onclick="return confirmx('确认要删除该充值规则吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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