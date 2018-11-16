<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>小说管理</title>
	<meta name="decorator" content="default"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-select2/3.5.4/select2.css"/>
	<script src="${ctxStatic}/jquery-select2/3.5.4/select2.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-select2/3.5.4/select2_locale_zh-CN.js" type="text/javascript"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		function openBookChapterForm(){
			var id = $("#chapter").select2("val");
			top.layer.open({
			    type: 2,  
			    area: ['800px', '500px'],
			    title:"查看小说章节",
			    content: "${ctx}/bus/bookChapter/form?id="+id,
			    name:'friend',
			    btn: ['确定', '关闭'],
			    yes: function(index, layero){
					 top.layer.close(index);//关闭对话框。
				  },
				  cancel: function(index){ 
			      }
			}); 
			return false;
		}
		
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			laydate({
	            elem: '#publishDate', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        
	        $("#chapter").select2();
		});
	</script>
	<style type="text/css">
		.select2-choice{height: 50px}
	</style>
</head>
<body>
	<form:form id="inputForm" modelAttribute="book" action="${ctx}/bus/book/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="64" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">简介：</label></td>
					<td class="width-35">
						<form:input path="title" htmlEscape="false" maxlength="300" class="form-control"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">作者：</label></td>
					<td class="width-35">
						<form:input path="author" htmlEscape="false" maxlength="64" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>原始编号：</label></td>
					<td class="width-35">
						<form:input path="originalId" htmlEscape="false" maxlength="64" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">分类：</label></td>
		            <td class="width-35" colspan="2">
						<c:choose> 
						    <c:when test="${book.condition == '0'}"> 
						    	<input type="radio" name="condition" value="0" checked="checked"/>女频
						    	<input type="radio" name="condition" value="1"">男频
						 	</c:when>      
							<c:otherwise> 
								<input type="radio" name="condition" value="0"/>女频   
								<input type="radio" name="condition" value="1" checked="checked"/>男频
							</c:otherwise> 
						</c:choose>
		            </td>
				</tr>

				<tr>
					<td class="width-15 active"><label class="pull-right">标签：</label></td>
					<td class="width-85" colspan="3">
						<form:input path="tags" htmlEscape="false" maxlength="100" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">图片链接：</label></td>
					<td class="width-85" colspan="3">
						<form:input path="image" htmlEscape="false" maxlength="255" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>出版日期：</label></td>
					<td class="width-35">
						<input id="publishDate" name="publishDate" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${book.publishDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35">
					</td>
					
		  		</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">小说章节：${book.chapterList[o].id }</label></td>
					<td class="width-85" colspan="3">
						<form:select id="chapter" path="chapterList" class="width-75" multiple="false">
							<c:forEach items="${book.chapterList }" var="chapter">
								<form:option value="${chapter.id }" label="第${chapter.chapter }章:${chapter.title }"></form:option>
							</c:forEach>
						</form:select>
						<button class="btn btn-primary btn-sm " onclick="openBookChapterForm();return false"><i class="fa fa-search"></i> 查看</button>
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>