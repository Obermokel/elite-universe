package borg.ed.universe;

import borg.ed.universe.constants.Element;
import borg.ed.universe.data.Coord;
import borg.ed.universe.eddn.EddnElasticUpdater;
import borg.ed.universe.eddn.EddnReaderThread;
import borg.ed.universe.journal.JournalEventReader;
import borg.ed.universe.journal.JournalReaderThread;
import borg.ed.universe.model.Body;
import borg.ed.universe.model.Body.MaterialShare;
import borg.ed.universe.service.UniverseService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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
            //            EddnReaderThread eddnReaderThread = ctx.getBean(EddnReaderThread.class);
            //            eddnReaderThread.addListener(ctx.getBean(EddnElasticUpdater.class));
            //            eddnReaderThread.start();
            //List<Element> mats = Arrays.asList(Element.POLONIUM, /*Element.YTTRIUM,*/ Element.NIOBIUM, Element.ARSENIC);
            List<MaterialShare> mats = new ArrayList<>();
            MaterialShare pol = new MaterialShare();
            pol.setName(Element.POLONIUM);
            pol.setPercent(new BigDecimal("0.6"));
            mats.add(pol);
            MaterialShare nio = new MaterialShare();
            nio.setName(Element.NIOBIUM);
            nio.setPercent(new BigDecimal("0.7"));
            mats.add(nio);
            MaterialShare ars = new MaterialShare();
            ars.setName(Element.ARSENIC);
            ars.setPercent(new BigDecimal("0.8"));
            mats.add(ars);
            final Coord location = new Coord(559f, -708f, -6947f);
            Page<Body> page = ctx.getBean(UniverseService.class).findPlanetsHavingElementsNear(location, 10000f, mats, PageRequest.of(0, 1000));
            if (page.getTotalElements() > 0) {
                List<Body> sortedList = new ArrayList<>(page.getContent());
                Collections.sort(sortedList, new Comparator<Body>() {
                    @Override
                    public int compare(Body b1, Body b2) {
                        //                        MaterialShare niobium1 = b1.getMaterialShares().stream().filter(sh -> Element.NIOBIUM.equals(sh.getName())).findFirst().orElse(null);
                        //                        MaterialShare niobium2 = b2.getMaterialShares().stream().filter(sh -> Element.NIOBIUM.equals(sh.getName())).findFirst().orElse(null);
                        //                        BigDecimal percent1 = niobium1 == null || niobium1.getPercent() == null ? BigDecimal.ZERO : niobium1.getPercent();
                        //                        BigDecimal percent2 = niobium2 == null || niobium2.getPercent() == null ? BigDecimal.ZERO : niobium2.getPercent();
                        //                        return -1 * percent1.compareTo(percent2);
                        return new Float(b1.getCoord().distanceTo(location)).compareTo(new Float(b2.getCoord().distanceTo(location)));
                    }
                });
                for (Body body : sortedList.subList(0, Math.min(10, sortedList.size()))) {
                    logger.info(String.format(Locale.US, "%s @ %.1f Ly", body.getName(), body.getCoord().distanceTo(location)));
                    for (MaterialShare share : body.getMaterialShares()) {
                        if (mats.stream().filter(sh -> sh.getName().equals(share.getName())).findAny().isPresent()) {
                            logger.info(String.format(Locale.US, "  %5.1f%% %s", share.getPercent().floatValue(), share.getName().toString()));
                        }
                    }
                }
            }
            logger.info("Planets found: " + page.getTotalElements());
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
    public EddnReaderThread eddnReaderThread() {
        return new EddnReaderThread();
    }

    @Bean
    public EddnElasticUpdater eddnElasticUpdater() {
        return new EddnElasticUpdater();
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
