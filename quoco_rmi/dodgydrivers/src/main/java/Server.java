import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;

import core.BrokerService;
import dodgydrivers.DDQService;

import core.QuotationService;
import core.Constants;

public class Server {
    public static void main(String[] args) {
        QuotationService ddqService = new DDQService();
        try {
            // Connect to the RMI Registry - creating the registry will be the
            // responsibility of the broker.
            // Rather than creating a new registry each time we re-use it so that we can later point our service at the existing registry
            Registry registry;
            registry = LocateRegistry.getRegistry(args[0], 1099);

            // Create the Remote Object
            QuotationService quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(ddqService, 0);

            BrokerService brokerService = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);

            // Register the object with the RMI Registry
            //registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService);
            brokerService.registerService(Constants.DODGY_DRIVERS_SERVICE, quotationService);

            System.out.println("STOPPING SERVER SHUTDOWN");
            while (true) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }
} 