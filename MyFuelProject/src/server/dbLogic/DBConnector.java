package server.dbLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * this class is responsible to connect the system to the DB and initialize the DBLOGIC classes
 * @author MyFuel Team
 *
 */
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
	/**
	 * this function initialize the DBLOGIC classes
	 */
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
	
	/**
	 * this function create the connection to the server and let us know if it succeed or failed.
	 */
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
	/**
	 * this function close the connection to the DB
	 */
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
