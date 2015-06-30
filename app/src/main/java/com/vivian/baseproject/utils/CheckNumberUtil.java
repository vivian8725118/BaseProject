package com.vivian.baseproject.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckNumberUtil {
	/**
	 * 检测是否为手机号
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumberValid(String phoneNumber)

	{

		boolean isValid = false;

		/*
		 * 可接受的电话格式有:
		 * ^//(? : 可以使用 "(" 作为开头
		 * (//d{3}): 紧接着三个数字
		 * //)? : 可以使用")"接续
		 * [- ]? : 在上述格式后可以使用具选择性的 "-".
		 * (//d{4}) : 再紧接着三个数字
		 * [- ]? : 可以使用具选择性的 "-" 接续.
		 * (//d{4})$: 以四个数字结束.
		 * 可以比较下列数字格式:
		 * (123)456-78900, 123-4560-7890, 12345678900, (123)-4560-7890
		 */

		String expression = "^//(?(//d{3})//)?[- ]?(//d{3})[- ]?(//d{5})$";

		String expression2 = "^//(?(//d{3})//)?[- ]?(//d{4})[- ]?(//d{4})$";

		CharSequence inputStr = phoneNumber;

		/* 创建Pattern */

		Pattern pattern = Pattern.compile(expression);

		/* 将Pattern 以参数传入Matcher作Regular expression */

		Matcher matcher = pattern.matcher(inputStr);

		/* 创建Pattern2 */

		Pattern pattern2 = Pattern.compile(expression2);

		/* 将Pattern2 以参数传入Matcher2作Regular expression */

		Matcher matcher2 = pattern2.matcher(inputStr);

		if (matcher.matches() || matcher2.matches())

		{

			isValid = true;

		}

		return isValid;

	}

	/**
	 * 检测是否为邮箱
	 * 
	 * @param strEmail
	 * @return
	 */
	public static boolean isEmail(String strEmail)

	{

		String str = "^(([0-9a-zA-Z]+)|([0-9a-zA-Z]+[_.0-9a-zA-Z-]*[+]*[0-9a-zA-Z-]+))@([a-zA-Z0-9-]+[.])+([a-zA-Z]|net|NET|asia|ASIA|com|COM|gov|GOV|mil|MIL|org|ORG|edu|EDU|int|INT|cn|CN|cc|CC)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(strEmail);
		return m.matches();

	}

	public static Boolean checkPhoneNub(String phoneNum) {
		String check = null;

		// 中国区的
		check = "^1[345678]\\d{9}$";

		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(phoneNum);
		boolean isMatched = matcher.matches();
		return isMatched;
	}

}
