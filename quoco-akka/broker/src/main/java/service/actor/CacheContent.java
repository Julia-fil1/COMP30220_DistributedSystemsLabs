package service.actor;

import akka.actor.ActorRef;
import service.messages.ApplicationResponse;

public class CacheContent {
    private ActorRef ref;
    private ApplicationResponse response;

    public CacheContent() {
    }

    public CacheContent(ActorRef ref, ApplicationResponse response) {
        this.ref = ref;
        this.response = response;
    }

    public ActorRef getRef() {
        return ref;
    }

    public void setRef(ActorRef ref) {
        this.ref = ref;
    }

    public ApplicationResponse getResponse() {
        return response;
    }

    public void setResponse(ApplicationResponse response) {
        this.response = response;
    }
}
