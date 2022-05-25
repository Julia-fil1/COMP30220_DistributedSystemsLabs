import broker.LocalBrokerService;
import core.BrokerService;
import core.ClientInfo;
import core.Constants;
import org.junit.BeforeClass;
import org.junit.Test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import static org.junit.Assert.*;

public class LocalBrokerUnitTest {
    private static Registry registry;
    private static BrokerService brokerService;
    private static LocalBrokerService localBrokerService;

    @BeforeClass
    public static void setup() {
        localBrokerService = new LocalBrokerService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            LocalBrokerService.registry = registry;
            brokerService = (BrokerService)
                    UnicastRemoteObject.exportObject(localBrokerService, 0);
            // Register the object with the RMI Registry
            registry.bind(Constants.BROKER_SERVICE, brokerService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void connectionTest() throws Exception {
        BrokerService service = (BrokerService)
                registry.lookup(Constants.BROKER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void getListTest() throws Exception {
        BrokerService service = (BrokerService) registry.lookup(Constants.BROKER_SERVICE);
        assertTrue(service.getQuotations(new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3")) instanceof List);
        assertTrue(service.getQuotations(new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3")).isEmpty());
    }
}
