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

	// 深度
	public int depth() {
		if (root == null) {
			return -1;
		}
		return height() - 1;
	}

	// 高度
	public int height() {
		return height(root);
	}

	private int height(RBNode node) {
		if (node == null) {
			return 0;
		}
		int lh = height(node.left);
		int rh = height(node.right);
		return lh > rh ? lh + 1 : rh + 1;
	}

	public RBTree() {

	}

	public RBTree(int rootKey) {
		insertRoot(rootKey);
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
		if (root == null) {
			insertRoot(key);
		} else {
			RBNode node = new RBNode();
			node.key = key;
			node.color = RED;
			insert(node, root);
			size++;
			// traverseLeft();
		}
	}

	private void insertRoot(int rootKey) {
		root = new RBNode();
		root.key = rootKey;
		root.color = BLACK;
		size++;
	}

	private void insert(RBNode newNode, RBNode tmpNode) {
		if (newNode.key < tmpNode.key) {
			// 若当前节点t的left节点为NIL，则直接将新节点作为t的left节点
			if (tmpNode.left == null) {
				tmpNode.left = newNode;
				newNode.parent = tmpNode;
				insertFixup(newNode);
			} else {
				insert(newNode, tmpNode.left);
			}
		} else if (newNode.key >= tmpNode.key) {
			// 若当前节点t的right节点为NIL，则直接将新节点作为t的right节点
			if (tmpNode.right == null) {
				tmpNode.right = newNode;
				newNode.parent = tmpNode;
				insertFixup(newNode);
			} else {
				insert(newNode, tmpNode.right);
			}
		} else {
			throw new RuntimeException("二叉排序树节点值不能重复!");
		}
	}

	public void remove(int key) {
		RBNode t = root;
		while (true) {
			if (key == t.key) {
				remove(t);
				size--;
				return;
			} else if (key < t.key) {
				t = t.left;
			} else if (key > t.key) {
				t = t.right;
			}
			if (t == null) {
				return;
			}
		}
	}

	private void remove(RBNode delete) {
		if (delete.left == null) {
			if (delete.right == null) {
				// 叶子节点
				if (delete.color) {
					// case1：叶子为黑色，与case5相同情况处理
					removeFixup(delete, true);
				}
				// case2：根节点或为红色叶子，直接删除即可
				removeLeaf(delete);
			} else {
				// case3: 只有非空右子树,则delete一定是黑色，且右子树一定为红色
				delete.key = delete.right.key;
				delete.right = null;
			}
		} else {
			if (delete.right == null) {
				// case4：只有非空左子树,则delete一定是黑色，且左子树一定为红色
				delete.key = delete.left.key;
				delete.left = null;
			} else {
				// 双非空子树
				// 找到替换的前驱节点（真正删除的节点）
				RBNode substitute = searchLastRight(delete.left);
				// 将substitute内容覆盖delete内容
				delete.key = substitute.key;
				if (substitute.color) {
					// case5：substitute为黑色节点
					// 若delete的前驱节点就是其左子树：则证明其左子树没有右子树，直接将左子树的左子树替换为其本身
					remove(substitute);
				} else {
					// case6： substitute为红色节点：直接断开即可
					removeLeaf(substitute);
				}
			}
		}
	}

	/**
	 * 解决移除节点冲突（黑黑冲突）
	 * @author HuaQi.Yang
	 * @date 2017年8月1日
	 * @param substitute
	 * @param first
	 */
	private void removeFixup(RBNode substitute, boolean first) {
		RBNode parent = substitute.parent;
		if (parent == null) {
			// 根节点则不用继续调整
			return;
		}
		RBNode brother;
		boolean leftBrother;
		if (parent.left == substitute) {
			leftBrother = false;
			brother = parent.right;
		} else {
			leftBrother = true;
			brother = parent.left;
		}
		// substitute为黑色时brother一定存在
		RBNode lNephew = brother.left;
		RBNode rNephew = brother.right;
		if (brother.color) {
			blackBrother(substitute, first, parent, brother, leftBrother, lNephew, rNephew);
		} else {
			redBrotherCase(first, parent, brother, leftBrother, lNephew);
		}
	}

	/**
	 * 黑兄情况
	 * @author HuaQi.Yang
	 * @date 2017年8月1日
	 * @param substitute
	 * @param first
	 * @param parent
	 * @param brother
	 * @param leftBrother
	 * @param lNephew
	 * @param rNephew
	 */
	private void blackBrother(RBNode substitute, boolean first, RBNode parent, RBNode brother, boolean leftBrother, RBNode lNephew, RBNode rNephew) {
		if (lNephew == null || lNephew.color) {
			if (rNephew == null || rNephew.color) {
				blackNephewCase(substitute, first, parent, brother, leftBrother);
			} else {
				if (leftBrother) {
					// 黑左兄红右侄：LR
					rNephew.color = parent.color;
					parent.color = BLACK;
					rotateLeft(brother);
					rotateRight(parent);
				} else {
					// 黑右兄红右侄：RR
					brother.color = parent.color;
					rNephew.color = BLACK;
					parent.color = BLACK;
					rotateLeft(parent);
				}
			}
		} else {
			if (rNephew == null) {
				if (leftBrother) {
					// 黑左兄红左侄：LL
					brother.color = parent.color;
					lNephew.color = BLACK;
					parent.color = BLACK;
					rotateRight(parent);
				} else {
					// 黑右兄红左侄：RL
					lNephew.color = parent.color;
					parent.color = BLACK;
					rotateRight(brother);
					rotateLeft(parent);
				}
			} else {
				parent.color = BLACK;
				brother.color = BLACK;
				if (leftBrother) {
					// 黑左兄红双侄:LL
					lNephew.color = BLACK;
					rotateRight(parent);
				} else {
					// 黑右兄红双侄:RR
					rNephew.color = BLACK;
					rotateLeft(parent);
				}
				if (first)
					removeFixup(brother, false);
			}
		}
	}

	/**
	 * 黑兄二黑侄情况
	 * @author HuaQi.Yang
	 * @date 2017年8月1日
	 * @param substitute
	 * @param first
	 * @param parent
	 * @param brother
	 * @param leftBrother
	 */
	private void blackNephewCase(RBNode substitute, boolean first, RBNode parent, RBNode brother, boolean leftBrother) {
		if (parent.color) {
			// 黑兄二黑侄黑父:父结点染成新结点的颜色，新结点染成黑色，兄结点染成红色
			parent.color = substitute.color;
			brother.color = RED;
			substitute.color = BLACK;
			if (first)
				removeFixup(parent, false);
		} else {
			// 黑兄二黑侄红父:只需将父结点变为黑色，兄结点变为红色，新结点变为黑色
			parent.color = BLACK;
			brother.color = RED;
			disconnectNode(parent, leftBrother);
		}
	}

	/**
	 * 红兄情况
	 * @author HuaQi.Yang
	 * @date 2017年8月1日
	 * @param first
	 * @param parent
	 * @param brother
	 * @param leftBrother
	 * @param lNephew
	 */
	private void redBrotherCase(boolean first, RBNode parent, RBNode brother, boolean leftBrother, RBNode lNephew) {
		// brother为红色时：父节点一定是黑色
		if (parent.parent == null) {
			// 父节点是根节点:则设置左侄子为红色即可
			lNephew.color = RED;
		} else
			parent.color = RED;
		if (first) {
			if (leftBrother) {// LL
				rotateRight(parent);
			} else {// RR
				rotateLeft(parent);
			}
		}
		brother.color = BLACK;
	}

	/**
	 * 断开真正被删除的节点
	 * @author HuaQi.Yang
	 * @date 2017年8月1日
	 * @param parent
	 * @param leftBrother
	 */
	private void disconnectNode(RBNode parent, boolean leftBrother) {
		if (leftBrother) {
			parent.right = null;
		} else {
			parent.left = null;
		}
	}

	/**
	 * 移除叶子节点
	 * @author HuaQi.Yang
	 * @date 2017年8月1日
	 * @param leaf
	 */
	private void removeLeaf(RBNode leaf) {
		RBNode parent = leaf.parent;
		if (parent == null) {
			root = null;
			return;
		}
		if (parent.left == leaf) {
			parent.left = null;
		} else {
			parent.right = null;
		}
	}

	/**
	 * 搜索最左叶子节点(后继节点)
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
	 * 搜索最右叶子节点(前驱节点)
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
	}

	/**
	 * 解决插入冲突（红红冲突）
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
		// 当前node为根节点：将rLeft作为其新的右节点
		node.right = rLeft;
		if (rLeft != null)
			rLeft.parent = node;

		if (parent == null) {
			// node.color = RED;
			// 设置right为新根
			root = right;
		} else {
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
		// node为根节点：将lRight作为其新的左节点
		node.left = lRight;
		if (lRight != null)
			lRight.parent = node;
		if (parent == null) {
			// 设置left为新根
			// node.color = RED;
			root = left;
		} else {
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
