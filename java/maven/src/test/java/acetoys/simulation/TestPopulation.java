package acetoys.simulation;

import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.core.PopulationBuilder;
import java.time.Duration;

public class TestPopulation {

    private static final int DURATION_SECONDS =
            Integer.parseInt(System.getProperty("DURATION", "60"));

    public static PopulationBuilder instantUsers =
            TestScenario.defaultScenario.injectOpen(
                    atOnceUsers(10)
            );

    public static PopulationBuilder rampUsers =
            TestScenario.defaultScenario.injectOpen(
                    rampUsers(20).during(Duration.ofSeconds(DURATION_SECONDS))
            );

    public static PopulationBuilder complexInjection =
            TestScenario.highPurchaseScenario.injectOpen(
                    nothingFor(Duration.ofSeconds(5)),
                    atOnceUsers(5),
                    rampUsers(15).during(Duration.ofSeconds(30)),
                    constantUsersPerSec(10).during(Duration.ofSeconds(20))
            );

    public static PopulationBuilder closedModel =
            TestScenario.defaultScenario.injectClosed(
                    rampConcurrentUsers(5).to(15).during(Duration.ofSeconds(DURATION_SECONDS))
            );
}