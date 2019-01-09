<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath }" scope="page"></c:set>
<!DOCTYPE HTML>
<html>
	<head>
	<meta charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>数据共享平台</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="" />
	<meta name="keywords" content="" />
	<meta name="author" content="" />
	
  	<!-- Facebook and Twitter integration -->
	<meta property="og:title" content=""/>
	<meta property="og:image" content=""/>
	<meta property="og:url" content=""/>
	<meta property="og:site_name" content=""/>
	<meta property="og:description" content=""/>
	<meta name="twitter:title" content="" />
	<meta name="twitter:image" content="" />
	<meta name="twitter:url" content="" />
	<meta name="twitter:card" content="" />	
	
	<!-- 标签页图标 -->
	<link rel="Shortcut Icon" href="${ctx }/static/favicon.ico">
	<!-- Animate.css -->
	<link rel="stylesheet" href="${ctx }/static/login/css/animate.css">
	<!-- Icomoon Icon Fonts-->
	<link rel="stylesheet" href="${ctx }/static/login/css/icomoon.css">
	<!-- Themify Icons-->
	<link rel="stylesheet" href="${ctx }/static/login/css/themify-icons.css">
	<!-- Bootstrap  -->
	<link rel="stylesheet" href="${ctx }/static/login/css/bootstrap.css">

	<!-- Magnific Popup -->
	<link rel="stylesheet" href="${ctx }/static/login/css/magnific-popup.css">

	<!-- Owl Carousel  -->
	<link rel="stylesheet" href="${ctx }/static/login/css/owl.carousel.min.css">
	<link rel="stylesheet" href="${ctx }/static/login/css/owl.theme.default.min.css">

	<!-- Theme style  -->
	<link rel="stylesheet" href="${ctx }/static/login/css/style.css">

	<!-- Modernizr JS -->
	<script src="${ctx }/static/login/js/modernizr-2.6.2.min.js"></script>
	<!-- FOR IE9 below -->
	<!--[if lt IE 9]>
	<script src="${ctx }/static/login/js/respond.min.js"></script>
	<![endif]-->
	<style type="text/css">
	.box-shadow-3{  
		-webkit-box-shadow:0 0 10px #b9c1ce;  
		-moz-box-shadow:0 0 10px #b9c1ce;  
		box-shadow:0 0 10px #b9c1ce;  
	}
	#showTK:HOVER {color: #f54c53;}  
	.form-group{margin-bottom: 0px;}
	</style>
	</head>
	<body>
<div id="page">
	
	<div class="page-inner">
	<!-- header -->
	<nav class="gtco-nav" role="navigation" style="border-bottom: 1px solid #ccc;">
		<div class="gtco-container">
			<div class="row">
				<div class="col-sm-4 col-xs-12">
					<img style="width: 60px;" src="${ctx }/static/logo.png"/>
					<div style="display: inline-block;font-size: 16px;">&nbsp;&nbsp;&nbsp;&nbsp;数据共享平台</div>
				</div>
	    		
				<div class="col-xs-8 text-right menu-1">
					<ul>
						<li class="has-dropdown">
							<a href="#" style="color: black;">关于我们</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</nav>
	<%-- background-image: url('${ctx}/system/login/beijing.png');background-position:0px 73px; --%>
	<header id="gtco-header" class="gtco-cover" role="banner" 
	style="background-color: white;">
		<div class="overlay"></div>
		<div class="gtco-container">
			<div class="row">
				<div class="col-md-12 col-md-offset-0 text-left">
					<div class="row row-mt-15em" style="margin-top: 150px">
						<div class="col-md-7 mt-text animate-box" data-animate-effect="fadeInUp">
							<img id="loginLeftImg" src="${ctx }/static/images/login_rassia.jpg" style="width: 100%;">
						</div>
						
						<div class="col-md-4 col-md-push-1 animate-box box-shadow-3" data-animate-effect="fadeInRight" style="padding: 0px;">
							<div class="form-wrap">
								<div class="tab">
									<ul class="tab-menu">
										<li class="active">
											<a href="${ctx }/static/login/#" data-tab="login" style="font-size: 24px;">用户登录</a>
										</li>
									</ul>
									<div class="tab-content">
										<!-- 登录 -->
										<div class="tab-content-inner active" data-content="login" id="loginWindow">
											<form action="#">
												<div class="row form-group">
													<div class="col-md-12">
														<label for="l_userName">
															用户名&nbsp;&nbsp;&nbsp;&nbsp;
															<span id="l_userName_tip" style="color: red"></span>
														</label>
														<input type="text" class="form-control" id="l_userName" placeholder="请输入用户名">
														<br>
													</div>
													
												</div>
												<div class="row form-group">
													<div class="col-md-12">
														<label for="l_password">
															密码
															&nbsp;&nbsp;&nbsp;&nbsp;
															<span id="l_password_tip" style="color: red"></span>
														</label>
														<input type="password" class="form-control" id="l_password" placeholder="请输入密码">
														<br>
													</div>
												</div>
												
												<div class="row form-group">
													<div class="col-md-12">
														<label for="l_code">
															验证码
															&nbsp;&nbsp;&nbsp;&nbsp;
															<span id="l_code_tip" style="color: red"></span>
														</label><br/>
														<input type="text" class="form-control" id="l_code"  placeholder="请输入验证码" style="width: 190px;display: inline-block;">
														<img id="verify_img" src="${ctx }/login/code.do" style="width: 100px; vertical-align: middle; cursor: pointer; 
														margin-left: 11px;position: relative;left: 0px;height: 43px;" title="点击刷新" />
													</div>
												</div>
												
												<div class="row form-group">
													<div class="col-md-12" style="text-align: right;">
														<label for="l_code">
															<a style="color: black;" href="${ctx }/reg/toReg">注册</a>					
														</label>
													</div>
												</div>
												
										
												<div class="row form-group">
													<div class="col-md-12">
														<input type="button" class="btn btn-primary" value="登录" style="width: 100%" onclick="login()">
													</div>
												</div>
											</form>	
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</header>
	<div>
	   	<table style="width: 100%;">
	   		<tr style="background-color: #0059a2;">
	   			<td style="width: 200px;height: 30px;">
		   			<div style="background-color: #0059a2;border-top: 1px solid #ccc;height: 32px;line-height: 32px;font-size: 12px;color: white;">
						<p style="float: left;margin-left: 100px;margin-bottom: 0px;">2017©版权：XXXXXXXXXXXXXXXXXXXX公司</p>
					</div>
	   			</td>
	   		</tr>
	   	</table>
    </div>
	
	</div>
</div>

	<!-- jQuery -->
	<script src="${ctx }/static/login/js/jquery.min.js"></script>
	<!-- jQuery Easing -->
	<script src="${ctx }/static/login/js/jquery.easing.1.3.js"></script>
	<!-- Bootstrap -->
	<script src="${ctx }/static/login/js/bootstrap.min.js"></script>
	<!-- Waypoints -->
	<script src="${ctx }/static/login/js/jquery.waypoints.min.js"></script>
	<!-- Carousel -->
	<script src="${ctx }/static/login/js/owl.carousel.min.js"></script>
	<!-- countTo -->
	<script src="${ctx }/static/login/js/jquery.countTo.js"></script>
	<!-- Magnific Popup -->
	<script src="${ctx }/static/login/js/jquery.magnific-popup.min.js"></script>
	<script src="${ctx }/static/login/js/magnific-popup-options.js"></script>
	<!-- Main -->
	<script src="${ctx }/static/login/js/main.js"></script>
	
	<!-- login script -->
	<script type="text/javascript">
		$(function(){
			//1.初始化页面样式
			//iniCss();
			//2.绑定登录事件
			document.onkeydown = function (event) { 
				var e = event || window.event || arguments.callee.caller.arguments[0];
				if (e && e.keyCode == 13) { 
					login();
				} 
			}; 
			//3.绑定验证码事件
			$("#verify_img").bind('click', function() {
	             $("#verify_img").attr("src", "${ctx}/login/code.do?time=" + new Date().getTime());
	        });
		});
		
		function iniCss(){
			setInterval(function(){
				//注册页面是否可见
				var hidden = $('#loginWindow').css('display');
				
				if(hidden  != 'none') {//注册页
					$('.gtco-cover').height(635);
					$('#page .page-inner').height(700);
				} else {//登录页
					$('.gtco-cover').height(800);
					$('#page .page-inner').height(900);
				}
			}, 100);
		}
		
		function login() {
			if (checkLoginInfo()) {
				//验证通过后登录
				$.ajax({
					cache : true,
					type : "POST",
					url : '${ctx }/login/go.do',
					data : {userName:$("#l_userName").val(), pwd:$("#l_password").val(), verification:$("#l_code").val()}, // 你的formid
					success : function(msg) {//1=可以登录，2=用户不存在，3=用户被锁，4=未审核，5=审核未通过,6=密码错误,7=验证码错误
						 if (msg == "1") {
							//$("#l_userName_tip").text('可以登录');
							window.location = "${ctx }";
						} else if (msg == "2") {
							$("#l_userName_tip").text('用户不存在');
						} else if (msg == "3") {
							$("#l_userName_tip").text('用户被锁，请联系管理员');
						} else if (msg == "4"){
							$("#l_userName_tip").text('未审核，请您耐心等候');
						} else if (msg == "5"){
							$("#l_userName_tip").text('审核未通过，请联系管理员');
						} else if (msg == "6"){
							$("#l_userName_tip").text('密码错误');
						} else if (msg == "7"){
							$("#l_userName_tip").text('验证码错误');
						}
						$("#verify_img").click();
					},
					error : function(msg) {
						$("#l_userName_tip").text('用户名或密码错误');
						$("#verify_img").click();
					}
				});
			}
		}
		
		function checkLoginInfo() {
			var username = $("#l_userName").val();
			var password = $("#l_password").val();
			var code = $("#l_code").val();
			
			if ("" == username || null == username) {
				$("#l_userName_tip").text('请输入账号');
				return false;
			} else {
				$("#l_userName_tip").text('');
			}
			
			if ("" == password || null == password) {
				$("#l_password_tip").text('请输入密码');
				return false;
			} else {
				$("#l_password_tip").text('');
			}
			
			if ("" == code || null == code) {
				$("#l_code_tip").text('请输入验证码');
				return false;
			} else {
				$("#l_code_tip").text('');
			}
			
			return true;
		}
	</script>
	</body>
</html>

