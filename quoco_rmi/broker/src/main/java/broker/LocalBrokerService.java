package broker;

import core.*;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the broker service that uses the Service Registry.
 *
 * @author Rem
 */
public class LocalBrokerService implements BrokerService {

    public static Registry registry;

    public List<Quotation> getQuotations(ClientInfo info) throws RemoteException {
        List<Quotation> quotations = new LinkedList<Quotation>();

        for (String name : registry.list()) {
            if (name.startsWith("qs-")) {
                QuotationService service;
                try {
                    service = (QuotationService) registry.lookup(name);
                    quotations.add(service.generateQuotation(info));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return quotations;
    }

    @Override
    public void registerService(String name, QuotationService service) throws AlreadyBoundException, RemoteException {
        registry.bind(name, service);
    }
}
