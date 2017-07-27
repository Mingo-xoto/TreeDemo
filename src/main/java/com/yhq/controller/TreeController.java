package com.yhq.controller;

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

	private RBTree tree;

	@ResponseBody
	@RequestMapping("insert")
	public RBTree insert(int key) {
		if (tree == null) {
			tree = new RBTree(key);
		} else {
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
	public RBTree defaultTree() {
		return RBTreeTest.createDefaultTree();
	}

	@ResponseBody
	@RequestMapping("remove")
	public RBTree remove(@RequestParam int key) {
		tree.remove(key);
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
	public void copy() {
		tree = RBTreeTest.createDefaultTree();
	}

}
