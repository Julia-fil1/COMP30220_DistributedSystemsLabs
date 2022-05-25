package service.core;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */
@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class Broker implements ServiceListener {
    private static TreeSet<String> urls = new TreeSet<String>();

    @WebMethod
    public List<Quotation> getQuotations(ClientInfo info) throws MalformedURLException {
        LinkedList<Quotation> quotations = new LinkedList<Quotation>();
        for (String url : urls) {
            URL wsdlUrl = new URL(url);
            QName serviceName = new QName("http://core.service/", "QuoterService");
            Service service = Service.create(wsdlUrl, serviceName);

            QName portName = new QName("http://core.service/", "QuoterPort");
            QuoterService quoterService = service.getPort(portName, QuoterService.class);
            quotations.add(quoterService.generateQuotation(info));
        }

        return quotations;
    }

    public static void main(String[] args) {
        try {
            // Create a JmDNS instance
            JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost());
            // Add a service listener
            Broker broker = new Broker();
            jmdns.addServiceListener("_http._tcp.local.", broker);
            Endpoint.publish("http://0.0.0.0:9000/quotation", broker);
            // Wait
            Thread.sleep(30000);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serviceAdded(ServiceEvent serviceEvent) {
        System.out.println("Service added: " + serviceEvent.getInfo());
    }

    @Override
    public void serviceRemoved(ServiceEvent serviceEvent) {
        System.out.println("Service removed: " + serviceEvent.getInfo());
    }

    @Override
    public void serviceResolved(ServiceEvent serviceEvent) {
        System.out.println("Service resolved: " + serviceEvent.getInfo());
        String url = serviceEvent.getInfo().getPropertyString("path");
        if (url != null) {
            try {
                urls.add(url);
            } catch (Exception e) {
                System.out.println("Problem with service: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}