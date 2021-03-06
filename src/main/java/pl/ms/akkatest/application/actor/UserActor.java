package pl.ms.akkatest.application.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.SupervisorStrategy;
import akka.cluster.pubsub.DistributedPubSub;
import akka.cluster.pubsub.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import pl.ms.akkatest.application.message.StopMeMessage;
import pl.ms.akkatest.application.message.ThrowRuntimeExceptionMessage;
import pl.ms.akkatest.application.message.UserMessage;
import pl.ms.akkatest.domain.UserRepository;
import pl.ms.akkatest.util.ApplicationContextProvider;
import scala.concurrent.duration.Duration;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.restart;

/*
 * Created by Marcin on 2017-10-31 13:09
 */
public class UserActor extends AbstractActor {

    public static final String BEAN_NAME = "userActor";
    private LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public UserActor() {
        ActorRef mediator = DistributedPubSub.get(getContext().system()).mediator();

        // register to the path
        mediator.tell(new DistributedPubSubMediator.Put(getSelf()), getSelf());
    }

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(5, Duration.create(1, TimeUnit.MINUTES), DeciderBuilder.
                    match(RuntimeException.class, e -> restart()).
                    matchAny(o -> escalate()).build());

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }

    @Override
    public void preRestart(Throwable reason, Optional<Object> message) throws Exception {
        log.info("pre restart");
        super.preRestart(reason, message);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        super.postRestart(reason);
        log.info("post restart");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(UserMessage.class, userMessage -> {
                    getUserRepository().save(userMessage.getUser());
                    log.info("UserMessage (" + userMessage.getUser() + ") in (" + this + ")");
                })
                .match(ThrowRuntimeExceptionMessage.class, throwRuntimeExceptionMessage -> {
                    System.out.println("Throw exception from (" + this + ") !");
                    throw new RuntimeException("Exception (" + this + ") !");
                })
                .match(StopMeMessage.class, stopMeMessage -> {
                    System.out.println("StopMe (" + this + ") !");
                    getContext().stop(getSelf());
                })
                .build();
    }

    UserRepository getUserRepository() {
        return ApplicationContextProvider.getManagedBean(UserRepository.class);
    }
}
