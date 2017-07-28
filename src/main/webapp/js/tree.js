	var canvas;
	var ctx;
	var i = 0;
	var radius = 20;
	var y_offset = 50;
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
		// 重绘画布区域背景
		function resize(){
			canvas.width = window.outerWidth;
			canvas.height = window.outerHeight;
		}
		resize();
	}

	// 绘制画布
	function buildCanvas(data){
		resizeCanvas();
		if(data.root == undefined){
			return;
		}
		var root = data.root;
		var x = getParam(root.left,0).lastHierarchyWidth/2;// 设置根节点的x坐标距离x=0的偏移量为其左子树的最大宽度
		var y = 30;
// var hierarchy = 2*Math.floor(Math.log2(data.size+1));//计算红黑树的最大深度
		var hierarchy = depth(root);
		var lastHierarchyWidth = Math.pow(2,hierarchy)*(radius+5)+x ;// 末层宽度
		if(lastHierarchyWidth >= canvas.width){
			canvas.width = lastHierarchyWidth;
		}
		if((nh = (hierarchy * (radius+y_offset) + 2*radius))>=canvas.height){
			canvas.height = nh;
		}
		
		ctx = canvas.getContext("2d");
		console.info(canvas);
		console.info("("+x+","+y+")");

		drawNode(root, x, y);
		drawChildNode(root, x, y);
	}

	// 绘制节点间连线---> p_x,p_y:父节点x,y坐标,c_x,c_y:子节点x,y坐标
	function link(p_x, p_y, c_x, c_y) {
		ctx.moveTo(p_x, p_y);
		ctx.lineTo(c_x, c_y);
		ctx.stroke();
	}
	
	// 绘制树节点圆圈
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

	// 绘制树节点值
	function key(key, x, y) {
		ctx.fillStyle = "white";
		ctx.font = "normal 20px 微软雅黑";
		ctx.textBaseline = "middle";
		ctx.textAlign = "center";　
		ctx.fillText(key, x, y, y);
	}

	// 绘制节点
	function drawNode(node, x, y) {
		circle(node.key, x, y, node.color);
		key(node.key, x, y);
	}

	
	
	function getParam(node,brotherWidth){
		var hierarchy = depth(node)+1;// 当前节点的子树深度
		var lastHierarchyWidth = brotherWidth==0?Math.pow(2,hierarchy)*(radius+5)*2:Math.pow(2,hierarchy)*(radius+5)*2+brotherWidth;// 当前子树末层宽度
		var x_offset = lastHierarchyWidth/4;
		if(brotherWidth > 0){
			console.info(brotherWidth);
		}
		// 圆心连线长（(x,y)圆心与(x - x_offset,y+y_offset)或者(x +
		// x_offset,y+y_offset)连线长度）
		var hypotenuse = Math.sqrt(x_offset*x_offset + y_offset*y_offset);
		// 斜边长比
		var rate = (radius/hypotenuse);
		
		// (x,y)圆心与(x - x_offset,y+y_offset)或者(x +
		// x_offset,y+y_offset)连线在(x,y)圆上的交点坐标
		var o_x = x_offset * rate;
		var o_y = y_offset * rate;
		return new param(node==null?"Nil":node.key,lastHierarchyWidth,x_offset,o_x,o_y);
	}
	
	function param(key,lastHierarchyWidth,x_offset,o_x,o_y){
		this.key=key;
		this.lastHierarchyWidth = lastHierarchyWidth;
		this.x_offset = x_offset;
		this.o_x = o_x;
		this.o_y = o_y;
	}
	
	// 绘制子节点-->node:节点，x:x坐标，y：y坐标，x_offset：x坐标偏移量
	function drawChildNode(node, x, y) {
		var left = node.left;
		var right = node.right;
		
		var p_param = getParam(node,0);
		var l_param;
		var r_param;
		var lastHierarchyWidth = 0;
		if (left != null) {
			l_param = getParam(left,0);
			lastHierarchyWidth = l_param.lastHierarchyWidth;
			link(x-l_param.o_x,y+l_param.o_y,x - l_param.x_offset,y+y_offset);
			drawNode(left, x - l_param.x_offset, y + y_offset);
			drawChildNode(left, x - l_param.x_offset, y + y_offset);
		}
		if (right != null) {
			r_param = getParam(right.left,lastHierarchyWidth);
			link(x+r_param.o_x,y+r_param.o_y,x+r_param.x_offset,y+y_offset);
			drawNode(right, x + r_param.x_offset, y + y_offset);
			drawChildNode(right, x + r_param.x_offset, y + y_offset);
		}
	}
