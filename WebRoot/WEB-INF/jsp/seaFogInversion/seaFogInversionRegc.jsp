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
					  	<li><a href="javascript:void(0)">海雾反演产品/</a></li>
					  	<li class="active">近海海雾反演</li>
					</ol>
					<hr style="width:100%">
					
					<div class="border_condition_main">
						起报日期：
						<input class="Wdate" id="datetime" type="text" style="width:90px;max-width:90px;height:25px;margin:0" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});"/>
						时间间隔：
						<select id="step">
						    <option value="05">5分钟</option>
							<option value="15">15分钟</option>
						</select>
						小时：
						<select id="hour">
									<c:forEach begin="0" end="23" step="1" varStatus="hourVar">
										<option><fmt:formatNumber pattern="00" value="${hourVar.index}"></fmt:formatNumber></option>
									</c:forEach>
								</select>
						分钟：
						　<select id="selectMinutes">
						    <option value='0'>请选择</option> 　　　  					
　　						 </select> 
						
								
						<input id="subDatetimeBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
						<input id="addDatetimeBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
						<input id="subTimelimitBtn" type="button" value="&lt;&lt;" class="easyui-linkbutton btn"/>
						<input id="addTimelimitBtn" type="button" value="&gt;&gt;" class="easyui-linkbutton btn"/>
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
		$("#801").addClass("selectedTitle");
		// 初始化起报时间
		var date = new Date();//获得当前的北京时间
		var datetime = getFormatDate(new Date(date.getTime()), "yyyyMMdd");
		$("#timeStart").val(datetime);
		$("#datetime").val(datetime);
		var hh = date.getHours();
		// 初始化绘制图像
		showImage(datetime, hh,'05','00' );
		
		// 绘制图像
		$("#drawBtn").click(function() {
			showImageProcess();
		});
		
		// 切换时次重新绘图
		$("#hour").change(function() {
			getMinutesListProcess();
		});
		
		
		// 切换时次重新绘图
		$("#step").change(function() {
			getMinutesListProcess();
		});
		// 切换时效重新绘图
		$("#timelimit").change(function() {
			getMinutesList()
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
			// 地点
/* 			var location = $("#location option:selected").val();
 */			  var location = $("input[name=location]:checked").val();
			showImage(datetime, hour, location);
		}
		
		// 显示图像
		function showImage(datetime, hour, step,minute) {
			$.post("${ctx}/seaFogInversion/regc/validImage.do", {datetime:datetime, hour:hour, step:step,minute:minute}, function(data){
				// 清空内容
				$("#imgDiv").html();
				if(data == false){
					$("#imgDiv").html("<img src='${ctx}/images/ui/nodata.jpg' style='width:100%;height:100%'/>");
				}else{
					$("#imgDiv").html("<img id='img' src='' style='width:100%;height:100%;border: 1px solid gray;' />");
					var img = $("#img");
					var url = "${ctx}/seaFogInversion/regc/showImage.do?datetime=" + datetime + "&hour=" + hour + "&location=" + location;
					img.attr("src", url);
				}
			});
		}
		
		
		function getMinutesListProcess() {
			var datetime = $("#datetime").val();
			var step = $("#step").val();
			var hour = $("#hour").val();
			// 地点
/* 			var location = $("#location option:selected").val();
 */			 
 			getMinutesList(datetime, hour, step);
		}
		
		//动态加载分钟
		function getMinutesList(datetime, hour,step) {
			$.post("${ctx}/seaFogInversion/regc/getMinutesList.do", {datetime:datetime, hour:hour,step:step}, function(data){
			
				$("#selectMinutes").html();
				if(data!=null){
					$("#selectMinutes").prepend("<option value='0'>请选择</option>");
				　　　　for (var i = 0; i < data.length; i++) {
					$("#selectMinutes").append("<option value='"+data[i]+"'>"+data[i]+"</option>");
 			}
			}});
		}
		
	</script>
</html>