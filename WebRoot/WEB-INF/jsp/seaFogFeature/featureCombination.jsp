<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/taglib.jsp"%>
<html>
	<head>
		<title>海洋气象精细化格点预报</title>
		<style type="text/css">
			.border_condition_main {font-size:12px;text-align:left;padding:0px 0px 17px 10px;display:block;width: 100%;}
			.div_border {border:1px solid #ccc;display:inline-block;padding: 3px 2px;}
			select {height:25px;width:60px;margin:0}
			input[type=button] {width:30px;height:25px;}
			#drawBtn {width:45px;}
			#showGraph {width:auto;height:700px;border-top: 1px solid gray}
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
					  	<li><a href="javascript:void(0)">海雾要素/</a></li>
					  	<li class="active">要素组合预报</li>
					</ol>
					<hr style="width:100%">
					
					<div class="border_condition_main">
						起报时间：
						<input class="Wdate" id="datetime" type="text" style="width:90px;max-width:90px;height:25px;margin:0" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
						<select id="hour">
						    <option value="00">08</option>
							<option value="12">20</option>
						</select>
						<input id="subDatetimeBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
						<input id="addDatetimeBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
						&nbsp;
						时效：
				
						<select id="timelimit">
							<c:forEach begin="0" end="240" step="6" varStatus="hour">
								<option><fmt:formatNumber pattern="000" value="${hour.index}"></fmt:formatNumber></option>
							</c:forEach>
						</select>
						<!-- &nbsp;
						
						要素：
						<select id="feature" style="width:150px;">
						   <option value="rh2m_wind10">rh2m_wind10m</option>	
						   <option value="rh1000_wind10">RH1000_wind10m</option>	
						   <option value="rh950_wind">RH950_wind</option>	
						   <option value="rh925_wind">RH925_wind</option>	
						   <option value="rh850_wind">RH850_wind</option>	
						   <option value="td_sst">Td-sst</option>	
						   <option value="ta_td">Ta-td</option>	
						   <option value="ta_sst">Ta-sst</option>	
						   <option value="t1000_t2m">T1000-t2m</option>	
						   <option value="t925_t1000">T925-t1000</option>	
						   <option value="sst">sst</option>	
						</select>
						<input id="drawBtn" type="button" value="查询" class="easyui-linkbutton btn"/> -->
					</div>
					<div class="border_condition_main">
 					要素名称：
							<input type="radio" name="feature" value="rh2m_wind10" checked/>rh2m_wind10m
							<input type="radio" name="feature" value="rh1000_wind10"/>RH1000_wind10m
							<input type="radio" name="feature" value="rh950_wind"/>RH950_wind
							<input type="radio" name="feature" value="rh925_wind"/>RH925_wind
							<input type="radio" name="feature" value="rh850_wind"/>RH850_wind
							<input type="radio" name="feature" value="td_sst"/>Td-sst			
							<input type="radio" name="feature" value="ta_sst"/>Ta-sst		
							<input type="radio" name="feature" value="t1000_t2m"/>T1000-t2m		
							<input type="radio" name="feature" value="t925_t1000"/>925-t1000		
							<input type="radio" name="feature" value="sst"/>sst			
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
		    </div>
			
			<!-- 第三层，内容，脚 -->
			<div id="footer">
				<%@include file="../common/footer.jsp" %>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		// 添加菜单栏选中标记
		$("#603").addClass("selectedTitle");
		// 初始化起报时间
		var date = new Date();//获得当前的北京时间
		var datetime = getFormatDate(new Date(date.getTime() - 24 * 60 * 60 * 1000), "yyyyMMdd");
		$("#timeStart").val(datetime);
		$("#datetime").val(datetime);
		
		// 初始化绘制图像
		showImage(datetime + "12", "000", "rh2m_wind10");
		
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
		
		// 切换产品绘图
		$("input[name=feature]").change(function(){
			showImageProcess();
		});
		
		// 图像绘制展示处理
		function showImageProcess() {
			var datetime = $("#datetime").val();
			var hour = $("#hour").val();
			datetime = datetime + hour;
			var timelimit = $("#timelimit").val();
			// 产品类型
/* 			var feature = $("#feature option:selected").val();
 */			
 			var feature = $("input[name=feature]:checked").val();
 			showImage(datetime, timelimit, feature);
		}
		
		// 显示图像
		function showImage(datetime, timelimit, feature) {
			$.post("${ctx}/seaFogFeature/featureCombination/validImage.do", {datetime:datetime, timelimit:timelimit, feature:feature}, function(data){
				// 清空内容
				$("#imgDiv").html();
				if(data == false){
					$("#imgDiv").html("<img src='${ctx}/images/ui/nodata.jpg' style='width:100%;height:100%'/>");
				}else{
					$("#imgDiv").html("<img id='img' src='' style='width:100%;height:100%;border: 1px solid gray;' />");
					var img = $("#img");
					var url = "${ctx}/seaFogFeature/featureCombination/showImage.do?datetime=" + datetime + "&timelimit=" + timelimit + "&feature=" + feature;
					img.attr("src", url);
				}
			});
		}
	</script>
</html>