/**
 * Author 이재현
 * 엑셀의 입,출력과 
 * 출력 시의 스타일을 관리하는 클래스
 */

package address.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import address.model.AddressDTO;
import address.model.TableDataModel;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class ExcelIOManager {
	
	XSSFWorkbook wb = null;
	XSSFSheet sheet = null;
	XSSFCellStyle titleStyle;
	XSSFCellStyle defaultStyle;
	XSSFCellStyle faultStyle;
	XSSFRow row = null;
	XSSFCell cell = null;
	
	java.awt.Color awtColor = null;
	
	int progress = 0;
	int rows = 0;

	
	/* 일련의 작업들의 순서를 헷갈리게 하지 않기 위해서 
	 * 하나의 Public 메소드로 정의한 후,
	 * 과정들은 Private으로 은닉화.
	 */
	public void setCustomizing(Color color) {
		wb = new XSSFWorkbook();
		sheet = wb.createSheet();
		
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 18000);
		
		/* 셀들의 스타일을 정의 
		 * titleStyle => 제목 셀의 스타일을 정의 
		 * defaultStyle => 주소 변환 완료 셀의 스타일을 정의
		 * faultStyle => 주소 변환 실패 셀의 스타일을 정의
		 */
		titleStyle = wb.createCellStyle();
		defaultStyle = wb.createCellStyle();
		faultStyle = wb.createCellStyle();
		
		setColor(color);
		setStyle();
		setHeader();
	}
	
	/** 변환 실패 시의 faultStyle의 색을 지정하기 위한 메소드 */
	private void setColor(Color color) {
		awtColor =  new java.awt.Color((int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255));
	}
	
	private void setStyle() {
		titleStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
		titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		titleStyle.setAlignment(HorizontalAlignment.CENTER);
		titleStyle.setBorderTop(BorderStyle.THICK);
		titleStyle.setBorderBottom(BorderStyle.THICK);
		titleStyle.setBorderLeft(BorderStyle.THICK);
		titleStyle.setBorderRight(BorderStyle.THICK);
		
		defaultStyle.setFillForegroundColor(IndexedColors.WHITE.index);
		defaultStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		defaultStyle.setBorderLeft(BorderStyle.THIN);
		defaultStyle.setBorderRight(BorderStyle.THIN);
		defaultStyle.setBorderBottom(BorderStyle.THIN);
		
		faultStyle.setFillForegroundColor(new XSSFColor(awtColor));
		faultStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		faultStyle.setBorderLeft(BorderStyle.THIN);
		faultStyle.setBorderRight(BorderStyle.THIN);
		faultStyle.setBorderBottom(BorderStyle.THIN);
	}
	
	private void setHeader() {
		row = sheet.createRow(0);
		
		/* 제목 셀을 정의 */
		cell = row.createCell(0);
		cell.setCellValue("상호이름");
		cell.setCellStyle(titleStyle);
		
		cell = row.createCell(1);
		cell.setCellValue("변환된 주소");
		cell.setCellStyle(titleStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("우편번호");
		cell.setCellStyle(titleStyle);
	}
	
	
	/** 엑셀 파일을 디스크에 쓰는 작업을 하는 메소드 */
	public void writeData(ObservableList convertAdds, String filePath) throws IOException {
		FileOutputStream fileOut = null;
		
		try {
			for(int i=0; i<convertAdds.size(); i++) {
				row = sheet.createRow(i+1);
				TableDataModel model = (TableDataModel)convertAdds.get(i);
				
				
				if(model.getAddress().getValue().equals(AddressDTO.EMPTY_STRING)) {
					cell = row.createCell(0);
					cell.setCellValue(model.getName().getValue());
					cell.setCellStyle(faultStyle);
					
					cell = row.createCell(1);
					cell.setCellValue(model.getAddress().getValue());
					cell.setCellStyle(faultStyle);
					
					cell = row.createCell(2);
					cell.setCellValue(model.getPostal().getValue());
					cell.setCellStyle(faultStyle);
				}
					
				else {
					cell = row.createCell(0);
					cell.setCellValue(model.getName().getValue());
					cell.setCellStyle(defaultStyle);
					
					cell = row.createCell(1);
					cell.setCellValue(model.getAddress().getValue());
					cell.setCellStyle(defaultStyle);
					
					cell = row.createCell(2);
					cell.setCellValue(model.getPostal().getValue());
					cell.setCellStyle(defaultStyle);
					
					fileOut = new FileOutputStream(filePath);
		            wb.write(fileOut);
				}
			}
		
		} finally {
			fileOut.close();
			wb.close();
		}
	}
	
	public HashMap<String, String> readData(String filePath) throws Exception {
		/* 엑셀의 파일 모두를 Map으로 전달하기 위한 변수 (상호명, 주소) */
		HashMap<String, String> datas = new HashMap<String, String>();
		XSSFRow row = null;
		XSSFCell name = null;
		XSSFCell address = null;
	
		wb = new XSSFWorkbook(new FileInputStream(filePath));
		sheet = wb.getSheetAt(0);
			
		rows = sheet.getPhysicalNumberOfRows();
		progress = 0;
		
		/* 엑셀 파일들을 1행부터 끝 행까지 모두 Map에 읽어들임 */
		for(int i=0; i<rows; i++) {
			row = sheet.getRow(i);
			
			name = row.getCell(0);
			address = row.getCell(1);
			
			datas.put(name.getStringCellValue(), address.getStringCellValue());
		}
		
		return datas;
	}

}
