package services.helpers.com.idfbins.routingnumber;

import services.enums.Broker;
import services.services.com.idfbins.routingnumber.RoutingNumber;
import services.utils.JSONUtil;

public class RoutingNumberHelper {
	
	private Broker broker;

	public RoutingNumberHelper(Broker broker) {
		this.broker = broker;
	}
	
	public RoutingNumber validateRoutingNumber(String routingNumber) throws Exception {

        String urlToTest = "http://" + this.broker.getMBHost() + ":7080/routingNumber/fetch?routingNumber=" + routingNumber;
		String jsonResponse = JSONUtil.getURLResponseAsString(urlToTest);
		RoutingNumber myRoutingNumObj = (RoutingNumber) JSONUtil.convertJsonResponseToObj(jsonResponse, RoutingNumber.class);
		
		return myRoutingNumObj;
	}
	
}
