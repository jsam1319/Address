/**
 *  Author 이재현
 *  엑셀로 부터 읽어온 주소의 단위 별로 구분하여 만든 VO 클래스
 *  
 */


package address.model;

public class Address {
	
	/* 구 주소 */
	private String siDo = "";
	private String siGoonGoo  = "";
	private String eupMyunDong  = "";
	private String ri  = "";
	private String jibunMain  = "";
	private String jibunSub  = "";
	
	/* 신 주소 */
	private String doro  = "";			
	private String buildMain  = "";
	private String buildSub  = "";
	
	private String dong  = "";						// 건물의 동
	private String ho  = "";							// 건물의 호 

	private String etc  = "";						// 나머지 주소 
	
	public Address(Builder builder) { 
		this.siDo = builder.siDo;
		this.siGoonGoo = builder.siGoonGoo;
		this.eupMyunDong = builder.eupMyunDong;
		this.ri = builder.ri;
		this.jibunMain = builder.jibunMain;
		this.jibunSub = builder.jibunSub;
		this.doro = builder.doro;
		this.buildMain = builder.buildMain;
		this.buildSub = builder.buildSub;
		this.etc = builder.etc;
		this.dong = builder.dong;
		this.ho = builder.ho;
	
	}
	
	/* Setter */

	/*
	* ***************************************************************
	*	builder 패턴으로 대체 
	*  Telescoping Constructor 패턴의 단점 -> 가독성, 실수 유발 
	*  Setter의 단점 -> 코드의 길이가 길어짐, 스레드 안정성 확보 불가
	*  builder 패턴을 사용함으로 둘의 단점을 보완
	*  ***************************************************************
	public void setEtc(String etc)								 				{ this.etc = etc; }
	public void setSi_do(String siDo) 										{ this.siDo = siDo; }
	public void setSi_goon_goo(String siGoonGoo)  				{ this.siGoonGoo = siGoonGoo; }
	public void setEup_myun_dong(String eupMyunDong)	{ this.eupMyunDong = eupMyunDong; }
	public void setRi(String ri) 													{ this.ri = ri; }
	public void setMain_jibun(String jibunMain) 					{ this.jibunMain = jibunMain; }
	public void setSub_jibun(String jibunSub) 							{ this.jibunSub = jibunSub; }
	public void setMain_build(String buildMain) 						{ this.buildMain = buildMain; }
	public void setSub_build(String buildSub) 							{ this.buildSub = buildSub; }
	public void setRoad_juso(String doro) 								{ this.doro = doro; }
	public void setApt_dong(String dong) 								{this.dong = dong; }
	public void setApt_ho(String ho) 											{this.ho = ho; }

	*/
	
	
	/* Getter */

	public String getSiDo() 						{ return siDo; }

	public String getSiGoonGoo() 			{ return siGoonGoo; }

	public String getEupMyunDong() 	{ return eupMyunDong; }

	public String getRi() 							{ return ri; }

	public String getJibunMain()				{ return jibunMain; }

	public String getJibunSub() 				{ return jibunSub; }

	public String getDoro() 						{ return doro; }

	public String getBuildMain() 			{ return buildMain; }

	public String getBuildSub() 				{ return buildSub; }

	public String getDong() 						{ return dong; }

	public String getHo() 							{ return ho; }

	public String getEtc() 						{ return etc; }

	/* 신 주소 여부를 반환 */
	public boolean isRoad() {
		return !doro.equals("") || doro.length() > 2;
	}
	
	/* 읍면 단위 여부를 반환 */
	public boolean isEupMyun() { 
		if(eupMyunDong.matches("[가-힣]*[읍면]")) return true;
		else																			 return false;
	}
	
	
	
	/* 빌더패턴 사용 */
	
	@Override
	public String toString() {
		return "Address [siDo=" + siDo + ", siGoonGoo=" + siGoonGoo + ", eupMyunDong=" + eupMyunDong + ", ri=" + ri
				+ ", jibunMain=" + jibunMain + ", jibunSub=" + jibunSub + ", doro=" + doro + ", buildMain=" + buildMain
				+ ", buildSub=" + buildSub + ", dong=" + dong + ", ho=" + ho + ", etc=" + etc + "]";
	}



	static class Builder {
		/* 구 주소 */
		private String siDo = "";
		private String siGoonGoo  = "";
		private String eupMyunDong  = "";
		private String ri  = "";
		private String jibunMain  = "";
		private String jibunSub  = "";
		
		/* 신 주소 */
		private String doro  = "";			
		private String buildMain  = "";
		private String buildSub  = "";
		
		private String dong  = "";						
		private String ho  = "";							

		private String etc  = "";						
		
	
		public Builder siDo(String siDo) 											{ this.siDo = siDo; return this; }
		public Builder siGoonGoo(String siGoonGoo) 				{ this.siGoonGoo = siGoonGoo; return this; }
		public Builder eupMyunDong(String eupMyunDong)	{ this.eupMyunDong = eupMyunDong; return this; }
		public Builder ri(String ri) 														{ this.ri = ri; return this; }
		public Builder jibunMain(String jibunMain) 					{ this.jibunMain = jibunMain; return this; }
		public Builder jibunSub(String jibunSub) 							{ this.jibunSub = jibunSub; return this; }
		
		public Builder doro(String doro) 											{ this.doro = doro; return this; }
		public Builder buildMain(String buildMain	) 					{ this.buildMain = buildMain; return this; }
		public Builder buildSub(String buildSub) 							{ this.buildSub = buildSub; return this; }
		
		public Builder etc(String etc) 												{ this.etc = etc; return this; }
		
		public Builder dong(String dong)										{ this.dong = dong; return this; }
		public Builder ho(String ho)													{ this.ho = ho; return this; }
		
		
		public Address build() {
			return new Address(this);
		}
 	}
}
