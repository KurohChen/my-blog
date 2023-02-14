$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	//获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	//发送异步请求
	$.post(
		//访问路径
		CONTEXT_PATH + "/discuss/add",
		{"title":title,"content":content},
		//返回结果处理
		function (data) {
			data=$.parseJSON(data);
			//在提示框中显示返回消息
			$("#hintBody").text(data.msg);
			//显示提示框
			$("#hintModal").modal("show");
			//两秒后不显示
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//刷新页面
				if(data.code == 0){
					window.location.reload();
				}
			}, 2000);

		}
	);


}