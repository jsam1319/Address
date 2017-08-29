package address.model;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class AddressTokenizer {
	/* Áö¹ø ÁÖ¼Ò */
	String siDo = "";
	String siGoon = "";
	String goo = "";
	String eupMyunDong = "";
	String ri = "";
	String jibunMain = "";
	String jibunSub = "";
	
	/* µµ·Î¸í ÁÖ¼Ò */
	String doro = "";
	String buildMain = "";
	String buildSub = "";
	
	String dong = "";
	String ho = "";
	
	String etc = "";
	
	public Address createVO(String address) {
		
		/* ÁÖ¼Ò¸¦ °ø¹éÀ¸·Î ³ª´©¾î ´ÜÀ§·Î ±¸º°ÇÑ´Ù. */
		StringTokenizer token = new StringTokenizer(address, " ");
		
		String unit = token.nextToken();
			
		/* µµ ±¸ºÐ */
		findSiDo(unit);
		if(!siDo.equals("")) unit = token.nextToken();
			
		/* ½Ã ±º ±¸ºÐ */
		findSiGoon(unit);
		if(!siGoon.equals("")) unit = token.nextToken();
		
		/* ±¸ ±¸ºÐ */
		findGoo(unit);
		if(!goo.equals("")) unit = token.nextToken();
		
		/* À¾ ¸é µ¿ ±¸ºÐ */
		findEupMyunDong(unit);
		if(!eupMyunDong.equals("")) unit = token.nextToken();
		
		/* ¸® ±¸ºÐ */
		findRi(unit);
		if(!ri.equals("")) unit = token.nextToken();
		
		/* Áö¹ø ±¸ºÐ*/
		findJibun(unit);
		if(!jibunMain.equals("")) unit = token.nextToken();
		
		
		/* µµ·Î¸í ±¸ºÐ */
		findDoro(unit);
		if(!doro.equals("")) unit = token.nextToken();
		
		/* °Ç¹° ±¸ºÐ */
		findBuild(unit);
		if(!buildMain.equals("")) unit = token.nextToken();
		
		/* ¾ÆÆÄÆ® µ¿ ±¸ºÐ */
		findDong(unit);
		if(!dong.equals("")) unit = token.nextToken();
			
		/* ¾ÆÆÄÆ® È£ ±¸ºÐ */
		findHo(unit);
		if(!ho.equals("")) unit = token.nextToken();
			
		while(token.hasMoreTokens()) {
			findEtc(unit);
			unit = token.nextToken();
		}
	
		

		return new Address.Builder()
				.siDo(siDo)
				.siGoonGoo(siGoon + " " + goo.trim())
				.eupMyunDong(eupMyunDong).ri(ri)
				.jibunMain(jibunMain)
				.jibunSub(jibunSub)
				.doro(doro)
				.buildMain(buildMain)
				.buildSub(buildSub)
				.dong(dong)
				.ho(ho)
				.etc(etc.trim())
				.build();
	}

	
	private void findSiDo(String unit) {
		/*
		 * ÀÔ·Â ½Ã "¼­¿ï½Ã"¿Í "¼­¿ïÆ¯º°½Ã" µîÀ» ±¸º° ÇÏ±â À§ÇÏ¿©
		 * ÁÙÀÓ¸»À» Å°·Î Á¤½Ä ÀÌ¸§À» °ªÀ¸·Î ³Ö¾î¼­ °ü¸®ÇÑ´Ù.
		 */
		HashMap<String, String> doMap = new HashMap<String, String>();
		
		doMap.put("¼­¿ï", "¼­¿ïÆ¯º°½Ã");		doMap.put("¼­¿ï½Ã", "¼­¿ïÆ¯º°½Ã");	
		doMap.put("°­¿ø", "°­¿øµµ");
		doMap.put("°æ±â", "°æ±âµµ");
		doMap.put("°æ³²", "°æ»ó³²µµ");
		doMap.put("°æºÏ", "°æ»óºÏµµ");
		doMap.put("±¤ÁÖ", "±¤ÁÖ±¤¿ª½Ã");
		doMap.put("´ë±¸", "´ë±¸±¤¿ª½Ã");
		doMap.put("´ëÀü", "´ëÀü±¤¿ª½Ã;");		doMap.put("´ëÀü½Ã", "´ëÀü±¤¿ª½Ã;");
		doMap.put("ºÎ»ê", "ºÎ»ê±¤¿ª½Ã");		doMap.put("ºÎ»ê½Ã", "ºÎ»ê±¤¿ª½Ã");
		doMap.put("¼¼Á¾", "¼¼Á¾Æ¯º°ÀÚÄ¡½Ã");	doMap.put("¼¼Á¾½Ã", "¼¼Á¾Æ¯º°ÀÚÄ¡½Ã");	doMap.put("¼¼Á¾Æ¯º°½Ã", "¼¼Á¾Æ¯º°ÀÚÄ¡½Ã");
		doMap.put("¿ï»ê", "¿ï»ê±¤¿ª½Ã");		doMap.put("¿ï»ê½Ã", "¿ï»ê±¤¿ª½Ã");
		doMap.put("ÀÎÃµ", "ÀÎÃµ±¤¿ª½Ã"); 		doMap.put("ÀÎÃµ½Ã", "ÀÎÃµ±¤¿ª½Ã");
		doMap.put("Àü³²", "Àü¶ó³²µµ");
		doMap.put("ÀüºÏ", "Àü¶óºÏµµ");
		doMap.put("Á¦ÁÖ", "Á¦ÁÖÆ¯º°ÀÚÄ¡µµ");	doMap.put("Á¦ÁÖµµ", "Á¦ÁÖÆ¯º°ÀÚÄ¡µµ");
		doMap.put("Ãæ³²", "ÃæÃ»³²µµ");
		doMap.put("ÃæºÏ", "ÃæÃ»ºÏµµ");
		
		if(doMap.containsValue(unit))					siDo = unit; 							
		else if(doMap.containsKey(unit))				siDo = doMap.get(unit);			
																															
		else if(unit.equals("±¤ÁÖ½Ã")) {															// °æ±âµµ ±¤ÁÖ½Ã¿Í ±¸ºÐ
			if(siDo.equals("°æ±âµµ"))								siGoon = "±¤ÁÖ½Ã";
			else 																	siDo = "±¤ÁÖ±¤¿ª½Ã";
		}
	}
	
	private void findSiGoon(String unit) {
		/* ½Ã ±ºÀÇ Á¤±Ô Ç¥Çö½ÄÀ¸·Î ½Ã ±º ±¸ºÐ */
		if(unit.matches("[°¡-ÆR]*[½Ã±º]")) siGoon = unit;
	}
	
	private void findGoo(String unit) {
		/* ½Ã ±ºÀÇ Á¤±Ô Ç¥Çö½ÄÀ¸·Î ½Ã ±º ±¸ºÐ */
		if(unit.matches("[°¡-ÆR]*[±¸]")) goo = unit;
	}
 
	private void findRi(String unit) {
		if(unit.matches("[°¡-ÆR]*¸®")) ri = unit;
	}
	
	private void findEupMyunDong(String unit) {
		if(unit.matches("[°¡-ÆR]*[À¾¸éµ¿°¡]")) eupMyunDong = unit;
	}
	
	private void findDoro(String unit) {
		if(unit.matches("[°¡-ÆR0-9]*[·Î±æ]")) doro = unit;
		
		/* µµ·Î¸í ÁÖ¼Ò µÚ °Ç¹° º»¹ø°ú ºÎ¹øÀÌ ºÙ¾î ÀÖ´Â °æ¿ì */
		else if(unit.matches("[°¡-ÆR0-9]*[·Î±æ][0-9]*?-?[0-9]*")) {
			findBuild(unit.split("[·Î±æ]")[1]);
		}
	}
	
	private void findDong(String unit) {
		if(unit.matches("[0-9]*µ¿")) dong = unit;
	}
	
	private void findHo(String unit) {
		if(unit.matches("[0-9]*È£")) ho = unit;
	}
	
	private void findEtc(String unit) {
		if(etc.length() < 1) etc = unit;
		else etc = etc + " " + unit;
	}
	
	
	
	private void findJibun(String unit) {
		/* Áö¹ø ÁÖ¼Ò¸¦ "-"À» ±âÁØÀ¸·Î ³ª´® */
		String tokenArr[] = unit.split("-");
	
		/* Áö¹øºÎ¹øÀÌ ¾øÀ» °æ¿ì */
		if(tokenArr.length != 2) {
			jibunMain = tokenArr[0];
			/* Áö¹ø ºÎ¹øÀ» 0À¸·Î ¸¸µé¾î ÁÜ */
			jibunSub = "0";
		}
		
		/* Áö¹ø ºÎ¹øÀÌ ÀÖÀ» °æ¿ì */
		else if(tokenArr.length == 2) {
			jibunMain = tokenArr[0];
			
			/* Áö¹ø ºÎ¹ø µÚ¿¡ ¹®ÀÚ¿­ÀÌ ¾øÀ» °æ¿ì */
			if(jibunSub.matches("[0-9}*")) jibunSub = tokenArr[1];
			
			/* Áö¹ø ºÎ¹ø µÚ¿¡ ¹®ÀÚ¿­ÀÌ ÀÖÀ» °æ¿ì */
			else {
				StringBuilder builder = new StringBuilder();

				for(int i=0; i<tokenArr[1].length(); i++) {
					/* ¼ýÀÚÀÏ °æ¿ì */
					if(tokenArr[1].charAt(i) > '0' && tokenArr[1].charAt(i) < '9')
						builder.append(tokenArr[1].charAt(i));
					else break;	
				}
				
				jibunSub = builder.toString();
			}
		}
		
	}
	
	private void findBuild(String unit) {
		/* Áö¹ø ÁÖ¼Ò¸¦ "-"À» ±âÁØÀ¸·Î ³ª´® */
		String tokenArr[] = unit.split("-");
	
		/* Áö¹øºÎ¹øÀÌ ¾øÀ» °æ¿ì */
		if(tokenArr.length != 2) {
			buildMain = tokenArr[0];
			/* Áö¹ø ºÎ¹øÀ» 0À¸·Î ¸¸µé¾î ÁÜ */
			buildSub = "0";
		}
		
		/* Áö¹ø ºÎ¹øÀÌ ÀÖÀ» °æ¿ì */
		else if(tokenArr.length == 2) {
			buildMain = tokenArr[0];
			
			/* Áö¹ø ºÎ¹ø µÚ¿¡ ¹®ÀÚ¿­ÀÌ ¾øÀ» °æ¿ì */
			if(buildSub.matches("[0-9}*")) buildSub = tokenArr[1];
			
			/* Áö¹ø ºÎ¹ø µÚ¿¡ ¹®ÀÚ¿­ÀÌ ÀÖÀ» °æ¿ì */
			else {
				StringBuilder builder = new StringBuilder();

				for(int i=0; i<tokenArr[1].length(); i++) {
					/* ¼ýÀÚÀÏ °æ¿ì */
					if(tokenArr[1].charAt(i) > '0' && tokenArr[1].charAt(i) < '9')
						builder.append(tokenArr[1].charAt(i));
					else break;	
				}
				
				buildSub = builder.toString();
			}
		}
		
	}
}
	

