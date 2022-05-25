import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.QuotationService;
import dodgydrivers.DDQService;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class DodgydriversUnitTest {
    private static Registry registry;
    private static QuotationService quotationService;
    private static QuotationService ddqService;

    @BeforeClass
    public static void setup() {
        ddqService = new DDQService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(ddqService, 0);
            // Register the object with the RMI Registry
            registry.bind(Constants.DODGY_DRIVERS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService)
                registry.lookup(Constants.DODGY_DRIVERS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotation() throws Exception {
        ClientInfo client = new ClientInfo("Niki Collier", ClientInfo.FEMALE, 43, 0, 5, "PQR254/1");
        QuotationService service = (QuotationService) registry.lookup(Constants.DODGY_DRIVERS_SERVICE);
        assertTrue(service.generateQuotation(client) instanceof Quotation);
    }

    @Test
    public void givenClientWithMoreThanThreePenaltyPointsCorrectDiscountForGeneratedQuotationIsApplied() {
        DDQService ddqService = new DDQService();
        ClientInfo client = new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 6, 0, "COL123/3");
        Quotation quotation = ddqService.generateQuotation(client);
        assertThat(quotation.price, greaterThanOrEqualTo(560.00));
        assertThat(quotation.price, lessThanOrEqualTo(700.00));
    }

    @Test
    public void givenClientWithNoClaimsCorrectDiscountForGeneratedQuotationIsApplied() {
        DDQService ddqService = new DDQService();
        ClientInfo client = new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 6, 3, "COL123/3");
        Quotation quotation = ddqService.generateQuotation(client);
        assertThat(quotation.price, greaterThanOrEqualTo(320.00));
        assertThat(quotation.price, lessThanOrEqualTo(400.00));
    }

    @Test
    public void givenClientReferenceInACorrectFormatIsGenerated() {
        DDQService ddqService = new DDQService();
        ClientInfo client = new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 6, 3, "COL123/3");
        Quotation quotation = ddqService.generateQuotation(client);
        assertThat(quotation.reference, matchesPattern("DD[0-9]{6}$"));
    }

}
