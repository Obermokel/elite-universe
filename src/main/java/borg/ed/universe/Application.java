package borg.ed.universe;

import borg.ed.universe.eddn.EddnReaderThread;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetSocketAddress;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "borg.ed.universe.repository")
@ComponentScan(basePackages = { "borg.ed.universe.service.impl", "borg.ed.universe.converter" })
public class Application {

    static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            ctx.getBean(EddnReaderThread.class).start();
        };
    }

    @Bean
    public EddnReaderThread eddnReaderThread() {
        return new EddnReaderThread();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(this.client());
    }

    @Bean
    public Client client() {
        Settings settings = Settings.builder().put("cluster.name", "eddbmirror").build();
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));
        return client;
        //		TransportClient client = TransportClient.builder().settings(settings).build();
        //		client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("192.168.178.31", 9300)));
        //		return client;
    }

}
