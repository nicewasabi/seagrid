<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/taglib.jsp"%>
<html>
	<head>
		<title>海洋气象精细化格点预报</title>
		<style type="text/css">
			.border_condition_main {text-align: left;padding: 15px 0px 10px 10px;}
			select {height:25px;width:60px;margin:0}
			#stationCode,#stnCode {width:150px;height:30px;}
			input[type=button] {width:40px;height:25px;}
			#searchBtn,#drawBtn,#drawMaxWindBtn,#errCheckSearchBtn {width:50px;}
			#showGraph {width:auto;height:700px;border-top: 1px solid gray}
			#showErrCheckGraph {width:auto;height:600px;border-top: 1px solid gray}
			.pannel {width: 80px;height: 645px;background-color: gray;opacity: 0.4;cursor: pointer;}
		</style>
		<script type="text/javascript" src="${ctx}/js/simpleFocus/simpleFocus.js"></script>
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
					  	<li><a href="${ctx}">首页/</a></li>
					  	<li><a href="javascript:void(0)">风场订正/</a></li>
					  	<li class="active">10米平均风</li>
					</ol>
					<hr style="width:100%">
					
					<div class="easyui-tabs" style="width:100%;">
						<div title="风速逐3小时变化图">
							<div class="border_condition_main">
								起报时间：
								<input class="Wdate" id="datetime" type="text" style="width:90px;max-width:90px;height:25px;margin:0" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
								<select id="hour">
									<option>08</option>
									<option>20</option>
								</select>
								<input id="subDatetimeBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
								<input id="addDatetimeBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
								&nbsp;
								时效：
								<select id="timelimit">
									<c:forEach begin="3" end="72" step="3" varStatus="hour">
										<option><fmt:formatNumber pattern="000" value="${hour.index}"></fmt:formatNumber></option>
									</c:forEach>
								</select>
								<input id="subTimelimitBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
								<input id="addTimelimitBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
								&nbsp;
								<input id="drawBtn" type="button" value="绘图" class="easyui-linkbutton btn"/>
							</div>
							<div style="height:654px;border:1px solid gray;">
								<table style="width:100%;height:100%">
									<tr>
										<td>
											<div id="leftPannel" class="pannel">
												<img src="${ctx}/images/simpleFocus/left_pannel.png" style="margin:250px 17px;"/>
											</div>
										</td>
										<td>
											<div id="imgDiv" style="width:870px;height:653px;text-align:center;margin:auto">
											</div>
										</td>
										<td>
											<div id="rightPannel" class="pannel">
												<img src="${ctx}/images/simpleFocus/right_pannel.png" style="margin:250px 17px;"/>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</div>
						<div title="最大风逐12小时变化图">
							<div class="border_condition_main">
								起报时间：
								<input class="Wdate" id="maxWindDatetime" type="text" style="width:90px;max-width:90px;height:25px;margin:0" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
								<select id="maxWindHour">
									<option>08</option>
									<option>20</option>
								</select>
								<input id="subMaxWindDatetimeBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
								<input id="addMaxWindDatetimeBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
								&nbsp;
								时效：
								<select id="maxWindTimelimit">
									<c:forEach begin="12" end="72" step="12" varStatus="hour">
										<option><fmt:formatNumber pattern="000" value="${hour.index}"></fmt:formatNumber></option>
									</c:forEach>
								</select>
								<input id="subMaxWindTimelimitBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
								<input id="addMaxWindTimelimitBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
								&nbsp;
								<input id="drawMaxWindBtn" type="button" value="绘图" class="easyui-linkbutton btn"/>
							</div>
							<div style="height:654px;border:1px solid gray;">
								<table style="width:100%;height:100%">
									<tr>
										<td>
											<div id="maxWindLeftPannel" class="pannel">
												<img src="${ctx}/images/simpleFocus/left_pannel.png" style="margin:250px 17px;"/>
											</div>
										</td>
										<td>
											<div id="maxWindImgDiv" style="width:870px;height:653px;text-align:center;margin:auto">
											</div>
										</td>
										<td>
											<div id="maxWindRightPannel" class="pannel">
												<img src="${ctx}/images/simpleFocus/right_pannel.png" style="margin:250px 17px;"/>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</div>
						<div title="风速风向逐小时变化趋势">
							<div class="border_condition_main">
								起报时间：
								<input class="Wdate" id="timeStart" type="text" style="width:90px;max-width:90px;height:25px;margin:0" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
								<select id="batch">
									<option>08</option>
									<option>20</option>
								</select>
								<input id="subTimeStartBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
								<input id="addTimeStartBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
								&nbsp;
								浮标名称：
								<select id="stationCode">
								</select>
								&nbsp;
								<input id="searchBtn" type="button" value="绘图" class="easyui-linkbutton btn"/>
							</div>
							<div id="showGraph"></div>
						</div>
						<!-- <div title="平均风误差检验">
							<div class="border_condition_main">
								实况时间：
								<input class="Wdate" id="liveTime" type="text" style="width:90px;max-width:90px;height:25px;margin:0" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
								<select id="errCheckHour">
									<option>08</option>
									<option>20</option>
								</select>
								<input id="subLiveTimeBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
								<input id="addLiveTimeBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
								&nbsp;
								浮标名称：
								<select id="stnCode">
								</select>
								&nbsp;
								<input id="errCheckSearchBtn" type="button" value="绘图" class="easyui-linkbutton btn"/>
							</div>
							<div id="showErrCheckGraph"></div>
						</div> -->
					</div>
				</div>
		    </div>
			
			<!-- 第三层，内容，脚 -->
			<div id="footer">
				<%@include file="../common/footer.jsp" %>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		// 添加菜单栏选中标记
		$("#201").addClass("selectedTitle");
		// 初始化起报时间
		var date = new Date();//获得当前的北京时间
		var datetime = getFormatDate(new Date(date.getTime() - 24 * 60 * 60 * 1000), "yyyyMMdd");
		$("#timeStart").val(datetime);
		$("#datetime").val(datetime);
		$("#maxWindDatetime").val(datetime);
		$("#liveTime").val(datetime);
		
		$.post("${ctx}/meanWind/getBuoyStationInfos.do", function(data){
			if (data != null && data.length > 0) {
				for (var i = 0; i < data.length; i++) {
					var stationCode = data[i].stationCode;// 站点编号
					var lon = data[i].lon;// 经度
					var lat = data[i].lat;// 纬度
					var buoyName = data[i].buoyName;// 浮标名称
					$("#stationCode").append("<option value='" + stationCode + "_" + lon + "_" + lat + "'>" + buoyName + "</option>");
					$("#stnCode").append("<option value='" + stationCode + "_" + lon + "_" + lat + "'>" + buoyName + "</option>");
				}
			}
		});
		
		// 初始化绘制图像
		showImage(datetime + "08", "003");
		// 初始化绘制最大风图像
		showMaxWindImage(datetime + "08", "012");
		// 初始化绘制图表
		showGraph(datetime + "08", "辽宁大浮标", "54558", 120.58, 39.25);
		// 初始化绘制平均风误差检验图表
		showErrCheckGraph(datetime + "08", "辽宁大浮标", "54558", 120.58, 39.25);
		
		// 绘制图表
		$("#searchBtn").click(function() {
			showGraphProcess();
		});
		
		// 切换站点自动绘图
		$("#stationCode").change(function() {
			showGraphProcess();
		});
		
		// 切换时次重新绘图
		$("#batch").change(function() {
			showGraphProcess();
		});
		
		// 时间时次往前推
		$("#subTimeStartBtn").click(function(){
			subTimeByInterval("hour", "batch", "timeStart", "yyyyMMdd");
			showGraphProcess();
		});
		
		// 时间时次往后推
		$("#addTimeStartBtn").click(function(){
			addTimeByInterval("hour", "batch", "timeStart", "yyyyMMdd");
			showGraphProcess();
		});
		
		// 图形绘制处理
		function showGraphProcess() {
			var timeStart = $("#timeStart").val();
			var batch = $("#batch").val();
			var datetime = timeStart + batch;
			var buoyName = $("#stationCode").find("option:selected").text();// 浮标名称
			var stationInfo = $("#stationCode").val().split("_");
			var stationCode = stationInfo[0];
			var lon = stationInfo[1];
			var lat = stationInfo[2];
			showGraph(datetime, buoyName, stationCode, lon, lat);
		}
		
		// 绘制图表
		function showGraph(datetime, buoyName, stationCode, lon, lat) {
			var args = {datetime: datetime, stationCode: stationCode};
			$.post("${ctx}/meanWind/getWindSpdAndirData.do", args, function(data){
				if (data.yDatas == null) {
					alert("指定时间的站点数据不存在");
					return;
				}
				
				var windSpeed = [];// 风速
				var windDirection = [];// 风向
				var maxWind = [];// 阵风
				var len = data.yDatas.length;
				
				if (len > 0) {
					for (var i=0; i<len; i++) {
						if (data.yDatas[i] != null) {
							windSpeed[i] = data.yDatas[i].windSpeed;// 风速
						} else {
							windSpeed[i] = null;
						}
					}
					// 获取最大的风速值
					var maxWindSpd = Math.max.apply(null, windSpeed);
					maxWindSpd = maxWindSpd + 1;
					// 风向显示在风速上方
					for (var j=0; j<len; j++) {
						if (data.yDatas[j] != null) {
							var windSpd = windSpeed[j];
							var windDir = data.yDatas[j].windDirection;// 风向
							windDirection[j] = {
								y: maxWindSpd,
								marker : {
									symbol : "url(${ctx}/meanWind/drawArrowByAngle.do?windSpd=" + windSpd + "&windDir=" + windDir + ")"
								}
							};
						} else {
							windDirection[j] = null;
						}
					}
				}
				
				// 阵风
				if (data.maxWindDatas != null && data.maxWindDatas.length > 0) {
					for (var i = 0; i < len; i++) {
						var maxWindSpeed = data.maxWindDatas[i];
						if (maxWindSpeed != null) {
							maxWind[i] = {
								y: maxWindSpeed,
								marker : {
									symbol : "url(${ctx}/images/ui/triangle.png)"
								}
							};
						} else {
							maxWind[i] = null;
						}
					}
				}
				
				$("#showGraph").highcharts({
					chart: {
						zoomType:"x",
						width: 1054
					},
					colors:["#0000FF","#FF0000","#000000","#1A9303","#C95A03","#5A5FBC","#DE0367","#D6B900","#038686"],
					title: {
						text: "<font color='blue'>风速风向逐小时变化趋势</font>",
						useHTML: true,
						style:{
							fontWeight: 'bold',
							fontSize:'22px'
						}
					},
					subtitle: {
						text: "<font style='font-size:14px;color:red'>浮标名称：" + buoyName + "&nbsp;&nbsp;&nbsp;&nbsp;站点编号：" + stationCode + "&nbsp;&nbsp;&nbsp;&nbsp;经度：" + lon + "&nbsp;&nbsp;&nbsp;&nbsp;纬度：" + lat + "</font>&nbsp;&nbsp;&nbsp;&nbsp;<font style='font-size:14px;color:blue'>" + data.xLabs[0] + "~" + data.xLabs[len - 1] + "</font>",
						useHTML: true		
					},
					xAxis: {
						categories: data.xLabs,
						crosshair: true,
						labels: {
							style:{
								fontWeight: 'bold',
								color:'block',
								fontSize:'16px'
							}
			            }, 
						tickmarkPlacement: "on"// 数据值和刻度对应
					},
					yAxis: {
						labels: {
							style:{
								fontWeight: 'bold',
								color:'block',
								fontSize:'16px'
							}
			            },
						title: {
							text: "单位：m/s",
							style:{
								fontWeight: 'bold',
								color:'block',
								fontSize:'16px'
							}
						}
					},
					tooltip: {
						enabled: true
					},
					series: [{
						name: "风速",
						type: "column",
						dataLabels: {
							enabled: true
						},
						data: windSpeed,
					},{
						name:"风向", 
						type: "line",
						color: ["#FFFFFF"],// 设置折线的颜色为白色
						data: windDirection,
						lineWidth:1
					},{
						name:"阵风", 
						type: "line",
						color: ["#FFFFFF"],// 设置折线的颜色为白色
						data: maxWind,
						lineWidth:1
					}],
					legend: {
						enabled: false// 不显示图例
					},
					credits: {
						enabled: false// 去除水印
					}
				});
			});
		}
		
		// 绘制图像
		$("#drawBtn").click(function() {
			showImageProcess();
		});
		
		// 切换时次重新绘图
		$("#hour").change(function() {
			showImageProcess();
		});
		
		// 切换时效重新绘图
		$("#timelimit").change(function() {
			showImageProcess();
		});
		
		// 起始日期往前推
		$("#subDatetimeBtn").click(function(){
			// 按时间间隔回退时间
			subTimeByInterval("hour", "hour", "datetime", "yyyyMMdd");
			showImageProcess();
		});
		
		// 起始日期往后推
		$("#addDatetimeBtn").click(function(){
			// 按时间间隔推进时间
			addTimeByInterval("hour", "hour", "datetime", "yyyyMMdd");
			showImageProcess();
		});
		
		// 往前推时效
		$("#subTimelimitBtn").click(function(){
			getSelectPreOption("timelimit");
			showImageProcess();
		});
		
		// 往后推时效
		$("#addTimelimitBtn").click(function(){
			getSelectNextOption("timelimit");
			showImageProcess();
		});
		
		// 往前推时效
		$("#leftPannel").click(function() {
			getSelectPreOption("timelimit");
			showImageProcess();
		});
		
		// 往后推时效
		$("#rightPannel").click(function() {
			getSelectNextOption("timelimit");
			showImageProcess();
		});
		
		// 图像绘制展示处理
		function showImageProcess() {
			var datetime = $("#datetime").val();
			var hour = $("#hour").val();
			datetime = datetime + hour;
			var timelimit = $("#timelimit").val();
			showImage(datetime, timelimit);
		}
		
		// 显示图像
		function showImage(datetime, timelimit) {
			$.post("${ctx}/meanWind/validImage.do", {datetime:datetime, timelimit:timelimit}, function(data){
				// 清空内容
				$("#imgDiv").html();
				if(data == false){
					$("#imgDiv").html("<img src='${ctx}/images/ui/nodata.jpg' style='width:100%;height:100%'/>");
				}else{
					$("#imgDiv").html("<img id='img' src='' style='width:100%;height:100%;border: 1px solid gray;' />");
					var img = $("#img");
					var url = "${ctx}/meanWind/showImage.do?datetime=" + datetime + "&timelimit=" + timelimit;
					img.attr("src", url);
				}
			});
		}
		
		// 绘制最大风图像
		$("#drawMaxWindBtn").click(function() {
			showMaxWindImageProcess();
		});
		
		// 切换时次重新绘图
		$("#maxWindHour").change(function() {
			showMaxWindImageProcess();
		});
		
		// 切换时效重新绘图
		$("#maxWindTimelimit").change(function() {
			showMaxWindImageProcess();
		});
		
		// 起始日期往前推
		$("#subMaxWindDatetimeBtn").click(function(){
			// 按时间间隔回退时间
			subTimeByInterval("hour", "maxWindHour", "maxWindDatetime", "yyyyMMdd");
			showMaxWindImageProcess();
		});
		
		// 起始日期往后推
		$("#addMaxWindDatetimeBtn").click(function(){
			// 按时间间隔推进时间
			addTimeByInterval("hour", "maxWindHour", "maxWindDatetime", "yyyyMMdd");
			showMaxWindImageProcess();
		});
		
		// 往前推时效
		$("#subMaxWindTimelimitBtn").click(function(){
			getSelectPreOption("maxWindTimelimit");
			showMaxWindImageProcess();
		});
		
		// 往后推时效
		$("#addMaxWindTimelimitBtn").click(function(){
			getSelectNextOption("maxWindTimelimit");
			showMaxWindImageProcess();
		});
		
		// 往前推时效
		$("#maxWindLeftPannel").click(function() {
			getSelectPreOption("maxWindTimelimit");
			showMaxWindImageProcess();
		});
		
		// 往后推时效
		$("#maxWindRightPannel").click(function() {
			getSelectNextOption("maxWindTimelimit");
			showMaxWindImageProcess();
		});
		
		// 最大风图像绘制展示处理
		function showMaxWindImageProcess() {
			var datetime = $("#maxWindDatetime").val();
			var hour = $("#maxWindHour").val();
			datetime = datetime + hour;
			var timelimit = $("#maxWindTimelimit").val();
			showMaxWindImage(datetime, timelimit);
		}
		
		// 显示最大风图像
		function showMaxWindImage(datetime, timelimit) {
			$.post("${ctx}/meanWind/validMaxWindImage.do", {datetime:datetime, timelimit:timelimit}, function(data){
				// 清空内容
				$("#maxWindImgDiv").html();
				if(data == false){
					$("#maxWindImgDiv").html("<img src='${ctx}/images/ui/nodata.jpg' style='width:100%;height:100%'/>");
				}else{
					$("#maxWindImgDiv").html("<img id='maxWindImg' src='' style='width:100%;height:100%;border: 1px solid gray;' />");
					var img = $("#maxWindImg");
					var url = "${ctx}/meanWind/showMaxWindImage.do?datetime=" + datetime + "&timelimit=" + timelimit;
					img.attr("src", url);
				}
			});
		}
		
		// 绘制图表
		$("#errCheckSearchBtn").click(function() {
			showErrCheckGraphProcess();
		});
		
		// 切换站点自动绘图
		$("#stnCode").change(function() {
			showErrCheckGraphProcess();
		});
		
		// 切换时次重新绘图
		$("#errCheckHour").change(function() {
			showErrCheckGraphProcess();
		});
		
		// 时间时次往前推
		$("#subLiveTimeBtn").click(function(){
			subTimeByInterval("hour", "errCheckHour", "liveTime", "yyyyMMdd");
			showErrCheckGraphProcess();
		});
		
		// 时间时次往后推
		$("#addLiveTimeBtn").click(function(){
			addTimeByInterval("hour", "errCheckHour", "liveTime", "yyyyMMdd");
			showErrCheckGraphProcess();
		});
		
		// 平均风误差检验图形绘制处理
		function showErrCheckGraphProcess() {
			var timeStart = $("#liveTime").val();
			var batch = $("#errCheckHour").val();
			var datetime = timeStart + batch;
			var buoyName = $("#stnCode").find("option:selected").text();// 浮标名称
			var stationInfo = $("#stnCode").val().split("_");
			var stationCode = stationInfo[0];
			var lon = stationInfo[1];
			var lat = stationInfo[2];
			showErrCheckGraph(datetime, buoyName, stationCode, lon, lat);
		}
		
		// 绘制平均风误差检验图表
		function showErrCheckGraph(datetime, buoyName, stationCode, lon, lat) {
			var args = {datetime: datetime, stationCode: stationCode};
			$.post("${ctx}/meanWind/getMeanWindErrCheckSeriesData.do", args, function(data){
				if (data == null)
					return;
				
				if (data.liveDatas == null) {
					alert(datetime + "站点[" + stationCode + "]所对应的实况数据不存在");
					return;
				}
				
				$("#showErrCheckGraph").highcharts({
					chart: {
						zoomType:"x",
						width: 1054
					},
					colors:["#FF0000","#0000FF","#000000"],
					title: {
						text: "<font color='blue'>10米平均风误差检验</font>",
						useHTML: true,
						style:{
							fontWeight: 'bold',
							fontSize:'22px'
						}
					},
					subtitle: {
						text: "<font style='font-size:14px;color:red'>浮标名称：" + buoyName + "&nbsp;&nbsp;&nbsp;&nbsp;站点编号：" + stationCode + "&nbsp;&nbsp;&nbsp;&nbsp;经度：" + lon + "&nbsp;&nbsp;&nbsp;&nbsp;纬度：" + lat + "</font>&nbsp;&nbsp;&nbsp;&nbsp;<font style='font-size:14px;color:blue'>" + datetime + "</font>",
						useHTML: true		
					},
					xAxis: {
						categories: data.xLabs,
						crosshair: true,
						labels: {
							style:{
								fontWeight: 'bold',
								color:'block',
								fontSize:'16px'
							}
			            }, 
						tickmarkPlacement: "on"// 数据值和刻度对应
					},
					yAxis: {
						labels: {
							style:{
								fontWeight: 'bold',
								color:'block',
								fontSize:'16px'
							}
			            },
						title: {
							text: "单位：m/s",
							style:{
								fontWeight: 'bold',
								color:'block',
								fontSize:'16px'
							}
						}
					},
					tooltip: {
						enabled: true
					},
					series: [{
						name: "订正前",
						type: "line",
						data: data.beforeCorrectDatas,
						lineWidth: 2.5
					},{
						name:"订正后", 
						type: "line",
						data: data.afterCorrectDatas,
						lineWidth: 2.5
					},{
						name:"实况值", 
						type: "line",
						data: data.liveDatas,
						lineWidth: 2.5
					}],
					credits: {
						enabled: false// 去除水印
					}
				});
			});
		}
	</script>
</html>