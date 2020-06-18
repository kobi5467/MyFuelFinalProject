package entitys.enums;
/**
 * This class is for Status enums objects and methods
 * @author MyFuel Team
 *
 */
public enum Status {

	//for fuel inventory order
	SENT_TO_CUSTOMER,
	
	//for home heating fuel order
	WAITING,
	
	//for Determining rate request
	WAITING_TO_APPROVE,
	NOT_APPROVED,
	APPROVED,

	//for all order
	DONE;
	
	/**
	 *this method get string and return the corresponding String to the Enum
	 * @param Stat - the string status
	 * @return - status enum
	 */
	public static Status stringToEnumVal(String Stat) {
		if (Stat == null || Stat.trim().equals("")) {
			return null;
		}
		if (Stat.equals("Sended To Customer") || Stat.equals("SENDED TO CUSTOMER"))  {
			return Status.SENT_TO_CUSTOMER;
		}
		if (Stat.equals("Waiting") || Stat.equals("WAITING")) {
			return Status.WAITING;
		}
		if (Stat.equals("Waiting To Approve")|| Stat.equals("WAITING TO APPROVE") ) {
			return Status.WAITING_TO_APPROVE;
		}
		if (Stat.equals("Not Approved")|| Stat.equals("NOT APPROVED") ) {
			return Status.NOT_APPROVED;
		}
		if (Stat.equals("Approved")|| Stat.equals("APPROVED") ) {
			return Status.APPROVED;
		}
		if (Stat.equals("Done")|| Stat.equals("DONE") ) {
			return Status.DONE;
		}

		return null;
	}
	
	/**
	 * this method get string and return the corresponding enum to the string
	 * @param stat - the enum status
	 * @return return- the string status
	 */
	public static String enumToString(Status stat) { // return to String ret the not CAPITAL version of the type
		String ret = "";
		switch (stat) {
		case SENT_TO_CUSTOMER:
			ret = "Sent To Customer";
			break;
		case WAITING:
			ret = "Waiting";
			break;
		case WAITING_TO_APPROVE:
			ret = "Waiting To Approve";
			break;
		case NOT_APPROVED:
			ret = "Waiting To Approve";
			break;
		case APPROVED:
			ret = "Approved";
			break;
		case DONE:
			ret = "Done";
			break;
			
		}
		return ret;
	}
	
}
