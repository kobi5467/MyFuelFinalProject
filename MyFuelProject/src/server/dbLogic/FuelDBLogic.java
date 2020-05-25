package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.google.gson.JsonArray;

import entitys.Fuel;
import entitys.enums.FuelType;

public class FuelDBLogic {
	
	public JsonArray getFuelTypes(){
		JsonArray fuelTypes = new JsonArray();

		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  fuel ";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					 fuelTypes.add(rs.getString("fuelType"));
				}
			}else {
				System.out.println("Conn is null");
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return fuelTypes;
		
	}
	
	public Fuel getFuelObjectByType(String fuelType) {
		Fuel fuel = null;

		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				query = "SELECT * FROM  fuel "
					  + "WHERE fuelType ='" + fuelType + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if(rs.next()) {
					float pricePerLitter = rs.getFloat("pricePerLitter");
					float maxPricePerLitter = rs.getFloat("maxPricePerLitter");
					fuel = new Fuel(FuelType.stringToEnumVal(fuelType) ,pricePerLitter,maxPricePerLitter);
				}
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return fuel;
	}
	
	public void updateFuel(Fuel fuel,String newPrice) {
		
		float PriceToUpdate= 0;
		PriceToUpdate=Float.parseFloat(newPrice);
		String fueltype=fuel.getFuelType().toString();
		String query = "";
		Statement stmt = null;
		try {
			if(DBConnector.conn != null) {
				stmt = DBConnector.conn.createStatement();
				query =  "UPDATE fuel " + 
						 "SET pricePerLitter = " + PriceToUpdate +  
						 " WHERE fuelType = '" + fueltype + "';";
				stmt.executeUpdate(query);
			}else {
				System.out.println("Conn is null");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	
	public JsonArray getFuelCompanyNames() {
		JsonArray companyNames = new JsonArray();
		
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM fuel_company;"; 
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while(rs.next()) {
					companyNames.add(rs.getString("companyName"));
				}
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return companyNames;
	}
}
