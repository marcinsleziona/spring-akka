package pl.ms.akkatest.application.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.routing.ActorRefRoutee;
import akka.routing.Routee;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.ms.akkatest.util.SpringExtension;
import pl.ms.akkatest.application.message.UserMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Created by Marcin on 2017-10-31 13:12
 */
@Component
@Scope("prototype")
public class SupervisorActor extends AbstractActor {

    public static final String BEAN_NAME = "supervisorActor";

    @Autowired
    private SpringExtension springExtension;

    private Router router;

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        super.preRestart(reason, message);
    }

    @Override
    public void preStart() throws Exception {
        List<Routee> routees = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ActorRef actor = getContext().actorOf(springExtension.props(UserActor.BEAN_NAME));
            getContext().watch(actor);
            routees.add(new ActorRefRoutee(actor));
        }
        router = new Router(new SmallestMailboxRoutingLogic(), routees);
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserMessage.class, userMessage -> {
                    router.route(userMessage, getSender());
                })
                .matchAny(any -> {
                    router.route(any, getSender());
                })
//                .match(PoisonPill.class, poisonPill -> {
//
//                })
                .build();
    }

    @Override
    public void postStop() throws Exception {
        super.postStop();
    }

}
