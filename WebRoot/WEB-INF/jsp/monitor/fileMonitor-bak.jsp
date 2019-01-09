<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <base href="<%=basePath%>">
    <%@ include file="../common/taglib.jsp"%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>文件监控</title>
	<script type="text/javascript" src="${ctx }/js/monitor/fileMonitor.js"></script>
</head>
<body>
<div style="width: 1350px;margin: 0 auto;">
			<!-- 第一层，内容，导航栏 -->
			<div id="firstContext">
		        <%@include file="../common/header.jsp" %>
		    </div>
		    
		    <!-- 第二层，内容 -->
		    <div id="secondContext">
			   	<!-- 滚动窗口 -->
				<%@include file="../common/msg.jsp" %>
				
				<!-- 左侧菜单 -->		
				<div style="width: 266px;height: 500px;float: left;text-align: center;">
					<%@include file="../common/menu.jsp" %>
				</div>
				
				<!-- 页面主体 -->
				<div id="mainContext" style="height: 1000px;">
				<!-- 导航bootstrap -->
				<ol class="breadcrumb" style="background-color:white;">
				  <li><a href="${ctx }">主页</a></li>
				  <li><a href="#">文件监控</a></li>
				  <li class="active">文件监控日志</li>
				</ol>
				<!-- tab标签 -->
				<ul class="nav nav-tabs" role="tablist">
				  <li ><a  href="#log"  data-toggle="tab">日志</a></li>
				  <li  class="active"><a href="#setLog"  data-toggle="tab">文件监控配置</a></li>
				</ul>
				
				<div id="myTabContent" class="tab-content">
				
					<div class="tab-pane fade in active" id="log">
					<div>
						<form role="form2" >
							<div style="text-align: center;height: 40px;line-height: 50px;">
								<input id="like" type="text" style="width: 200px;height: 25px;" value="" placeholder="文件名或者文件路径查询"/>
									<a href="javascript:updateOption2();" style="height:25px;" class="btn btn-xs" data-options="iconCls:'icon-search'" >查询</a>
									<input type="button" style="width: 60px;opacity:0;height: 10px;"/>		
										排序
										<select id="orderBy2" onchange="updateOption2()" style="width:120px;">
											<option value="order by fileName ">按文件名排序</option>
											<option value="order by filePath ">按路径排序</option>
									        <option value="order by endTime ">按迟到时间</option>
									         <option value="order by deadLine ">按失效时间</option>
									        
										</select>
										<input type="button" style="width: 60px;opacity:0;height: 10px;"/>
										排序方式
										<input type="radio" name="desc2" value="asc" checked="checked" onclick="updateOption2();"/>升序
										<input type="radio" name="desc2" value="desc" onclick="updateOption2();"/>降序
										<!-- <a href="javascript:updateOption2();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a> -->
							</div>
					</form>
					</div>
					<div>
					<table id ="dg2" class="table table-striped table-bordered table-hover table-condensed">
				 	
					</table>
					</div>
<!-- 					<form role="form" >
					<div style="text-align: center;height: 40px;line-height: 50px;">
						按日期查询：<input class="Wdate" placeholder="开始时间" style="height: 20px; width:120px; border: 1px solid gray;" id="beginDate" type="text" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
					~
					<input class="Wdate" placeholder="结束时间" style="height: 20px; width:120px; border: 1px solid gray;" id="endDate" type="text" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
								
								排序
								<select id="orderBy" onchange="updateOption()" style="width:120px;">
									<option value="order by fileName ">按文件名排序</option>
							        <option value="order by lastmodifiedtime ">按抓取时间排序</option>
							        <option value="order by status ">按状态</option>
								</select>
								<input type="button" style="width: 60px;opacity:0;height: 10px;"/>
								排序方式
								<input type="radio" name="desc" value="asc" checked="checked" onclick="updateOption();"/>升序
								<input type="radio" name="desc" value="desc" onclick="updateOption();"/>降序
								<a href="javascript:updateOption();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a>
					</div>
				</form>
					<table id ="dg" class="table table-striped table-bordered table-hover table-condensed">
				 
					</table> -->
					</div>
					<div class="tab-pane " id="setLog">
					dsf
				
						
					</div>
					
				
				</div>
				

				
					

	
				</div>	
		    </div>
			
			<!-- 第三层，内容，脚 -->
			<div id="footer">
				<%@include file="../common/footer.jsp" %>
			</div>
			
		</div>
	
</body>
</html>