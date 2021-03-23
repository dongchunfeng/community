$(function(){

    $("#setTopBtn").click(setTop);
    $("#wonderfulBtn").click(wonderful);
    $("#delBtn").click(del);

});

function setTop() {
    $.post(
        CONTEXT_PATH +"/discuss/setTop",
        {"id":$('#postId').val()},
        function (data) {
            data = $.parseJSON(data);
            if(data.code==0){
                $('#setTopBtn').attr("disabled","disabled");
            }else{
                alert(data.msg);
            }
        }
    );
}

function wonderful() {
    $.post(
        CONTEXT_PATH +"/discuss/wonderful",
        {"id":$('#postId').val()},
        function (data) {
            data = $.parseJSON(data);
            if(data.code==0){
                $('#wonderfulBtn').attr("disabled","disabled");
            }else{
                alert(data.msg);
            }
        }
    );
}

function del() {
    $.post(
        CONTEXT_PATH +"/discuss/delete",
        {"id":$('#postId').val()},
        function (data) {
            data = $.parseJSON(data);
            if(data.code==0){
                location.href= CONTEXT_PATH + "/index";
            }else{
                alert(data.msg);
            }
        }
    );
}


function like(btn,entityType,entityId,entityUserId,postId){
    $.post(
        CONTEXT_PATH +"/like",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"postId":postId},
        function(data){
            data = $.parseJSON(data);
            if(data.code == 0){
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
            }else{
                console.log(data.msg);
            }
        }
    );
}