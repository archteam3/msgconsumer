package team.three.msgconsumer.props;

import org.apache.commons.lang.StringUtils;

public class IdMaker {
	public static String makeEqpId(int index) {
		return "EQP-" + StringUtils.leftPad(Integer.toString(index), 4, '0');
	}
}
