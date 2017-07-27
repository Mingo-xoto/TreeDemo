package com.yhq.structures;

/**
 * @author HuaQi.Yang
 * @date 2017年7月17日
 */
public enum ErrMsgEnum {
	required_item_id("商品id为必填项"), invalid_app_id("应用标识错误"), invalid_date("时间格式错误");

	private String value;

	private ErrMsgEnum(String value) {
		this.setValue(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
