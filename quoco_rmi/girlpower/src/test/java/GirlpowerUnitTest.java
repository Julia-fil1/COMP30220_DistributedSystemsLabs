import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.QuotationService;
import girlpower.GPQService;
import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class GirlpowerUnitTest {
    private static Registry registry;
    private static QuotationService quotationService;
    private static QuotationService gpqService;

    @BeforeClass
    public static void setup() {
         gpqService = new GPQService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(gpqService, 0);
            // Register the object with the RMI Registry
            registry.bind(Constants.GIRL_POWER_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService)
                registry.lookup(Constants.GIRL_POWER_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotation() throws Exception {
        ClientInfo client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1");
        QuotationService service = (QuotationService) registry.lookup(Constants.GIRL_POWER_SERVICE);
        assertTrue(service.generateQuotation(client) instanceof Quotation);
    }

    @Test
    public void givenFemaleClientCorrectDiscountForGeneratedQuotationIsApplied() {
        GPQService gpqService = new GPQService();
        ClientInfo client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 4, 0, "PQR254/1");
        Quotation quotation = gpqService.generateQuotation(client);
        assertThat(quotation.price, greaterThanOrEqualTo(300.00));
        assertThat(quotation.price, lessThanOrEqualTo(500.00));
    }

    @Test
    public void givenClientReferenceInACorrectFormatIsGenerated() {
        GPQService gpqService = new GPQService();
        ClientInfo client = new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 6, 3, "COL123/3");
        Quotation quotation = gpqService.generateQuotation(client);
        assertThat(quotation.reference, matchesPattern("GP[0-9]{6}$"));
    }

} 