/**
 * 获取当前日期的年月日字符串比如：20170829
 * @returns
 */
function getyyyyMMdd(){
	 var date = new Date();
	 var month=(date.getMonth()+1).toString().length==1?'0'+(date.getMonth()+1).toString():(date.getMonth()+1);
	 var day = (date.getDate()-1).toString().length==1?'0'+(date.getDate()-1):(date.getDate()-1);
	 return date.getFullYear().toString()+month+day;
	 //测试
//	 return '20170822';
}
/**
 * @author zhongsb
 * 脚本编号自定义
 * 【注】如果修改需要修改调用脚本方法后面的日志更新方法
 */
var shell={
	1:"WAMHRES-12生成grib和micaps",
	2:"WAMHRES-00生成grib和micaps",
	3:"ftp获取00时次文件",
	4:"ftp获取12时次文件",
	5:"wget获取流数据",
	6:"rtofs：nc生成micaps",
	7:"HRES-12生成grib和micaps",
	8:"HRES-00生成grib和micaps"
}
/**
 * @author zhongsb
 * 自定义产品编号
 * 【注】如果修改需要修改解析功能对应的js分页显示内的代码
 */
var product = {
		1: "rtofs",
		2: "HRES",
		3: "WAMHRES"
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
 * 根据long型时间返回格式化的时间
 * @param l
 * @param isFull
 * @returns
 */
function getSmpFormatDateByLong(l, isFull) {
	return getSmpFormatDate(new Date(l), isFull);
}

/**
 * @author zhongsb
 * @param date
 * @param isFull 是否返回时分秒
 * @returns 返回格式化的时间，根据isFull参数确定是否返回时分秒
 */
function getSmpFormatDate(date, isFull) {
	var pattern = "";
	if (isFull == true || isFull == undefined) {
		pattern = "yyyy-MM-dd hh:mm:ss";
	} else {
		pattern = "yyyy-MM-dd";
	}
	return getFormatDate(date, pattern);
}

/**
 * 返回格式化数据
 * @param date
 * @param pattern
 * @returns
 */
function getFormatDate(date, pattern) {
	if (date == undefined) {
		date = new Date();
	}
	if (pattern == undefined) {
		pattern = "yyyy-MM-dd hh:mm:ss";
	}
	return date.format(pattern);
}

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	}
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
};

/**
 * @Description: 获取下拉列表中当前的上一个选项
 * @author: songwj
 * @date: 2017-10-23 下午5:44:01
 * @param id select标签的id
 */
function getSelectPreOption(id) {
	var timelimit = document.getElementById(id);
	// 获取选中的下标值
	var index = document.getElementById(id).selectedIndex;
	
	if(index > 0){
		index = index - 1;
		var value = timelimit.options[index].value;
		document.getElementById(id).value = value;
	}
}

/**
 * @Description: 获取下拉列表中当前的下一个选项
 * @author: songwj
 * @date: 2017-10-23 下午5:44:22
 * @param id
 */
function getSelectNextOption(id) {
	var timelimit = document.getElementById(id);
	// 获取下拉列表长度
	var len = timelimit.options.length;
	// 获取选中的下标值
	var index = document.getElementById(id).selectedIndex;
	
	if(index <= (len - 2)){
		index = index + 1;
		var value = timelimit.options[index].value;
		document.getElementById(id).value = value;
	}
}

/**
 * @Description: 将时间按时间间隔往前推（减）
 * @author: songwj
 * @date: 2017-10-24 上午11:25:35
 * @param timeInterval 时间间隔
 * @param id 小时或季度id
 * @param timeId 时间日期id
 * @param pattern 日期模版，如：yyyyMMdd
 */
function subTimeByInterval(timeInterval, id, timeId, pattern) {
	if (timeInterval == "hour") {
		var hourStart = document.getElementById(id);
		var len = hourStart.options.length;
		var index = hourStart.selectedIndex;

		if (index > 0) {
			index = index - 1;
			var value = hourStart.options[index].value;
			hourStart.value = value;
		} else {
			var value = hourStart.options[len - 1].value;
			hourStart.value = value;

			// 将日期向前推一天
			var timeStart = document.getElementById(timeId).value;
			while(timeStart.indexOf("-") != -1) {
				timeStart = timeStart.replace("-", "");
			}
			var year = timeStart.substring(0, 4);
			var month = timeStart.substring(4, 6);
			var day = timeStart.substring(6, 8);
			// 减一天
			timeStart = new Date(year, month - 1, day).getTime() - 24 * 60 * 60 * 1000;
			timeStart = getFormatDate(new Date(timeStart), pattern);
			document.getElementById(timeId).value = timeStart;
		}
	} else if (timeInterval == "day") {
		// 将日期向前推一天
		var timeStart = document.getElementById(timeId).value;
		while(timeStart.indexOf("-") != -1) {
			timeStart = timeStart.replace("-", "");
		}
		var year = timeStart.substring(0, 4);
		var month = timeStart.substring(4, 6);
		var day = timeStart.substring(6, 8);
		// 减一天
		timeStart = new Date(year, month - 1, day).getTime() - 24 * 60 * 60 * 1000;
		timeStart = getFormatDate(new Date(timeStart), pattern);
		document.getElementById(timeId).value = timeStart;
	} else if (timeInterval == "month") {
		var timeStart = document.getElementById(timeId).value;
		while(timeStart.indexOf("-") != -1) {
			timeStart = timeStart.replace("-", "");
		}
		var year = timeStart.substring(0, 4);
		var month = timeStart.substring(4, 6);
		// 减一月
		timeStart = new Date(year, month - 1, 1).getTime() - 15 * 24 * 60 * 60 * 1000;
		timeStart = getFormatDate(new Date(timeStart), pattern);
		document.getElementById(timeId).value = timeStart;
	} else if (timeInterval == "quarter") {
		var quarterStart = document.getElementById(id);
		var len = quarterStart.options.length;
		var index = quarterStart.selectedIndex;

		if (index > 0) {
			index = index - 1;
			var value = quarterStart.options[index].value;
			quarterStart.value = value;
		} else {
			var value = quarterStart.options[len - 1].value;
			quarterStart.value = value;

			// 将日期向前推一年
			var timeStart = document.getElementById(timeId).value;
			var year = timeStart.substring(0, 4);
			// 减一年
			timeStart = new Date(year, 0, 1).getTime() - 180 * 24 * 60 * 60 * 1000;
			timeStart = getFormatDate(new Date(timeStart), pattern);
			document.getElementById(timeId).value = timeStart;
		}
	} else {
		// 将日期向前推一年
		var timeStart = document.getElementById(timeId).value;
		var year = timeStart.substring(0, 4);
		// 减一年
		timeStart = new Date(year, 0, 1).getTime() - 180 * 24 * 60 * 60 * 1000;
		timeStart = getFormatDate(new Date(timeStart), pattern);
		document.getElementById(timeId).value = timeStart;
	}
}

/**
 * @Description: 将时间按时间间隔往后推（增）
 * @author: songwj
 * @date: 2017-10-24 上午11:26:38
 * @param timeInterval 时间间隔
 * @param id 小时或季度id
 * @param timeId 时间日期id
 * @param pattern 日期模版，如：yyyyMMdd
 */
function addTimeByInterval(timeInterval, id, timeId, pattern) {
	if (timeInterval == "hour") {
		var hourStart = document.getElementById(id);
		var len = hourStart.options.length;
		var index = hourStart.selectedIndex;

		if (index < (len - 1)) {
			index = index + 1;
			var value = hourStart.options[index].value;
			hourStart.value = value;
		} else {
			var value = hourStart.options[0].value;
			hourStart.value = value;

			// 将日期向后推一天
			var timeStart = document.getElementById(timeId).value;
			while(timeStart.indexOf("-") != -1) {
				timeStart = timeStart.replace("-", "");
			}
			var year = timeStart.substring(0, 4);
			var month = timeStart.substring(4, 6);
			var day = timeStart.substring(6, 8);
			// 加一天
			timeStart = new Date(year, month - 1, day).getTime() + 24 * 60 * 60 * 1000;
			timeStart = getFormatDate(new Date(timeStart), pattern);
			document.getElementById(timeId).value = timeStart;
		}
	} else if (timeInterval == "day") {
		// 将日期向后推一天
		var timeStart = document.getElementById(timeId).value;
		while(timeStart.indexOf("-") != -1) {
			timeStart = timeStart.replace("-", "");
		}
		var year = timeStart.substring(0, 4);
		var month = timeStart.substring(4, 6);
		var day = timeStart.substring(6, 8);
		// 加一天
		timeStart = new Date(year, month - 1, day).getTime() + 24 * 60 * 60 * 1000;
		timeStart = getFormatDate(new Date(timeStart), pattern);
		document.getElementById(timeId).value = timeStart;
	} else if (timeInterval == "month") {
		var timeStart = document.getElementById(timeId).value;
		while(timeStart.indexOf("-") != -1) {
			timeStart = timeStart.replace("-", "");
		}
		var year = timeStart.substring(0, 4);
		var month = timeStart.substring(4, 6);
		// 加一月
		timeStart = new Date(year, month - 1, 28).getTime() + 15 * 24 * 60 * 60 * 1000;
		timeStart = getFormatDate(new Date(timeStart), pattern);
		document.getElementById(timeId).value = timeStart;
	}  else if (timeInterval == "quarter") {
		var quarterStart = document.getElementById(id);
		var len = quarterStart.options.length;
		var index = quarterStart.selectedIndex;

		if (index < (len - 1)) {
			index = index + 1;
			var value = quarterStart.options[index].value;
			quarterStart.value = value;
		} else {
			var value = quarterStart.options[0].value;
			quarterStart.value = value;

			// 将日期增一年
			var timeStart = document.getElementById(timeId).value;
			var year = timeStart.substring(0, 4);
			// 加一年
			timeStart = new Date(year, 11, 28).getTime() + 180 * 24 * 60 * 60 * 1000;
			timeStart = getFormatDate(new Date(timeStart), pattern);
			document.getElementById(timeId).value = timeStart;
		}
	} else {
		// 将日期向前推一年
		var timeStart = document.getElementById(timeId).value;
		var year = timeStart.substring(0, 4);
		// 减一年
		timeStart = new Date(year, 11, 28).getTime() + 180 * 24 * 60 * 60 * 1000;
		timeStart = getFormatDate(new Date(timeStart), pattern);
		document.getElementById(timeId).value = timeStart;
	}
}