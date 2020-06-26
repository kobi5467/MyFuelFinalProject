package unittest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class HomeHeatingFuelOrderTest {

	private static SetUpTest setUpTest;
	
	@BeforeClass
	public static void setUp() throws Exception {
		setUpTest = new SetUpTest();
	}
	
	/****************************************************************************************/
	/**************************	      CALC TOTAL PRICE TESTS	   **************************/
	/****************************************************************************************/
	
	@Test
	public void testCalcPrice_NoSaleDiscount_NoUrgentOrder_LessThen600Litters() {
		boolean isUrgentOrder = false;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 0;
		float amountOfLitters = 50;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		
		float expectedResult = 310; // 6 * 50 + shipping (10) = 310
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}
	
	@Test
	public void testCalcPrice_NoSaleDiscount_NoUrgentOrder_Between600To800Litters() {
		boolean isUrgentOrder = false;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 0;
		float amountOfLitters = 700;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		
		float expectedResult = 4084F; // (6 * 700) * 3% discount + shipping (10) = 4084
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}
	
	@Test
	public void testCalcPrice_NoSaleDiscount_NoUrgentOrder_MoreThen800Litters() {
		boolean isUrgentOrder = false;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 0;
		float amountOfLitters = 1000;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		
		float expectedResult = 5770F; // (6 * 1000) * 4% discount + shipping (10) = 5770
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}

	@Test
	public void testCalcPrice_NoSaleDiscount_YesUrgentOrder_LessThen600Litters() {
		boolean isUrgentOrder = true;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 0;
		float amountOfLitters = 50;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		
		float expectedResult = 316; // 6 * 50 * (-2% for urgent order = -2% discount) + shipping (10) = 316
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}
	
	@Test
	public void testCalcPrice_NoSaleDiscount_YesUrgentOrder_Between600To800Litters() {
		boolean isUrgentOrder = true;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 0;
		float amountOfLitters = 700;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		
		float expectedResult = 4168F; // (6 * 700) * (3% - 2% for urgent order = 1% discount) + shipping (10) = 4168
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}
	
	@Test
	public void testCalcPrice_NoSaleDiscount_YesUrgentOrder_MoreThen800Litters() {
		boolean isUrgentOrder = true;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 0;
		float amountOfLitters = 1000;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		
		float expectedResult = 5890F; // (6 * 1000) * (4% - 2% for urgent order = 2% discount) + shipping (10) = 5890
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}
	
	@Test
	public void testCalcPrice_YesSaleDiscount_YesUrgentOrder_LessThen600Litters() {
		boolean isUrgentOrder = true;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 10;
		float amountOfLitters = 50;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		// 6 * 50 * (10% of sale -2% for urgent order = 8% discount) + shipping (10) = 286.0
		float expectedResult = 286; 
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}
	
	@Test
	public void testCalcPrice_YesSaleDiscount_YesUrgentOrder_Between600To800Litters() {
		boolean isUrgentOrder = true;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 20;
		float amountOfLitters = 700;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		// (6 * 700) * (20% for sale + 3% - 2% for urgent order = 21% discount) + shipping (10) = 3328.0
		float expectedResult = 3328.0F; 
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}
	
	@Test
	public void testCalcPrice_YesSaleDiscount_YesUrgentOrder_MoreThen800Litters() {
		boolean isUrgentOrder = true;
		setUpTest.pricePerLitter = 6;
		setUpTest.saleDiscount = 30;
		float amountOfLitters = 1000;
		
		setUpTest.homeHeatingFuelController.setHomeHeatingFuelInterface(setUpTest.iHomeHeatingFuelDBManager);
		// (6 * 1000) * (30% for sale + 4% - 2% for urgent order = 32% discount) + shipping (10) = 4090.0F
		float expectedResult = 4090.0F; 
		float actualResult = setUpTest.homeHeatingFuelController.calcTotalPrice(amountOfLitters,isUrgentOrder);
		assertTrue((expectedResult - actualResult == 0));
	}
	
	/****************************************************************************************/
	/**************************	   	    USER INPUT TESTS		   **************************/
	/****************************************************************************************/
	
	@Test
	public void testCheckAmountField_NegativeAmount() {
		String amount = "-1";
		boolean expectedResult = false;
		
		boolean actualResult = setUpTest.homeHeatingFuelController.checkFuelAmount(amount);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCheckAmountField_EmptyAmount() {
		String amount = "";
		boolean expectedResult = false;
		
		boolean actualResult = setUpTest.homeHeatingFuelController.checkFuelAmount(amount);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCheckAmountField_AmountContainCharacters() {
		String amount = "12asb";
		boolean expectedResult = false;
		
		boolean actualResult = setUpTest.homeHeatingFuelController.checkFuelAmount(amount);
		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testCheckStreetField_EmptyStreetString() {
		String street = "";
		boolean expectedResult = false;
		
		boolean actualResult = setUpTest.homeHeatingFuelController.checkStreet(street);
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCheckDateSupplyField_EmptyDateSupply() {
		String date = "";
		
		boolean expectedResult = false;
		boolean actualResult = setUpTest.homeHeatingFuelController.checkIfDateSupplyIsValid(date);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCheckDateSupplyField_PassedDateSupply() {
		LocalDate date = LocalDate.now();
		date = date.minusMonths(2); // date of 2 months ago
		
		boolean expectedResult = false;
		boolean actualResult = setUpTest.homeHeatingFuelController.checkIfDateSupplyIsPassed(date);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCheckDateSupplyField_GoodDateSupply() {
		LocalDate date = LocalDate.now();
		date = date.plusMonths(2); // date of 2 months later
		
		boolean expectedResult = true;
		boolean actualResult = setUpTest.homeHeatingFuelController.checkIfDateSupplyIsPassed(date);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testCheckPaymentMethodField_UnSelectedPaymentMethod() {
		String paymentMethod = "Choose type";
		String defualtValue = "Choose type";
		boolean expectedResult = false;
		boolean actualResult = setUpTest.homeHeatingFuelController.checkIfSelectedPaymentMethod(paymentMethod,defualtValue);
		
		assertEquals(expectedResult, actualResult);
	}
	
	/****************************************************************************************/
	/**************************	   	    	DB TESTS			   **************************/
	/****************************************************************************************/
	
	@Test
	public void testInsertNewHomeHeatingFuelOrderToDB() {
		JsonObject json = new JsonObject();
		float fuelAmount = setUpTest.rnd.nextFloat() * 200;
		setUpTest.pricePerLitter = 6;
		
		json.addProperty("customerId", "12345678");
		json.addProperty("amount", fuelAmount);
		json.addProperty("city", "Yokneam Illit");
		json.addProperty("street", "Yarden 66");
		json.addProperty("isUrgentOrder", "false");
		json.addProperty("paymentMethod", "Cash");
		json.addProperty("dateSupplay", "2020-07-05");
		json.addProperty("totalPrice", setUpTest.iHomeHeatingFuelDBManager.getPricePerLitter() * fuelAmount);
		json.addProperty("orderDate", "2020-06-25");
		json.addProperty("saleTemplateName", "");
		json.addProperty("fuelCompany", "Ten");
		json.addProperty("customerType", "Private");
		
		boolean expectedResult = true;	// true means the submit was success.
		boolean actualResult = setUpTest.iHomeHeatingFuelDBManager.submitHomeHeatingFuelOrder(json);
		
		assertEquals(expectedResult, actualResult);
	}
	
	@Test
	public void testReadOrderFromDB_getHomeHeatingFuelOrdersByCustomerID_CheckIfInsertIsWorking() {
		String customerID = "12345678";
		
		JsonArray orders = setUpTest.dbConnector.orderDBLogic.getHomeHeatingFuelOrdersByCustomerID(customerID);
		int countOfOrderBeforeAdd = orders.size();
		
		// create order.
		JsonObject json = new JsonObject();
		float fuelAmount = setUpTest.rnd.nextFloat() * 200;
		setUpTest.pricePerLitter = 6;
		
		json.addProperty("customerId", "12345678");
		json.addProperty("amount", fuelAmount);
		json.addProperty("city", "Yokneam Illit");
		json.addProperty("street", "Yarden 66");
		json.addProperty("isUrgentOrder", "false");
		json.addProperty("paymentMethod", "Cash");
		json.addProperty("dateSupplay", "2020-07-05");
		json.addProperty("totalPrice", setUpTest.iHomeHeatingFuelDBManager.getPricePerLitter() * fuelAmount);
		json.addProperty("orderDate", "2020-06-25");
		json.addProperty("saleTemplateName", "");
		json.addProperty("fuelCompany", "Ten");
		json.addProperty("customerType", "Private");
		
		setUpTest.iHomeHeatingFuelDBManager.submitHomeHeatingFuelOrder(json);
		
		orders = setUpTest.dbConnector.orderDBLogic.getHomeHeatingFuelOrdersByCustomerID(customerID);
		int expectedSize = countOfOrderBeforeAdd + 1;
		int actualResult = orders.size();
		
		assertEquals(expectedSize, actualResult);
	}
	
	@Test
	public void testReadOrdersFromDB_getHomeHeatingFuelOrderByOrderID_InvalidOrderID() {
//		dbConnector = new DBConnector();
		
		String orderID = "100";
		JsonObject json = setUpTest.dbConnector.orderDBLogic.getHomeHeatingFuelOrderByOrderID(orderID);
		assertTrue(json.get("orderID") == null);
	}
	
	@Test
	public void testReadOrdersFromDB_getHomeHeatingFuelOrderByOrderID_GoodOrderID() {
		String customerID = "12345678";
		String orderID = "135";
		JsonObject json = setUpTest.dbConnector.orderDBLogic.getHomeHeatingFuelOrderByOrderID(orderID);
		assertTrue(json.get("orderID") != null && json.get("customerID").getAsString().equals(customerID));
	}
	
	@Test
	public void testPricePerLitterDB_getPricePerLitterOfHomeHeatingFuel() {
		String fuelType = "Home Heating Fuel";
		float expectedResult = 7.65F;
		
		float actualResult = setUpTest.dbConnector.fuelDBLogic.getFuelObjectByType(fuelType).getPricePerLitter();
		
		assertTrue(expectedResult - actualResult == 0);
	}
	
	@Test
	public void testReadFuelCompaniesFromDbByCustomerID() {
		String customerID = "123456";
		
		String expectedResult = "Ten,Sonol,Paz";
		String actualResult = setUpTest.dbConnector.customerDBLogic.getFuelCompaniesByCustomerID(customerID);
		
		assertEquals(expectedResult, actualResult);
	}
	
}
