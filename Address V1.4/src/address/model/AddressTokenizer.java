package address.model;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class AddressTokenizer {
	/* ���� �ּ� */
	String siDo = "";
	String siGoon = "";
	String goo = "";
	String eupMyunDong = "";
	String ri = "";
	String jibunMain = "";
	String jibunSub = "";
	
	/* ���θ� �ּ� */
	String doro = "";
	String buildMain = "";
	String buildSub = "";
	
	String dong = "";
	String ho = "";
	
	String etc = "";
	
	public Address createVO(String address) {
		
		/* �ּҸ� �������� ������ ������ �����Ѵ�. */
		StringTokenizer token = new StringTokenizer(address, " ");
		
		String unit = token.nextToken();
			
		/* �� ���� */
		findSiDo(unit);
		if(!siDo.equals("")) unit = token.nextToken();
			
		/* �� �� ���� */
		findSiGoon(unit);
		if(!siGoon.equals("")) unit = token.nextToken();
		
		/* �� ���� */
		findGoo(unit);
		if(!goo.equals("")) unit = token.nextToken();
		
		/* �� �� �� ���� */
		findEupMyunDong(unit);
		if(!eupMyunDong.equals("")) unit = token.nextToken();
		
		/* �� ���� */
		findRi(unit);
		if(!ri.equals("")) unit = token.nextToken();
		
		/* ���� ����*/
		findJibun(unit);
		if(!jibunMain.equals("")) unit = token.nextToken();
		
		
		/* ���θ� ���� */
		findDoro(unit);
		if(!doro.equals("")) unit = token.nextToken();
		
		/* �ǹ� ���� */
		findBuild(unit);
		if(!buildMain.equals("")) unit = token.nextToken();
		
		/* ����Ʈ �� ���� */
		findDong(unit);
		if(!dong.equals("")) unit = token.nextToken();
			
		/* ����Ʈ ȣ ���� */
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
		 * �Է� �� "�����"�� "����Ư����" ���� ���� �ϱ� ���Ͽ�
		 * ���Ӹ��� Ű�� ���� �̸��� ������ �־ �����Ѵ�.
		 */
		HashMap<String, String> doMap = new HashMap<String, String>();
		
		doMap.put("����", "����Ư����");		doMap.put("�����", "����Ư����");	
		doMap.put("����", "������");
		doMap.put("���", "��⵵");
		doMap.put("�泲", "��󳲵�");
		doMap.put("���", "���ϵ�");
		doMap.put("����", "���ֱ�����");
		doMap.put("�뱸", "�뱸������");
		doMap.put("����", "����������;");		doMap.put("������", "����������;");
		doMap.put("�λ�", "�λ걤����");		doMap.put("�λ��", "�λ걤����");
		doMap.put("����", "����Ư����ġ��");	doMap.put("������", "����Ư����ġ��");	doMap.put("����Ư����", "����Ư����ġ��");
		doMap.put("���", "��걤����");		doMap.put("����", "��걤����");
		doMap.put("��õ", "��õ������"); 		doMap.put("��õ��", "��õ������");
		doMap.put("����", "���󳲵�");
		doMap.put("����", "����ϵ�");
		doMap.put("����", "����Ư����ġ��");	doMap.put("���ֵ�", "����Ư����ġ��");
		doMap.put("�泲", "��û����");
		doMap.put("���", "��û�ϵ�");
		
		if(doMap.containsValue(unit))					siDo = unit; 							
		else if(doMap.containsKey(unit))				siDo = doMap.get(unit);			
																															
		else if(unit.equals("���ֽ�")) {															// ��⵵ ���ֽÿ� ����
			if(siDo.equals("��⵵"))								siGoon = "���ֽ�";
			else 																	siDo = "���ֱ�����";
		}
	}
	
	private void findSiGoon(String unit) {
		/* �� ���� ���� ǥ�������� �� �� ���� */
		if(unit.matches("[��-�R]*[�ñ�]")) siGoon = unit;
	}
	
	private void findGoo(String unit) {
		/* �� ���� ���� ǥ�������� �� �� ���� */
		if(unit.matches("[��-�R]*[��]")) goo = unit;
	}
 
	private void findRi(String unit) {
		if(unit.matches("[��-�R]*��")) ri = unit;
	}
	
	private void findEupMyunDong(String unit) {
		if(unit.matches("[��-�R]*[���鵿��]")) eupMyunDong = unit;
	}
	
	private void findDoro(String unit) {
		if(unit.matches("[��-�R0-9]*[�α�]")) doro = unit;
		
		/* ���θ� �ּ� �� �ǹ� ������ �ι��� �پ� �ִ� ��� */
		else if(unit.matches("[��-�R0-9]*[�α�][0-9]*?-?[0-9]*")) {
			findBuild(unit.split("[�α�]")[1]);
		}
	}
	
	private void findDong(String unit) {
		if(unit.matches("[0-9]*��")) dong = unit;
	}
	
	private void findHo(String unit) {
		if(unit.matches("[0-9]*ȣ")) ho = unit;
	}
	
	private void findEtc(String unit) {
		if(etc.length() < 1) etc = unit;
		else etc = etc + " " + unit;
	}
	
	
	
	private void findJibun(String unit) {
		/* ���� �ּҸ� "-"�� �������� ���� */
		String tokenArr[] = unit.split("-");
	
		/* �����ι��� ���� ��� */
		if(tokenArr.length != 2) {
			jibunMain = tokenArr[0];
			/* ���� �ι��� 0���� ����� �� */
			jibunSub = "0";
		}
		
		/* ���� �ι��� ���� ��� */
		else if(tokenArr.length == 2) {
			jibunMain = tokenArr[0];
			
			/* ���� �ι� �ڿ� ���ڿ��� ���� ��� */
			if(jibunSub.matches("[0-9}*")) jibunSub = tokenArr[1];
			
			/* ���� �ι� �ڿ� ���ڿ��� ���� ��� */
			else {
				StringBuilder builder = new StringBuilder();

				for(int i=0; i<tokenArr[1].length(); i++) {
					/* ������ ��� */
					if(tokenArr[1].charAt(i) > '0' && tokenArr[1].charAt(i) < '9')
						builder.append(tokenArr[1].charAt(i));
					else break;	
				}
				
				jibunSub = builder.toString();
			}
		}
		
	}
	
	private void findBuild(String unit) {
		/* ���� �ּҸ� "-"�� �������� ���� */
		String tokenArr[] = unit.split("-");
	
		/* �����ι��� ���� ��� */
		if(tokenArr.length != 2) {
			buildMain = tokenArr[0];
			/* ���� �ι��� 0���� ����� �� */
			buildSub = "0";
		}
		
		/* ���� �ι��� ���� ��� */
		else if(tokenArr.length == 2) {
			buildMain = tokenArr[0];
			
			/* ���� �ι� �ڿ� ���ڿ��� ���� ��� */
			if(buildSub.matches("[0-9}*")) buildSub = tokenArr[1];
			
			/* ���� �ι� �ڿ� ���ڿ��� ���� ��� */
			else {
				StringBuilder builder = new StringBuilder();

				for(int i=0; i<tokenArr[1].length(); i++) {
					/* ������ ��� */
					if(tokenArr[1].charAt(i) > '0' && tokenArr[1].charAt(i) < '9')
						builder.append(tokenArr[1].charAt(i));
					else break;	
				}
				
				buildSub = builder.toString();
			}
		}
		
	}
}
	

