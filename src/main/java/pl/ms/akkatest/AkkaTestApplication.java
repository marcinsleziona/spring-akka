package pl.ms.akkatest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.pubsub.DistributedPubSub;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.ms.akkatest.application.actor.UserActor;
import pl.ms.akkatest.application.cluster.SimpleClusterListener;
import pl.ms.akkatest.util.SpringExtension;

/*
 * Created by Marcin on 2017-10-31 12:52
 */
@SpringBootApplication
@EnableScheduling
public class AkkaTestApplication {

    @Value("${akka.actor-system}")
    private String actorSystem;

    @Value("${akka.remote.netty.tcp.hostname}")
    private String hostname;
    @Value("${akka.remote.netty.tcp.port}")
    private int port;

    public static void main(String[] args) {
        SpringApplication.run(AkkaTestApplication.class, args);
    }

    @Bean
    public ActorSystem actorSystem(Config akkaConfiguration) {
        ActorSystem system = ActorSystem.create(actorSystem, akkaConfiguration);

        system.actorOf(Props.create(UserActor.class), UserActor.BEAN_NAME);

        return system;
    }

    @Bean
    public Config akkaConfiguration() {
//        return ConfigFactory.parseString(
//                "akka.remote.netty.tcp.port=" + port).withFallback(
//                ConfigFactory.load());

        return ConfigFactory.parseString(
                "akka {\n" +
                    "  actor {\n" +
                    "    provider = \"cluster\"\n" +
                    "  }\n" +
                    "\n" +
                    "  remote {\n" +
                    "    log-remote-lifecycle-events = off\n" +
                    "    netty.tcp {\n" +
                    "      hostname = \""+hostname+"\"\n" +
                    "      port = "+port+"\n" +
                    "    }\n" +
                    "  }\n" +
                    "  cluster {\n" +
                    "      seed-nodes = [\n" +
                    "        \"akka.tcp://"+actorSystem+"@localhost:2551\",\n" +
                    "        \"akka.tcp://"+actorSystem+"@localhost:2552\"\n" +
                    "      ]\n" +
                    "\n" +
                    "      auto-down-unreachable-after = 300s\n" +
                    "    }\n" +
                    "}").withFallback(ConfigFactory.load());
    }

    @Bean
    public ActorRef simpleClusterListener(ActorSystem actorSystem) {
        return actorSystem.actorOf(Props.create(SimpleClusterListener.class), "simpleClusterListener");
    }

//    @Bean
//    public ActorRef mediator(ActorSystem actorSystem) {
//        return DistributedPubSub.get(actorSystem).mediator();
//    }

}
