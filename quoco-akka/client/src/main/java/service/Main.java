package service;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import service.actor.Client;
import service.core.ClientInfo;
import service.messages.ApplicationRequest;

public class Main {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef ref = system.actorOf(Props.create(Client.class), "client");
        //ref.tell(new Init(new AFQService()), null);
        ActorSelection selection =
                system.actorSelection("akka.tcp://default@127.0.0.1:2551/user/broker");
        for(ClientInfo clientInfo : clients) {
            selection.tell(new ApplicationRequest(clientInfo), ref);
        }
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
    };
}
