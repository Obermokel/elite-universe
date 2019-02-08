package borg.ed.universe;

import java.net.InetSocketAddress;

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

import borg.ed.universe.eddn.EddnBufferThread;
import borg.ed.universe.eddn.EddnElasticUpdater;
import borg.ed.universe.eddn.EddnGoogleBgsUpdater;
import borg.ed.universe.eddn.EddnReaderThread;
import borg.ed.universe.journal.JournalEventReader;
import borg.ed.universe.journal.JournalReaderThread;
import borg.ed.universe.journal.StatusReaderThread;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "borg.ed.universe.repository")
@ComponentScan(basePackages = { "borg.ed.universe.service.impl", "borg.ed.universe.converter" })
public class UniverseApplication {

	static final Logger logger = LoggerFactory.getLogger(UniverseApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UniverseApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			EddnBufferThread eddnBufferThread = ctx.getBean(EddnBufferThread.class);
			eddnBufferThread.addListener(ctx.getBean(EddnElasticUpdater.class));
			eddnBufferThread.addListener(ctx.getBean(EddnGoogleBgsUpdater.class));
			eddnBufferThread.start();

			EddnReaderThread eddnReaderThread = ctx.getBean(EddnReaderThread.class);
			eddnReaderThread.start();
		};
	}

	@Bean
	public JournalEventReader journalEventReader() {
		return new JournalEventReader();
	}

	@Bean
	public JournalReaderThread journalReaderThread() {
		return new JournalReaderThread();
	}

	@Bean
	public StatusReaderThread statusReaderThread() {
		return new StatusReaderThread();
	}

	@Bean
	public EddnReaderThread eddnReaderThread() {
		return new EddnReaderThread();
	}

	@Bean
	public EddnBufferThread eddnBufferThread() {
		return new EddnBufferThread();
	}

	@Bean
	public EddnElasticUpdater eddnElasticUpdater() {
		return new EddnElasticUpdater();
	}

	@Bean
	public EddnGoogleBgsUpdater eddnGoogleBgsUpdater() {
		return new EddnGoogleBgsUpdater();
	}

	@Bean
	public ElasticsearchOperations elasticsearchTemplate() {
		return new ElasticsearchTemplate(this.client());
	}

	@Bean
	public Client client() {
		Settings settings = Settings.builder().put("cluster.name", "eddbmirror").build();
		TransportClient client = new PreBuiltTransportClient(settings);
		//client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("127.0.0.1", 9300)));
		//client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("192.168.178.3", 9300))); // 192.168.178.3 = T410s
		client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("192.168.178.23", 9300))); // 192.168.178.23 = XPC
		return client;
		//		TransportClient client = TransportClient.builder().settings(settings).build();
		//		client.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress("192.168.178.31", 9300)));
		//		return client;
	}

}
