package com.yhq.structures;

import java.util.HashMap;
import java.util.Random;

/**
 * @author HuaQi.Yang
 * @date 2017年7月21日
 */
public class RBTreeTest {

	public static void main(String[] args) {
		// System.out.println(true ^ false);
		createDefaultTree(32);
	}

	public static RBTree createDefaultTree(int nodeCount) {
		RBTree tree = new RBTree();
		HashMap<Integer, Boolean> map = new HashMap<>();
		// map.put(8, true);
		// for (int i = 1; i < nodeCount; ++i) {
		// tree.insert(i);
		// }
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
		for (int i = 0; i < nodeCount; ++i) {
			int value = new Random().nextInt(300);
			if (map.containsKey(value)) {
				continue;
			}
			map.put(value, true);
			System.out.println(value);
			tree.insert(value);
		}
		// tree.traverseRight();
		// tree.traverseMid();
		// tree.remove(9);
		// tree.remove(100);
		return tree;
	}
}
