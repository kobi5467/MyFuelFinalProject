package entity;

import java.util.ArrayList;

public enum UserPermission {
	CUSTOMER,
	MARKETING_MANAGER,
	MARKETING_REPRESENTATIVE,
	STATION_MANAGER,
	SUPPLIER;
	
	public ArrayList<String> getButtonNames(UserPermission userPermission) {
		ArrayList<String> buttonNames = new ArrayList<>();
		buttonNames.add("Home");
		
		switch(userPermission) {
		case CUSTOMER:{
			buttonNames.add("");
			buttonNames.add("");
		}break;
		
		
		}
		
		return buttonNames;
	}
}
