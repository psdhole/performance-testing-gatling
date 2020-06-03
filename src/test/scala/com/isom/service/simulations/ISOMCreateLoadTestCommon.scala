package com.isom.service.simulations

import java.sql.Connection

import com.isom.service.config.ISOMTestConfig
import com.isom.service.db.ISOMDBConnection
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

import scala.concurrent.duration._

class ISOMCreateLoadTestCommon extends Simulation {

  var testConfig: ISOMTestConfig = new ISOMTestConfig
  var dbConnection: ISOMDBConnection = new ISOMDBConnection

  before {
    dbConnection.getDBConnection()
  }

  val baseScenario1: ScenarioBuilder = scenario("isom-create-scenario-1")
    .exec(
      http("isom-create-scenario-1")
        .post(testConfig.requestUrl)
        .body(RawFileBody("isom-create-scenarios-requests/isom-create-scenario1.json")).asJson
        .headers(testConfig.headers)
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("id"))
    )
    .exec(session => {
      dbConnection.writeFOToDatabase(session("id").as[String])
      session
    })

  val baseScenario2: ScenarioBuilder = scenario("isom-create-scenario-2")
    .exec(
      http("isom-create-scenario-2")
        .post(testConfig.requestUrl)
        .body(RawFileBody("isom-create-scenarios-requests/isom-create-scenario2.json")).asJson
        .headers(testConfig.headers)
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("id"))
    )
    .exec(session => {
      dbConnection.writeFOToDatabase(session("id").as[String])
      session
    })

  val baseScenario3: ScenarioBuilder = scenario("isom-create-scenario-3")
    .exec(
      http("isom-create-scenario-3")
        .post(testConfig.requestUrl)
        .body(RawFileBody("isom-create-scenarios-requests/isom-create-scenario3.json")).asJson
        .headers(testConfig.headers)
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("id"))
    )
    .exec(session => {
      dbConnection.writeFOToDatabase(session("id").as[String])
      session
    })

  val baseScenario4: ScenarioBuilder = scenario("isom-create-scenario-4")
    .exec(
      http("isom-create-scenario-4")
        .post(testConfig.requestUrl)
        .body(RawFileBody("isom-create-scenarios-requests/isom-create-scenario4.json")).asJson
        .headers(testConfig.headers)
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("id"))
    )
    .exec(session => {
      dbConnection.writeFOToDatabase(session("id").as[String])
      session
    })

  val baseScenario5: ScenarioBuilder = scenario("isom-create-scenario-5")
    .exec(
      http("isom-create-scenario-5")
        .post(testConfig.requestUrl)
        .body(RawFileBody("isom-create-scenarios-requests/isom-create-scenario5.json")).asJson
        .headers(testConfig.headers)
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("id"))
    )
    .exec(session => {
      dbConnection.writeFOToDatabase(session("id").as[String])
      session
    })

  val baseScenario6: ScenarioBuilder = scenario("isom-create-scenario-6")
    .exec(
      http("isom-create-scenario-6")
        .post(testConfig.requestUrl)
        .body(RawFileBody("isom-create-scenarios-requests/isom-create-scenario6.json")).asJson
        .headers(testConfig.headers)
        .check(status.is(200))
        .check(jsonPath("$.id").saveAs("id"))
    )
    .exec(session => {
      dbConnection.writeFOToDatabase(session("id").as[String])
      session
    })

  setUp(
    baseScenario1.inject(constantUsersPerSec(testConfig.users1) during (testConfig.duration1 seconds)),
    baseScenario2.inject(constantUsersPerSec(testConfig.users2) during (testConfig.duration2 seconds)),
    baseScenario3.inject(constantUsersPerSec(testConfig.users3) during (testConfig.duration3 seconds)),
    baseScenario4.inject(constantUsersPerSec(testConfig.users4) during (testConfig.duration4 seconds)),
    baseScenario5.inject(constantUsersPerSec(testConfig.users5) during (testConfig.duration5 seconds)),
    baseScenario6.inject(constantUsersPerSec(testConfig.users6) during (testConfig.duration6 seconds))
  ).protocols(testConfig.httpProtocol)
    .assertions(global.successfulRequests.percent.is(testConfig.throughput))
    .assertions(global.responseTime.max.lt(testConfig.maxResponseTime))

  after {
    dbConnection.closeDBConnection()
  }
}