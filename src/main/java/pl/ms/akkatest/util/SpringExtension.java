package pl.ms.akkatest.util;

import akka.actor.Extension;
import akka.actor.Props;
import akka.pattern.Backoff;
import akka.pattern.BackoffSupervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/*
 * Created by Marcin on 2017-10-31 13:05
 */
@Component
public class SpringExtension implements Extension {

    private ApplicationContext applicationContext;

    @Autowired
    public SpringExtension(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Props props(String actorBeanName) {
        Props childProps = Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
        Props supervisorProps = BackoffSupervisor.props(
                Backoff.onFailure(
                        childProps,
                        actorBeanName,
                        Duration.create(3, TimeUnit.SECONDS),
                        Duration.create(30, TimeUnit.SECONDS),
                        0.2)
//                        .withAutoReset(Duration.create(10, TimeUnit.SECONDS))
//                        .withSupervisorStrategy(new OneForOneStrategy(
//                                0,
//                                Duration.create(0, TimeUnit.SECONDS),
//                                new Function<Throwable, SupervisorStrategy.Directive>() {
//                                    @Override
//                                    public SupervisorStrategy.Directive apply(Throwable t) {
//                                        if (t instanceof Error) {
//                                            return OneForOneStrategy.escalate();
//                                        } else if (t instanceof RuntimeException) {
//                                            return OneForOneStrategy.restart();
//                                        } else {
//                                            return OneForOneStrategy.restart();
//                                        }
//                                    }
//                                }))
        );

        return childProps;
    }

    public Props props(String actorBeanName, Object... args) {
        return Props.create(SpringActorProducer.class, applicationContext, actorBeanName, args);
    }

}
