package com.vivian.baseproject.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

	public static String encodeCH(String url) {

		if (url != null) {
			Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
			Matcher matcher = pattern.matcher(url);
			if (matcher.find()) {
				int nameIndex = url.lastIndexOf("/") + 1;
				String name = url.substring(nameIndex);
				String urlPre = url.substring(0, nameIndex);
				try {
					return urlPre + URLEncoder.encode(name, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return url;
	}

	/**
	 * 判断是否为邮箱
	 * 
	 * @param tag
	 * @return
	 */
	public static boolean isEmail(String tag) {
		Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher m = p.matcher(tag);
		return m.matches();
	}
}
