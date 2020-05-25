package entitys.enums;

public enum FuelType {

	GASOLINE, DIESEL, SCOOTER_FUEL, HOME_HEATING_FUEL;

	public static FuelType stringToEnumVal(String FuelType2) {
		if (FuelType2 == null || FuelType2.trim().equals("")) {
			return null;
		}
		if (FuelType2.equals("Gasoline") || FuelType2.equals("GASOLINE")) {
			return FuelType.GASOLINE;
		}
		if (FuelType2.equals("Diesel") || FuelType2.equals("DIESEL"))  {
			return FuelType.DIESEL;
		}
		if (FuelType2.equals("Scooter Fuel") || FuelType2.equals("SCOOTER_FUEL")) {
			return FuelType.SCOOTER_FUEL;
		}
		if (FuelType2.equals("Home Heating Fuel")|| FuelType2.equals("HOME_HEATING_FUEL") ) {
			return FuelType.HOME_HEATING_FUEL;
		}

		return null;
	}

	public static String enumToString(FuelType fuelType) { // return to String ret the not CAPITAL version of the type
		String ret = "";
		switch (fuelType) {
		case GASOLINE:
			ret = "Gasoline";
			break;
		case DIESEL:
			ret = "Diesel";
			break;
		case SCOOTER_FUEL:
			ret = "Scooter Fuel";
			break;
		case HOME_HEATING_FUEL:
			ret = "Home Heating Fuel";
			break;
		}
		return ret;
	}

}
