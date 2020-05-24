package server.dbLogic;

import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.JsonObject;

public class OrderDBLogic {
	
	public void insertHomeHeatingFuelOrder(JsonObject order) {
		String query = " ";
		Statement stmt = null;
		String orderID=order.get("orderID").getAsString();
		String customerId=order.get("customerId").getAsString();
		String amount=order.get("amount").getAsString();
		String Street=order.get("street").getAsString();
		String dateSupply=order.get("dateSupplay").getAsString();
		String urgentOrder=order.get("isUrgentOrder").getAsString();
		String saleTemplateName=order.get("dateSupplay").getAsString();
		try {
			if (DBConnector.conn != null) {
				query = "INSERT INTO  home_heating_fuel_orders(orderID,customerId,amount,Street,dateSupply,urgentOrder,saleTemplateName)" +
						"VALUES("+"'"+orderID+"'"+","+"'"+customerId+"'"+","+"'"+ amount+"'"+","+"'"+Street+"'"+","+"'"+dateSupply+"'"+","+"'"+urgentOrder+"'"+","+"'"+saleTemplateName+"'"+");" ;
				System.out.println(query);
				stmt = DBConnector.conn.createStatement();
				stmt.execute(query);
				System.out.println(query);
			} else {
				System.out.println("Conn is null");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
