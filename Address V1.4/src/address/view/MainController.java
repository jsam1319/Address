package address.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import address.MainApp;
import address.model.Address;
import address.model.AddressDAO;
import address.model.AddressDTO;
import address.model.AddressTokenizer;
import address.model.TableDataModel;
import address.util.ExcelIOManager;
import address.util.Option;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController implements Initializable{
		
	
	/* 변수 선언 부 */
	@FXML private AnchorPane basisPane;
	
	@FXML private TextField pathField;
	@FXML private TableView<TableDataModel> table;
	@FXML private TableColumn<TableDataModel, Integer> numColumn;
	@FXML private TableColumn<TableDataModel, String> nameColumn;
	@FXML private TableColumn<TableDataModel, String> addressColumn;
	@FXML private TableColumn<TableDataModel, String> postalColumn;
	
	@FXML private TextField siDoField;
	@FXML private TextField siGoonGooField;
	@FXML private TextField eupMyunDongField;
	@FXML private TextField doroField;
	@FXML private TextField sbField;
	@FXML private TextField mainBuildField;
	@FXML private TextField subBuildField;
	@FXML private TextField postalField;
	@FXML private TextField etcField;
	@FXML private TextField dongField;
	@FXML private TextField hoField;
	
	@FXML private TextField singleSidoField;
	@FXML private TextField singleSigoongooField;
	@FXML private TextField singleEupField;
	@FXML private TextField singleDoroField;
	@FXML private TextField singleSbField;
	@FXML private TextField singleMainBuildField;
	@FXML private TextField singleSubBuildField;
	@FXML private TextField singlePostalField;
	@FXML private TextField singleEtcField;
	@FXML private TextField singleDongField;
	@FXML private TextField singleHoField;
	
	@FXML private Button convertButton;
	@FXML private Button openButton;
	@FXML private Button saveButton;	
	@FXML private Label describeLabel;
	@FXML private Button daumMapButton;
	@FXML private ProgressIndicator progress;
	
	@FXML private Button singleConvertButton;
	@FXML private TextField addressText;
	@FXML private Label	addressLabel;
	@FXML private WebView mapView;
	
	@FXML private CheckBox etcCheck;
	@FXML private CheckBox buildCheck;
	@FXML private CheckBox colorCheck;
	@FXML private CheckBox openCheck;
	
	@FXML private CheckBox singleEtcCheck;
	@FXML private CheckBox singleBuildCheck;
	
	@FXML private ColorPicker colorPicker;
	
	private Stage primaryStage;
	
	/* 변환된 데이터를 담든 List(테이블에 추가하기 위한 List) */
	ObservableList convertAdds = FXCollections.observableArrayList();
	/* 변환 데이터를 담고있는 DTO */
	ArrayList<AddressDTO> dtos = new ArrayList<AddressDTO>();
	
	boolean isData = false;
	
	@Override
    public void initialize(URL loc, ResourceBundle res) {
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().getName());
		addressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress());
		numColumn.setCellValueFactory(cellData -> cellData.getValue().getNumber().asObject());
		postalColumn.setCellValueFactory(cellData -> cellData.getValue().getPostal());
		
		daumMapButton.setDisable(true);
		
		Image openImage = new Image(getClass().getResourceAsStream("resource/open.png"));
		openButton.setGraphic(new ImageView(openImage));
		
		Image saveImage = new Image(getClass().getResourceAsStream("resource/save.png"));
		saveButton.setGraphic(new ImageView(saveImage));
		
		Image convertImage = new Image(getClass().getResourceAsStream("resource/convert.png"));
		convertButton.setGraphic(new ImageView(convertImage));
		
		basisPane.toFront();
		
		
		/* 엔터값 이벤트 지정 */
		addressText.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if((int)e.getCharacter().charAt(0) == 13) {
					convertAddress();
				}
			}
		});
    }

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void saveData() {
		FileChooser chooser = new FileChooser();
		ExcelIOManager excelManager = new ExcelIOManager();
		
		/* 파일 선택 시 확장자를 제한(엑셀 파일 */
	    chooser.getExtensionFilters().addAll(
	    		new ExtensionFilter("Excel Files", "*.xlsx"),
	    	    new ExtensionFilter("All Files", "*.*")
	    );
	 
	    File selectedDc = chooser.showSaveDialog(primaryStage);
	    
	    /* 파일 선택이 안되었을 시에 경고창을 띠움 */
	    if(selectedDc.getAbsolutePath() == null) {
	    	try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				Alert alert = new Alert(AlertType.WARNING);
		        alert.initOwner(primaryStage);
		        alert.setTitle("오류!");
		        alert.setHeaderText("파일이 선택되지 않았습니다.");
		        alert.setContentText("엑셀 파일을 선택해 주십시오.");

		        alert.showAndWait();
			}
	    }
	    
		try {
			
			/* 컬러피커가 disable시의 디폴트로 색깔을 지정한다 */
			if(colorPicker.isDisable()) excelManager.setCustomizing(Color.LIGHTCORAL);
			else									   excelManager.setCustomizing(colorPicker.getValue());
			
			excelManager.writeData(convertAdds, selectedDc.getAbsolutePath());
			
	    } catch(IOException ioe) {
	    	ioe.printStackTrace();
	    }
	}
	
	
	@FXML
	private void checkColorPicker() {
		if(colorCheck.isSelected())  colorPicker.setDisable(false);
		else										  colorPicker.setDisable(true);
		
	}
	

	
	@FXML
	private void handleOpenFile() {
	    FileChooser chooser = new FileChooser();
	    	
	    chooser.getExtensionFilters().addAll(
	    		new ExtensionFilter("Excel Files", "*.xlsx"),
	    	    new ExtensionFilter("All Files", "*.*")
	    	);
	   
	    File selectedFile = chooser.showOpenDialog(primaryStage);
	    String filePath = selectedFile.getAbsolutePath();
	   
	    /* 파일 선택이 안되었을 시에 경고창을 띠움 */
	    if(filePath == null) {
	    	try {
				throw new FileNotFoundException();
			} catch (FileNotFoundException e) {
				Alert alert = new Alert(AlertType.WARNING);
		        alert.initOwner(primaryStage);
		        alert.setTitle("오류!");
		        alert.setHeaderText("파일이 선택되지 않았습니다.");
		        alert.setContentText("엑셀 파일을 선택해 주십시오.");

		        alert.showAndWait();
			}
	    }
	    
	    pathField.setText(filePath);
	}
	
	
	/* 엑셀로 받은 데이터를 변환하여 테이블에 적용하는 작업 */
	@FXML
	private void convertFile() {
		if(isData) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.initOwner(primaryStage);
	  
	        alert.setTitle("확인!");
	        alert.setHeaderText("저장되지 않은 정보가 있습니다.");
	        alert.setContentText("저장 하시겠습니까?");
	        
	        alert.showAndWait();
	  
	        
	        if(alert.getResult().getText().equals("OK")) {
	        	saveData();
	        }
	        
	        /* 저장되어 있던 정보를 초기화 한 후 테이블을 초기화된 데이터로 저장 */
	        convertAdds = FXCollections.observableArrayList();
	        table.setItems(convertAdds);
		        
		}
		
		
		try {
			new FileInputStream(pathField.getText());
		} catch(FileNotFoundException fe) {
			Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(primaryStage);
	        alert.setTitle("오류!");
	        alert.setHeaderText("파일을 찾을 수 없습니다.");
	        alert.setContentText("파일 경로를 확인해 주십시오.");
	        
	        alert.showAndWait();
	        
	        return;
	        
		} 
		
		isData = true;
		
		/* 옵션 데이터를 체크하여 옵션 객체로 만들어 관리함 */
		Option option = new Option(etcCheck.isSelected(), buildCheck.isSelected());
		option.setEtc(etcCheck.isSelected());
		option.setBuild(buildCheck.isSelected());
		
		
		Task<Integer> convertTask;
	
		
		convertTask = new Task<Integer>() {
			long start = System.currentTimeMillis();
			long end;
			
			int success = 0;
			int fail = 0;
			
			/* Convert 작업 중 작업량을 확인하기 위하여 스레드를 이용 -> progressIndicator */
			@Override
			protected Integer call() throws Exception {
				AddressTokenizer tokenizer = new AddressTokenizer();
				AddressDAO dao = new AddressDAO();
				ExcelIOManager excelManager = new ExcelIOManager();
				
				HashMap<String, String> convertDatas = null;
				
				int totalCnt = 0;
				int progressCnt = 0;

				convertDatas = excelManager.readData(pathField.getText());

				/* 반복문을 돌리기 위한 Iterator 정의 */
				Iterator<String> it = convertDatas.keySet().iterator();
				
		
	
				for(int i=0; i<convertDatas.size(); i++) {
					progressCnt = convertDatas.size();
					System.out.println("123456778");
					while(it.hasNext()) {
						System.out.println("1234555");
						String name = it.next();
						String inputAddress = convertDatas.get(name);	// 변환 전 주소
						System.out.println("12345");
						ArrayList<AddressDTO> changes = dao.changeRoad(tokenizer.createVO(inputAddress));
						
						totalCnt++;
						
						updateProgress(totalCnt, progressCnt);
						updateMessage(progressCnt + "/" + totalCnt);
						
						 /* 복수 주소가 나올 경우를 대비하여 반복문 사용 */
						for(int j=0; j<changes.size(); j++) {
							System.out.println(progressCnt + " : " + name + " : " + changes.get(j).getAddress(option));
							if(!changes.get(j). isComplete()) {
								fail++;
								convertAdds.add(new TableDataModel(new SimpleIntegerProperty(progressCnt), 
																								new SimpleStringProperty(name), 
																								/* 변환 실패 시 받은 주소를 그대로 넘겨준다 */
																								new SimpleStringProperty(inputAddress), 
																								new SimpleStringProperty("-----")));
								dtos.add(changes.get(j));
							}
								
							else {
								success++;
								
								convertAdds.add(new TableDataModel(new SimpleIntegerProperty(progressCnt), 
																								new SimpleStringProperty(name), 
																								new SimpleStringProperty(changes.get(j).getAddress(option)), 
																								new SimpleStringProperty(changes.get(j).getPostal())));
								dtos.add(changes.get(j));
							}
						}
					}	
				}
				
				table.setItems(convertAdds);
				
				return totalCnt;
			}
			
			/* 동작하고 있을 시에 */
			@Override
			protected void running() {
				// TODO Auto-generated method stub
				basisPane.toBack();
				convertAdds = FXCollections.observableArrayList();
			}

			/* 작업이 모두 끝난 후에 */
			@Override
			protected void succeeded() {
				// TODO Auto-generated method stub
				progress.setVisible(false);
				basisPane.toFront();
				
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.initOwner(primaryStage);
		        alert.setTitle("주소 변환 완료!");
		        alert.setHeaderText("주소 변환이 완료되었습니다.");
		        alert.setContentText("전체주소 : " + (success + fail) + "\n" + "변환된 주소 : " + success + "\n" + "실패한 주소 : " + fail);
		        
		        alert.showAndWait();
		        
		        
		        end = System.currentTimeMillis();
		        
		        System.out.println("시간 : " + (end - start) / 1000 + "소요");
			}		
		};
		
		Thread thread = new Thread(convertTask);
		
		thread.setDaemon(true);
		progress.setVisible(true);
		progress.progressProperty().bind(convertTask.progressProperty());
	
		
		thread.start();	
		
		thread = null;
	}
	
	/* 단일 주소를 변환하는 작업 */
	@FXML
	private void convertAddress() {
		AddressTokenizer tokenizer = new AddressTokenizer();
		AddressDAO dao = null;
		try {
			dao = new AddressDAO();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<AddressDTO> dtos = dao.changeRoad(tokenizer.createVO(addressText.getText()));
		
		Option option = new Option(etcCheck.isSelected(), buildCheck.isSelected());
		option.setEtc(singleEtcCheck.isSelected());
		option.setBuild(singleBuildCheck.isSelected());
		
		addressLabel.setText(dtos.get(0).getAddress(option));
		
		if(dtos.get(0).getSiDo() == null) 					dtos.get(0).setSiDo(" ");
		if(dtos.get(0).getSiGoonGoo() == null) 			dtos.get(0).setSiGoonGoo(" ");
		if(dtos.get(0).getEupMyunDong() == null) 	dtos.get(0).setEupMyunDong(" ");
		if(dtos.get(0).getDoro() == null) 					dtos.get(0).setDoro(" ");
		if(dtos.get(0).getBuildName() == null) 			dtos.get(0).setBuildName(" ");
		if(dtos.get(0).getBuildMain() == null) 			dtos.get(0).setBuildMain(" ");
		if(dtos.get(0).getBuildSub() == null || 
		   dtos.get(0).getBuildSub().equals("0")) 		dtos.get(0).setBuildSub(" ");
		if(dtos.get(0).getPostal() == null) 					dtos.get(0).setPostal(" ");
		if(dtos.get(0).getEtc() == null)						dtos.get(0).setEtc(" ");
		if(dtos.get(0).getDong() == null)					dtos.get(0).setDong(" ");
		if(dtos.get(0).getHo() == null)						dtos.get(0).setHo(" ");
		
		singleSidoField.setText(dtos.get(0).getSiDo());
		singleSigoongooField.setText(dtos.get(0).getSiGoonGoo());
		singleEupField.setText(dtos.get(0).getEupMyunDong());
		singleDoroField.setText(dtos.get(0).getDoro());
		
		if(singleBuildCheck.isSelected())
			singleSbField.setText(dtos.get(0).getBuildName());
		
		singleMainBuildField.setText(dtos.get(0).getBuildMain());
		singleSubBuildField.setText(dtos.get(0).getBuildSub());
		singlePostalField.setText(dtos.get(0).getPostal());
		
		if(singleEtcCheck.isSelected())
			singleEtcField.setText(dtos.get(0).getEtc());
		
		singleDongField.setText(dtos.get(0).getDong());
		singleHoField.setText(dtos.get(0).getHo());
		
		//engine.load("http://map.daum.net/link/search/" + addressLabel.getText());
	}
	

	/* 맵을 보여주기 위한 맵 다이얼로그를 여는 작업 */
	@FXML
	private void showMapDialog() {
		try {
			TableViewSelectionModel selection = table.getSelectionModel();
			AddressDTO dto = dtos.get(selection.getSelectedIndex());
			
			Option option = new Option(etcCheck.isSelected(), buildCheck.isSelected());
			option.setEtc(etcCheck.isSelected());
			option.setBuild(buildCheck.isSelected());
			
	   		FXMLLoader loader = new FXMLLoader();
	   		loader.setLocation(MainApp.class.getResource("view/MapDialog.fxml"));
	   		AnchorPane page = (AnchorPane) loader.load();
	   		
	   		MapController mc = loader.getController();
	   		mc.setAddress(dto.getAddress(option));
	   		
	   		Stage dialogStage = new Stage();
	  	    dialogStage.setTitle("Daum Map");
	   	    dialogStage.initModality(Modality.WINDOW_MODAL);
	   	    dialogStage.initOwner(primaryStage);
	   	    Scene scene = new Scene(page);
	   	    dialogStage.setScene(scene);
	   	    
	   	    mc.setMap();
	   	    
	   	    dialogStage.show();
	   	    
	   	} catch(Exception e) {
	   		e.printStackTrace();
	   	}
	}  
	
	@FXML
	private void handleDetailAddress() {
		TableViewSelectionModel selection = table.getSelectionModel();
		AddressDTO dto = dtos.get(selection.getSelectedIndex());
		
		Option option = new Option(etcCheck.isSelected(), buildCheck.isSelected());
		option.setEtc(etcCheck.isSelected());
		option.setBuild(buildCheck.isSelected());
		
		
		if(dto.isComplete()) {
			siDoField.setText("");
			siGoonGooField.setText("");
			eupMyunDongField.setText("");
			doroField.setText("");
			sbField.setText("");
			mainBuildField.setText("");
			subBuildField.setText("");
			postalField.setText("");
			etcField.setText("");
			dongField.setText("");
			hoField.setText("");
			
			daumMapButton.setDisable(true);
			
			return;
		}
		
		/* 중복 코드를 제거하는 방법에 대해서 생각해 볼 것 */
		if(dtos.get(0).getSiDo() == null) 					dtos.get(0).setSiDo(" ");
		if(dtos.get(0).getSiGoonGoo() == null) 			dtos.get(0).setSiGoonGoo(" ");
		if(dtos.get(0).getEupMyunDong() == null) 	dtos.get(0).setEupMyunDong(" ");
		if(dtos.get(0).getDoro() == null) 					dtos.get(0).setDoro(" ");
		if(dtos.get(0).getBuildName() == null) 			dtos.get(0).setBuildName(" ");
		if(dtos.get(0).getBuildMain() == null) 			dtos.get(0).setBuildMain(" ");
		if(dtos.get(0).getBuildSub() == null || 
		   dtos.get(0).getBuildSub().equals("0")) 		dtos.get(0).setBuildSub(" ");
		if(dtos.get(0).getPostal() == null) 					dtos.get(0).setPostal(" ");
		if(dtos.get(0).getEtc() == null)						dtos.get(0).setEtc(" ");
		if(dtos.get(0).getDong() == null)					dtos.get(0).setDong(" ");
		if(dtos.get(0).getHo() == null)						dtos.get(0).setHo(" ");
		
		siDoField.setText(dto.getSiDo());
		siGoonGooField.setText(dto.getSiGoonGoo());
		eupMyunDongField.setText(dto.getEupMyunDong());
		doroField.setText(dto.getDoro());
		
		if(buildCheck.isSelected())
			sbField.setText(dto.getBuildName());
		
		mainBuildField.setText(dto.getBuildMain());
		subBuildField.setText(dto.getBuildSub());
		postalField.setText(dto.getPostal());
		
		if(etcCheck.isSelected())
			etcField.setText(dto.getEtc());
		
		dongField.setText(dto.getDong());
		hoField.setText(dto.getHo());
		
		daumMapButton.setDisable(false);
	}
	
	@FXML
	private void handleModify() {
		StringBuilder builder = new StringBuilder();
		
		builder.append(siDoField.getText());		builder.append(" ");
		builder.append(siGoonGooField.getText());	builder.append(" ");
		if(eupMyunDongField.getText().length() > 2) {
			builder.append(eupMyunDongField.getText());		builder.append(" "); 
		}
		builder.append(doroField.getText());		builder.append(" ");
		builder.append(mainBuildField.getText());	
		if(subBuildField.getText().length() > 1) {
			builder.append("-");
			builder.append(subBuildField.getText()); 
		}
		builder.append(" ");
		builder.append("(");	builder.append(sbField.getText());	builder.append(")");
		
		if(etcField.getText().length() > 2) {
			builder.append(" ");
			builder.append("(");	builder.append(etcField.getText());	builder.append(")");
		}
		
		if(dongField.getText().length() > 2) {
			builder.append(" ");
			builder.append(dongField.getText());
		}
		
		if(hoField.getText().length() > 2) {
			builder.append(" ");
			builder.append(etcField.getText());
		}
			
		
		TableViewSelectionModel selection = table.getSelectionModel();

		TableDataModel model = (TableDataModel) convertAdds.get(selection.getSelectedIndex());
		model.setAddress(builder.toString());
		
		convertAdds.set(selection.getSelectedIndex(), model);
		table.setItems(convertAdds);
	}
	
	
	/**/
	@FXML
	private void handleEntered(Event e) {
		try {
			if(((Button)e.getSource()).getId().toString().equals("convertButton")) 
				describeLabel.setText("지정된 파일의 구 주소를 신 주소로 변환합니다.");
			
			else if(((Button)e.getSource()).getId().toString().equals("singleConvertButton")) 
				describeLabel.setText("입력된 한가지 주소를 변환합니다.");
			
			else if(((Button)e.getSource()).getId().toString().equals("saveButton")) 
				describeLabel.setText("변환된 주소를 엑셀로 저장합니다.");
			
			else if(((Button)e.getSource()).getId().toString().equals("modifyButton")) 
				describeLabel.setText("입력된 주소로 주소를 수정합니다.");
			
			else if(((Button)e.getSource()).getId().toString().equals("openButton")) 
				describeLabel.setText("구 주소가 담긴 엑셀 파일을 엽니다");
			
		} catch(ClassCastException cce) {
			
		}
	}	
	
	@FXML
	private void handleExited() {
		describeLabel.setText("");
	}
}

/*
@FXML
private void showSingleChangeDialog() {
   	try {
   		FXMLLoader loader = new FXMLLoader();
   		
   		loader.setLocation(MainApp.class.getResource("view/SingleChangeDialog.fxml"));
   		AnchorPane page = (AnchorPane) loader.load();
   		
   		Stage dialogStage = new Stage();
  	    dialogStage.setTitle("Single Coverter");
   	    dialogStage.initModality(Modality.WINDOW_MODAL);
   	    dialogStage.initOwner(primaryStage);
   	    Scene scene = new Scene(page);
   	    dialogStage.setScene(scene);
   	    
   	    dialogStage.show();
   	    
   	    
   	    
   	    
   	} catch(Exception e) {
   		e.printStackTrace();
   	}
}
*/

/*
@FXML
private void handleSaveAs() {
	FileChooser chooser = new FileChooser();
	
    chooser.getExtensionFilters().addAll(
    		new ExtensionFilter("Excel Files", "*.xlsx"),
    	    new ExtensionFilter("All Files", "*.*")
    	);

    XSSFWorkbook wb = null;
    
    Color color;
    
    if(colorPicker.isDisable()) color = Color.LIGHTCORAL;
    else						color = colorPicker;
     
    java.awt.Color acolor = new java.awt.Color((int)(color.getRed()*255), (int)(color.getGreen()*255), (int)(color.getBlue()*255));
    System.out.println(acolor);
    
	try {
		File selectedDc = chooser.showSaveDialog(primaryStage);

		File saveFile = new File(selectedDc.getAbsolutePath());
		
		wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet();
		
		sheet.setColumnWidth(0, 8000);
		sheet.setColumnWidth(1, 18000);
		
		
		XSSFRow row = null;
		XSSFCell cell = null;
		XSSFCellStyle titleStyle = wb.createCellStyle();
		XSSFCellStyle defaultStyle = wb.createCellStyle();
		XSSFCellStyle faultStyle = wb.createCellStyle();
		
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
		
		faultStyle.setFillForegroundColor(new XSSFColor(acolor));
		faultStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		faultStyle.setBorderLeft(BorderStyle.THIN);
		faultStyle.setBorderRight(BorderStyle.THIN);
		faultStyle.setBorderBottom(BorderStyle.THIN);
		
		
		
		row = sheet.createRow(0);
		
		cell = row.createCell(0);
		cell.setCellValue("상호이름");
		cell.setCellStyle(titleStyle);
		
		cell = row.createCell(1);
		cell.setCellValue("변환된 주소");
		cell.setCellStyle(titleStyle);
		
		cell = row.createCell(2);
		cell.setCellValue("우편번호");
		cell.setCellStyle(titleStyle);
		
		
		for(int i=0; i<convertAdds.size(); i++) {
			row = sheet.createRow(i+1);
			TableDataModel model = (TableDataModel)convertAdds.get(i);
			
			
			if(model.getAddress().equals(AddressDTO.EMPTY_STRING)) {
				cell = row.createCell(0);
				cell.setCellValue(model.getName());
				cell.setCellStyle(faultStyle);
				
				cell = row.createCell(1);
				cell.setCellValue(model.getAddress());
				cell.setCellStyle(faultStyle);
				
				cell = row.createCell(2);
				cell.setCellValue(model.getPostal());
				cell.setCellStyle(faultStyle);
			}
				
			else {
				cell = row.createCell(0);
				cell.setCellValue(model.getName());
				cell.setCellStyle(defaultStyle);
				
				cell = row.createCell(1);
				cell.setCellValue(model.getAddress());
				cell.setCellStyle(defaultStyle);
				
				cell = row.createCell(2);
				cell.setCellValue(model.getPostal());
				cell.setCellStyle(defaultStyle);
			}
					
		
			
		}
		
		FileOutputStream fileOut = new FileOutputStream(saveFile);
        wb.write(fileOut);
		
        wb.close();
        
        if(isOpen.isSelected()) {
        	Process p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + 
        				saveFile.getAbsolutePath());
            
        	p.waitFor();
        }
        

	} catch(NullPointerException ne) {
    	Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(primaryStage);
        alert.setTitle("No Selection");
        alert.setHeaderText("No Person Selected");
        alert.setContentText("Please select a person in the table.");

        alert.showAndWait();
    } catch(Exception ee) {
    	ee.printStackTrace();
    }
	

}
*/

