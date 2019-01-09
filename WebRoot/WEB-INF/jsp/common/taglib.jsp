<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	
<meta http-equiv="cache-control" content="no-cache, must-revalidate">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link type="text/css" rel="shortcut icon" href="${ctx}/images/ui/favicon.ico">

<!-- jquery -->
<script src="${ctx}/js/jquery-easyui-1.4.5/jquery.min.js"></script>
<link href="${ctx}/css/ui/jquery-ui.min.css" type="text/css" rel="stylesheet">
<script src="${ctx }/js/colorbox-master/jquery.colorbox.js"></script>
<link type="text/css" rel="stylesheet" href="${ctx}/js/colorbox-master/colorbox.css" />

<!-- 时间控件 -->   
<script type="text/javascript" src="${ctx}/js/My97DatePicker/WdatePicker.js"></script>
<link type="text/css" rel="stylesheet" href="${ctx}/js/My97DatePicker/skin/WdatePicker.css" />

<!-- 公共js -->
<script type="text/javascript" src="${ctx}/js/util.js"></script>


<!-- BootStrap -->
<link type="text/css" rel="stylesheet" href="${ctx}/static/bootstrap/css/bootstrap.css" />
<script type="text/javascript" src="${ctx}/static/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctx}/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/js/highcharts/highcharts-3d.js"></script>
<script type="text/javascript" src="${ctx}/js/highcharts/highcharts-more.js"></script>
<script type="text/javascript" src="${ctx}/js/highcharts/modules/data.js"></script>

<!-- easyui -->


<script src="${ctx}/js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>
<script src="${ctx}/js/jquery-easyui-1.4.5/jquery.easyui.mobile.js"></script>
<link href="${ctx}/js/jquery-easyui-1.4.5/themes/mobile.css" type="text/css" rel="stylesheet">
<link href="${ctx}/js/jquery-easyui-1.4.5/themes/icon.css" type="text/css" rel="stylesheet">
<link href="${ctx}/js/jquery-easyui-1.4.5/themes/default/easyui.css" type="text/css" rel="stylesheet">
<link href="${ctx}/js/jquery-easyui-1.4.5/themes/bootstrap/datagrid.css" type="text/css" rel="stylesheet">
<link href="${ctx}/js/jquery-easyui-1.4.5/themes/mobile.css" type="text/css" rel="stylesheet">

<!-- 公共样式 -->
<style type="text/css">
  	body {font: 12px "微软雅黑", "宋体", "Helvetica Neue", HelveticaNeue, Helvetica, Arial, sans-serif;width:100%;}
  	html, body, div, span, p, ul,li, a {padding: 0;list-style: none; margin: 0;}
	a {text-decoration: none;outline: none;}
	*{margin:0px;padding:0px;list-style-type:none;}
	#emsloading {
	background: url("${ctx}/images/ui/gitloading.gif") no-repeat center;
	left: 0px;top: 0px;position: absolute;height: 100%;width: 100%;overflow: hidden;z-index: 10000;display: none;}
	
	.window_left{background-color: #3366FF;width: 150px;height: 34px;text-align: center;font-size: 20px;
		line-height: 34px;color: white;float: left;}
		
	.window_right{background-color:white;width: 1190px;height: 34px;border: 2px solid #FFCC00;
		line-height: 24px;font-size: 14px;float: left;}
	#mainContext{width: 1056px;float: left;margin: 20px 10px 0px 10px;border-radius: 20px;background-color: white;border: 1px solid #A1A1A1;}
	#secondContext{border: 1px solid black;background-color: #F2F2F2;}
	.selectedTitle {background: rgba(0, 0, 0, 0) url("${ctx}/static/images/selected.png") no-repeat scroll 20px center;}
</style>
<!-- 导航栏样式 -->
<style type="text/css">
	#navbar{width:72%;height:auto;margin-top:9px;position: relative;left:350px;display: inline;}
	#li01,#li02,#li03,#li04{width: 140px;} 
	#navbar li{float:left;height:30px;line-height:30px;text-align:center;font-size:14px;position:relative;background-color: #3399FF;}	
	#navbar li a{color:#FFF;text-decoration:none;font-size:16px; display:block;margin-top:10px;}
	#navbar li a.link{float:left;width: 108px; }
	#navbar ul li a:hover{display:block;color:#1A96DD;background-color:#FFF;}
	#navbar dl dd{margin:auto;line-height:30px;}
	#navbar dl{border-radius:0px 0px 5px 5px;width:140px;line-height:30px;display:none;font-size:12px;background:#5489CB;position:absolute;top:30px;left:0px;}
	#navbar dl dd a{border-radius:0px 0px 5px 5px;color:#FFF;font-size:14px;margin-top:0px;border-top:1px solid #FFF;}/* dashed */
	#navbar dl dd a:hover{color:#FFF;background:#6CA5F7;font-size:16px;}
	.link{width: 160px;background-color: #3399FF;}
	.link:HOVER {background-color: #FF9900;}
	.shugang{background-color: #3399FF;color: gray;font-size: 10px;}
</style>
<!-- 菜单样式 -->
<style>
	.menu_border{margin: 0 auto;background-color: white;width: 230px;border-radius: 20px;
		margin-top: 20px;padding-top: 10px;border: 1px solid #A1A1A1;}
	.menu_level_1{font-size: 16px;height: 40px;line-height: 40px;cursor: pointer;color: white;}
	.menu_level_2{font-size: 15px;height: 40px;line-height: 40px;cursor: pointer;color: black;}
	.menu_border img{float: right;margin-right: 30px;}
</style>

<!-- <script type="text/javascript">
$('.moFo').hide();
$('.moFo').eq(0).show();
var targetLength = $('.moTarget').length;
$('.moTarget').click(function(){
	$('.moTarget').removeClass("active");
	$('.moFo').hide();
	var mo = $(this).attr("target");
	$('.moFo[of='+mo+']').show();
	$(this).addClass("active");
});

</script> -->