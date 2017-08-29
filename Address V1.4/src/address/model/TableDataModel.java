package address.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableDataModel {
	IntegerProperty number;
	StringProperty name;
	StringProperty address;
	StringProperty postal;
	
	public TableDataModel(IntegerProperty number, StringProperty name, StringProperty address, StringProperty postal) {
		this.number = number;
		this.name = name;
		this.address = address;
		this.postal = postal;
	}

	public StringProperty getName() {
		return name;
	}

	public StringProperty getAddress() {
		return address;
	}
	
	public IntegerProperty getNumber() {
		return number;
	}
	
	public StringProperty getPostal() {
		return postal;
	}
	
	public void setAddress(String address) {
		this.address.setValue(address);
	}
}
