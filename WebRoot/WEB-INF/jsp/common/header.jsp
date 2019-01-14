<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div style="height: 60px;">
	<img alt="" src="${ctx }/system/login/1350head.jpg" style="width: 1350px;height: 60px;"/>
	<%-- <img src="${ctx }/static/logo.png" style="width: 80px;position: relative;bottom: 130px;left: 200px;"/> --%>
	<div style="display: inline-block;font-size: 26px;color: white;position: relative;bottom: 140px;left: 250px;text-align: center;">
		
	</div>
<%-- 	<div style="display: inline-block;font-size: 16px;color: white;position: relative;bottom: 180px;left: 700px;text-align: center;">
		<span id="welcome_msg"></span>${sessionScope.user.userTname }，&nbsp;&nbsp;&nbsp;&nbsp;<a href="#" style="text-decoration: none;color: white;font-size: 16px;" onclick="userLogout()">退出</a>
	</div> --%>
 	
	<table style="position: relative;bottom: 30px;left: 375px;text-align: center;color: white;font-size: 16px;" cellspacing="0" cellpadding="0">
		<tr>
		<td style="width: 60px;height: 30px;background-color: #3399FF;border-radius: 10px 0px 0px 0px;border: 0px solid black;"></td>			
			<td class="shugang">|</td>	
			<td class="link" >
				<a style="color: white;text-decoration: none;"  href="${ctx}${childPermission.url}/meanWind/main.do">风场订正</a>
			</td>
			<td class="shugang">|</td>
			<td class="link" style="width: 160px;">
				<a style="color: white;text-decoration: none;"  href="${ctx}${childPermission.url}/np/main.do">10米8级大风概率</a>
			</td>
			<td class="shugang">|</td>
			<td class="link"  style="width: 160px;">
				<a style="color: white;text-decoration: none;"  href="${ctx}${childPermission.url}/np/determineForecastMain.do">8级大风确定性预报</a>
			</td>
			<td class="shugang">|</td>
			<td class="link" >
				<a style="color: white;text-decoration: none;"  href="${ctx}${childPermission.url}/objectiveMethodProduct/huananSeaFog/main.do">客观方法产品</a>
			</td>
			<td class="shugang">|</td>
			<td class="link" >
				<a style="color: white;text-decoration: none;"  href="${ctx}${childPermission.url}/seaFogFeature/section3days/main.do">海雾要素</a>
			</td>
			<td class="shugang">|</td>
			<td class="link" >
				<a style="color: white;text-decoration: none;"  href="http://web.kma.go.kr/chn/weather/images/satellite.jsp">网站链接</a>
			</td>
			<td class="shugang">|</td>
			<td class="link" >
				<a style="color: white;text-decoration: none;"  href="${ctx}${childPermission.url}/monitor/fileMonitor.do">系统管理</a>
			</td>
	<%-- 	<td class="shugang">|</td>
			<td class="link" >
				<a style="color: white;text-decoration: none;"  href="${ctx }${childPermission.url}">文件监控</a>
			</td> --%>
<!-- 			<td style="width: 60px;background-color: #3399FF;border-radius: 0px 10px 0px 0px;border: 0px solid black;"></td>	
 -->		</tr>
	</table> 
	<%-- <table style="position: relative;bottom: 133px;left: 375px;text-align: center;color: white;font-size: 16px;" cellspacing="0" cellpadding="0">
		<tr>
			<td style="width: 60px;height: 30px;background-color: #3399FF;border-radius: 10px 0px 0px 0px;border: 0px solid black;"></td>			
			<td class="shugang">|</td>	
			
			<c:forEach var="permission" items="${sessionScope.user_permission }" >
				<c:if test="${permission.pid eq 0}">
					<td class="link" >
						<c:set var="flag" value="true" />
						<c:forEach var="childPermission" items="${sessionScope.user_permission }">
							<c:if test="${flag && childPermission.pid eq permission.id}">
								<a style="color: white;text-decoration: none;"  href="${ctx }${childPermission.url}">${permission.permissionName }</a>
								<c:set var="flag" value="false"/>
							</c:if>
						</c:forEach>
						
					</td>
					<td class="shugang">|</td>
				</c:if>
			</c:forEach>
			<td style="width: 60px;background-color: #3399FF;border-radius: 0px 10px 0px 0px;border: 0px solid black;"></td>		
		</tr>
	</table> --%>
	<script type="text/javascript">
		function userLogout(){
			var b = confirm("确定要退出吗？");
			if(b) {
				window.location.href = "${ctx}/login/logout.do";
			}
		}
		$(function(){
			var hour = new Date().getHours();
			if(hour >= 6 && hour < 11) {
				$('#welcome_msg').text("上午好：");
			} else if(hour >= 11 && hour < 14) {
				$('#welcome_msg').text("中午好：");
			} else if(hour >= 14 && hour < 18) {
				$('#welcome_msg').text("下午好：");
			} else if(hour >= 18 && hour < 24) {
				$('#welcome_msg').text("晚上好：");
			} else {
				$('#welcome_msg').text("您好：");
			}
		});
	</script>
</div>