// 鼠标移入移出效果切换
$(function() {
	$("#leftPannel").hover(function() {
		$(this).stop(true, false).animate({
			"opacity" : "1"
		}, 300);
	}, function() {
		$(this).stop(true, false).animate({
			"opacity" : "0.4"
		}, 300);
	});
	
	$("#rightPannel").hover(function() {
		$(this).stop(true, false).animate({
			"opacity" : "1"
		}, 300);
	}, function() {
		$(this).stop(true, false).animate({
			"opacity" : "0.4"
		}, 300);
	});
});