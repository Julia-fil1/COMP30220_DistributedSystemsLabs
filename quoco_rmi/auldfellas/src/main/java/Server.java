import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import auldfellas.AFQService;
import core.BrokerService;
import core.QuotationService;
import core.Constants;

public class Server {
    public static void main(String[] args) {
        QuotationService afqService = new AFQService();
        try {
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
            // Rather than creating a new registry each time we re-use it so that we can later point our service at the existing registry
            Registry registry;
            registry = LocateRegistry.getRegistry(args[0], 1099);

            // Create the Remote Object
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(afqService, 0);

            BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

            // Register the object with the RMI Registry

            brokerService.registerService(Constants.AULD_FELLAS_SERVICE, quotationService);

            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
            e.printStackTrace();
        }
    }
}
