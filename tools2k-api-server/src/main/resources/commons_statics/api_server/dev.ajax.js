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

	$(document).ready(function() {
		$(".js_form").submit(function() {
			try {
				var that = $(this);
				var url = $(this).attr("data-url")
				console.log("post to " + url);

				var formData = new FormData(that[0]);

				$.ajax({
					type : "post", //提交方式  
					dataType : "json", //如果是要上传文件,只能用html
					data : formData,
					url : url, //请求url
					contentType : false, // 当有文件要上传时，此项是必须的，否则后台无法识别文件流的起始位置(详见：#1)
					processData : false, // 是否序列化data属性，默认true(注意：false时type必须是post，详见：#2)
					success : function(data) { //提交成功的回调函数
						//var res = eval("(" + data + ")");
						var res = data;
						if (!res.success) {
							console.log("错误信息:" + res.message);
						}
						$("#testResult").text("url:" + url + "\n返回结果:\n" + JSON.stringify(data, null, 4));
					},
					error : function(xhr, status, error) {
						$("#testResult").text("错误码:" + xhr.status + "\n" + xhr.responseText);
					},
				});
			} catch (ex) {
				console.log("ajax 错误", ex)
			}
			return false;
		});

		$(".js_dropify").dropify({
			messages : {
				'default' : '点击或拖拽文件到这里',
				'replace' : '点击或拖拽文件到这里来替换文件',
				'remove' : '移除文件',
				'error' : '对不起，你上传的文件太大了'
			}
		});

		$("#js_clear").click(function() {
			$("#testResult").text('')
		})

		$('[data-toggle="tooltip"]').tooltip()

		$(".js_add_input").click(function() {
			var div = $(this).parent().clone();

			var icon = div.find(".glyphicon-plus")
			icon.removeClass("glyphicon-plus");
			icon.addClass("glyphicon-minus");

			div.find("a").click(function() {
				$(this).parent().remove();
			})

			var parent = $(this).parent().parent();
			console.debug('点击增加')
			parent.append(div);
		})
	});
