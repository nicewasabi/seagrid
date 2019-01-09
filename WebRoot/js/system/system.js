$(function () {
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
	
	
	
});


//查询分页信息
function getPage() {
	$('#tt').datagrid({//查询每一个根节点下这个人的oid
		idField : 'id',
		nowrap : true,
		url : '${ctx }/ipLog/page.do',
		checkOnSelect : false,
		ctrlSelect : true,
		striped : true,
		pagination : true,
		rownumbers : true,
		nowrap : false,
		columns : [ [
				{field : 'ip',title : 'IP',width : '150px',align : 'center'},
				{field : 'url',title : 'URL',width : '250px',align : 'center'},
				{field : 'method',title : '请求类型',width : '150px',align : 'center'},
				{field : 'params',title : '参数',width : '170px',align : 'center'},
				/* {field : 'userId',title : '访问ID',width : '50px',align : 'center'}, */
				{field : 'userName',title : '访问者',width : '150px',align : 'center'},
				{field : 'createTime',title : '访问时间',width : '150px',align : 'center',
					formatter : function(value,row,index) {
						var d = new Date(value);
						return d.pattern("yyyy-MM-dd hh:mm:ss");
					}
				}]],
		pageSize : 20,
		pageList : [10,20],
		toolbar : "#tb"
	});
	$('#tt').datagrid('reload');
}

//查询
function seach() {
	var query = {
		beginTime : $('#beginTime').val(),
		endTime : $('#endTime').val(),
		userName : $('#logUserName').val(),
		ip : $('#logIp').val()
	};
	$('#tt').datagrid('options').queryParams = query;
	$('#tt').datagrid('reload');
	$('#tt').datagrid('clearSelections').datagrid('clearChecked');
}