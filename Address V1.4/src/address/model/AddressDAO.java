/**
 * Author 이재현
 * 
 * 
 */

package address.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {
	Connection connection = null;
	Statement state = null;
	PreparedStatement preState = null;
	ResultSet set = null;
	
	ConnectionFactory factory = null;	
	
	public AddressDAO() throws Exception{
		factory = new ConnectionFactory();
		
		connection = factory.getConnection();
		state = connection.createStatement();
	}
	
	public String createWhere(String column, String value) {
		return " AND " + column + " = '" + value + "'"; 
	}
	
	public String createQuery(Address address) {
		String query = "SELECT 시도명,"
								+" 시군구명,"
								+" 법정읍면동명,"
								+" 도로명,"
								+" 건물본번,"
								+" 건물부번,"
								+" 우편번호,"
								+" 시군구용건물명"
								+" FROM 전체주소";
		
		if(!address.isEupMyun()) 
			query = query.replaceFirst(" 법정읍면동명,", "");						// 읍면 단위일 경우 읍면동명을 제거
		
		StringBuilder builder = new StringBuilder(query);					// Where절 추가를 위한 StringBuilder
		builder.append(" WHERE 1=1");													// AND만 추가하기 위한 작업 
		
		/*where절 경우의 수  
		 * 
		 * ** 도로명 주소일 경우
		 * 1. 시도, 시군구 empty
		 * 2. 시도 empty
		 * 3. 모두 있는 경우
		 * 
		 * ** 지번 주소일 경우
		 * 1. 시도, 시군구 empty
		 * 2. 시도 empty
		 * 3. 모두 있는 경우
		 */
		
		
		/* 도로명일경우
		 * * 시도명
		 * * 시군구명
		 * * 법정읍면동명
		 * * 도로명
		 * * 건물본번
		 * * 건물부번
		 * 순으로 값이 있는지 확인 후 WHERE절 삽입.
		 */
		if(address.isRoad()) {
			// 1.시도명
			if (address.getSiDo().length() > 2)
				builder.append(createWhere("시도명", address.getSiDo()));
			// 2. 시군구명
			if (address.getSiGoonGoo().length() > 2)
				builder.append(createWhere("시군구명", address.getSiGoonGoo()));
			// 3. 법정읍면동명
			if (address.getEupMyunDong().length() > 2)
				builder.append(createWhere("법정읍면동명", address.getEupMyunDong()));
			// 4. 도로명
			if (address.getDoro().length() > 2)
				builder.append(createWhere("도로명", address.getDoro()));
			// 5. 건물본번
			if (address.getBuildMain().length() > 0)
				builder.append(createWhere("건물본번", address.getBuildMain()));
			// 6. 건물부번
			if (address.getBuildSub().length() > 0)
				builder.append(createWhere("건물부번", address.getBuildSub()));
		}
		
		/* 지번일경우
		 * * 시도명
		 * * 시군구명
		 * * 법정읍면동명
		 * * 법정리명
		 * * 지번본번
		 * * 지번부번
		 * 순으로 값이 있는지 확인 후 WHERE절 삽입.
		 */
		else {
			// 1.시도명
			if (address.getSiDo().length() > 2)
				builder.append(createWhere("시도명", address.getSiDo()));
			// 2. 시군구명
			if (address.getSiGoonGoo().length() > 2)
				builder.append(createWhere("시군구명", address.getSiGoonGoo()));
			// 3. 법정읍면동명
			if (address.getEupMyunDong().length() > 2)
				builder.append(createWhere("법정읍면동명", address.getEupMyunDong()));
			// 4. 법정리명
			if (address.getRi().length() > 2)
				builder.append(createWhere("법정리명", address.getRi()));
			// 5. 지번본번
			if (address.getJibunMain().length() > 0)
				builder.append(createWhere("지번본번", address.getJibunMain()));
			// 6. 지번부번
			if (address.getJibunSub().length() > 0)
				builder.append(createWhere("지번부번", address.getJibunSub()));
		}
		
		return builder.toString();
	}

	public ArrayList<AddressDTO> changeRoad(Address address) {
		
		
		AddressDTO dto = new AddressDTO();
		ArrayList<AddressDTO> dtos= new ArrayList<AddressDTO>();
	
		
		try {
			set = state.executeQuery(createQuery(address));
			
			while(set.next()) {
				
				dto.setSiDo(set.getString("시도명"));
				dto.setSiGoonGoo(set.getString("시군구명"));
				if(address.isEupMyun()) 	
					dto.setEupMyunDong(set.getString("법정읍면동명"));
				dto.setDoro(set.getString("도로명"));
				dto.setBuildMain(set.getString("건물본번"));
				dto.setBuildSub(set.getString("건물부번"));
				dto.setPostal(set.getString("우편번호"));
				dto.setBuildName(set.getString("시군구용건물명")); 
				dto.setDong(address.getDong());
				dto.setHo(address.getHo());
				dto.setEtc(address.getEtc());
				
				dtos.add(dto);
			}
			
			/* 중복 주소 관리 */
			if(dtos.size() > 1) {
				
				/* 중복된 주소 삭제 */
				for(int i=dtos.size()-1; i>0; i--) {
					if(dtos.get(0).equals(dtos.get(i))) dtos.remove(i);
				}
				
				/* 중복 주소가 존재할 시 */
				if(dtos.size() > 1) {
					for(int i=0; i<dtos.size(); i++) {
						dtos.get(i).setComplete(true);
						dtos.get(i).setPlural(true);
					}
				}
				
				else {
					dtos.get(0).setPlural(false);
					/* 변환 성공을 나타내는 플래그 */
					dtos.get(0).setComplete(true);
				}
				
			}
			
			else if(dtos.size() == 1) {
				dtos.get(0).setPlural(false);
				dtos.get(0).setComplete(true);
			}
			
			/* 주소가 존재하지 않을 시 */
			else {
				dto.setComplete(false);
				
				dtos.add(dto);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return dtos;
	}
}
	
	
	
	

