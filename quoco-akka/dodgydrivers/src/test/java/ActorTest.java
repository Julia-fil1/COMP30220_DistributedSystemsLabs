import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import service.actor.Quoter;
import service.core.ClientInfo;
import service.dodgydrivers.DDQService;
import service.message.Init;
import service.messages.QuotationRequest;
import service.messages.QuotationResponse;

public class ActorTest {
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }


    @Test
    public void testQuoter() {
        ActorRef quoterRef = system.actorOf(Props.create(Quoter.class), "test");
        TestKit probe = new TestKit(system);
        quoterRef.tell(new Init(new DDQService()), null);
        quoterRef.tell(new QuotationRequest(1,
                        new ClientInfo("Niki Collier", ClientInfo.getFEMALE(), 43, 0, 5, "PQR254/1")),
                probe.getRef());
        probe.awaitCond(probe::msgAvailable);
        probe.expectMsgClass(QuotationResponse.class);
    }
}
