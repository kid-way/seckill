<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common/tag.jsp" %>
<!DOCTYPE HTML>
<html>
  <head>
	<title>秒杀详情页</title>
	<%@ include file="common/head.jsp" %>
  </head>
  <style>
  	.modal-backdrop {
  		z-index:0;
  	}
  </style>
  <body>
  <div class="modal fade" id="killPhoneModal">
   		<div class="modal-dialog">
   			<div class="modal-content">
   				<div class="modal-header">
   					<h3 class="modal-title text-center">
   						<span class="glyphicon glyphicon-phone"></span>秒杀电话：
   					</h3>
   				</div>
   				<div class="modal-body">
   					<form>
   						<div class="row">
   						<div class="col-xs-8 col-xs-offset-2">
   							<input type="text" name="killPhone" id="killPhoneKey"
   							placeholder="填手机号^0^" class="form-control">
   						</div>
   					</div>
   					</form>	
   				</div>
   				<div class="modal-footer">
   					<span id="killPhoneMessage" class="glyphicon"></span>
   					<button type="button" id="killPhoneBtn" class="btn btn-success">
   						<span class="glyphicon glyphicon-phone"></span>
   						Submit
   					</button>
   				</div>
   			</div>
   		</div>
   	</div>
   	<div class="container">
   		<div class="panel panel-default text-center">
   			<div class="panel-heading">${seckill.name }</div>
   			<div class="panel-body">
   			<h2 class="text-danger">
   				<span class="glyphicon glyphicon-time"></span>
   				<span class="glyphicon" id="seckill-box"></span>
   			</h2>
   		</div>
   		</div>
   		
   	</div>
   	<!-- 登录弹出层，输入电话 -->
   	
   	
   	<script type="text/javascript" src="https://cdn.bootcss.com/jquery/2.0.0/jquery.min.js"></script>
  	<script type="text/javascript" src="https://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
  	
  	<script type="text/javascript" src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script> 
  	<script type="text/javascript" src="https://cdn.bootcss.com/jquery.countdown/2.1.0/jquery.countdown.min.js"></script>
  	
  	<script type="text/javascript" src="http://localhost:8080/seckill/resources/script/seckill.js"></script>
  	<script type="text/javascript">
  		$(function(){
  			seckill.detail.init({
  				seckillId : ${seckill.seckillId},
  				startTime : ${seckill.startTime.time},
  				endTime : ${seckill.endTime.time}
  			});
  		});
  	</script>
  </body>
</html>
