<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!-- 滚动窗口 -->
<div style="margin: 2px;height: 34px;text-align: center;">
	<div class="window_left" id="msg_scroll_type">欢迎使用</div> 
	<div class="window_right">
		<table>
			<tr>
				<td style="width: 150px;text-align: center;">最新消息通知：</td>
				<td style="color: red; width:900px;">
					<!-- <marquee id="scrollText"  align="absmiddle" onmouseover="scrollText.stop();" onmouseout="scrollText.start();" scrollAmount="2" direction="left">
	     				您暂时没有消息
	           		</marquee> -->
				</td>
				<td style="width: 150px;"><a id="msg_jsp" href="#" style="color: black;text-decoration: none;">更多消息&gt;&gt;</a></td>
			</tr>
		</table>
	</div> 
</div>
<!-- /*<script type="text/javascript">
	 $(function(){
		$.post("${ctx}/userMessage/newMsg.do",{},function(msg){
			if(msg == null || msg == "")
				return;
			
			var date = new Date(msg.sendTime).pattern("yyyy-MM-dd");
			var marquee = msg.title +"&nbsp;&nbsp;&nbsp;("+date+"通知)";
			$('#scrollText').html(marquee);
			$('#msg_scroll_type').html(msg.type == 1 ? "更新通知" : "迟到通知");
			$('#msg_jsp').attr("href",msg.type == 1 ? "${ctx }/userMessage/message.html" : "${ctx }/userMessage/message.html");
		});
	}); 
</script>*/ -->
