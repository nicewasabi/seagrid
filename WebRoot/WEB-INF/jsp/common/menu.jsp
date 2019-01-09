<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
	/**
	 * 显示隐藏
	 */
	function showHide(menu_id, menu_image){
		$("#" + menu_id).toggle();
		var src = $('#' + menu_image).attr("src");
		
		if(src == "${ctx }/static/images/up.png") {
			$('#' + menu_image).attr("src", "${ctx}/static/images/down.png");
		} else {
			$('#' + menu_image).attr("src", "${ctx}/static/images/up.png");
		}
	}
	/**
	 * 切换页面
	 */
	function changeTab(url) {
		window.location.href = url;
	}
</script>
<div class="menu_border" style="padding: 20px 0px;">

	<!-- <ul class="nav nav-pills nav-stacked">
	  <li class="active"><a href="#">集合预报10m风订正</a></li>
	  <li><a href="#">EC模式检验</a></li>
	  <li><a href="#">NCEP模式检验</a></li>
	  <li><a href="#">GRAPES-GFS模式检验</a></li>
	  <li><a href="#">JMA模式检验</a></li>
	   <li><a href="/seaGrid/monitor/fileMonitor.do">文件监控</a></li>
	</ul> -->
	
	<div class="menu_level_1" onclick="showHide('menu_id_2','menu_image_2')" style="margin: 1px 0px;background-color: #3399FF;">
		风场订正&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<img alt="箭头" id="menu_image_2" src="/seaGrid/static/images/up.png">
	</div>
	<div id="menu_id_2">
		<div class="menu_level_2" id="201" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/meanWind/main.do')">10米平均风</div>									
		<div class="menu_level_2" id="202" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/maxWind/main.do')">10米阵风</div>
		<div class="menu_level_2" id="203" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/area/index.do')">一致性检验</div>									
	</div>
	
	<div class="menu_level_1" onclick="showHide('menu_id_3','menu_image_3')" style="margin: 1px 0px;background-color: #3399FF;">
		10米8级大风概率
		<img alt="箭头" id="menu_image_3" src="/seaGrid/static/images/up.png">
	</div>
	<div id="menu_id_3">
		<div class="menu_level_2" id="301" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/np/main.do')">北太平洋</div>									
		<div class="menu_level_2" id="302" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/ni/main.do')">北印度洋</div>									
	</div>
	
	<div class="menu_level_1" onclick="showHide('menu_id_4','menu_image_4')" style="margin: 1px 0px;background-color: #3399FF;">
		8级大风确定性预报
		<img alt="箭头" id="menu_image_4" src="/seaGrid/static/images/up.png">
	</div>
	<div id="menu_id_4">
		<div class="menu_level_2" id="401" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/np/determineForecastMain.do')">北太平洋</div>									
		<div class="menu_level_2" id="402" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/ni/determineForecastMain.do')">北印度洋</div>									
	</div>

	<div class="menu_level_1" onclick="showHide('menu_id_1','menu_image_1')" style="margin: 1px 0px;background-color: #3399FF;">
		系统管理&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<img alt="箭头" id="menu_image_1" src="/seaGrid/static/images/up.png">
	</div>
	<div id="menu_id_1">
		<div class="menu_level_2" id="101" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/monitor/fileMonitor.do')">文件监控</div>									
		<div class="menu_level_2" id="103" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/fileParseLog/fileMonitor.do')">数据监控</div>
		<div class="menu_level_2" id="104" style="text-align: left;padding-left: 50px;" onclick="changeTab('/seaGrid/computer/manage.do')">系统监控</div>					
	</div>
	
</div>
