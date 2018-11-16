<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="url" type="java.lang.String" required="false" description="上传的地址" %>
<%@ attribute name="flieType" type="java.lang.String" required="false" description="上传文件的类型,默认为系统设置 web.uploadFileType" %>
<%@ attribute name="maxFileSize" type="java.lang.String" required="false" description="上传文件的最大长度,支持kb,mb如1kb,1mb等 默认单位是b web.maxUploadSize" %>
<%@ attribute name="maxUploadCount" type="java.lang.String" required="false" description="上传文件的最大上传个数" %>
<%@ attribute name="filesAdded" type="java.lang.String" required="false" description="添加文件执行的js方法名,不填按默认方法执行" %>
<%@ attribute name="uploadProgress" type="java.lang.String" required="false" description="上传文件上传进度,不填按默认方法执行" %>
<%@ attribute name="error" type="java.lang.String" required="false" description="上传失败执行的js方法"%>
<%@ attribute name="fileUploaded" type="java.lang.String" required="false" description="单个文件上传完成后执行的方法" %>
<%@ attribute name="dataName" type="java.lang.String" required="true" description="附件列表对应的列表名称" %>
<%@ attribute name="name" type="java.lang.String" required="false" description="提交的名称" %>
<%@ attribute name="hideList" type="java.lang.Boolean" required="false"  description="若为true 则下载列表不显示, dataName仅用于 控件的事件帮定,可自定义列表的展现形式 "%>
<%@ attribute name="hideRemarks" type="java.lang.Boolean" required="false"  description="若为true 则隐藏备注"%>
<%@ attribute name="disabled" type="java.lang.Boolean" required="false"  description="是否可修改"%>

<script type="text/javascript" src="${ctxStatic}/plupload-2.1.9/js/plupload.full.min.js"></script>
<a id="${dataName}_pickfiles" class="btn selectFile btn-success" href="javascript:;">选择文件</a> 
<a id="${dataName}_uploadfiles" class="btn uploadFile btn-primary"  href="javascript:;">上传文件</a>
<c:if test="${empty hideList || hideList}">
	<div id="${dataName}_list">
	    <c:forEach items="${requestScope[dataName]}" var="archive">
	    	<div id="${archive.id}" class="alert alert-success file-item"> 
	    		<button type="button" class="close" data-dismiss="alert">×</button>
	    		<div class="file-content">
	    			<a href="${ctx}/bus/nodArchive/download?id=${archive.id}" target="_blank" class="filename">${archive.fileName}.${archive.extension}</a> 
	    			<b><span class="badge badge-success">100%</span></b>
	    		</div>
	    		<%-- <div class="file-depict">
	    		<c:if test="${empty hideRemarks || hideRemarks}">
	    			<label class="control-label">描述:</label><span>${archive.remarks}</span>
	    		</c:if>
	    		</div> --%>
	    		<input type="hidden" <c:choose><c:when test="${empty name}">name="nodArchiveIds"</c:when><c:otherwise>name="${name}"</c:otherwise></c:choose> value="${archive.id}">
	    	</div>
	   </c:forEach>
	</div>
</c:if>


<script type="text/javascript">
var ${dataName}_uploader = new plupload.Uploader({
	        browse_button :'${dataName}_pickfiles',
			runtimes : 'flash,html5,html4',
			url : '<c:choose><c:when test="${empty url}">${ctx}/bus/nodArchive/upload</c:when><c:otherwise>${url}</c:otherwise></c:choose>',
			unique_names : true,
			multiple_queues:true,
			filters : {
				mime_types: [
					//允许的上传的文件类型
					{title : "文件", extensions : "<c:choose><c:when test="${empty flieType}">${fns:getConfig('web.uploadFileType')}</c:when><c:otherwise>${flieType}</c:otherwise></c:choose>"}
				],
				//上传文件的大小最大为
				max_file_size :<c:choose><c:when test="${empty maxFileSize}">${fns:getConfig('web.maxUploadSize')}</c:when><c:otherwise>${maxFileSize}</c:otherwise></c:choose> 
		    },
			init : {
					PostInit: function() {
						document.getElementById('${dataName}_uploadfiles').onclick = function() {
							${dataName}_uploader.start();
							return false;
						};
					},
					FilesAdded: function(up, files) {
						// 上传文件的数量
						// <c:choose><c:when test="${empty filesAdded}">
							var maxUploadCount = <c:choose><c:when test="${empty maxUploadCount}">${fns:getConfig('web.maxUploadCount')}</c:when><c:otherwise>${maxUploadCount}</c:otherwise></c:choose>;
							if($("#${dataName}_list").find(".alert").size() + files.length > maxUploadCount){ 
		            			//up.splice(maxUploadCount,999);
		            			alert("最多只能上传"+maxUploadCount+"个文件!");
		        			}else{
		        				plupload.each(files, function(file) {
		        					var html='<div id="' + file.id + 
									'" class="alert alert-info file-item"> <button type="button" class="close" data-dismiss="alert">×</button><div class="file-content"><a href="javascript:;" target="_blank" class="filename">'
									+ file.name + '</a> <span class="file-size">(' + plupload.formatSize(file.size) + ')</span><b></b>'+
									'</div>';
									html+='</div>';
									$("#${dataName}_list").append(html);
								});
		        			}
							//</c:when><c:otherwise>
								${filesAdded}();
							//</c:otherwise></c:choose>
					},
					UploadProgress: function(up, file) {
						//<c:choose><c:when test="${empty uploadProgress}">
						
						//	</c:when><c:otherwise>
								${uploadProgress}();
						//</c:otherwise></c:choose>
					},
					Error: function(up, err) {
						//<c:choose><c:when test="${empty error}">
						$("#"+err.file.id).find("b").first().html('<span  class="badge badge-error" >' + err.code + ": " + err.message + "</span>");
						$("#"+err.file.id).removeClass("alert-info").addClass("alert-error");						
						//	</c:when><c:otherwise>
								${error}();
							//</c:otherwise></c:choose>
					
					},
					//单个文件上传成功后
           		    FileUploaded: function(up, file, info) {
						//<c:choose><c:when test="${empty fileUploaded}">
							//<pre>出现在IE9，<PRE>出现在IE8
							var obj=eval("("+info.response.replace("<pre>","")
														  .replace("</pre>","")
														  .replace("<PRE>","")
														  .replace("</PRE>","")+")");
							if (obj.resultCode == "1"){
								var filelist = [];
								try{
									filelist = eval("("+obj.resultMsg+")");
								}catch (e) {
									alert("上传文件失败！请重新上传文件")
								}
								$("#"+file.id).find("b").html('<span  class="badge badge-success" >' + file.percent + "%</span>");
								for(var i=0;i<filelist.length;i++){
									$("#"+file.id).append("<input type='hidden'  <c:choose><c:when test="${empty name}">name='nodArchiveIds'</c:when><c:otherwise>name='${name}'</c:otherwise></c:choose>  value='"+filelist[i].id+"'>");
									$("#"+file.id).removeClass("alert-info").addClass("alert-success").find(".filename").attr("href","${ctx}/bus/nodArchive/download?id="+filelist[i].id);
								}
								//<c:if test="${empty hideRemarks || hideRemarks}">
								var remarks=$("#"+file.id).find("input[name=remarks]").val();
								$("#"+file.id).find("input[name=remarks]").remove();;
								$("#"+file.id).find(".file-depict").append("<span>"+remarks+"</span>");
								//</c:if>
							}else{
								$("#"+file.id).find("b").first().html('<span  class="badge badge-error" >上传文件失败 '  + ": " + obj.resultMsg + "</span>")
							}					
						//	</c:when><c:otherwise>
								${FileUploaded}();
							//</c:otherwise></c:choose>
            		},
					BeforeUpload:function(up,file){
						//<c:if test="${empty hideRemarks || hideRemarks}">
	                    up.setOption("multipart_params",{"remarks":$("#"+file.id).find("input[name=remarks]").val()}); 
	                    //</c:if>
	               }
		}
	});
	${dataName}_uploader.init();
	//<c:if test="${not empty disabled && disabled}">
	$(".selectFile,.uploadFile").hide();
	//</c:if>
</script>
