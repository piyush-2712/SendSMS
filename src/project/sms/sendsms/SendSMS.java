package project.sms.sendsms;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class SendSMS {
	 
	private static RestTemplate restTemplate;
	public static void main(String args[]){
		
		try{
		SendSMS obj = new SendSMS();
		obj.sendMessage("9560031788", "5163339", "hello", "http://10.5.176.22:55000/", "fhecaf", "fhecaf2");
		}
		catch(Exception es){
			es.printStackTrace();
		}
		
	}
	
	 private RestTemplate getRestTemplateInstance() {
		    if (null == restTemplate) {
		      synchronized (this) {
		        if (null == restTemplate) {
		          restTemplate = new RestTemplate();
		        }
		      }
		    }
		    return restTemplate;
		  }

	 
	 public boolean sendMessage(String msisdn, String shortCode, String smsText,
		      String messageBrokerurl,String messageBrokerusername,
		      String messageBrokerPassword) throws Exception {
		    String methodName = "sendMessage";
		    System.out.println(methodName  +  "starts");

		    System.out.println(methodName + " url" + messageBrokerurl);
		    System.out.println(methodName + " username" + messageBrokerusername);
		    System.out.println(methodName + " password" + messageBrokerPassword);
		    System.out.println(methodName + " shortCode" + shortCode);


		    Map<String, Object> mapRequest = new HashMap<String, Object>();
		    mapRequest.put("msisdn", msisdn);
		    mapRequest.put("shortCode", shortCode);
		    mapRequest.put("smsText", smsText);

		    String userPas = messageBrokerusername  +  ":"  +  messageBrokerPassword;
		    byte[] credentialString = userPas.getBytes();
		    String encodedString = DatatypeConverter.printBase64Binary(credentialString);
		    HttpHeaders header = new HttpHeaders();
		    System.out.println("encoded string" + encodedString);
		    header.add("Authorization", "Basic " + encodedString);
		    header.add("Content-Type", "text/xml");

		    StringBuilder myRequest = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?> <message>"
		         + "<sms type=\"mt\">"
		         + "<destination>"
		         + "  <address>"
		         + "  <number type=\"national\">" + msisdn + "</number>"
		         + "</address>"
		         + "</destination>"
		         + "       <source>"
		         + "         <address>"
		         + "         <alphanumeric>" + shortCode + "</alphanumeric>"  
		        + "       </address>" 
		        + "     </source>" 
		        + "    <rsr type=\"al\"/>" 
		        + "     <ud type='text' encoding='default'>" + smsText + "</ud>" 
		        + "   </sms>" 
		        + "  </message>");



		    System.out.println(methodName + "Request :: " + myRequest.toString());

		    HttpEntity<String> entity = new HttpEntity<String>(myRequest.toString(), header);
		    String responseString = getRestTemplateInstance().postForObject(
		        messageBrokerurl, entity, String.class);
		    System.out.println(methodName + "*****responseString" + responseString);
		    if (responseString.equalsIgnoreCase("ok")) {
		      System.out.println(methodName + "ok");
		      return true;
		    }

		    return false;
		    
		    //return true;
		  }
	 
	 
	 
}



