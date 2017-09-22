package address.model;

import address.util.Option;

public class AddressDTO {
	public static final String EMPTY_STRING = "주소 정보가 없습니다.";
	
	private String siDo = "";
	private String siGoonGoo = "";
	private String eupMyunDong = "";
	private String doro = "";
	private String buildMain = "";		
	private String buildSub = "";				
	private String postal = "";
	private String buildName = "";
	private String dong = "";
	private String ho = "";
	private String etc = "";
	
	private boolean complete;
	private boolean plural = false;
	
	/* Getter */
	public String getSiDo() { return siDo; }
	
	public String getSiGoonGoo() { return siGoonGoo; }

	public String getEupMyunDong() { return eupMyunDong; }

	public String getDoro() { return doro; }

	public String getBuildMain() { return buildMain; }

	public String getBuildSub() { return buildSub; }

	public String getPostal() { return postal; }

	public String getBuildName() { return buildName; }

	public String getDong() { return dong; }

	public String getHo() { return ho; }

	public String getEtc() { return etc; }

	public boolean isComplete() { return complete; }

	public boolean isPlural() { return plural; }

	
	/* Setter */
	public void setSiDo(String siDo) { this.siDo = siDo; }

	public void setSiGoonGoo(String siGoonGoo) { this.siGoonGoo = siGoonGoo; }

	public void setEupMyunDong(String eupMyunDong) { this.eupMyunDong = eupMyunDong; }

	public void setDoro(String doro) { this.doro = doro; }

	public void setBuildMain(String buildMain) { this.buildMain = buildMain; }

	public void setBuildSub(String buildSub) { this.buildSub = buildSub; }

	public void setPostal(String postal) { this.postal = postal; }

	public void setBuildName(String buildName) { this.buildName = buildName; }
	
	public void setDong(String dong) { this.dong = dong; }

	public void setHo(String ho) { this.ho = ho; }

	public void setEtc(String etc) { this.etc = etc; }

	public void setComplete(boolean complete) { this.complete = complete; }
	
	public void setPlural(boolean plural) { this.plural = plural; }

	
	/* 주소를 만들어서 반환 */
	public String getAddress(Option option) {
		StringBuilder builder = new StringBuilder();
		
		if(!complete){ 
			builder.append(EMPTY_STRING);
		}
		
		else {
			builder.append(siDo); 							builder.append(" ");
			builder.append(siGoonGoo); 				builder.append(" ");
			builder.append(eupMyunDong);		builder.append(" ");
			builder.append(doro);							builder.append(" ");
			builder.append(buildMain);		
			
			/* 건물부번이 존재한다면 */
			if(!buildSub.equals("0")){
				builder.append("-");
				builder.append(buildSub);				builder.append(" ");
			}
			
			if(buildName.length() > 0 && option.isBuild()) {
				builder.append(" (");
				builder.append(buildName);
				builder.append(")");
			}
			
			if(dong.length() > 0) {
				builder.append(" ");
				builder.append(dong);
				builder.append(" ");
				builder.append(ho);
			}
			
			if(etc.length() > 0 && option.isEtc()) {
				builder.append(" ");
				builder.append("(");
				builder.append(etc);
				builder.append(")");
			}
		}
		
		return new String(builder.toString());
	}
	
	
}
