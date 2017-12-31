//存放主要交互逻辑js代码
//javascript 模块化
var seckill = {
		//封装秒杀相关ajax的URL
		URL : {
			now : function(){
				return '/seckill/time/now';
			},
			exposer : function(seckillId){
				return '/seckill/'+seckillId+'/exposer';
			},
			execution : function(seckillId,md5){
				return '/seckill/'+seckillId+'/'+md5+'/execution';
			}
		},
		handleSeckillkill : function(seckillId,node){
			node.hide()
				.html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
			$.post(seckill.URL.exposer(seckillId),{},function(result){
				if(result && result['success']){
					var exposer = result['data'];
					if(exposer['exposed']){
						var md5 = exposer['md5'];
						var killUrl = seckill.URL.execution(seckillId,md5);
						console.log("killUrl:"+killUrl);
						$('#killBtn').one('click',function(){
							$(this).addClass('disabled');
							$.post(killUrl,{},function(result){
								if(result && result['success']){
									var killResult = result['data'];
									var state = killResult['state'];
									var stateInfo = killResult['stateInfo'];
									node.html('<span class="label label-success">'+stateInfo+'</span>');
								}
							});
						});
						node.show();
					}else{
						var now = exposer['now'];
						var start = exposer['start'];
						var end = exposer['end'];
						seckill.countdown(seckillId,now,start,end);
					}
					
				}else{
					console.log('result:'+result);
				}
			});
		},
		//验证手机号
		validatePhone: function(phone){
			if(phone && phone.length == 11 && !isNaN(phone)){
				return true;
			}else{
				return false;
			}
			
		},
		countdown : function(seckillId,nowTime,startTime,endTime){
			var seckillBox = $('#seckill-box');
			if(nowTime>endTime){
				seckillBox.html("秒杀结束");
			}else if(nowTime<startTime){
				var killTime = new Date(startTime+1000);
				console.log("killTime:"+killTime);
				seckillBox.countdown(killTime,function(event){
					var format = event.strftime('秒杀倒计时：%D天  %H时 %M分 %S秒');
					seckillBox.html(format);
				}).on('finish.countdown',function(){
					seckill.handleSeckillkill(seckillId,seckillBox);
				});
			}else{
				seckill.handleSeckillkill(seckillId,seckillBox);
			}
		},
		detail : {
			init : function(params){
				//手机验证和登录，计时交互
				//规划我们的交互流程
				//在cookie中查找手机号
				var killPhone = $.cookie('killPhone');
				
				//验证手机号
				if(!seckill.validatePhone(killPhone)){
					//绑定phone
					//控制输出
					var killPhoneModal = $('#killPhoneModal');
					killPhoneModal.modal({
						show:true,
						backdrop: "static",
						keyboard: false
						
					});
					$('#killPhoneBtn').click(function(){
						var inputPhone = $('#killPhoneKey').val();
						console.log('inputPhone='+inputPhone);
						if(seckill.validatePhone(inputPhone)){
							$.cookie('killPhone',inputPhone,{expires:7,path:'/seckill'});
							window.location.reload();
						}else{
							$('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误</label>').show(300);
						}
					});
				}
				var startTime = params['startTime'];
				var endTime = params['endTime'];
				var seckillId = params['seckillId'];
				console.log("startTime:"+startTime);
				$.get(seckill.URL.now(),{},function(result){
					console.log("success:"+result['success']);
					if(result && result['success']){
						var nowTime = result['data'];
						console.log("nowTime:"+nowTime);
						seckill.countdown(seckillId,nowTime,startTime,endTime);
					}else{
						console.log('result:'+result);
					}
				});
			}
		}
}