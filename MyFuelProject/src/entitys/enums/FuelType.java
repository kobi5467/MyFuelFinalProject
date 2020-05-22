package entitys.enums;

public enum FuelType {

	GASOLINE, DIESEL, SCOOTER_FUEL, HOME_HEATING_FUEL;

	public static FuelType stringToEnumVal(String FuelType2) {
		if (FuelType2 == null || FuelType2.trim().equals("")) {
			return null;
		}
		if (FuelType2.equals("Gasoline")) {
			return FuelType.GASOLINE;
		}
		if (FuelType2.equals("Diesel")) {
			return FuelType.DIESEL;
		}
		if (FuelType2.equals("Scooter Fuel")) {
			return FuelType.SCOOTER_FUEL;
		}
		if (FuelType2.equals("Home Heating Feul")) {
			return FuelType.HOME_HEATING_FUEL;
		}

		return null;
	}

	public String toString() { // return to String ret the not CAPITAL version of the type
		String ret = "";
		switch (this) {
		case GASOLINE:
			ret = "Gasoline";
			break;
		case DIESEL:
			ret = "Diesel";
			break;
		case SCOOTER_FUEL:
			ret = "Scooter Fuel";
		case HOME_HEATING_FUEL:
			ret = "Home Heating Fuel";
			break;
		}
		return ret;
	}

}
