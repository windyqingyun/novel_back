<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>支付单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//外部js调用
	        laydate({
	            elem: '#beginPayDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endPayDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>支付单列表 </h5>
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
	<form:form id="searchForm" modelAttribute="paymentBill" action="${ctx}/bus/paymentBill/" method="post" class="form-inline">
		 <div class="col-sm-12">
			<div class="pull-left">
				<shiro:hasPermission name="bus:paymentBill:edit">
					<table:editRow url="${ctx}/bus/paymentBill/form" title="支付单" id="contentTable"></table:editRow><!-- 编辑按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:paymentBill:import">
					<table:importExcel url="${ctx}/bus/paymentBill/import"></table:importExcel><!-- 导入按钮 -->
				</shiro:hasPermission>
				<shiro:hasPermission name="bus:paymentBill:export">
					<table:exportExcel url="${ctx}/bus/paymentBill/export"></table:exportExcel><!-- 导出按钮 -->
				</shiro:hasPermission>
			   <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
			
				</div>
			<div class="pull-right">
				<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
				<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
				<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
				<div class="form-group">
					<span>用户：</span>
						<sys:treeselect id="user" name="user.id" value="${paymentBill.user.id}" labelName="user.name" labelValue="${paymentBill.user.name}"
							title="用户" url="/sys/office/treeData?type=3" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
					<span>支付渠道：</span>
						<form:select path="payChannel"  class="form-control m-b">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pay_channel')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					<span>充值小说站：</span>
						<sys:treeselect id="office" name="office.id" value="${paymentBill.office.id}" labelName="office.name" labelValue="${paymentBill.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
					<span>支付日期范围：&nbsp;</span>
						<input id="beginPayDate" name="beginPayDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${paymentBill.beginPayDate }" pattern="yyyy-MM-dd"/>"/>
						<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
						<input id="endPayDate" name="endPayDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
							value="<fmt:formatDate value="${paymentBill.endPayDate }" pattern="yyyy-MM-dd"/>"/>
					<span>支付结果：</span>
						<form:select path="issuccess"  class="form-control m-b">
							<form:options items="${fns:getDictList('is_success')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					<span>用户来源：</span>
						<sys:treeselect id="user_office" name="user.office.id" value="${paymentBill.user.office.id}" labelName="user.office.name" labelValue="${paymentBill.user.office.name}"
							title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="true"/>
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
				<th  class="sort-column id">编号</th>
				<th  class="sort-column user.name">用户</th>
				<th class="">用户来源</th>
				<th  class="sort-column price">支付金额（元）</th>
				<th  class="sort-column payChannel">支付渠道</th>
				<th  class="sort-column office.name">充值小说站</th>
				<th  class="resultCoin">充值阅读币</th>
				<th  class="sort-column payDate">支付时间</th>
				<th  class="successDate">成功时间</th>
				<th  class="issuccess">支付结果</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="paymentBill">
			<tr>
				<td> <input type="checkbox" id="${paymentBill.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看支付单', '${ctx}/bus/paymentBill/form?id=${paymentBill.id}','800px', '500px')">
					${paymentBill.id}
				</a></td>
				<td>
					${paymentBill.user.name}
				</td>
				<td>
					${paymentBill.user.office.name}
				</td>
				<td>
					${paymentBill.price}
				</td>
				<td>
					${fns:getDictLabel(paymentBill.payChannel, 'pay_channel', '')}
				</td>
				<td>
					${paymentBill.office.name}
				</td>
				<td>
					${paymentBill.resultCoin}
				</td>
				<td>
					<fmt:formatDate value="${paymentBill.payDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${paymentBill.successDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(paymentBill.issuccess, 'is_success', '')}
				</td>
				<td>
					<shiro:hasPermission name="bus:paymentBill:view">
						<a href="#" onclick="openDialogView('查看支付单', '${ctx}/bus/paymentBill/form?id=${paymentBill.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
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