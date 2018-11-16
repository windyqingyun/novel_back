<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>素材管理</title>
	<meta name="decorator" content="default"/>
	 <link href="${ctxStatic}/summernote/summernote.css" rel="stylesheet">
	 <link href="${ctxStatic}/summernote/summernote-bs3.css" rel="stylesheet">
	 <script src="${ctxStatic}/summernote/summernote.min.js"></script>
	 <script src="${ctxStatic}/summernote/summernote-zh-CN.js"></script>
	 <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-select2/3.5.4/select2.css"/>
	<script src="${ctxStatic}/jquery-select2/3.5.4/select2.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-select2/3.5.4/select2_locale_zh-CN.js" type="text/javascript"></script>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  changeReq();
		  if(validateForm.form()){
			  var html = ue.getContent();
			  $("#content").val(html);   //取富文本的值
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		function changeReq(){
			var val = $("input[name='book.id']").val();
			if(val != null && val != ''){
				$("input[name='chapter']").addClass("required");
			}else{
				$("input[name='chapter']").removeClass("required");
			}
		}
		
		function strIsEmpty(str){
			return str == null || str == '';
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
			
			var bookInfoUrl = "http://39.106.4.97/nrfx_intertem/service/interface/bookChapter/info";
			
			$("#chapter").change(function(){
				var chapter = $(this).val();
				var bookId = $("input[name='book.id']").val();
				if(!strIsEmpty(chapter) && !strIsEmpty(bookId)){
					$("input[name=linkUrl]").val(bookInfoUrl);
				}
			});
			
			$('#bookId').change(function(){
				var bookId = $(this).val();
				
				$.ajax("${ctx}/bus/bookChapter/findHundredChapterList", {
					type: 'post',
					data: {bookId: bookId},
					success: function(rstData){
						$("#chapter").empty();
						$("#chapter").select2("destroy");
						$.each(rstData, function(i, item){
							console.log(item.chapter+" :"+item.title);
							$("#chapter").append('<option value="'+item.chapter+'">'+item.title+'</option>');
							 $("#chapter").select2();
						});
					}
				});
			});
			
			 $("#chapter").select2();
		});
	</script>
</head>
<body>
		<form:form id="inputForm" modelAttribute="fodder" action="${ctx}/bus/fodder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>标题：</label></td>
					<td class="width-85" colspan="3">
						<form:input path="title" htmlEscape="false" maxlength="64" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">标题图片：</label></td>
					<td class="width-85" colspan="3">
						<form:hidden id="titleImage" path="titleImage" htmlEscape="false" maxlength="250" class="form-control"/>
						<sys:ckfinder input="titleImage" type="images" uploadPath="/bus/fodder" selectMultiple="false"/>
						<%-- <sys:plupload dataName="nodArchiveList" maxUploadCount="1" name="titleImage" flieType="png,jpg,jpeg,bmp" hideRemarks="true"/> --%>
					</td>
				</tr>
				<tr class="hide">
					<td class="width-15 active"><label class="pull-right">链接地址：</label></td>
					<td class="width-85" colspan="3">
						<form:input path="linkUrl" htmlEscape="false" maxlength="250" class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">素材内容：</label></td>
					<td class="width-85" colspan="3">
						<form:hidden path="content"/>
						<textarea name="contexts" style="width:660px;height: 500px;" id="rich_contents">${fodder.content}</textarea>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">小说：</label></td>
					<td class="width-85" colspan="3">
						<sys:gridselect url="${ctx}/bus/fodder/selectbook" id="book" name="book.id"
						 value="${fodder.book.id}"  title="选择书籍" labelName="fodder.book.name"
						 labelValue="${fodder.book.name}" cssClass="form-control" fieldLabels="名称|作者|小说编号"
						 fieldKeys="name|author|originalId" searchLabel="名称|作者" searchKey="name|author">
						</sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">章节：</label></td>
					<td class="width-85" colspan="3">
						<form:select id="chapter" path="chapter" class="width-75" multiple="false">
							<c:forEach items="${chapterList }" var="chapter">
								<form:option value="${chapter.chapter }" label="${chapter.title }"></form:option>
							</c:forEach>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-85" colspan="3">
						<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
	 <!-- 配置文件 -->
   	<script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <!-- 实例化编辑器 -->
    <script type="text/javascript">
       	//修改上传地址
        UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
		UE.Editor.prototype.getActionUrl = function(action) {
		    if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadvideo' || action == 'uploadfile') {
		        return "${ctx}/bus/nodArchive/umEditorUpload";
		    } else {
		        return this._bkGetActionUrl.call(this, action);
		    }
		}
        var ue = UE.getEditor('rich_contents');
        
       
    </script>
</body>
</html>