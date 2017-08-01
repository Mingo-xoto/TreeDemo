package com.yhq.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yhq.structures.RBTree;
import com.yhq.structures.RBTreeTest;

/**
 * @author HuaQi.Yang
 * @date 2017年7月25日
 */
@Controller
@RequestMapping("/tree/")
public class TreeController {

	private int pos = 0;

	private RBTree tree;

	private List<RBTree> trees = new ArrayList<>();

	@ResponseBody
	@RequestMapping("insert")
	public RBTree insert(int key) {
		if (tree == null) {
			tree = new RBTree(key);
		} else {
			trees.add(tree.cloneTree());
			pos++;
			tree.insert(key);
		}
		return tree;
	}

	@RequestMapping("index")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("tree");
		modelAndView.addObject("tree", tree);
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping("get")
	public RBTree tree() {
		return tree;
	}

	@ResponseBody
	@RequestMapping("default")
	public RBTree defaultTree(@RequestParam int nodeCount) {
		return RBTreeTest.createDefaultTree(nodeCount);
	}

	@ResponseBody
	@RequestMapping("remove")
	public RBTree remove(@RequestParam int key) {
		if (tree != null) {
			trees.add(tree.cloneTree());
			pos++;

			tree.remove(key);
		}
		return tree;
	}

	@ResponseBody
	@RequestMapping("delete")
	public RBTree delete() {
		tree = null;
		return tree;
	}

	@ResponseBody
	@RequestMapping("copy")
	public void copy(@RequestParam int nodeCount) {
		tree = RBTreeTest.createDefaultTree(nodeCount);
	}

	@ResponseBody
	@RequestMapping("backward")
	public RBTree backward() {
		if (!trees.contains(tree))
			trees.add(tree.cloneTree());
		if (pos > 0 && pos <= trees.size())
			tree = trees.get(--pos);
		return tree;
	}

	@ResponseBody
	@RequestMapping("forward")
	public RBTree forward() {
		if (!trees.contains(tree))
			trees.add(tree.cloneTree());
		if (pos >= 0 && pos < trees.size() - 1)
			tree = trees.get(++pos);
		return tree;
	}

}
