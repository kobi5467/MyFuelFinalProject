package server.dbLogic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonArray;
/**
 * This class responsible to rule on all the purchase model DB Logic with all the requests from the server.
 * @author oyomtov
 * @version - Final
 */
public class PurchaseModelDBLogic {
	/**
	 * This method is responsible to get the purchase model types data from the DB.
	 * @return - return Json array with all the data.
	 */
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
	/**
	 * This method is responsible to get the purchase model discount by type from the DB.
	 * @param purchaseModel - string value of purchase model
	 * @return - Float value with discount.
	 */
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
