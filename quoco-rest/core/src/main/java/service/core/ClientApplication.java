package service.core;

import java.io.Serializable;
import java.util.List;

public class ClientApplication implements Serializable {
    private int applicationNumber;
    private ClientInfo info;
    private List<Quotation> quotations;

    public ClientApplication() {
    }

    public ClientApplication(int applicationNumber, ClientInfo info, List<Quotation> quotations) {
        this.applicationNumber = applicationNumber;
        this.info = info;
        this.quotations = quotations;
    }

    public int getApplicationNumber() {
        return applicationNumber;
    }

    public void setApplicationNumber(int applicationNumber) {
        this.applicationNumber = applicationNumber;
    }

    public ClientInfo getInfo() {
        return info;
    }

    public void setInfo(ClientInfo info) {
        this.info = info;
    }

    public List<Quotation> getQuotations() {
        return quotations;
    }

    public void setQuotations(List<Quotation> quotations) {
        this.quotations = quotations;
    }


}
