/**
 * 文件监控查看js
 */
$(function(){
//	tab();
//	tab2();
	//查询数据
	getAllMonitorLog();
	getAllMonitorTask();
	$("#101").addClass("selectedTitle");
});

function addTask(){
	alert(111);
}

function tab2(){
	$('#myTab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
	});
}

function tab(){
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
		// 获取已激活的标签页的名称
		var activeTab = $(e.target).text(); 
		// 获取前一个激活的标签页的名称
		var previousTab = $(e.relatedTarget).text(); 
		$(".active-tab span").html(activeTab);
		$(".previous-tab span").html(previousTab);
	});
}

function getLikeValue(){
	$('#like').focus(function(){
		if($('#like').val() == "文件名或者文件路径查询") {
			return "";
		}else{
			return $('#like').val();
		}
	});
}
/**
 * 判断一个字符串是否为null
 * @param startTime
 * @returns {Boolean}
 */
function isEmpty(startTime){
	if(startTime==null || startTime==""){
		return true;
	}
	return false;
}


/**
 * 删除监控任务
 */
function deleteTask(id){
	var row = $('#dg2').datagrid('getSelected');
	if (row){
		$.messager.confirm('Confirm','Are you sure you want to destroy this task?',function(r){
			if (r){
				$.post('/seaGrid/monitor/deleteTask.do',{id:id},function(result){
					if (result.success){
						$('#dg2').datagrid('reload');	// reload the user data
					} else {
						$.messager.show({	// show error message
							title: 'Error',
							msg: result.errorMsg
						});
					}
				},'json');
			}
		});
	}
	
}
/**
 * 根据ID展示任务原始数据
 */
function updateTask(id){
//	 $('#updateTask').modal("show");//触发模态框
//	$('#updateTask').modal({keyboard : true});
		//获取之前的值写入框
		 $.post("/seaGrid/monitor/getTaskById.do", {id:id}, function(product){
			 $('#id').val(product.task.id);
	 		$('#filePath').val(product.task.filePath);
	 		$('#fileName').val(product.task.fileName);
	 		$('#endTime').val(product.task.endTime);
	 		$('#deadLine').val(product.task.deadLine);
	 		
	 	});
	 
	 
	 //修改保存
}
/**
 * 保存修改
 * @param id
 */
function saveTask(){
	var param = {
			id:$("#id").val(),
			fileName: $("#fileName").val(),
			filePath:$("#filePath").val(),
			endTime:$("#endTime").val(),
			deadLine:$("#deadLine").val()
	}
	$.post("/seaGrid/monitor/updateTaskById.do",param,function(flag){
		if(flag.flag=="true"){
			alert("保存成功！")
			$('#updateTask').modal("hide");
		}else{
			alert("保存失败");
		}
	});
}

/**
 * 保存增加
 * @param id
 */
function saveAddTask(){
	var param = {
			fileName: $("#fileName_add").val(),
			filePath:$("#filePath_add").val(),
			toPath:$("#toPath_add").val(),
			endTime:$("#endTime_add").val(),
			deadLine:$("#deadLine_add").val()
	}
	$.post("/seaGrid/monitor/addTask.do",param,function(flag){
		if(flag.flag=="true"){
			alert("添加成功！")
			$('#addTask').modal("hide");
		}else{
			alert("添加失败");
		}
	});
}
/**
 * 查询所有文件监控配置信息
 */
function getAllMonitorTask(){
	$('#dg2').datagrid({
		idField : 'id',
		nowrap : true,//设置为 true，则把数据显示在一行里。设置为 true 可提高加载性能。
		url : '/seaGrid/monitor/getAllMonitorTask.do',
//		sortName:[fileName,filePath,endTime,deadLine],
		queryParams: {
		       like: getLikeValue(),
		       orderBy2: $("#orderBy2").val()
		},
	    columns:[[
	      		{field:'productId',title:'产品类别',width:144,height:30,align:'center',
	      			formatter : function(value,row,index){
	      				if(value==1){
	      					return '大气模式确定性预报';
	      				}else if(value==2){
	      					return '海浪模式';
	      				}
	      			}},
	      		{field:'filePath',title:'文件路径',width:295,height:30,align:'center'},
	      		{field:'toPath',title:'本地路径',width:290,height:30,align:'center'},
	      		{field:'businessCode',title:'时次',width:150,height:30,align:'center'},
	      		/*{field:'deadLine',title:'失效时间',width:150,height:30,align:'center'},*/
	      		{field:'id',title:'操作',width:140,height:30,align:'center',
	      			formatter : function(value,row,index) {
						var id = row.id;
						var butHtml = '<button type="button" class="btn  btn-info" onclick="deleteTask(\''+id+'\')">删除</button>&nbsp;&nbsp;';
						butHtml += '<button type="button" class="btn  btn-success" data-toggle="modal" data-target="#updateTask" onclick="updateTask(\''+id+'\')">修改</button>';
						return butHtml;
					}
	      		}
	          ]],
		checkOnSelect : false,//如果设置为 false 时，只有当用户点击了复选框时，才会选中/取消选中复选框。
		ctrlSelect : true,
		striped : true,//设置为 true，则把行条纹化。
		pagination : true,//设置为 true，则在数据网格（datagrid）底部显示分页工具栏。
		rownumbers : true,//设置为 true，则显示带有行号的列。
		pageSize : 10,
		pageList : [10],
		toolbar : "#tb2"
	});
	$('#dg2').datagrid('reload');
}
/**
 * 查询所有文件监控日记数据
 */
function getAllMonitorLog(){
	var beginDate = $("#beginDate").val();
	if(isEmpty(beginDate)){
		beginDate=getyyyyMMdd();
		$("#beginDate").val(beginDate);
	}
	var endDate = $("#endDate").val();
	if(isEmpty(endDate)){
		endDate=getyyyyMMdd();
		$("#endDate").val(endDate);
	}
	var orderBy = $('#orderBy').val() + $('input[name=desc]:checked').val()
	
	$('#dg').datagrid({
		idField : 'id',
		nowrap : true,//设置为 true，则把数据显示在一行里。设置为 true 可提高加载性能。
		url : '/seaGrid/filelog/getAllMonitorLog.do',
		queryParams: {
		       startTime: beginDate,
		       endTime: endDate,
		       orderBy: orderBy
		},
	    columns:[[
	      		{field:'fileName',title:'文件名',width:408,align:'center'},
	      		{field:'filePath',title:'文件路径',width:255,align:'center'},
	      		{field:'lastmodifiedtime',title:'抓取时间',width:187,align:'center',
	      			formatter:function(value,row,index){
	      				return getSmpFormatDateByLong(value);
	      			}},
	      		{field:'yyyymmdd',title:'文件日期',width:90,align:'center'},
	      		{field:'status',title:'推送状态',width:80,align:'center',
	      			formatter:function(value,row,index) {
	      				if(value==1){
	      					return "正常";
	      				}else if(value==2){
	      					return "迟到";
	      				}else if(value==4){
	      					return "失效";
	      				}else if(value==0){
	      					return "ftp连接异常";
	      				}else if(value==3){
	      					return "ftp抓取异常";
	      				}
	      			}
	      		}
	          ]],
		checkOnSelect : false,//如果设置为 false 时，只有当用户点击了复选框时，才会选中/取消选中复选框。
		ctrlSelect : true,
		striped : false,//设置为 true，则把行条纹化。
		pagination : true,//设置为 true，则在数据网格（datagrid）底部显示分页工具栏。
		rownumbers : true,//设置为 true，则显示带有行号的列。
		pageSize : 10,
		pageList : [10],
		rowStyler: function(index,row){
			/*if(row.status==1){
				return 'background-color:#DFF0D8;';
			}else*/ if(row.status==2){
				return "background-color:#FCF8E3";
			}else if(row.status==4){
				return "background-color:#F2DEDE";
			}else if(row.status==4){
				return "background-color:red";
			}
		},
		toolbar : "#tb"
	});
	$('#dg').datagrid('reload');
}
/**
 * 修改查询条件时执行--监控配置
 */
function updateOption2(){
	
	var query = {
		like: $('#like').val(),
		orderBy2 : $('#orderBy2').val() + $('input[name=desc2]:checked').val()
	};
	$('#dg2').datagrid('options').queryParams = query;
	$('#dg2').datagrid('reload');
	$('#dg2').datagrid('clearSelections').datagrid('clearChecked');
}
/**
 * 修改查询条件时执行--日志
 */
function updateOption(){
	var startTime = $('#beginDate').val();
	if(isEmpty(startTime)){
		startTime=getyyyyMMdd();
	}
	var endTime = $("#endDate").val();
	if(isEmpty(endTime)){
		endTime=getyyyyMMdd();
	}
	var query = {
		orderBy : $('#orderBy').val() + $('input[name=desc]:checked').val(),
		startTime: startTime,
		endTime: endTime,
		status: 1
	};
	$('#dg').datagrid('options').queryParams = query;
	$('#dg').datagrid('reload');
	$('#dg').datagrid('clearSelections').datagrid('clearChecked');
}