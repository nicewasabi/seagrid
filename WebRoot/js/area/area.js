
var lower = "${ctx }/static/images/u264.png";//更多
var upper = "${ctx }/static/images/u265.png";//收起

var map = null;

var productId = null;//产品编号
var productName = null;//产品编号

var productList = null;//保存所有的产品数据
var commonArea = null;//保存常用区域
var productCommon = null;//保存常用定制

var limitBounds2 = {
		southwest:{
			lng:'95',
			lat:'-10'
			
		},
		northeast:{
			lng:'142',
			lat:'45'
		}
};

var limitBounds = "95,-40;142,45";
//js获取项目根路径，如： http://localhost:8083/uimcardprj  
function getRootPath(){  
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp  
    var curWwwPath=window.document.location.href;  
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp  
    var pathName=window.document.location.pathname;  
    var pos=curWwwPath.indexOf(pathName);  
    //获取主机地址，如： http://localhost:8083  
    var localhostPaht=curWwwPath.substring(0,pos);  
    //获取带"/"的项目名，如：/uimcardprj  
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);  
    return(localhostPaht+projectName);  
}  
var ctx = getRootPath();
/**
 * 获取昨天的年月日
 * @returns
 */
function getLastday(){
	 var date = new Date();
	 var month=(date.getMonth()+1).toString().length==1?'0'+(date.getMonth()+1).toString():(date.getMonth()+1);
	 var day = (date.getDate()-1).toString().length==1?'0'+(date.getDate()-1):(date.getDate()-1);
	 return date.getFullYear().toString()+month+day;
}

//点击计算结果
function openWindow(){
	var northLatitude = $('#northLatitude').val();//北纬
	var southLatitude = $('#southLatitude').val();//南纬
	var westLongitude = $('#westLongitude').val();//西经
	var eastLongitude = $('#eastLongitude').val();//东经
	var searchDate = $("#searchDate").val();//日期
	if(southLatitude == null || southLatitude  == "" || westLongitude == null || westLongitude  == ""
		|| northLatitude == null || northLatitude  == "" || eastLongitude == null || eastLongitude  == "") {
		alert("经纬度不能为空");
		return;
	}
	if(searchDate == null || searchDate == "") {
		alert("日期不能为空");
		return;
	}
	var timeAge = null//时效
	var radio = document.getElementsByName("timeLevel");
	for (i=0; i<radio.length; i++) {
		if (radio[i].checked) {
			timeAge= radio[i].value;
		}
	}
	$.colorbox({width:"60%", height:"90%", iframe:true, href:ctx+"/jsp/area_chart.jsp?productCode="+"uv10" + "&searchDate=" + searchDate
		+ "&timeAge=" + timeAge + "&northLatitude=" + northLatitude+ "&southLatitude=" + southLatitude+ "&westLongitude=" + westLongitude
		+ "&eastLongitude=" + eastLongitude});
}

$(function(){
	//设置选中标志
	$("#203").addClass("selectedTitle");
	//初始化地图
	initMap();
});

//重置选项
function reset(){
	$('#name').val("");//定制名称
	$('#beginTime').val("");//开始时间
	$('#endTime').val("");//结束时间
	$('#northLatitude').val("");
    $('#southLatitude').val("");
    $('#westLongitude').val("");
    $('#eastLongitude').val("");
    if(map != null)
    	map.clearMap();
}

//保存区域
function saveArea(){
	var areaName = $('#areaName').val();
	var northLatitude = $('#northLatitude').val();
	var southLatitude = $('#southLatitude').val();
	var westLongitude = $('#westLongitude').val();
	var eastLongitude = $('#eastLongitude').val();
	var args = {"areaName":areaName, "northLatitude":northLatitude, "southLatitude":southLatitude,
			"westLongitude":westLongitude, "eastLongitude":eastLongitude};
	if(southLatitude == null || southLatitude  == "" || westLongitude == null || westLongitude  == ""
		|| northLatitude == null || northLatitude  == "" || eastLongitude == null || eastLongitude  == "") {
		alert("经纬度不能为空");
		return;
	}
	$.post("${ctx}/areaCommon/save.do",args,function(res){
		if(res == "1" ){
			alert("保存成功");
		} else if(res == "0"){
			alert("保存失败");
		} else if(res == "-1") {
			alert("保存失败，区域名称已存在");
		}
		$('#addAreaWinodw').dialog("close");
	});
}
//添加多边形覆盖物  
function addPolygon(){  
   var polygonArr=new Array();//多边形覆盖物节点坐标数组   
   polygonArr.push(new AMap.LngLat("95","-10"));   
   polygonArr.push(new AMap.LngLat("95","45"));  
   polygonArr.push(new AMap.LngLat("142","45"));   
   polygonArr.push(new AMap.LngLat("142","-10"));   
  
   polygon=new AMap.Polygon({     
   path:polygonArr,//设置多边形边界路径  
   strokeColor:"#0000ff", //线颜色  
   strokeOpacity:0.2, //线透明度   
   strokeWeight:3,    //线宽   
   fillColor: "#f5deb3", //填充色  
   fillOpacity: 0.1//填充透明度  
  });   
   polygon.setMap(map);  
}  

//初始化地图
function initMap(){
	$("#searchDate").val(getLastday());
	map = new AMap.Map('show_map', {
        /*dragEnable:true,//是否可通过鼠标拖拽平移
        zoomEnable:true,//是否可缩放
        scrollWheel:true,//是否可通过鼠标滚轮缩放
        zoom:4,*/
		zoom:4,
		center:[118, 13],
        resizeEnable: true
    });
	addPolygon();

    //画矩形
    $('#polygon').click(function(){
    	beginDrawMap();
    });
    //清空地图
    $('#clearMap').click(function(){
    	map.clearMap();  // 清除地图覆盖物
    	addPolygon();
    });
    
    $('#northLatitude').bind('input propertychange', function() { 
    	changeLatLon();
    });
    $('#southLatitude').bind('input propertychange', function() { 
    	changeLatLon();
    });
    $('#westLongitude').bind('input propertychange', function() { 
    	changeLatLon();
    });
    $('#eastLongitude').bind('input propertychange', function() { 
    	changeLatLon();
    });
}
//修改经纬度
function changeLatLon(){
	map.clearMap();  // 清除地图覆盖物
	addPolygon();
	var northLatitude = $('#northLatitude').val();
	var southLatitude = $('#southLatitude').val();
	var westLongitude = $('#westLongitude').val();
	var eastLongitude = $('#eastLongitude').val();
	
	var polygonArr = new Array();//多边形覆盖物节点坐标数组
    polygonArr.push([westLongitude, northLatitude]);
    polygonArr.push([eastLongitude, northLatitude]);
    polygonArr.push([eastLongitude, southLatitude]);
    polygonArr.push([westLongitude, southLatitude]);
    var  polygon = new AMap.Polygon({
        path: polygonArr,//设置多边形边界路径
        fillColor:"white",//填充色
    	strokeColor:"#FFAA00",//轮廓线透明度
    	fillOpacity:0.3
    });
    polygon.setMap(map);
}
//开始画地图
function beginDrawMap(){
//	map.clearMap();  // 清除地图覆盖物
	var mouseTool = new AMap.MouseTool(map); //在地图中添加MouseTool插件
    var drawRectangle = mouseTool.rectangle({//用鼠标工具画矩形
    	fillColor:"white",//填充色
    	strokeColor:"#FFAA00"//轮廓线透明度
    }); 
    AMap.event.addListener(mouseTool,'draw',function(e){ //添加事件
    	var path = e.obj.getPath();
    	var northLatitude = path[2].lat;//北纬
    	var southLatitude = path[0].lat;//南纬
    	var westLongitude = path[3].lng;//西经
    	var eastLongitude = path[1].lng;//东经
    	//限制区域
    	if(northLatitude > 45 && southLatitude < -10 && westLongitude < 95 && eastLongitude > 142) {
    		alert("该区域数据不存在");
    		return;
    	}else if(northLatitude > 45){
    		$('#northLatitude').val(45);
    	}else if(southLatitude < -10){
    		$('#southLatitude').val(-10);
    	}else if(westLongitude < 95){
    		$('#westLongitude').val(95);
    	}else if(eastLongitude > 142){
    		$('#eastLongitude').val(142);
    	}
        $('#northLatitude').val(northLatitude.toFixed(2));
        $('#southLatitude').val(southLatitude.toFixed(2));
        $('#westLongitude').val(westLongitude.toFixed(2));
        $('#eastLongitude').val(eastLongitude.toFixed(2));
        mouseTool.close();//关闭鼠标操作
    });
}
//填充字符串
function fillString(len, str){
	var length = str.length;
	if(length < len) {
		str += "<span style='opacity: 0;'>";
		for (var i = 0; i < len - length; i++) {
			str += "1";
		}
		str += "</span>";
	}
	return str;
}
//获得选择的值
function checkedValues(name, str){
	var values = "";
	$("input[name="+name+"]:checked").each(function(){ 
		values += $(this).val() + str; 
    });
    if(values != "") //把最后一个逗号去掉
    	values = values.substring(0, values.length - 1); 
    return values;
}