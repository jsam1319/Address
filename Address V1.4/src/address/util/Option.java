package address.util;

public class Option {
	boolean etc;
	boolean build;
	
	public Option(boolean etc, boolean build) {
		this.etc = etc;
		this.build = build;
	}
	
	public boolean isEtc() {
		return etc;
	}
	public boolean isBuild() {
		return build;
	}
	public void setEtc(boolean isEtc) {
		this.etc = isEtc;
	}
	public void setBuild(boolean isBuild) {
		this.build = isBuild;
	}
	
	
}
