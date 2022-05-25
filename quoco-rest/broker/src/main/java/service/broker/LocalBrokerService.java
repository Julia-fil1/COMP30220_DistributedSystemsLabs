package service.broker;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.core.ClientApplication;
import service.core.ClientInfo;
import service.core.NoSuchQuotationException;
import service.core.Quotation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


@RestController
public class LocalBrokerService {
    //private final String[] urls = {"http://localhost:8081/quotations", "http://localhost:8082/quotations", "http://localhost:8083/quotations"};
    private final String[] urls = {"http://auldfellas:8081/quotations", "http://dodgydrivers:8082/quotations", "http://girlpower:8083/quotations"};
    private Map<Integer, ClientApplication> clientApplicationMap = new HashMap<>();

    private int applicationNumber = 0;

    @RequestMapping(value = "/applications", method = RequestMethod.POST)
    public ResponseEntity<ClientApplication> createApplication(@RequestBody ClientInfo clientInfo) {
		List<Quotation> quotations = getQuotations(clientInfo);
        HttpHeaders httpHeaders = new HttpHeaders();

		ClientApplication clientApplication = new ClientApplication(applicationNumber++, clientInfo, quotations);
        clientApplicationMap.put(applicationNumber, clientApplication);

        String path = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + "/quotations/" + applicationNumber;
        try {
            httpHeaders.setLocation(new URI(path));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(clientApplication, httpHeaders, HttpStatus.CREATED);
    }

    public List<Quotation> getQuotations(ClientInfo info) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ClientInfo> request = new HttpEntity<>(info);
        List<Quotation> quotationsList = new LinkedList<>();

        for (String url : urls) {
            Quotation quotation = restTemplate.postForObject(url, request, Quotation.class);
            quotationsList.add(quotation);
        }

        return quotationsList;
    }

    @RequestMapping(value = "/applications/{application-number}", method = RequestMethod.GET)
    public ClientApplication getApplication(@PathVariable("application-number") int appNumber) {
        ClientApplication clientApplication = clientApplicationMap.get(appNumber);
        if (clientApplication == null) throw new NoSuchQuotationException();
        return clientApplication;
    }

    @RequestMapping(value = "/applications", method = RequestMethod.GET)
    public List<ClientApplication> applicationList() {
        return new ArrayList<>(clientApplicationMap.values());
    }

}
