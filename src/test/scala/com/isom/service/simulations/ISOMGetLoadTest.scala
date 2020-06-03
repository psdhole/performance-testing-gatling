package com.isom.service.simulations

import com.isom.service.config.ISOMTestConfig
import io.gatling.core.Predef.{Simulation, _}
import io.gatling.http.Predef.http
import io.gatling.jdbc.Predef.jdbcFeeder


class ISOMGetLoadTest extends Simulation {

  var testConfig: ISOMTestConfig = new ISOMTestConfig
  val users: Int = System.getProperty("users", "1").toInt
  val duration: Int = System.getProperty("duration", "1").toInt

  val baseScenario1 = scenario(testConfig.getScenarioName)
    .feed(jdbcFeeder(testConfig.dbUrl, testConfig.dbUsername, testConfig.dbPassword, testConfig.getSQLQuery).circular)
    .exec(
      http(testConfig.getScenarioName)
        .get(testConfig.baseUrl + testConfig.apiUri + testConfig.idColumnName)
    )

  setUp(
    baseScenario1.inject(constantUsersPerSec(users) during (duration))
  ).protocols(testConfig.httpProtocol)
    .assertions(global.successfulRequests.percent.is(testConfig.throughput))
    .assertions(global.responseTime.max.lt(testConfig.maxResponseTime))
}