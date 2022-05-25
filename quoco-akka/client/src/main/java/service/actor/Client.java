package service.actor;

import akka.actor.AbstractActor;
import service.core.ClientInfo;
import service.core.Quotation;
import service.messages.ApplicationRequest;
import service.messages.ApplicationResponse;
import service.messages.QuotationRequest;

import java.text.NumberFormat;

public class Client extends AbstractActor {
    private int SEED_ID = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApplicationResponse.class,
                        msg -> {
                            //receive and output contents of msg
                            displayProfile(msg.getClientInfo());
                            for (Quotation quotation : msg.getQuotations())
                                displayQuotation(quotation);
                            System.out.println("\n");
                        })
                .match(ApplicationRequest.class,
                        msg -> {
                            for (ClientInfo clientInfo : clients) {
                                QuotationRequest quotationRequest =
                                        new QuotationRequest(SEED_ID++, clientInfo);
                                //send request messages to the broker
                                getSender().tell(quotationRequest, getSelf());
                            }


                        })
                .build();
    }

    /**
     * Display the client info nicely.
     *
     * @param info
     */
    public static void displayProfile(ClientInfo info) {
        System.out.println("|=================================================================================================================|");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println(
                "| Name: " + String.format("%1$-29s", info.getName()) +
                        " | Gender: " + String.format("%1$-27s", (info.getGender() == ClientInfo.getMALE() ? "Male" : "Female")) +
                        " | Age: " + String.format("%1$-30s", info.getAge()) + " |");
        System.out.println(
                "| License Number: " + String.format("%1$-19s", info.getLicenseNumber()) +
                        " | No Claims: " + String.format("%1$-24s", info.getNoClaims() + " years") +
                        " | Penalty Points: " + String.format("%1$-19s", info.getPoints()) + " |");
        System.out.println("|                                     |                                     |                                     |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Display a quotation nicely - note that the assumption is that the quotation will follow
     * immediately after the profile (so the top of the quotation box is missing).
     *
     * @param quotation
     */
    public static void displayQuotation(Quotation quotation) {
        System.out.println(
                "| Company: " + String.format("%1$-26s", quotation.getCompany()) +
                        " | Reference: " + String.format("%1$-24s", quotation.getReference()) +
                        " | Price: " + String.format("%1$-28s", NumberFormat.getCurrencyInstance().format(quotation.getPrice())) + " |");
        System.out.println("|=================================================================================================================|");
    }

    /**
     * Test Data
     */
    public static final ClientInfo[] clients = {
            new ClientInfo("Niki Collier", ClientInfo.getFEMALE(), 43, 0, 5, "PQR254/1"),
            new ClientInfo("Old Geeza", ClientInfo.getMALE(), 65, 0, 2, "ABC123/4"),
            new ClientInfo("Hannah Montana", ClientInfo.getFEMALE(), 16, 10, 0, "HMA304/9"),
            new ClientInfo("Rem Collier", ClientInfo.getMALE(), 44, 5, 3, "COL123/3"),
            new ClientInfo("Jim Quinn", ClientInfo.getMALE(), 55, 4, 7, "QUN987/4"),
            new ClientInfo("Donald Duck", ClientInfo.getMALE(), 35, 5, 2, "XYZ567/9")
    };}
