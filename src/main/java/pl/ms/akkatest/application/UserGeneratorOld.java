package pl.ms.akkatest.application;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.ms.akkatest.application.actor.SupervisorActor;
import pl.ms.akkatest.application.message.UserMessage;
import pl.ms.akkatest.domain.User;
import pl.ms.akkatest.domain.UserRepository;
import pl.ms.akkatest.util.SpringExtension;

/*
 * Created by Marcin on 2017-12-05 14:33
 */
//@Component
//@ConditionalOnUserGenerator
public class UserGeneratorOld {

    private static final Logger LOG = LoggerFactory.getLogger(UserGeneratorOld.class);

    private ActorSystem actorSystem;
    private SpringExtension springExtension;
    private UserRepository userRepository;

    @Autowired
    public UserGeneratorOld(ActorSystem actorSystem, SpringExtension springExtension, UserRepository userRepository) {
        this.actorSystem = actorSystem;
        this.springExtension = springExtension;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRate = 5000)
    public void schedule1() {
        ActorRef supervisor = actorSystem.actorOf(springExtension.props(SupervisorActor.BEAN_NAME));
        for (int i = 0; i < 10; i++) {
//            if (i == 5) {
//                supervisor.tell(new ThrowRuntimeExceptionMessage(), null);
//            } else {
//                supervisor.tell(new UserMessage(new User("username_" + i + "_" + RandomStringUtils.randomAlphabetic(10))), null);
//            }
            supervisor.tell(new UserMessage(new User("username_" + i + "_" + RandomStringUtils.randomAlphabetic(10))), null);
        }
        LOG.info("users {}", userRepository.count());
    }

}
