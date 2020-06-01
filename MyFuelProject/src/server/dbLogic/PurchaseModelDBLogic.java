package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;

public class PurchaseModelDBLogic {

	public JsonArray getPurchaseModelsTypes() {
		JsonArray types = new JsonArray();

		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  purchase_model;";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					types.add(rs.getString("purchaseModelType"));
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return types;
	}

	public float getPurchaseModelDiscountByType(String purchaseModel) {
		float discount = 0;
		String query = "";

		Statement stmt = null;
		try {
			if (DBConnector.conn != null) {
				query = "SELECT * FROM  purchase_model "
						+ "WHERE purchaseModelType = '" + purchaseModel + "';";
				stmt = DBConnector.conn.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					discount = rs.getFloat("purchaseModelDiscount");
				}
			} else {
				System.out.println("Conn is null");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return discount;
	}

}
