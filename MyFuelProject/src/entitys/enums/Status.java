package entitys.enums;

public enum Status {

	//for fuel inventory order
	WATING_FOR_APPROVE,
	SENDED_TO_CUSTOMER,
	
	//for home heating fuel order
	WAITING,
	
	//for Determining rate request
	WAITING_TO_APPROVE,
	NOT_APPROVED,
	APPROVED,

	//for all order
	DONE;
	
	public static Status stringToEnumVal(String Stat) {
		if (Stat == null || Stat.trim().equals("")) {
			return null;
		}
		if (Stat.equals("Waiting For Approve") || Stat.equals("WATING FOR APPROVE")) {
			return Status.WATING_FOR_APPROVE;
		}
		if (Stat.equals("Sended To Customer") || Stat.equals("SENDED TO CUSTOMER"))  {
			return Status.SENDED_TO_CUSTOMER;
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
	
	public static String enumToString(Status stat) { // return to String ret the not CAPITAL version of the type
		String ret = "";
		switch (stat) {
		case WATING_FOR_APPROVE:
			ret = "Waiting For Approve";
			break;
		case SENDED_TO_CUSTOMER:
			ret = "Sended To Customer";
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
