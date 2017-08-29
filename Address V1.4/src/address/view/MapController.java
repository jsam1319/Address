package address.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MapController implements Initializable{
	
	String address = null;
	
	@FXML WebView mapView;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setMap() {
		WebEngine engine = mapView.getEngine();
		engine.load("http://map.daum.net/link/search/" + address);
	}
	
	
}
