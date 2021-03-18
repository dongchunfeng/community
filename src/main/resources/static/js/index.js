$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

	//在ajax请求之前将csrf令牌设置到请求的消息头中
	// var token = $("meta[name='_csrf']").attr("content");
	// var header = $("meta[name='_csrf_header']").attr("content");
	// $(document).ajaxSend(function(e,xhr,options){
	// 	xhr.setRequestHeader(header,token);
	// });

	let title = $("#recipient-name").val();
	let content = $("#message-text").val();
	//发送ajax请求
	$.post(
		CONTEXT_PATH+"/discuss/add",
		{"title":title,"content":content},
		function(data){
			data = $.parseJSON(data);
			$("#hintBody").text(data.msg);
			//显示提示框
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				title = $("#recipient-name").val('');
				content = $("#message-text").val('');
				if(data.code==0){
					window.location.reload();
				}
			}, 2000);
		}
	);

}