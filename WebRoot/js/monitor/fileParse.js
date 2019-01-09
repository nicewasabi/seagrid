/**
 * 文件监控查看js
 */
$(function(){
//	tab();
//	tab2();
	//查询数据
	getAllParseLog();
	getAllParseTask();
	$("#103").addClass("selectedTitle");
});

function getLikeValue(){
	if($('#like').val() == "监控路径查询" || $('#like').val()=="") {
		return "";
	}else{
		return $('#like').val();
	}
}

/**
 * 获取昨天的年月日字符串比如：20170829
 * @returns
 */
/*function getyyyyMMdd(){
	 var date = new Date();
	 var month=(date.getMonth()+1).toString().length==1?'0'+(date.getMonth()+1):date.getMonth()+1;
	 var day = (date.getDate()-1).toString().length==1?'0'+(date.getDate()-1):(date.getDate()-1);
	 return date.getFullYear().toString()+month+day;
	 //测试
//	 return '20170822';
}*/
/**
 * 删除监控任务
 */
function deleteTask(id){
	/*var row = $('#dg2').datagrid('getSelected');
	if (row){
		$.messager.confirm('Confirm','Are you sure you want to destroy this task?',function(r){
			if (r){
				$.post('/seaGrid/fileParseLog/deleteTask.do',{id:id},function(result){
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
	}*/
	
}
/**
 * 根据ID展示任务原始数据
 */
function updateTask(id){
//	 $('#updateTask').modal("show");//触发模态框
//	$('#updateTask').modal({keyboard : true});
		//获取之前的值写入框
	/*	 $.post("/seaGrid/fileParseLog/getTaskById.do", {id:id}, function(product){
			 $('#id').val(product.task.id);
	 		$('#filePath').val(product.task.filePath);
	 		$('#fileName').val(product.task.fileName);
	 		$('#endTime').val(product.task.endTime);
	 		$('#deadLine').val(product.task.deadLine);
	 		
	 	});
	 */
	 
	 //修改保存
}
/**
 * 保存修改
 * @param id
 */
function saveTask(){
	/*var param = {
			id:$("#id").val(),
			fileName: $("#fileName").val(),
			filePath:$("#filePath").val(),
			endTime:$("#endTime").val(),
			deadLine:$("#deadLine").val()
	}
	$.post("/seaGrid/fileParseLog/updateTaskById.do",param,function(flag){
		if(flag.flag=="true"){
			alert("保存成功！")
			$('#updateTask').modal("hide");
		}else{
			alert("保存失败");
		}
	});*/
}

/**
 * 保存增加
 * @param id
 */
function saveAddTask(){
/*	var param = {
			fileName: $("#fileName_add").val(),
			filePath:$("#filePath_add").val(),
			toPath:$("#toPath_add").val(),
			endTime:$("#endTime_add").val(),
			deadLine:$("#deadLine_add").val()
	}
	$.post("/seaGrid/fileParseLog/addTask.do",param,function(flag){
		if(flag.flag=="true"){
			alert("添加成功！")
			$('#addTask').modal("hide");
		}else{
			alert("添加失败");
		}
	});*/
}
/**
 * 查询所有文件监控配置信息
 */
function getAllParseTask(){
	var like = getLikeValue();
	
	$('#task').datagrid({
		idField : 'id',
		nowrap : true,//设置为 true，则把数据显示在一行里。设置为 true 可提高加载性能。
		url : '/seaGrid/fileParseTask/getAllParseTask.do',
		queryParams: {
		       like: like,
		       orderBy: $("#orderBy2").val()
		},
	    columns:[[
	      		{field:'shellcode',title:'步骤名称',width:219,height:30,align:'center',
	      			formatter : function(value,row,index){
	      					return shell[value];
	      			}},
	      		{field:'monitorpath',title:'监控路径',width:365,height:30,align:'center'},
	      		{field:'product_type',title:'产品名称',width:150,height:30,align:'center',
	      			formatter: function(value, row, index){
	      			return product[value];
	      		}},
	      		{field:'filenum',title:'文件数量',width:142,height:30,align:'center'},
	      		{field:'id',title:'操作',width:142,height:30,align:'center',
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
	$('#task').datagrid('reload');
}

/**
 * 查询所有文件监控日记数据
 */
function getAllParseLog(){
	var startDate = $("#startDate").val();
	if(isEmpty(startDate)){
		startDate=getyyyyMMdd();
		$("#startDate").val(startDate);
	}
	var endDate = $("#endDate").val();
	if(isEmpty(endDate)){
		endDate=getyyyyMMdd();
		$("#endDate").val(endDate);
	}
	var orderBy = $('#orderBy').val() + $('input[name=desc]:checked').val();
	
	$('#parseLog').datagrid({
		idField : 'id',
		nowrap : true,//设置为 true，则把数据显示在一行里。设置为 true 可提高加载性能。
		url : '/seaGrid/fileParseLog/getAllParseLog.do',
		queryParams: {
		       startDate: startDate,
		       endDate: endDate,
		       orderBy: orderBy
		},
	    columns:[[
	      		{field:'shellcode',title:'解析过程',width:228,align:'center',
	      			formatter:function(value,row,index){
	      					return shell[value];
	      			}},
	      		{field:'monitorpath',title:'文件路径',width:379,align:'center'},
	      		{field:'optime',title:'抓取时间',width:168,align:'center',
	      			formatter:function(value,row,index){
	      				return getSmpFormatDateByLong(value);
	      			}},
	      		{field:'yyyymmdd',title:'文件日期',width:80,align:'center'},
	      		{field:'file_num',title:'文件数量',width:83,align:'center'},
	      		{field:'isnormal',title:'推送状态',width:80,align:'center',
	      			formatter:function(value,row,index) {
	      				if(value==1){
	      					return "正常";
	      				}else if(value==0){
	      					return "<font style='color:red;'>异常</font>";
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
		singleSelect: true,
		rowStyler: function(index,row){
			/*if(row.isnormal==0){
				return "font-color:#E3E4E6; ";
			}else if(row.isnormal==0){
				return "background-color:#F2DEDE";
			}*/
		},
		toolbar : "#tb"
	});
	$('#parseLog').datagrid('reload');
}
/**
 * 修改查询条件时执行--监控配置
 */
function updateOption2(){
	var query = {
		like: $('#like').val(),
		orderBy : $('#orderBy2').val() + $('input[name=desc2]:checked').val()
	};
	$('#task').datagrid('options').queryParams = query;
	$('#task').datagrid('reload');
	$('#task').datagrid('clearSelections').datagrid('clearChecked');
}
/**
 * 修改查询条件时执行--日志
 */
function updateOption(){
	var startTime = $('#startDate').val();
	if(isEmpty(startTime)){
		startTime=getyyyyMMdd();
	}
	var endTime = $("#endDate").val();
	if(isEmpty(endTime)){
		endTime=getyyyyMMdd();
	}
	var orderBy = $('#orderBy').val() + $('input[name=desc]:checked').val()
	var query = {
		startDate: startTime,
		endDate: endTime,
		orderBy: orderBy
	};
	$('#parseLog').datagrid('options').queryParams = query;
	$('#parseLog').datagrid('reload');
	$('#parseLog').datagrid('clearSelections').datagrid('clearChecked');
}