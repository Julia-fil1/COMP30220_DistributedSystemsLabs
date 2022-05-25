package service.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import scala.concurrent.duration.Duration;
import service.messages.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Broker extends AbstractActor {
    List<ActorRef> actorRefs = new ArrayList<>();
    Map<Integer, CacheContent> cache = new HashMap<>();
    int SEED_ID = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class,
                        msg -> {
                            if (!msg.equals("register")) return;
                            actorRefs.add(getSender());
                            System.out.println("Actors: " + actorRefs.toString());
                            System.out.println("REGISTRATION WORKING");

                        })
                .match(ApplicationRequest.class,
                        msg -> {
                            System.out.println("received application: " );
                            QuotationRequest quotationRequest = new QuotationRequest(SEED_ID, msg.getClientInfo());
                            cache.put(quotationRequest.getId(), new CacheContent(getSender(), new ApplicationResponse(quotationRequest.getClientInfo())));
                            for (ActorRef ref : actorRefs) {
                                ref.tell(quotationRequest, getSelf());
                            }

                            getContext().system().scheduler().scheduleOnce(
                                    Duration.create(2, TimeUnit.SECONDS),
                                    getSelf(),
                                    new RequestDeadline(SEED_ID++),
                                    getContext().dispatcher(), null);
                        })
                .match(QuotationResponse.class,
                        msg -> {
                            cache.get(msg.getId()).getResponse().getQuotations().add( msg.getQuotation());
                        })

                .match(RequestDeadline.class,
                        msg -> {
                    CacheContent content = cache.get(msg.getSEED_ID());
                    content.getRef().tell(content.getResponse(), getSelf());
                    })
                .build();

    }
}
