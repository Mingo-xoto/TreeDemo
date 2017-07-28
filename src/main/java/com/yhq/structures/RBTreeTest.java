package com.yhq.structures;

/**
 * @author HuaQi.Yang
 * @date 2017年7月21日
 */
public class RBTreeTest {

	public static void main(String[] args) {
		// System.out.println(true ^ false);
		createDefaultTree();
	}

	public static RBTree createDefaultTree() {
		RBTree tree = new RBTree(0);
		// HashMap<Integer, Boolean> map = new HashMap<>();
		// map.put(8, true);
		for (int i = 1; i < 11; ++i) {
			tree.insert(i);
		}
		System.out.println(tree.depth());
		System.out.println(tree.height());
		// tree.insert(6);
		// tree.insert(7);
		// tree.insert(11);
		// tree.insert(9);
		// tree.insert(1);
		// tree.insert(31);
		// tree.insert(131);
		// tree.insert(41);
		// tree.insert(2);
		// tree.insert(17);
		// tree.insert(100);
		// tree.insert(101);
		// tree.insert(104);
		// for (int i = 0; i < 20; ++i) {
		// int value = new Random().nextInt(300);
		// if (map.containsKey(value)) {
		// continue;
		// }
		// map.put(value, true);
		// tree.insert(new RBTree(value));
		// }
		// tree.traverseRight();
		// tree.traverseMid();
		// tree.remove(9);
		// tree.remove(100);
		return tree;
	}
}
