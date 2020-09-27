$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");


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