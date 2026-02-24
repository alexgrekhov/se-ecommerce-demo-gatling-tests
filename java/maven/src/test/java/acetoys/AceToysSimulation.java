package acetoys;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import acetoys.simulation.TestPopulation;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class AceToysSimulation extends Simulation {

  // Параметры из Maven
  private static final String TEST_TYPE = System.getProperty("TEST_TYPE", "INSTANT_USERS");
  private static final String DOMAIN = System.getProperty("DOMAIN", "acetoys.uk");

  // HTTP протокол
  private HttpProtocolBuilder httpProtocol =
          http.baseUrl("https://" + DOMAIN)
                  .inferHtmlResources(
                          AllowList(),
                          DenyList(
                                  ".*\\.js",
                                  ".*\\.css",
                                  ".*\\.gif",
                                  ".*\\.jpeg",
                                  ".*\\.jpg",
                                  ".*\\.ico",
                                  ".*\\.woff",
                                  ".*\\.woff2",
                                  ".*\\.(t|o)tf",
                                  ".*\\.png",
                                  ".*detectportal\\.firefox\\.com.*"
                          )
                  )
                  .acceptEncodingHeader("gzip, deflate")
                  .acceptLanguageHeader("en-GB,en;q=0.9");

  {
    PopulationBuilder population;

    switch (TEST_TYPE) {
      case "RAMP_USERS":
        population = TestPopulation.rampUsers;
        break;
      case "COMPLEX_INJECTION":
        population = TestPopulation.complexInjection;
        break;
      case "CLOSED_MODEL":
        population = TestPopulation.closedModel;
        break;
      case "INSTANT_USERS":
      default:
        population = TestPopulation.instantUsers;
        break;
    }

    setUp(population)
            .protocols(httpProtocol)
            .assertions(
                    global().responseTime().mean().lt(3000),
                    global().successfulRequests().percent().gt(99.0),
                    forAll().responseTime().max().lt(5000)
            );
  }
}