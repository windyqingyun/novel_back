<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>作品点击数据</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			laydate({
	            elem: '#beginDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
		});
		function page(n,s){
			$("#pageNo_").val(n);
			$("#pageSize_").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>作品点击数据 </h5>
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
			   <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
				</div>
			<div class="pull-right">
				<form:form id="searchForm" modelAttribute="statisticsForm" action="${ctx}/bus/statistics/bookClickCount" method="post" class="form-inline">
					<input id="pageNo_" name="pageNo" type="hidden" value="${page.pageNo}"/>
					<input id="pageSize_" name="pageSize" type="hidden" value="${page.pageSize}"/>
					<div class="form-group">
						<span>作品标题：</span>
							<form:input path="title" htmlEscape="false" maxlength="64" class="form-control input-sm"/>
						<c:if test="${fodderClick.groupType != '03'}"><span>用户名称：</span>
							<form:input path="formNickName" htmlEscape="false" maxlength="64" class="form-control input-sm"/></c:if>
						<span>分组类型：</span>
							<form:select path="groupType" class="form-control input-sm">
								<form:option value="01" label="全部数据"></form:option>
								<form:option value="02" label="按照用户分组"></form:option>
								<form:option value="03" label="按照作品分组"></form:option>
							</form:select>
						<shiro:hasRole name="system">
						<span>作品所属部门：</span>
							<sys:treeselect id="fodderOfficeId" name="fodderOfficeId" value="${fodderClick.fodderOfficeId}" labelName="fodderOfficeName" labelValue="${fodderClick.fodderOfficeName}"
								title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="false"/>
							<c:if test="${fodderClick.groupType != '03'}"><span>用户所属部门：</span>
								<sys:treeselect id="userOfficeId" name="userOfficeId" value="${fodderClick.userOfficeId}" labelName="userOfficeName" labelValue="${fodderClick.userOfficeName}"
									title="部门" url="/sys/office/treeData?type=2" cssClass="form-control input-sm" allowClear="true" notAllowSelectParent="false"/></c:if>
						</shiro:hasRole>
						<span>日期范围：</span>
							<input id="beginDate" name="beginDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${fodderClick.beginDate }" pattern="yyyy-MM-dd"/>"/>
							<label>&nbsp;--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
							<input id="endDate" name="endDate" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
								value="<fmt:formatDate value="${fodderClick.endDate }" pattern="yyyy-MM-dd"/>"/>
				  </div>
					<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
					<button class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
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
				<th class="">作品标题</th>
				<c:if test="${statisticsForm.groupType != '03'}"><th class="">用户昵称</th></c:if>
				<th class="">点击数量</th>
				<th class="">作品所属机构</th>
				<c:if test="${statisticsForm.groupType != '03'}"><th class="">用户所属机构</th></c:if>
				<c:if test="${statisticsForm.groupType != '03'}"><th class="">创建时间</th></c:if>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="item">
			<tr>
				<td>
					${item.bookTitle}
				</td>
				<c:if test="${statisticsForm.groupType != '03'}"><td>
					<c:choose>
						<c:when test="${item.nickName != ''}">
						${item.nickName}
						</c:when>
						<c:otherwise>
						游客
						</c:otherwise>
					</c:choose>
				</td></c:if>
				<td>
					${item.count}
				</td>
				<td>
					${item.bookOffice}
				</td>
				<c:if test="${statisticsForm.groupType != '03'}"><td>
					${item.originOffice}
				</td></c:if>
				<c:if test="${statisticsForm.groupType != '03'}"><td>
					<fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td></c:if>
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