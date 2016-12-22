package com.kfx.android.util.js;

import java.util.regex.Pattern;

public class JSValUtil {
	public static boolean valSealCode(String codeStr){
		Pattern p = Pattern.compile("^[1-9]\\d{12}$");
		if (p.matcher(codeStr).matches()) {
			return true;
		}else{
			return false;
		}
	}
	public static boolean valSealTelp(String telpStr){
		Pattern pTelp = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); 
		Pattern p1 = Pattern.compile("^[0][1-9]{2,3}[-]{0,1}[0-9]{5,10}$");
		Pattern p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");
		if(pTelp.matcher(telpStr).matches()||p1.matcher(telpStr).matches()||p2.matcher(telpStr).matches()){
			return true;
		}else{
			return false;
		}
	}

	
	public static void main(String[] args) {
		valSealCode("1234567891234");
	}
}
