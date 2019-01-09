<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

	<head>
		<title>一致性检验</title>
		<%@ include file="../common/taglib.jsp"%>
		<!-- 地图js库 -->
		<script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&plugin=AMap.MouseTool"></script>
		<style type="text/css" >
			.amap-logo{
				display:none!important;
			}
			.amap-copyright{
				display:none!important;
			}
			#cboxMiddleRight{
			display:none!important;
			}
			#cboxMiddleLeft{
			display:none!important;
			}
			#cboxTopLeft{
			display:none!important;
			}
			#cboxTopRight{
			display:none!important;
			}
			#cboxBottomLeft{
			display:none!important;
			}
			#cboxBottomRight{
			display:none!important;
			}
		</style>
		<script type="text/javascript" src="${ctx }/js/area/area.js"></script>
		<style>
			.pro_block{border: 1px solid gray;width: 98%;margin: auto;border-radius: 10px;margin-bottom: 10px;font-size: 14px;
			text-align: center;}
			.input{width: 100%;margin: 4px;}
			.args_li{text-align: center;float: left;height: 40px;width: 80px;background-color:#99FFFF;line-height: 40px;margin: 0px 10px;cursor: pointer;}
			.args_li2{background-color: #99FFFF;border: 1px solid gray;border-radius: 4px;float: left;height: 40px;line-height: 40px;
			margin: 5px 20px;cursor: pointer;}
			.args_li_active{background-color: #9999FF;}
			red{color: red;font-size: 10px;}
			.map_button{position: relative;z-index: 999;top: 40px;left: -380px;}
			label{font-weight: normal;width: 100px; display: inline-block;margin-bottom: 5px;}
			label input{margin: 4px 0 0;}
			#text{width:24px;height:17px;}
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
				
				<!-- 左侧菜单 -->		
				<div id="leftContext" style="width: 266px;height: 500px;float: left;text-align: center;">
					<%@include file="../common/menu.jsp" %>
				</div>
				
				<!-- 页面主体 -->
				<div id="mainContext" >
					
<!-- 导航栏 -->
<ol class="breadcrumb" style="margin-top: 10px;background-color: white;margin-left: 10px;margin-bottom: 0px;">
  <li><a href="${ctx }">首页/</a></li>
  <li><a href="#">风场订正/</a></li>
  <li class="active">一致性检验</li>
</ol>
<hr style="margin:5px;">
<!-- 3.基本条件-->
<div class="pro_block" >
	<table style="text-align: left;">
			<tr style="height: 35px;line-height:36px;margin:0 aut0;">
				<td style="width: 60px;padding-left: 45px;">定制时间</td>
				<td style="width: 100px;align:center;">
					<input class="Wdate" id="searchDate" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});" style="height: 20px; width: 80px;margin-top:7px; border: 1px solid gray;" type="text">
				</td>
				<td style="width: 20px;"></td>
				<td style="padding-left: 10px;height: 40px;">定制时次:</td>
				<td id="timeAge" style="width: 100px;align:center;padding-left:4px">
					<input name="timeLevel" style="margin:0 auto;padding-top:10px;" onclick="changeTimeLevel()" value="08" checked="checked" type="radio">08 &nbsp;&nbsp;&nbsp;&nbsp;
					<input name="timeLevel" style="margin:0 auto;padding-top:10px;" onclick="changeTimeLevel()" value="20" type="radio">20
				</td>
				<td style="width: 300px;"></td>
			</tr>
			<!-- <td style="width: 80px;padding-left: 45px;">定制时间</td>
			<td style="width: 100px;align:center;">
				<input class="Wdate" style="height: 25px; width: 80px;margin-top:7px; border: 1px solid gray;" id="searchDate" type="text" onfocus="WdatePicker({skin:'twoer', dateFmt:'yyyyMMdd',autoPickDate: true});">
			</td>
			<td style="padding-left: 45px;height: 40px;">定制时次</td>
			<td  id="timeAge" style="width: 100px;align:center;padding-left: 45px;" >
				<input type="radio" name="timeLevel" style="margin:0 auto;padding-top:10px;" onclick="changeTimeLevel()" value="08" checked="checked"/>08
				<input type="radio" name="timeLevel" style="margin:0 auto;padding-top:10px;" onclick="changeTimeLevel()" value="20" />20
			</td>
			<td style="width: 320px;"></td> -->
		</tr>
	</table>
</div>
<!-- 6.区域选择-->
<div class="pro_block" style="background:white;">
	<div style="display: inline-block;width: 500px;text-align: left;height: 40px;line-height: 40px;font-size: 14px;">
		<img alt="" >&nbsp;&nbsp;区域选择
	</div>
	<div style="display: inline-block;width: 450px;text-align: right;cursor: pointer;"></div>
	<hr style="margin: 0px;">
	<table style="width:1035px;">
		<tr style="height: 40px;">
			<td style="width: 51px;"></td>
			<td style="width: 80px">起始纬度:</td>
			<td><input style="width: 80px" id="southLatitude"></td>
			<td style="width: 80px">终止纬度:</td>
			<td><input style="width: 80px" id="northLatitude"></td>
			<td style="width: 80px">起始经度:</td>
			<td><input style="width: 80px" id="westLongitude"></td>
			<td style="width: 80px">终止经度:</td>
			<td><input style="width: 80px" id="eastLongitude"></td>
			<td style="width: 150px;">
				<button onclick="openWindow()">计算结果</button>
			</td>
			<td style="width: 30px;"></td>
			<td style="width:280px;text-align: right;color: red;">注：地图中矩形区域为数据区域</td>
		</tr>
		<!-- <tr style="height: 40px;">
			<td style="width: 51px;"></td>
			<td style="width: 80px">起始纬度:</td>
			<td><input style="width: 80px" id="southLatitude"/></td>
			<td style="width: 80px">终止纬度:</td>
			<td><input style="width: 80px" id="northLatitude"/></td>
			<td style="width: 80px">起始经度:</td>
			<td><input style="width: 80px" id="westLongitude"/></td>
			<td style="width: 80px">终止经度:</td>
			<td><input style="width: 80px" id="eastLongitude"/></td>
			<td style="width: 150px;">
				<button onclick="openWindow()">计算结果</button>
			</td>
			<td style="width: 30px;"></td>
			<td style="width:280px;text-align: right;color: red;">注：地图中矩形区域为数据区域</td>
		</tr> -->
		<tr>
			<td colspan="12" style="padding: 0px 0px;">
				<input type="button" class="map_button" value="绘制区域" id="polygon"/>
				<input type="button" class="map_button" value="清空地图" id="clearMap"/>
				<div style="width: 100%;height: 500px;" id="show_map"></div>
			</td>
		</tr>
	</table>
	
</div>


</div>

<script type="text/javascript">

</script>
				</div>	
		    </div>
			
			<!-- 第三层，内容，脚 -->
			<div id="footer">
				<%@include file="../common/footer.jsp" %>
			</div>
			
		</div>
		
	</body>
</html>