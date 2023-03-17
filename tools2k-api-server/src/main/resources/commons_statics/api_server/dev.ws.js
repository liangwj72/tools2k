$(function() {
	initVm();
});

var lastObj = null;
function show(id) {
		var obj = document.getElementById(id);

		if (obj != null) {
			if (lastObj != null) {
				lastObj.style.display = "none";
			}

			if (obj.style.display == "none") {
				obj.style.display = "block";
				lastObj = obj;
			} else {
				obj.style.display = "none";
			}
		}
	};

	  /**
		 * 格式时间 - Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 -
		 * Format("yyyy-M-d h:m:s.S") ==> 2006-7-2 8:9:4.18
		 * 
		 * @param date
		 *            Date对象
		 * @param fmt
		 *            格式字符串
		 * @returns {string}
		 */
	 function dateFormat(date, fmt) {
	    const o = {
	      'M+': date.getMonth() + 1, // 月份
	      'd+': date.getDate(), // 日
	      'h+': date.getHours(), // 小时
	      'm+': date.getMinutes(), // 分
	      's+': date.getSeconds(), // 秒
	      'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
	      'S': date.getMilliseconds(), // 毫秒
	    }
	    var res = ''

	    if (fmt) {
	      res = fmt

	      if (/(y+)/.test(fmt)) {
	        res = res.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
	      }

	      for (let k in o) {
	        if (new RegExp('(' + k + ')').test(res)) {
	          res = res.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
	        }
	      }
	    }
	    return res
	  };

var sockjs = null
var sid = 0;
function initVm() {
	const vm = new Vue({
		el : '#myapp',

		/** 属性 */
		data : {
			connected : false,
			sendText : '',
			curUser:'未登录',
			
			/** 收到的消息列表 */
			msgList:[],
			
			oldTitle:document.title,
		},

		/** 计算属性 */
		computed : {
			connectStatus : function() {
				return (this.connected) ? '已连接' : '断线'
			},
			connectBtnText : function() {
				return (this.connected) ? '断开' : '连接'
			}
		},

	    /** 构建页面时 */
	    mounted () {
	      this.connect();
	    },
		
		/** 可用的方法 */
		methods : {
			onBtnClick : function() {
				if (this.connected) {
					// 如果连接了，就断开
					if (sockjs) {
						sockjs.close()
						sockjs = null
					}
				} else {
					this.connect();
				}
			},

			connect: function() {
				console.debug("连接 WebSocket")
				const that = this;
				sockjs = new SockJS("/wsApiSockJs");
				sockjs.onopen = function(event) {
					that.onopen(event)
				};
				sockjs.onmessage = function(event) {
					that.onmessage(event)
				};
				sockjs.onerror = function(event) {
					that.onerror(event)
				};
				sockjs.onclose = function(event) {
					that.onclose(event)
				}
			},
			
			onopen : function(event) {
				console.log("WebSocket:已连接", event);
				this.connected = true;
				this.addLog("WebSocket:已连接");
			},
			onmessage : function(event) {
				// var data = JSON.parse(event.data);
				console.log("WebSocket 收到一条消息:", event.data);
				
				if (event.data == 'ping') {
					return
				}
				
				var json = JSON.parse(event.data);
				console.log("对象", json);
				
				this.addJsonLog(json, event.data);
			},
			onerror : function(event) {
				console.log("WebSocket:发生错误 ", event);
				this.addLog("WebSocket:发生错误");
			},
			onclose : function(event) {
				console.log("WebSocket:已关闭", event);
				this.connected = false;
				this.curUser = '未登录';
				this.addLog("WebSocket:已关闭");
				document.title = this.oldTitle;
			},

			addJsonLog : function(json, txt) {
				
				var title ='';
				var clazz='text-success';
				if (json.msgType == 0) {
					title = '调用 ' + json.url + ' 成功'
					clazz = 'text-primary';
				} else if (json.msgType == -1){
					title = '调用 ' + json.url + ' 错误信息'
					clazz = 'text-danger'
				} else {
					title = '收到了服务器事件:' + json.eventName
					clazz = 'text-info'

					if (json.eventName=='SimpleUserLoginEvent') {
						this.curUser = '当前用户: ' + json.data.account;
						console.debug('用户 ' + this.curUser + " 登录")
						document.title = json.data.account;
					}	
				}
				
				var row = {
					date:dateFormat(new Date(),"hh:mm:ss"),
					clazz:clazz,
					title:title,
					data:txt
				}
				this.msgList.unshift(row);
			},

			addLog : function(msg) {
				var row = {
					date:dateFormat(new Date(),"hh:mm:ss"),
					title : '系统',
					clazz : 'text-muted',
					data:msg,
				}
				this.msgList.unshift(row);
			},
			
			clearLog : function() {
				console.debug("清空消息")
				this.msgList=[];
				this.addLog("清空");
			},
			
			onMethodFormSubmit : function(event) {
				var that = $(event.target);
				var url = that.attr("data-url")

				console.log('提交接口，url=', url);

				var form = {}
				var formData = new FormData(that[0]);
				for (var pair of formData.entries()) {
					const key=pair[0];
					const value = pair[1];
					form[key]=value
				}

				sid++;
				var cmd = {
					url : url,
					form : form,
					sid : ''+sid,
				}
				
				var sendText = JSON.stringify(cmd)
				if (this.connected) {
					console.log("发送文本:", sendText);
					sockjs.send(sendText);
				}
			},

			send : function() {
				console.log("发送文本:", this.sendText);
				if (this.connected) {
					sockjs.send(this.sendText)
				}
			},
		}
	})
}

