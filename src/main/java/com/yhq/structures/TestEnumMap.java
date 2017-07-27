package com.yhq.structures;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author HuaQi.Yang
 * @date 2017年7月17日
 */
public class TestEnumMap {
	public static void main(String[] args) {
		EnumMap<ErrMsgEnum, String> errMsgMap = new EnumMap<ErrMsgEnum, String>(ErrMsgEnum.class);
		errMsgMap.put(ErrMsgEnum.required_item_id, "2,4,6");
		errMsgMap.put(ErrMsgEnum.invalid_app_id, "1,7");
		errMsgMap.put(ErrMsgEnum.invalid_date, "8");

		for (Map.Entry<ErrMsgEnum, String> entry : errMsgMap.entrySet()) {
			System.out.println(entry.getValue() + "<--->" + entry.getKey().getValue());
		}
	}
}
