package service.broker;

/**
* Out of the scope of the lab, done as an additional exercise
* */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lombok.val;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.Quotation;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT) //required for the testRestTemplate, allows the application to actually be deployed in an embedded Servlet container
public class LocalBrokerServiceTest {

    private TestRestTemplate testRestTemplate;
    private ClientInfo clientInfo;
    private Quotation quotationAF;
    private Quotation quotationDD;
    private Quotation quotationGP;
    private List<Quotation> quotationsList;
    private LocalBrokerService mockLocalBrokerService;
    private ClientApplication clientApplication;
    private Map<Integer, ClientApplication> clientApplicationMap;

    @BeforeEach
    public void setup() {
        testRestTemplate = new TestRestTemplate();
        mockLocalBrokerService = mock(LocalBrokerService.class);
        clientInfo = new ClientInfo("Niki Collier", ClientInfo.getFEMALE(), 43, 0, 5, "PQR254/1");
        quotationAF = new Quotation("Auld Fellas Ltd.", "AF001000", 786.40);
        quotationDD = new Quotation("Dodgy Drivers Corp.", "DD001000", 882.00);
        quotationGP = new Quotation("Girl Power Inc.", "GP001000", 41.10);
        quotationsList = new LinkedList<>();
        quotationsList.add(quotationAF);
        quotationsList.add(quotationDD);
        quotationsList.add(quotationGP);
        clientApplication = new ClientApplication(1, new ClientInfo("Niki Collier", ClientInfo.getFEMALE(), 43, 0, 5, "PQR254/1"), quotationsList);
        clientApplicationMap = new HashMap<>();
        clientApplicationMap.put(1, clientApplication);
    }

    @Test
    public void givenClientInfoShouldReturnAllQuotations() {
        when(mockLocalBrokerService.getQuotations(clientInfo)).thenReturn(quotationsList);
        String expected = "[Quotation{company='Auld Fellas Ltd.', reference='AF001000', price=786.4}, Quotation{company='Dodgy Drivers Corp.', reference='DD001000', price=882.0}, Quotation{company='Girl Power Inc.', reference='GP001000', price=41.1}]";
        assertEquals(expected, mockLocalBrokerService.getQuotations(clientInfo).toString());
    }

    @Test
    // attempt at an integration test, used for testing before docker containerization, services still used the localhost urls and were invoked with the mvn spring-boot:run command
    // for testing I ran the quotations services, then I ran the broker, made a post request to the /applications endpoint, meaning the applicationList would have contained the values from clientApplicationMap
    public void shouldReturnAllApplications() {
        List<ClientApplication> applicationList = new ArrayList<>(clientApplicationMap.values());
        when(mockLocalBrokerService.applicationList()).thenReturn(applicationList);
        val reply = testRestTemplate.exchange("http://localhost:8080/applications", HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });

        assertEquals(OK, reply.getStatusCode());
        assertNotNull(reply.getBody());

    }
}
