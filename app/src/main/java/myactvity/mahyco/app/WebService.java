package myactvity.mahyco.app;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WebService {
     private static String NAMESPACE = "http://tempuri.org/";
    private static String URL = "https://cmr.mahyco.com/FormerApp.asmx";
    private static String SOAP_ACTION = "http://tempuri.org/";
    private static String MDOurlpath="https://cmr.mahyco.com/Service.asmx?WSDL";

   public WebService() {
    }

    public static String invokeHelloWorldWS(String name, String pagename, String webMethName) {
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

       // Add the property to request object
     //   request.addProperty("lang",name);
        request.addProperty("gr_code","602280");

       // request.addProperty("pagename",pagename);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL,700000);
       // androidHttpTransport.debug = true;
       // androidHttpTransport.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        Object response=null;
        try {
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
           // androidHttpTransport.setTimeout(70000);
           // SoapPrimitive response1 = (SoapPrimitive) envelope.getResponse();
           // response = envelope.getResponse();
           // SoapObject result=(SoapObject)envelope.bodyIn; //get response
            resTxt =response.toString();

        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = e.toString();
        }
        //Return resTxt to calling object
        return resTxt;
    }
    public  String MDOMAsterdata( String pagename, String webMethName) {
        String resTxt = null;
        // Create request
        SoapObject request = new SoapObject(NAMESPACE, webMethName);

        // Add the property to request object
        //   request.addProperty("lang",name);
        request.addProperty("username","602280");
        request.addProperty("sapcode","2");
        request.addProperty("password","1");

        // request.addProperty("pagename",pagename);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(MDOurlpath,1000*60*2);
        androidHttpTransport.setTimeout(1000*60*2);

        // androidHttpTransport.debug = true;
        // androidHttpTransport.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        Object response=null;
        try {
            androidHttpTransport.call(SOAP_ACTION+webMethName, envelope);
            // androidHttpTransport.setTimeout(70000);
            // SoapPrimitive response1 = (SoapPrimitive) envelope.getResponse();
            // response = envelope.getResponse();
            // SoapObject result=(SoapObject)envelope.bodyIn; //get response
            resTxt =response.toString();

            if (envelope.bodyIn instanceof SoapFault)

            {

                resTxt = ((SoapFault) envelope.bodyIn).faultstring;

            }
            else
            {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                resTxt=resultsRequestSOAP.toString();
            }

        } catch (Exception e) {
            //Print error
            e.printStackTrace();
            //Assign error message to resTxt
            resTxt = e.toString();



        }
        //Return resTxt to calling object
        return resTxt;
    }
}