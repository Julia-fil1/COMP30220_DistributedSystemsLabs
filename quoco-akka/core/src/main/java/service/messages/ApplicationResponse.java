package service.messages;

import service.core.ClientInfo;
import service.core.Quotation;

import java.util.ArrayList;
import java.util.List;

public class ApplicationResponse implements MySerializable{
    private ClientInfo clientInfo;
    private List<Quotation> quotations;

    public ApplicationResponse() {
    }

    public ApplicationResponse(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
        this.quotations = new ArrayList<>();
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }
}
