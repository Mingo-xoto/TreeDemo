package com.yhq.structures;

/**
 * @author HuaQi.Yang
 * @date 2017年7月21日
 */
public class RBTree2<K> {

	private RBNode root;// 根节点
	private static final boolean RED = false;
	private static final boolean BLACK = true;
	private int size = 0;

	public int size() {
		return size;
	}

	public RBTree2(int key) {
		root = new RBNode();
		root.key = key;
		root.color = BLACK;
	}

	public class RBNode {
		boolean color;// 颜色
		RBNode left;// 左节点
		RBNode right;// 右节点
		RBNode parent;// 父节点
		int key;// 键值
	}

	public void insert(int key) {
		RBNode node = new RBNode();
		node.key = key;
		node.color = RED;
		insert(node, root);
	}

	private void insert(RBNode newNode, RBNode tmpNode) {
		if (newNode.key < tmpNode.key) {
			// 若当前节点t的left节点为NIL，则直接将新节点作为t的left节点
			if (tmpNode.left == null) {
				tmpNode.left = newNode;
				newNode.parent = tmpNode;
			} else {
				insert(newNode, tmpNode.left);
			}
		} else if (newNode.key > tmpNode.key) {
			if (tmpNode.right == null) {
				tmpNode.right = newNode;
				newNode.parent = tmpNode;
			} else {
				insert(newNode, tmpNode.right);
			}
		} else {
			throw new RuntimeException("二叉排序树节点值不能重复!");
		}
		fixup(newNode);
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
			// 被删除替换为左节点,搜索出左节点的最右子树,并将右节点作为左节点最右子树的右节点
			root = left;
			root.parent = null;
			RBNode tr = searchLastRight(left);
			tr.right = right;
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
			// 被删除替换为左节点,搜索出左节点的最右子树,并将右节点作为左节点最右子树的右节点
			replaceWithNode(delete, parent, left);
			left.parent = parent;
			RBNode tr = searchLastRight(left);
			tr.right = right;
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

	private void replaceWithNode(RBNode delete, RBNode parent, RBNode node) {
		if (parent.left == delete) {
			parent.left = node;
		} else {
			parent.right = node;
		}
	}

	private RBNode searchLastRight(RBNode node) {
		if (node.right == null) {
			return node;
		}
		return searchLastRight(node.right);
	}

	public void fixup(RBNode node) {
		RBNode parent = node.parent;
		// 根节点
		if (parent == null) {
			node.color = BLACK;
			return;
		}
		boolean left;
		// 若父节点为红色
		if (!parent.color) {
			System.out.println("--调整前");
			traverseMid();
			RBNode uncle;
			if (parent.parent.left == parent) {
				// node节点的父节点为node祖父节点的左节点
				uncle = parent.parent.right;
				left = false;
			} else {
				// node节点的父节点为node祖父节点的右节点
				uncle = parent.parent.left;
				left = true;
			}
			// 叔叔节点为黑色
			if (uncle == null || uncle.color) {
				if (left) {
					// Case 3 当前节点的父节点是红色，叔叔节点是黑色，且当前节点是其父节点的左孩子
					// (01) 将“父节点”设为“黑色”。
					// (02) 将“祖父节点”设为“红色”。
					// (03) 以“祖父节点”为支点进行右旋。
					parent.color = BLACK;
					parent.parent.color = RED;
					rotateRight(parent);
				} else {
					// Case 2 当前节点的父节点是红色，叔叔节点是黑色，且当前节点是其父节点的右孩子
					// (01) 将“父节点”作为“新的当前节点”。
					// (02) 以“新的当前节点”为支点进行左旋。
					node.color = BLACK;
					parent.parent.color = RED;
					rotateLeft(parent);
				}
			} else if (!uncle.color) {
				// Case 1: 当前节点的父节点是红色，且当前节点的祖父节点的另一个子节点（叔叔节点）也是红色。
				// 将“父节点”和“叔叔节点”设为黑色。
				uncle.color = parent.color = BLACK;
				// 将“祖父节点”设为“红色”且设为“当前节点”(红色节点)；即，之后继续对“当前节点”进行操作。
				if (parent.parent != null) {
					parent.parent.color = RED;
					node = parent.parent;
				}
			}
			System.out.println("--调整后");
			traverseMid();
		}
	}

	private void rotateLeft(RBNode node) {
		if (node != null) {
			// 取得tree的右节点:r
			RBNode r = node.right;
			RBNode parent = node.parent;
			// 原节点为其右节点的左节点
			r.left = node;
			node.parent = r;
			node.right = null;
			// 原节点的父节点为其右节点的右节点
			r.parent = parent.parent;
			r.right = parent;
			parent.parent = r;
			parent.left = null;
			if (r.parent == null) {
				root = r;
			}
		}
	}

	private void rotateRight(RBNode node) {
		if (node != null) {
			// 取得tree的左节点:r
			RBNode l = node.left;
			RBNode parent = node.parent;
			// 原节点为其左节点的右节点
			l.right = node;
			node.parent = l;
			node.left = null;
			// 原节点的父节点为其左节点的左节点
			l.parent = parent.parent;
			l.left = parent;
			parent.parent = l;
			parent.right = null;
			if (l.parent == null) {
				root = l;
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
		System.out.println("\n前序遍历");
		traverseLeft(root);
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
