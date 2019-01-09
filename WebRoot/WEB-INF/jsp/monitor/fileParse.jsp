<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <base href="<%=basePath%>"/>
    <%@ include file="../common/taglib.jsp"%>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>文件监控</title>
	<script type="text/javascript" src="${ctx }/js/monitor/fileParse.js"></script>
	<script type="text/javascript" src="${ctx }/js/util.js"></script>
	<style type="text/css">
		.datagrid-header-rownumber,.datagrid-cell-rownumber{
   			height:30px;
		 }
	</style>
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
<%-- 				<%@include file="../common/msg.jsp" %> --%>
				
				<!-- 左侧菜单 -->		
				<div style="width: 266px;height: 500px;float: left;text-align: center;">
					<%@include file="../common/menu.jsp" %>
				</div>
				
				<!-- 页面主体 -->
				<div id="mainContext" style="height: 1000px;">
				<!-- 导航bootstrap -->
				<ol class="breadcrumb" style="margin-top: 10px;margin-left: 10px;background-color:white;">
				  <li><a href="${ctx }">主页/</a></li>
				  <li><a href="#">文件解析/</a></li>
				  <li class="active">文件解析日志</li>
				</ol>
				<hr></hr>
				
			<div class="easyui-tabs" style="background-color:white;font-size:14px;plain;true;tabHeight:35px;">
				<!-- 日志 -->
				<div of="log"   role="tabpanel" title="文件解析日志">
				<!-- 搜索框start -->
				<form role="form" >
					<div style="text-align: center;height: 40px;line-height: 50px;">
						按日期查询：<input class="Wdate" placeholder="开始时间" style="height: 20px; width:120px; border: 1px solid gray;" id="startDate" type="text" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
					~
					<input class="Wdate" placeholder="结束时间" style="height: 20px; width:120px; border: 1px solid gray;" id="endDate" type="text" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
								
								排序
								<select id="orderBy" onchange="updateOption()" style="width:120px;">
									<option value="order by isnormal ">按状态排序</option>
							        <option value="order by monitorpath ">按抓取路径排序</option>
							        <option value="order by optime ">按抓取时间排序</option>
								</select>
								<input type="button" style="width: 60px;opacity:0;height: 10px;"/>
								排序方式
								<input type="radio" name="desc" value="asc" checked="checked" onclick="updateOption();"/>升序
								<input type="radio" name="desc" value="desc" onclick="updateOption();"/>降序
								<a href="javascript:updateOption();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" ><font style="font-size:12px;">查询</font></a>
					</div>
				</form>
				<!-- 搜索框end -->
					<table id ="parseLog" class="table table-striped table-bordered table-hover table-condensed">
				 
					</table>
					</div>
					
				<!-- 监控配置 -->
				<div of="task"  role="tabpanel" title="文件解析配置">
					<form role="form2" >
					<div style="text-align: center;height: 40px;line-height: 50px;">
						<table style="margin-left:150px;">	
							<tr>
								<td>
									<input id="like" type="text" style="width: 200px;height: 25px;" value="" placeholder="监控路径查询"/>
								</td>
								<td>
									<input type="button" style="width: 60px;opacity:0;height: 10px;"/>
									
								</td>
									<td>
									排序
									</td>
								<td>
									<select id="orderBy2" onchange="updateOption2()" style="width:120px;">
										<option value="order by monitorpath ">按路径排序</option>
										<option value="order by product_type ">按产品编号排序</option>
								        <option value="order by process_type ">按过程编号时间</option>
								         <option value="order by shellcode ">按状态时间</option>
									</select>
								</td>
								<td>
									排序方式
								</td>
								<td>
								<input type="radio" name="desc2" value="asc" checked="checked" onclick="updateOption2();"/>升序
								<input type="radio" name="desc2" value="desc" onclick="updateOption2();"/>降序
								</td>
								<td>
									<input type="button" style="height:25px;width:50px;font-size:15px;" class="'easyui-searchbox" value="查询" onclick="updateOption2()"/>
								</td>
								
								<td>
									<!-- <input id="ss" class="easyui-searchbox" style="width:300px"
   									 data-options="searcher:qq,prompt:'Please Input Value',menu:'#mm'"></input> -->
								</td>
							</tr>
						
						</table>
						
							
						<!-- 	<input type="button" style="width: 60px;opacity:0;height: 10px;"/>		
								
								
								<input type="button" style="width: 60px;opacity:0;height: 10px;"/>
								
								<input type="button" class="'easyui-searchbox" value="搜索"/>
								
								
								<a href="javascript:updateOption2();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a> -->
								
					</div>
				</form> 
				<!-- 搜索框end -->
					<table id ="task" class="table table-striped table-bordered table-hover table-condensed">
				 		
					</table>
					<div id="tb2">
						<a href="javascript:void(0)" data-toggle="modal" data-target="#addTask" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"></a>
						<a href="javascript:void(0)"  class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true"></a>
					</div>
					<!-- 模态框（Modal）修改任务配置 -->
	<div class="modal fade hide" id="updateTask" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" 
						aria-hidden="true">×
				</button>
				<h4 class="modal-title" id="myModalLabel">
					修改监控任务配置
				</h4>
			</div>
			<div class="modal-body">
			<form class="form-horizontal" role="form">
				<div class="form-group">
					<div class="col-sm-10">
						<input type="hidden" class="form-control" id="id"/>
					</div>
				</div>
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">文件路径:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="filePath" />
					</div>
				</div>
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">文件名称:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="fileName" />
					</div>
				</div>
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">迟到时间:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="endTime" />
					</div>
				</div>
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">失效时间:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="deadLine" />
					</div>
				</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" 
						data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary" onclick="saveTask()">
					保存
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!-- 增加监控配置 -->
	<div class="modal fade hide" id="addTask" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" 
						aria-hidden="true">×
				</button>
				<h4 class="modal-title" id="myModalLabel">
					增加监控任务配置
				</h4>
			</div>
			<div class="modal-body">
			<form class="form-horizontal" role="form">
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">文件名称:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="fileName_add" placeholder="不填表示路径下所有文件"/>
					</div>
				</div>
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">文件路径:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="filePath_add" placeholder="文件路径"/>
					</div>
				</div>
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">目的路径:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="toPath_add" placeholder="目的路径"/>
					</div>
				</div>
				
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">迟到时间:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="endTime_add" placeholder="迟到时间"/>
					</div>
				</div>
				<div class="form-group">
					<label for="lastname" class="col-sm-2 control-label">失效时间:&nbsp;</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" id="deadLine_add" placeholder="失效时间"/>
					</div>
				</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" 
						data-dismiss="modal">关闭
				</button>
				<button type="button" class="btn btn-primary" onclick="saveAddTask()">
					保存
				</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal-dialog -->
</div><!-- /.modal -->
					</div>
			
			
			<!-- 监控配置结束 -->
			
			
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