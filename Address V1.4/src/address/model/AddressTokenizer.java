package address.model;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class AddressTokenizer {
	/* 지번 주소 */
	String siDo = "";
	String siGoon = "";
	String goo = "";
	String eupMyunDong = "";
	String ri = "";
	String jibunMain = "";
	String jibunSub = "";
	
	/* 도로명 주소 */
	String doro = "";
	String buildMain = "";
	String buildSub = "";
	
	String dong = "";
	String ho = "";
	
	String etc = "";
	
	
	
	@Override
	public String toString() {
		return "AddressTokenizer [siDo=" + siDo + ", siGoon=" + siGoon + ", goo=" + goo + ", eupMyunDong=" + eupMyunDong
				+ ", ri=" + ri + ", jibunMain=" + jibunMain + ", jibunSub=" + jibunSub + ", doro=" + doro
				+ ", buildMain=" + buildMain + ", buildSub=" + buildSub + ", dong=" + dong + ", ho=" + ho + ", etc="
				+ etc + "]";
	}


	public Address createVO(String address) {
		
		/* 주소를 공백으로 나누어 단위로 구별한다. */
		StringTokenizer token = new StringTokenizer(address, " ");
		
		
		while(token.hasMoreTokens()) {
			String unit = token.nextToken();
			
			/* 도 구분 */
			findSiDo(unit);
			
			/* 시 군 구분 */
			findSiGoon(unit);
			
			/* 구 구분 */
			findGoo(unit);
			
			/* 읍 면 동 구분 */
			findEupMyunDong(unit);
			
			/* 리 구분 */
			findRi(unit);
			
			/* 지번 구분*/
			findJibun(unit);
			
			/* 도로명 구분 */
			findDoro(unit);
		
			/* 건물 구분 */
			findBuild(unit);
			
			/* 아파트 동 구분 */
			findDong(unit);
			
			/* 아파트 호 구분 */
			findHo(unit);
			
			/* 나머지 주소 구분 */
//			findEtc(unit);
		}
		/* 도 구분 */
	

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
		 * 입력 시 "서울시"와 "서울특별시" 등을 구별 하기 위하여
		 * 줄임말을 키로 정식 이름을 값으로 넣어서 관리한다.
		 */
		HashMap<String, String> doMap = new HashMap<String, String>();
		
		doMap.put("서울", "서울특별시");		doMap.put("서울시", "서울특별시");	
		doMap.put("강원", "강원도");
		doMap.put("경기", "경기도");
		doMap.put("경남", "경상남도");
		doMap.put("경북", "경상북도");
		doMap.put("광주", "광주광역시");
		doMap.put("대구", "대구광역시");
		doMap.put("대전", "대전광역시;");		doMap.put("대전시", "대전광역시;");
		doMap.put("부산", "부산광역시");		doMap.put("부산시", "부산광역시");
		doMap.put("세종", "세종특별자치시");	doMap.put("세종시", "세종특별자치시");	doMap.put("세종특별시", "세종특별자치시");
		doMap.put("울산", "울산광역시");		doMap.put("울산시", "울산광역시");
		doMap.put("인천", "인천광역시"); 		doMap.put("인천시", "인천광역시");
		doMap.put("전남", "전라남도");
		doMap.put("전북", "전라북도");
		doMap.put("제주", "제주특별자치도");	doMap.put("제주도", "제주특별자치도");
		doMap.put("충남", "충청남도");
		doMap.put("충북", "충청북도");
		
		if(doMap.containsValue(unit))							siDo = unit; 							
		else if(doMap.containsKey(unit))					siDo = doMap.get(unit);			
																															
		else if(unit.equals("광주시")) {															// 경기도 광주시와 구분
			if(siDo.equals("경기도"))								siGoon = "광주시";
			else 																siDo = "광주광역시";
		}
	}
	
	private void findSiGoon(String unit) {
		/* 시 군의 정규 표현식으로 시 군 구분 */
		if(unit.matches("[가-힣]*[시군]")) siGoon = unit;
	}
	
	private void findGoo(String unit) {
		/* 시 군의 정규 표현식으로 시 군 구분 */
		if(unit.matches("[가-힣]*[구]")) goo = unit;
	}
 
	private void findRi(String unit) {
		if(unit.matches("[가-힣]*리")) ri = unit;
	}
	
	private void findEupMyunDong(String unit) {
		if(unit.matches("[가-힣]*[읍면동가]")) eupMyunDong = unit;
	}
	
	private void findDoro(String unit) {
		if(unit.matches("[가-힣0-9]*[로길]")) doro = unit;
		
/*		 도로명 주소 뒤 건물 본번과 부번이 붙어 있는 경우 
		else if(unit.matches("[가-힣0-9]*[로길][0-9]*?-?[0-9]*")) {
			findBuild(unit.split("[로길]")[1]);
		}*/
	}
	
	private void findDong(String unit) {
		if(unit.matches("[0-9]*동")) dong = unit;
	}
	
	private void findHo(String unit) {
		if(unit.matches("[0-9]*호")) ho = unit;
	}
	
	private void findEtc(String unit) {
		if(etc.length() < 1) etc = unit;
		else etc = etc + " " + unit;
	}
	
	
	
	private void findJibun(String unit) {
		if(!unit.matches("[0-9]+?-?[0-9]*?[번지]*")) return;
		
		/* 지번 주소를 "-"을 기준으로 나눔 */
		String tokenArr[] = unit.split("-");
	
		/* 지번부번이 없을 경우 */
		if(tokenArr.length != 2) {
			if(tokenArr[0].matches("[0-9]*")) { 
				jibunMain = tokenArr[0];
				/* 지번 부번을 0으로 만들어 줌 */
				jibunSub = "0";
			}
			
			else {
				StringBuilder builder = new StringBuilder();

				for(int i=0; i<tokenArr[0].length(); i++) {
					/* 숫자일 경우 */
					if(tokenArr[0].charAt(i) >= '0' && tokenArr[0].charAt(i) <= '9')
						builder.append(tokenArr[0].charAt(i));
					else break;	
				}
				
				jibunMain = builder.toString();
				jibunSub = "0";
			}
			
			
		}
		
		/* 지번 부번이 있을 경우 */
		else if(tokenArr.length == 2) {
			jibunMain = tokenArr[0];
			
			/* 지번 부번 뒤에 문자열이 없을 경우 */
			if(jibunSub.matches("[0-9]*")) jibunSub = tokenArr[1];
			
			/* 지번 부번 뒤에 문자열이 있을 경우 */
			else {
				StringBuilder builder = new StringBuilder();

				for(int i=0; i<tokenArr[1].length(); i++) {
					/* 숫자일 경우 */
					if(tokenArr[1].charAt(i) >= '0' && tokenArr[1].charAt(i) <= '9')
						builder.append(tokenArr[1].charAt(i));
					else break;	
				}
				
				jibunSub = builder.toString();
			}
		}
		
	}
	
	private void findBuild(String unit) {
		if(!unit.matches(	"[0-9]+?-?[0-9]*?[번지]*")) return;

		/* 건물 주소를 "-"을 기준으로 나눔 */
		String tokenArr[] = unit.split("-");

		/* 건물부번이 없을 경우 */
		if(tokenArr.length != 2) {
			if(tokenArr[0].matches("[0-9]*")) { 
				buildMain = tokenArr[0];
				/* 지번 부번을 0으로 만들어 줌 */
				buildSub = "0";
			}
			
			else {
				StringBuilder builder = new StringBuilder();

				for(int i=0; i<tokenArr[0].length(); i++) {
					/* 숫자일 경우 */
					if(tokenArr[0].charAt(i) >= '0' && tokenArr[0].charAt(i) <= '9')
						builder.append(tokenArr[0].charAt(i));
					else break;	
				}
				
				buildMain = builder.toString();
				buildSub = "0";
			}
		}
		
		/* 건물 부번이 있을 경우 */
		else if(tokenArr.length == 2) {
			buildMain = tokenArr[0];
			
			/* 건물 부번 뒤에 문자열이 없을 경우 */
			if(buildSub.matches("[0-9]*")) buildSub = tokenArr[1];
			
			/* 건물 부번 뒤에 문자열이 있을 경우 */
			else {
				StringBuilder builder = new StringBuilder();

				for(int i=0; i<tokenArr[1].length(); i++) {
					/* 숫자일 경우 */
					if(tokenArr[1].charAt(i) >= '0' && tokenArr[1].charAt(i) <= '9')
						builder.append(tokenArr[1].charAt(i));
					else break;	
				}
				
				buildSub = builder.toString();
			}
			
			
		}
		
	}
}
	

