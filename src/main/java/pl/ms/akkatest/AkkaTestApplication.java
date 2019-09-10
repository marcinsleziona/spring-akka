package pl.ms.akkatest;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.ms.akkatest.application.actor.UserActor;
import pl.ms.akkatest.application.cluster.SimpleClusterListener;

import java.util.List;

/*
 * Created by Marcin on 2017-10-31 12:52
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties
public class AkkaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AkkaTestApplication.class, args);
    }

    @Bean
    public ActorSystem actorSystem(AkkaProperties akkaProperties, Config akkaConfiguration) {
        ActorSystem system = ActorSystem.create(akkaProperties.getActorSystem(), akkaConfiguration);
        system.actorOf(Props.create(UserActor.class), UserActor.BEAN_NAME);
        return system;
    }

    @Bean
    public Config akkaConfiguration(AkkaProperties akkaProperties) {
        return ConfigFactory.parseString(
                "akka {\n" +
                        "  actor {\n" +
                        "    provider = \"cluster\"\n" +
                        "  }\n" +
                        "\n" +
                        "  remote {\n" +
                        "    log-remote-lifecycle-events = off\n" +
                        "    netty.tcp {\n" +
                        "      hostname = \"" + akkaProperties.getHostname() + "\"\n" +
                        "      port = " + akkaProperties.getPort() + "\n" +
                        "    }\n" +
                        "  }\n" +
                        "  cluster {\n" +
                        buildSeedNodes(akkaProperties)+
//                        "      seed-nodes = [\n" +
//                        "        \"akka.tcp://" + akkaProperties.getActorSystem() + "@localhost:2551\",\n" +
//                        "        \"akka.tcp://" + akkaProperties.getActorSystem() + "@localhost:2552\",\n" +
//                        "      ]\n" +
                        "\n" +
                        "      auto-down-unreachable-after = 300s\n" +
                        "    }\n" +
                        "}").withFallback(ConfigFactory.load());
    }

    @Bean
    public ActorRef simpleClusterListener(ActorSystem actorSystem) {
        return actorSystem.actorOf(Props.create(SimpleClusterListener.class), "simpleClusterListener");
    }


    /**
     * "      seed-nodes = [\n" +
     * "        \"akka.tcp://"+actorSystem+"@localhost:2551\",\n" +
     * "        \"akka.tcp://"+actorSystem+"@localhost:2552\"\n" +
     * "      ]\n" +
     */
    private String buildSeedNodes(AkkaProperties akkaProperties) {
        StringBuilder sb = new StringBuilder();
        sb.append("      seed-nodes = [\n");
        akkaProperties.getSeedNodes()
                .forEach(s ->
                        sb.append("        \"akka.tcp://")
                                .append(akkaProperties.getActorSystem())
                                .append("@")
                                .append(s)
                                .append("\",\n"));
        sb.append("      ]\n");
        return sb.toString();
    }

}
