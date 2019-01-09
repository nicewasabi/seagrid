<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

	<head>
		<title>一致性检验</title>
		<%@ include file="/WEB-INF/jsp/common/taglib.jsp"%>
		<style type="text/css" >
			.amap-logo{
				display:none!important;
			}
			.amap-copyright{
				display:none!important;
			}
		</style>
		
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
		<script type="text/javascript" src="${ctx }/js/area/area_chart.js"></script>
		<script type="text/javascript">
			var productCode = "<%= request.getParameter("productCode") %>";
			var searchDate = "<%= request.getParameter("searchDate") %>";
			var timeAge = "<%= request.getParameter("timeAge") %>";
			var northLatitude = "<%= request.getParameter("northLatitude") %>";
			var southLatitude = "<%= request.getParameter("southLatitude") %>";
			var eastLongitude = "<%= request.getParameter("eastLongitude") %>";
			var westLongitude = "<%= request.getParameter("westLongitude") %>";
			openWindow();
		</script>
		
	</head>
<body>
		

<div class="pro_block" id="chartUv10" >
</div>
<div class="pro_block" id="chartNcepUv10" >
</div>
</body>
</html>