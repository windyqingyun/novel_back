<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>机构货币配置管理</title>
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
		<h5>机构货币配置列表 </h5>
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
				<shiro:hasPermission name="bus:officeCoinConfig:add">
					<table:addRow url="${ctx}/bus/officeCoinConfig/form" title="机构货币配置"></table:addRow><!-- 增加按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:officeCoinConfig:edit">
					<table:editRow url="${ctx}/bus/officeCoinConfig/form" title="机构货币配置" id="contentTable"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:officeCoinConfig:del">
					<table:delRow url="${ctx}/bus/officeCoinConfig/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:officeCoinConfig:import">
					<table:importExcel url="${ctx}/bus/officeCoinConfig/import"></table:importExcel><!-- 导入按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:officeCoinConfig:export">
					<table:exportExcel url="${ctx}/bus/officeCoinConfig/export"></table:exportExcel><!-- 导出按钮 -->
				</shiro:hasPermission>
			   <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			
				</div>
			<div class="pull-right">
				<form:form id="searchForm" modelAttribute="officeCoinConfig" action="${ctx}/bus/officeCoinConfig/" method="post" class="form-inline">
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
						<div class="form-group">
							<shiro:hasRole name="system">
								<span>机构：</span>
									<sys:treeselect id="office" name="office.id" value="${officeCoinConfig.office.id}" labelName="office.name" labelValue="${officeCoinConfig.office.name}"
										title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
							</shiro:hasRole> 
							<span>阅读币名称：</span>
								<form:input path="coinname" htmlEscape="false" maxlength="64"  class=" form-control input-sm"/>
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
				<th  class="sort-column officesimplyname">机构简称</th>
				<th  class="sort-column office.name">机构</th>
				<th  class="sort-column coinname">阅读币名称</th>
				<th  class="sort-column coinname">阅读币汇率</th>
				<th  class="remarks">备注信息</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="officeCoinConfig">
			<tr>
				<td> <input type="checkbox" id="${officeCoinConfig.id}" class="i-checks"></td>
				<td>
					${officeCoinConfig.officesimplyname}
				</td>
				<td>
					${officeCoinConfig.office.name}
				</td>
				<td>
					${officeCoinConfig.coinname}
				</td>
				<td>
					${officeCoinConfig.coinRate}
				</td>
				<td>
					${officeCoinConfig.remarks}
				</td>
				<td>
					<shiro:hasPermission name="bus:officeCoinConfig:view">
						<a href="#" onclick="openDialogView('查看机构货币配置', '${ctx}/bus/officeCoinConfig/form?id=${officeCoinConfig.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="bus:officeCoinConfig:edit">
    					<a href="#" onclick="openDialog('修改机构货币配置', '${ctx}/bus/officeCoinConfig/form?id=${officeCoinConfig.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="bus:officeCoinConfig:del">
						<a href="${ctx}/bus/officeCoinConfig/delete?id=${officeCoinConfig.id}" onclick="return confirmx('确认要删除该机构货币配置吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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