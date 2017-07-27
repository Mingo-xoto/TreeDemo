package com.yhq.structures;

import java.io.Serializable;

/**
 * @author HuaQi.Yang
 * @date 2017年7月21日
 */
public class RBTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1985702815201438895L;
	private RBNode root;// 根节点
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	private int size = 0;// 节点数
	private int hierarchy = 0;// 树深

	public RBNode getRoot() {
		return root;
	}

	public void setRoot(RBNode root) {
		this.root = root;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int size() {
		return size;
	}

	public int getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(int hierarchy) {
		this.hierarchy = hierarchy;
	}

	public RBTree() {

	}

	public RBTree(int rootKey) {
		root = new RBNode();
		root.key = rootKey;
		root.color = BLACK;
		size++;
	}

	public class RBNode implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8322400180516540581L;
		boolean color;// 颜色
		RBNode left;// 左节点
		RBNode right;// 右节点
		RBNode parent;// 父节点
		int key;// 键值

		public boolean isColor() {
			return color;
		}

		public void setColor(boolean color) {
			this.color = color;
		}

		public RBNode getLeft() {
			return left;
		}

		public void setLeft(RBNode left) {
			this.left = left;
		}

		public RBNode getRight() {
			return right;
		}

		public void setRight(RBNode right) {
			this.right = right;
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

	}

	public void insert(int key) {
		RBNode node = new RBNode();
		node.key = key;
		node.color = RED;
		insert(node, root, 1);
		size++;
		// traverseLeft();
	}

	private void insert(RBNode newNode, RBNode tmpNode, int h) {
		if (newNode.key < tmpNode.key) {
			// 若当前节点t的left节点为NIL，则直接将新节点作为t的left节点
			if (tmpNode.left == null) {
				tmpNode.left = newNode;
				newNode.parent = tmpNode;
				hierarchy = h;
				insertFixup(newNode);
			} else {
				insert(newNode, tmpNode.left, ++h);
			}
		} else if (newNode.key >= tmpNode.key) {
			// 若当前节点t的right节点为NIL，则直接将新节点作为t的right节点
			if (tmpNode.right == null) {
				tmpNode.right = newNode;
				newNode.parent = tmpNode;
				hierarchy = h;
				insertFixup(newNode);
			} else {
				insert(newNode, tmpNode.right, ++h);
			}
		} else {
			throw new RuntimeException("二叉排序树节点值不能重复!");
		}
	}

	public void remove(int key) {
		remove(key, root);
	}

	public void remove(int key, RBNode t) {
		while (true) {
			if (key == t.key) {
				if (t.parent == null) {
					removeRoot();
				} else {
					remove(t, t.parent);
				}
				return;
			} else if (key < t.key) {
				t = t.left;
			} else {
				t = t.right;
			}
		}
	}

	private void removeRoot() {
		RBNode left = root.left;
		RBNode right = root.right;
		if (left == null && right == null) {
			// 叶子节点,直接删除
			root = null;
		} else if (left != null && right != null) {
			remove(root);
		} else {
			if (left == null) {
				// 只左节点为null，被删除替换为右节点
				root = right;
			} else {
				// 只右节点为null，被删除替换为左节点
				root = left;
			}
		}
	}

	private void remove(RBNode delete, RBNode parent) {
		RBNode left = delete.left;
		RBNode right = delete.right;
		if (left == null && right == null) {
			// 叶子节点,直接删除
			replaceWithNode(delete, parent, null);
		} else if (left != null && right != null) {
			remove(delete);
		} else {
			if (left == null) {
				// 只左节点为null，被删除替换为右节点
				replaceWithNode(delete, parent, right);
				right.parent = parent;
			} else {
				// 只右节点为null，被删除替换为左节点
				replaceWithNode(delete, parent, left);
				left.parent = parent;
			}
		}
	}

	/**
	 * 移除节点
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月26日
	 * @param left
	 */
	private void remove(RBNode delete) {
		if (delete.left.right == null) {
			RBNode left = delete.left;
			remove(left, delete);
			delete.key = left.key;
		} else {
			RBNode tr = searchLastRight(delete.left);
			RBNode rfParent = tr.parent;
			RBNode rfLeft = tr.left;
			// 将tr内容替换delete内容
			delete.key = tr.key;
			// 将rfLeft替换tr（删除tr）
			rfParent.right = rfLeft;
			if (rfLeft != null)
				rfLeft.parent = rfParent;
		}
		// 若删除的是黑色节点：需要调整结构
		if (delete.color) {

		}
	}

	private void replaceWithNode(RBNode delete, RBNode parent, RBNode node) {
		if (parent.left == delete) {
			parent.left = node;
		} else {
			parent.right = node;
		}
	}

	/**
	 * 搜索最左叶子节点
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月26日
	 * @param node
	 * @return
	 */
	private RBNode searchLastLeft(RBNode node) {
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	/**
	 * 搜索最右叶子节点
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月26日
	 * @param node
	 * @return
	 */
	private RBNode searchLastRight(RBNode node) {
		while (node.right != null) {
			node = node.right;
		}
		return node;
		// if (node.right == null) {
		// return node;
		// }
		// return searchLastRight(node.right);
	}

	/**
	 * 解决插入冲突
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月26日
	 * @param node
	 */
	public void insertFixup(RBNode node) {
		RBNode parent = node.parent;
		// 根节点
		if (parent == null) {
			node.color = BLACK;
			root = node;
			return;
		}
		// 若父节点为红色
		if (!parent.color) {
			RBNode grandParent = parent.parent;
			boolean left;
			RBNode uncle;
			if (parent.parent.left == parent) {
				left = true;
				uncle = parent.parent.right;
			} else {
				left = false;
				uncle = parent.parent.left;
			}
			// 叔叔节点为黑色
			if (uncle == null || uncle.color) {
				redBlackConflict(node, parent, grandParent, left);
			} else {
				redRedConflict(parent, grandParent, uncle);
			}
		}
	}

	/**
	 * 红红冲突
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月26日
	 * @param parent
	 * @param grandParent
	 * @param uncle
	 */
	private void redRedConflict(RBNode parent, RBNode grandParent, RBNode uncle) {
		// Case 1: 当前节点的父节点是红色，且当前节点的祖父节点的另一个子节点（叔叔节点）也是红色。
		// 将“父节点”和“叔叔节点”设为黑色。
		uncle.color = parent.color = BLACK;
		// 将“祖父节点”设为“红色”且设为“当前节点”(红色节点)；即，之后继续对“当前节点”进行操作。
		grandParent.color = RED;
		insertFixup(grandParent);
	}

	/**
	 * 红黑冲突（红色父节点，黑色叔叔节点）
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月26日
	 * @param node
	 * @param parent
	 * @param grandParent
	 * @param left
	 */
	private void redBlackConflict(RBNode node, RBNode parent, RBNode grandParent, boolean left) {
		hierarchy--;

		if (node == parent.left) {// 当前节点的父节点是红色，叔叔节点是黑色，且当前节点是其父节点的左孩子
			if (left) {// LL: 父节点为祖父节点左节点：以祖父节点为支点右旋
				parent.color = BLACK;
				grandParent.color = RED;
				rotateRight(grandParent);
			} else {// LR: 父节点为祖父节点右节点：先以父节点作为支点右旋，后祖父节点作为支点左旋
				node.color = BLACK;
				grandParent.color = RED;
				rotateRight(parent);
				rotateLeft(grandParent);
			}
		} else {// 当前节点的父节点是红色，叔叔节点是黑色，且当前节点是其父节点的右孩子
			if (left) {// RL: 父节点为祖父节点左节点：先以父节点左旋，后祖父节点右旋
				node.color = BLACK;
				grandParent.color = RED;
				rotateLeft(parent);
				rotateRight(grandParent);
			} else {// RR: 父节点为祖父节点右节点：以祖父节点为支点左旋
				System.out.println("RR");
				parent.color = BLACK;
				grandParent.color = RED;
				rotateLeft(grandParent);
			}
		}
	}

	/**
	 * 左旋转
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月26日
	 * @param node
	 */
	private void rotateLeft(RBNode node) {
		RBNode right = node.right;
		RBNode rLeft = right.left;
		RBNode parent = node.parent;
		right.parent = parent;
		right.left = node;
		node.parent = right;
		if (parent == null) {
			// 当前node为根节点：将rLeft作为其新的右节点，并设置right为新根
			node.color = RED;
			root = right;
			node.right = rLeft;
			if (rLeft != null)
				rLeft.parent = node;
		} else {
			// node为非根节点：其新右节点指向null，并且right替换node原位置
			node.right = null;
			if (parent.left == node) {
				parent.left = right;
			} else {
				parent.right = right;
			}
		}
	}

	/**
	 * 右旋转
	 * 
	 * @author HuaQi.Yang
	 * @date 2017年7月26日
	 * @param node
	 */
	private void rotateRight(RBNode node) {
		RBNode left = node.left;
		RBNode lRight = left.right;
		RBNode parent = node.parent;
		left.parent = parent;
		left.right = node;
		node.parent = left;
		if (parent == null) {
			// node为根节点：将lRight作为其新的左节点，并设置left为新根
			node.color = RED;
			root = left;
			node.left = lRight;
			if (lRight != null)
				lRight.parent = node;
		} else {
			// node为非根节点：其新左节点指向null，并且right替换node原位置
			node.left = null;
			if (parent.left == node) {
				parent.left = left;
			} else {
				parent.right = left;
			}
		}
	}

	public void traverseMid() {
		System.out.println("\n中序遍历");
		traverseMid(root);
	}

	private void traverseMid(RBNode node) {
		if (node.left != null) {
			traverseMid(node.left);
		}
		show(node);
		if (node.right != null) {
			traverseMid(node.right);
		}
	}

	private void show(RBNode node) {
		System.out.println(node.key + "[" + (node.color ? "黑" : "红") + ",左->" + (node.left == null ? "NIL" : node.left.key) + ",父->"
				+ (node.parent == null ? "NIL" : node.parent.key) + ",右->" + (node.right == null ? "NIL" : node.right.key) + "]");
	}

	public void traverseLeft() {
		System.out.println("前序遍历");
		traverseLeft(root);
		System.out.println();
	}

	private void traverseLeft(RBNode node) {
		show(node);
		if (node.left != null) {
			traverseLeft(node.left);
		}
		if (node.right != null) {
			traverseLeft(node.right);
		}
	}

	public void traverseRight() {
		System.out.println("\n后序遍历");
		traverseRight(root);
	}

	private void traverseRight(RBNode node) {
		if (node.left != null) {
			traverseRight(node.left);
		}
		if (node.right != null) {
			traverseRight(node.right);
		}
		show(node);
	}

}
