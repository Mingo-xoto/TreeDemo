<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script src="${ctx}/js/jquery.min.js"></script>

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
	<br>
	<br>
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

	$("#tree").click(function() {
		$.ajax({
			url : "${ctx}/tree/get",
			success : function(data) {
				buildCanvas(data);
			}
		});
	});
	
	$("#copy").click(function() {
		$.ajax({
			url : "${ctx}/tree/copy",
			success : function(data) {
				resizeCanvas();
			}
		});
	});

	var canvas;
	var ctx;
	var i = 0;
	var radius = 20;
	var y_offset = 50;
	
	
	$("#default").click(function() {
		$.ajax({
			url : "${ctx}/tree/default",
			success : function(data) {
				buildCanvas(data);
			}
		});
	});
	
	// 深度
	function depth(root) {
		if (root == null) {
			return -1;
		}
		return height(root) - 1;
	}

	function height(node) {
		if (node == null) {
			return 0;
		}
		var lh = height(node.left);
		var rh = height(node.right);
		return lh > rh ? lh + 1 : rh + 1;
	}

	
	function resizeCanvas(){
		canvas = document.getElementById("tree-div");
		//重绘画布区域背景
		function resize(){
			canvas.width = window.outerWidth;
			canvas.height = window.outerHeight;
		}
		resize();
	}

	//绘制画布
	function buildCanvas(data){
		resizeCanvas();
		if(data.root == undefined){
			return;
		}
		var root = data.root;
		var x;
		var y = 30;
// 		var hierarchy = 2*Math.floor(Math.log2(data.size+1));//计算红黑树的最大深度
		var hierarchy = depth(root);//data.hierarchy;//深度
		console.info("树深度："+hierarchy);
		var lastHierarchyWidth = Math.pow(2,hierarchy)*2*(radius+5) ;//末层宽度
		
		if(lastHierarchyWidth >= canvas.width){
			canvas.width = lastHierarchyWidth;
		}
		x = canvas.width/2;
		if((nh = (hierarchy * (radius+y_offset) + 2*radius))>=canvas.height){
			canvas.height = nh;
		}
		
		ctx = canvas.getContext("2d");

		buildNode(root, x, y);
		buildChildNode(root, x, y,1,lastHierarchyWidth/Math.pow(2,2));
	}
	
	//绘制节点间连线
	function link(px, py, cx, cy) {
		ctx.moveTo(px, py);
		ctx.lineTo(cx, cy);
		ctx.stroke();
	}

	//绘制树节点圆圈
	function circle(key, x, y, color) {
		ctx.beginPath();
		ctx.arc(x, y, radius, 0, 2 * Math.PI);
		ctx.stroke();
		if (color) {
			ctx.fillStyle = "black";
		} else {
			ctx.fillStyle = "red";
		}
		ctx.fill();
	}

	//绘制树节点值
	function key(key, x, y) {
		ctx.fillStyle = "white";
		ctx.font = "normal 20px 微软雅黑";
		ctx.textBaseline = "middle";
		ctx.textAlign = "center";　
		ctx.fillText(key, x, y, y);
	}

	//绘制节点
	function buildNode(node, x, y) {
// 		console.info(x,y);
		circle(node.key, x, y, node.color);
		key(node.key, x, y);
	}

	//绘制子节点-->node:节点，x:x坐标，y：y坐标，h：层级，offset：x坐标偏移量
	function buildChildNode(node, x, y,h,x_offset) {
		var left = node.left;
		var right = node.right;
		
		//圆心连线长（(x,y)圆心与(x - x_offset,y+y_offset)或者(x + x_offset,y+y_offset)连线长度）
		var hypotenuse = Math.sqrt(x_offset*x_offset + y_offset*y_offset);
		//斜边长比
		var rate = (radius/hypotenuse);
		
		//(x,y)圆心与(x - x_offset,y+y_offset)或者(x + x_offset,y+y_offset)连线在(x,y)圆上的交点坐标
		var o_x = x_offset * rate;
		var o_y = y_offset * rate;
		
		if (left != null) {
			link(x-o_x,y+o_y,x - x_offset,y+y_offset);
			buildNode(left, x - x_offset, y + y_offset);
			buildChildNode(left, x - x_offset, y + y_offset,h+1,x_offset/2);
		}
		if (right != null) {
			link(x+o_x,y+o_y,x+x_offset,y+y_offset);
			buildNode(right, x + x_offset, y + y_offset);
			buildChildNode(right, x + x_offset, y + y_offset,h+1,x_offset/2);
		}
	}

	$("#remove").click(function() {
		$.ajax({
			url : "${ctx}/tree/remove",
			data:{
				"key":$("#d_key").val()
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