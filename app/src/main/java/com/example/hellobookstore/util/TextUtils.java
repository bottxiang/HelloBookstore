package com.example.hellobookstore.util;

public class TextUtils {

	public static boolean isEmpty(String s){
		if(s.equals(null) || s.equals("")) {
			return true;
		}else {
			return false;
		}
	}

	public static boolean isNotEmpty(String s){
		return !isEmpty(s);
	}
}