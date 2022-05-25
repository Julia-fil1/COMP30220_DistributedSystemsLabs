import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import core.ClientInfo;
import core.Constants;
import core.Quotation;
import core.QuotationService;
import auldfellas.AFQService;
import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class AuldfellasUnitTest {
    private static Registry registry;
    private static QuotationService quotationService;
    private static AFQService afqService;

    @BeforeClass
    public static void setup() {
        afqService = new AFQService();
        try {
            registry = LocateRegistry.createRegistry(1099);
            quotationService = (QuotationService)
                    UnicastRemoteObject.exportObject(afqService, 0);
            // Register the object with the RMI Registry
            registry.bind(Constants.AULD_FELLAS_SERVICE, quotationService);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
    }

    @Test
    public void connectionTest() throws Exception {
        QuotationService service = (QuotationService)
                registry.lookup(Constants.AULD_FELLAS_SERVICE);
        assertNotNull(service);
    }

    @Test
    public void generateQuotation() throws Exception {
        ClientInfo client = new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 5, 3, "COL123/3");
        QuotationService service = (QuotationService) registry.lookup(Constants.AULD_FELLAS_SERVICE);
        assertTrue(service.generateQuotation(client) instanceof Quotation);
    }

    @Test
    public void givenMaleClientCorrectDiscountForGeneratedQuotationIsApplied() {
        AFQService afqService = new AFQService();
        ClientInfo client = new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 6, 3, "COL123/3");
        Quotation quotation = afqService.generateQuotation(client);
        assertThat(quotation.price, greaterThanOrEqualTo(420.00));
        assertThat(quotation.price, lessThanOrEqualTo(840.00));
    }

    @Test
    public void givenClientOverSixtyCorrectDiscountForGeneratedQuotationIsApplied() {
        AFQService afqService = new AFQService();
        ClientInfo client = new ClientInfo("Rem Collier", ClientInfo.MALE, 64, 6, 3, "COL123/3");
        Quotation quotation = afqService.generateQuotation(client);
        assertThat(quotation.price, greaterThanOrEqualTo(372.00));
        assertThat(quotation.price, lessThanOrEqualTo(816.00));
    }

    @Test
    public void givenClientWithLessThanThreePenaltyPointsCorrectDiscountForGeneratedQuotationIsApplied() {
        AFQService afqService = new AFQService();
        ClientInfo maleClient = new ClientInfo("Rem Collier", ClientInfo.MALE, 64, 1, 3, "COL123/3");
        Quotation quotation = afqService.generateQuotation(maleClient);
        assertThat(quotation.price, greaterThanOrEqualTo(252.00));
        assertThat(quotation.price, lessThanOrEqualTo(504.00));
    }

    @Test
    public void givenClientReferenceInACorrectFormatIsGenerated() {
        AFQService afqService = new AFQService();
        ClientInfo client = new ClientInfo("Rem Collier", ClientInfo.MALE, 44, 6, 3, "COL123/3");
        Quotation quotation = afqService.generateQuotation(client);
        assertThat(quotation.reference, matchesPattern("AF[0-9]{6}$"));
    }

}
