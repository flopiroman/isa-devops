package utils;

import org.json.JSONException;
import org.json.JSONObject;
import exceptions.ExternalPartnerException;
import org.apache.cxf.jaxrs.client.WebClient;

import javax.ws.rs.core.MediaType;


public class BankAPI {

	private String url;

	public BankAPI(String host, String port) {
		this.url = "http://" + host + ":" + port;
	}

	public BankAPI() { this("localhost", "9090"); }

	public boolean performPayment(String hello) throws ExternalPartnerException, JSONException {
		// Building payment request
		JSONObject request = new JSONObject().put("Message", "hello");

		// Sending a Payment request to the mailbox
		Integer id;
		try {
			String str = WebClient.create(url).path("/mailbox")
					.accept(MediaType.APPLICATION_JSON_TYPE).header("Content-Type", MediaType.APPLICATION_JSON)
					.post(request.toString(), String.class);
			id = Integer.parseInt(str);
		} catch (Exception e) {
			throw new ExternalPartnerException(url+"/mailbox", e);
		}

		// Retrieving the payment status
		JSONObject payment;
		try {
			String response = WebClient.create(url).path("/payments/" + id).get(String.class);
			payment = new JSONObject(response);
		} catch (Exception e) {
			throw new ExternalPartnerException(url + "payments/" + id, e);
		}
		// Assessing the payment status
		return (payment.getInt("Status") == 0);
	}

}