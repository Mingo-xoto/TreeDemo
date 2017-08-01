<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/tree.js"></script>

<style type="text/css">
.r_node {
	line-height: 40px;
	text-align: center;
	background-image: url(${ctx}/images/red.png);
	background-repeat: no-repeat;
	background-position: center;
	width: 40px;
	height: 40px;
}

.b_node {
	line-height: 40px;
	text-align: center;
	background-image: url(${ctx}/images/black.png);
	background-repeat: no-repeat;
	background-position: center;
	width: 40px;
	height: 40px;
	background-image: url(${ctx}/images/black.png);
}
</style>
</head>
<body>
	<input type="text" value="" id="i_key">
	<input type="button" value="插入" id="insert">
	<br>
	<br>
	<input type="text" value="" id="d_key">
	<input type="button" value="移除节点" id="remove">
	<input type="button" value="删除树" id="delete">
	<br>
	<br>
	<input type="button" value="取出树" id="tree">
	<input type="button" value="前驱" id="forward">
	<input type="button" value="回退" id="backward">
	<br>
	<br>
	<input type="text" value="11" id="nodeCount">
	<input type="button" value="默认树" id="default">
	<input type="button" value="复制默认树" id="copy">
	<br>
	<br>
	<canvas id="tree-div" width="1000" height="800"></canvas>
</body>
<script>
	$("#insert").click(function() {
		$.ajax({
			url : "${ctx}/tree/insert",
			data : {
				key : $("#i_key").val()
			},
			success : function(data) {
				buildCanvas(data);
			}
		});
		$("#i_key").val("");
	});

	function getTree() {
		$.ajax({
			url : "${ctx}/tree/get",
			success : function(data) {
				buildCanvas(data);
			}
		});
	}
	$("#tree").click(function() {
		getTree();
	});

	$("#backward").click(function() {
		$.ajax({
			url : "${ctx}/tree/backward",
			success : function(data) {
				buildCanvas(data);
			}
		});
	});

	$("#forward").click(function() {
		$.ajax({
			url : "${ctx}/tree/forward",
			success : function(data) {
				buildCanvas(data);
			}
		});
	});

	$("#copy").click(function() {
		$.ajax({
			url : "${ctx}/tree/copy",
			data : {
				"nodeCount" : $("#nodeCount").val()
			},
			success : function(data) {
				getTree();
			}
		});
	});

	$("#default").click(function() {
		$.ajax({
			url : "${ctx}/tree/default",
			data : {
				"nodeCount" : $("#nodeCount").val()
			},
			success : function(data) {
				buildCanvas(data);
			}
		});
	});

	$("#remove").click(function() {
		$.ajax({
			url : "${ctx}/tree/remove",
			data : {
				"key" : $("#d_key").val()
			},
			success : function(data) {
				buildCanvas(data);
			}
		});
		$("#d_key").val("");
	});

	$("#delete").click(function() {
		$.ajax({
			url : "${ctx}/tree/delete",
			success : function(data) {
				buildCanvas(data);
			}
		});
	});
</script>
</html>