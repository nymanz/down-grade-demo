package com.sinbad.demo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sinbad on 2020/07/12.
 */
@Getter
@AllArgsConstructor
public enum RuleTypePathEnum {


	FLOW(1, "流控", "flow"),

	DEGRADE(2, "降级", "degrade");


	private int type;
	private String typeName;
	private String typePath;


	public static String getPathByType(int type) {
		for (RuleTypePathEnum value : values()) {
			if (value.getType() == type) {
				return value.typePath;
			}
		}

		return null;
	}
}
