<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored ="false" %> 
<!DOCTYPE html>
<html>
	<head>
		<title>海洋气象格点精细化</title>
		<%@ include file="../common/taglib.jsp"%>
		<script type="text/javascript" src="${ctx }/js/system/system.js"></script>
	
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
				<%-- <%@include file="../common/msg.jsp" %> --%>
				
				<!-- 左侧菜单 -->		
				<div id="leftContext" style="width: 266px;min-height: 500px;float: left;text-align: center;">
					<%@include file="../common/menu.jsp" %>
				</div>
				
				<!-- 页面主体 -->
				<div id="mainContext" style="min-height: 40%;">
					<!-- 导航栏 -->
					<ol class="breadcrumb" style="margin-top: 10px;margin-left: 10px;background-color: white;width: 40%;">
					  <li><a href="${ctx }">首页/</a></li>
					  <li><a href="#">系统管理/</a></li>
					  <li class="active">系统监控</li>
					</ol>
					<hr style="width:85%">
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="moTarget active" target="mo1"><a href="javascript:void(0);">服务器状态</a></li>
				        <li role="presentation" class="moTarget" target="mo2"><a href="javascript:seach();">系统日志</a></li>
					</ul>
					
					<div class="moFo" of="mo1">
						<div style="font-size: 16px;line-height: 34px;padding-left: 100px;margin-top: 10px;float: left;">
							<p>CPU使用率：${requestScope.computer.cpuUsed}%</p>
							<p>
								操作系统：${requestScope.computer.coName}&nbsp;&nbsp;&nbsp;
								系统内核：${requestScope.computer.osDataModel}&nbsp;&nbsp;&nbsp;
							</p>
							<p>
								磁盘使用情况：总容量${requestScope.computer.ssdTotal }GB
							</p>
						</div>
						
						<div style="font-size: 16px;line-height: 34px;padding-left: 100px;margin-top: 10px;float: left;">
							<p>
								WEB端：${requestScope.computer.connecterAll - requestScope.computer.connecterClient}
							</p>
							<p>CPU内核数量：${requestScope.computer.cpuMhz}个</p>
							<p>
								已使用：${requestScope.computer.ssdUsed }GB&nbsp;&nbsp;&nbsp;
								未使用：${requestScope.computer.ssdTotal - requestScope.computer.ssdUsed }GB
							</p>
						</div>
						
						<div style="width: 85%;float: left;height: 30px;padding: 0px 30px;"><hr></div>
						
						<div id="container1" style="width: 47%;float: left;"></div>
						<div id="container2" style="width: 47%;float: left;"></div>
						<script type="text/javascript">
						$(function () {
							var memTotal = ${requestScope.computer.memTotal };//内存总量
							var memUsed = ${requestScope.computer.memUsed };//内存使用量
							
							var ssdTotal = ${requestScope.computer.ssdTotal };//磁盘总量
							var ssdUsed = ${requestScope.computer.ssdUsed };//磁盘使用量
							var series1 = {type: 'pie',name: '占比', size: '40%',
						            data: [
										{name: '已使用：' + memUsed+ "MB", color : "rgb(128, 133, 233)",y: memUsed},
										{name: '未使用：' + (memTotal - memUsed) + "MB",color : "rgb(247, 163, 92)",y: memTotal - memUsed,sliced: true,selected: true }
						            ]};
							
							var series2 = {type: 'pie',name: '占比', size: '40%',
						            data: [
										{name: '已使用：'+ ssdUsed+ "GB",color : "rgb(128, 133, 233)",y: ssdUsed},
										{name: '未使用：'+ (ssdTotal - ssdUsed)+ "GB",color : "rgb(247, 163, 92)",y: ssdTotal - ssdUsed,sliced: true,selected: true }
						            ]};
							
							createCharts('container1','内存使用情况',series1);
							createCharts('container2','磁盘使用情况',series2);
						});
					
						function createCharts(divId, title, series){
							var chart = $('#' + divId).highcharts({
						        chart: {type: 'pie',options3d: {enabled: true,alpha: 45,beta: 0}},
						        title: {text: title}, 
						        tooltip: {pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'},
						        credits : {enabled:false},
						        plotOptions: {
						            pie: {
						                allowPointSelect: true,
						                cursor: 'pointer',
						                dataLabels: {
						                    enabled: true,
						                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
						                    style: {
						                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
						                    }
						                }
						            }
						        },
						        plotOptions: {
						            pie: {allowPointSelect: true,cursor: 'pointer',depth: 35,
						                dataLabels: {enabled: true,format: '{point.name}'}
						            }
						        },
						        series: [series]
						    });
							
						}
					</script>
					</div>
					
					<div class="moFo" of="mo2">
						<!-- 查询面板 -->
						<div style="text-align: center;height: 50px;line-height: 50px;">
					    	记录时间：<input class="Wdate" id="beginTime" type="text" style="width:90px;height: 25px;">&nbsp;~&nbsp;
							时间：<input class="Wdate" id="endTime" type="text" style="width: 90px;height: 25px;">
							&nbsp;&nbsp;&nbsp;&nbsp;
							访问者IP：<input id="logIp" type="text" style="width: 90px;height: 25px;">
							&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="javascript:seach();" class="easyui-linkbutton" data-options="iconCls:'icon-search'" >查询</a>
						</div>
						
						<!-- 数据 -->
						<table id="tt" style="width: 1054px;"></table>
						
						<!-- 信息提示 -->
						<div style="background-color: #f37b1d;border-color: #e56c0c;color: #fff;text-align: center;">
							用户参数有可能涉及到用户隐私信息，请管理员自觉准守作为一个网络管理员的基本原则。否则后果自负。
						</div>
						
						<script type="text/javascript">
							$(function(){
								//初始化时间框
								$('#beginTime').bind('click', function () { WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd',autoPickDate: true}); });
								$('#endTime').bind('click', function () { WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd',autoPickDate: true}); });
								
								//初始化查询数据
								getPage();
								
							});
							
							//查询分页信息
							function getPage() {
								$('#tt').datagrid({//查询每一个根节点下这个人的oid
									idField : 'id',
									nowrap : true,
									url : '${ctx }/ipLog/page.do',
									checkOnSelect : false,
									ctrlSelect : true,
									striped : true,
									pagination : true,
									rownumbers : true,
									nowrap : false,
									columns : [ [
											{field : 'ip',title : 'IP',width : '149px',align : 'center'},
											{field : 'url',title : 'URL',width : '250px',align : 'center'},
											{field : 'method',title : '请求类型',width : '107px',align : 'center'},
											{field : 'params',title : '参数',width : '352px',align : 'center'},
											/* {field : 'userId',title : '访问ID',width : '50px',align : 'center'}, */
											{field : 'createTime',title : '访问时间',width : '160px',align : 'center',
												formatter : function(value,row,index) {
													return getSmpFormatDateByLong(value);
												}
											}]],
									pageSize : 20,
									pageList : [10,20],
									toolbar : "#tb"
								});
								$('#tt').datagrid('reload');
							}
							
							//查询
							function seach() {
								var query = {
									beginTime : $('#beginTime').val(),
									endTime : $('#endTime').val(),
									userName : $('#logUserName').val(),
									ip : $('#logIp').val()
								};
								$('#tt').datagrid('options').queryParams = query;
								$('#tt').datagrid('reload');
								$('#tt').datagrid('clearSelections').datagrid('clearChecked');
							}
						</script>
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