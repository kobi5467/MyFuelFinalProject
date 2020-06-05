package server.dbLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

	public static Connection conn;	//
	
	private String url = "jdbc:mysql://localhost/myfuel?serverTimezone=IST";
	private String username = "root";
	private String password = "KJB5:jUM8m+fsc8V";
	
	public UserDBController userDBController;
	public FuelDBLogic fuelDBLogic;
	public CustomerDBLogic customerDBLogic;
	public PurchaseModelDBLogic purchaseModelDBLogic;
	public SaleDBLogic saleDBLOgic;
	public OrderDBLogic orderDBLogic;
	public EmployeeDBLogic employeeDBLogic;
	public ReportDBLogic reportDBLogic;
	
	public DBConnector() {
		createConnection();
		initLogicObjects();
	}
	
	public void initLogicObjects() {
		userDBController = new UserDBController();
		fuelDBLogic = new FuelDBLogic();
		customerDBLogic = new CustomerDBLogic();
		purchaseModelDBLogic = new PurchaseModelDBLogic();
		saleDBLOgic = new SaleDBLogic();
		orderDBLogic = new OrderDBLogic();
		employeeDBLogic = new EmployeeDBLogic();
		reportDBLogic =  new ReportDBLogic();
	}
	
	private void createConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			System.out.println("Driver definition failed");
		}
		try {
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public void closeConnection() {
		try {
			if(!conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
