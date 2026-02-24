package acetoys.simulation;

import static io.gatling.javaapi.core.CoreDsl.*;

import io.gatling.javaapi.core.ScenarioBuilder;
import java.time.Duration;

public class TestScenario {

  private static final Duration TEST_DURATION =
          Duration.ofSeconds(Integer.parseInt(System.getProperty("DURATION", "60")));

  // --- Сценарий по умолчанию ---
  public static ScenarioBuilder defaultScenario =
          scenario("Default User Journey")
                  .during(TEST_DURATION)
                  .on(
                          randomSwitch()
                                  .on(
                                          percent(60).then(exec(UserJourney.browseStore)),
                                          percent(30).then(exec(UserJourney.abandonBasket)),
                                          percent(10).then(exec(UserJourney.completePurchase))
                                  )
                  );

  // --- Сценарий с высокой вероятностью покупки ---
  public static ScenarioBuilder highPurchaseScenario =
          scenario("High Purchase User Journey")
                  .during(TEST_DURATION)
                  .on(
                          randomSwitch()
                                  .on(
                                          percent(30).then(exec(UserJourney.browseStore)),
                                          percent(30).then(exec(UserJourney.abandonBasket)),
                                          percent(40).then(exec(UserJourney.completePurchase))
                                  )
                  );
}