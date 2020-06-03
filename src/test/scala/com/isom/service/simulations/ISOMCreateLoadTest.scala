package com.isom.service.simulations

import com.isom.service.config.ISOMTestConfig
import com.isom.service.db.ISOMDBConnection
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

import scala.concurrent.duration._

class ISOMCreateLoadTest extends Simulation {

  var testConfig: ISOMTestConfig = new ISOMTestConfig
  var dbConnection: ISOMDBConnection = new ISOMDBConnection

  val users: Int = System.getProperty("users", "1").toInt
  val duration: Int = System.getProperty("duration", "1").toInt

  before {
    dbConnection.getDBConnection()
  }

  val baseScenario: ScenarioBuilder = scenario(testConfig.getScenarioName)
    .feed(csv(testConfig.scenarioCsvFileName).circular)
    .exec(
      http(testConfig.scenarioExpression)
        .post(testConfig.requestUrl)
        .body(RawFileBody(testConfig.scenarioExpression + ".json")).asJson
        .headers(testConfig.headers)
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("id"))
    )
    .exec(session => {
      dbConnection.writeFOToDatabase(session("id").as[String])
      session
    })

  setUp(
    baseScenario.inject(constantUsersPerSec(users) during (duration seconds))
  ).protocols(testConfig.httpProtocol)
    .assertions(global.successfulRequests.percent.is(testConfig.throughput))
    .assertions(global.responseTime.max.lt(testConfig.maxResponseTime))

  after {
    dbConnection.closeDBConnection()
  }

}