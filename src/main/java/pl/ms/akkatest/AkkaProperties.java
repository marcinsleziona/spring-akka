package pl.ms.akkatest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by Marcin on 2018-02-23 15:15
 */
@Component
@ConfigurationProperties("akka.cluster")
public class AkkaProperties {

    @Value("${akka.actor-system}")
    private String actorSystem;
    @Value("${akka.remote.netty.tcp.hostname}")
    private String hostname;
    @Value("${akka.remote.netty.tcp.port}")
    private int port;
    //    @Value("${akka.cluster.seed-nodes}")
    private List<String> seedNodes = new ArrayList<>();

    public String getActorSystem() {
        return actorSystem;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public List<String> getSeedNodes() {
        return seedNodes;
    }
}
