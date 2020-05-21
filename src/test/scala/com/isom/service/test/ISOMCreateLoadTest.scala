package com.isom.service.test

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef.{jumpToRps, _}
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

import scala.concurrent.duration._;

class ISOMCreateLoadTest extends Simulation {

  val conf = ConfigFactory.load()
  val baseUrl = conf.getString("isom-baseUrl")
  val apiUri = conf.getString("isom-apiUri")
  val throughput = conf.getInt("isom-throughputValue")
  val requestUrl = baseUrl + apiUri


  val users1 = conf.getInt("isom-create-scenario1-users")
  val duration1 = conf.getInt("isom-create-scenario1-duration")
  val users2 = conf.getInt("isom-create-scenario2-users")
  val duration2 = conf.getInt("isom-create-scenario2-duration")
  val users3 = conf.getInt("isom-create-scenario3-users")
  val duration3 = conf.getInt("isom-create-scenario3-duration")
  val users4 = conf.getInt("isom-create-scenario4-users")
  val duration4 = conf.getInt("isom-create-scenario4-duration")
  val users5 = conf.getInt("isom-create-scenario5-users")
  val duration5 = conf.getInt("isom-create-scenario5-duration")
  val users6 = conf.getInt("isom-create-scenario6-users")
  val duration6 = conf.getInt("isom-create-scenario6-duration")

  val totalDuration = duration1 + duration2 + duration3 + duration4 + duration5 + duration6

  val httpProtocol: HttpProtocolBuilder = http.baseUrl(baseUrl)

  val headers = Map("Content-Type" -> HttpHeaderValues.ApplicationJson,
    "Accept" -> HttpHeaderValues.ApplicationJson
  )

  val baseScenario1: ScenarioBuilder = scenario("isom-create-scenario-1")
    .exec(
      http("isom-create-scenario-1")
        .post(requestUrl)
        .body(RawFileBody("isom-create-scenario1.json")).asJson
        .headers(headers)
        .check(status.is(200))
    )

  val baseScenario2: ScenarioBuilder = scenario("isom-create-scenario-2")
    .exec(
      http("isom-create-scenario-2")
        .post(requestUrl)
        .body(RawFileBody("isom-create-scenario2.json")).asJson
        .headers(headers)
        .check(status.is(200))
    )

  val baseScenario3: ScenarioBuilder = scenario("isom-create-scenario-3")
    .exec(
      http("isom-create-scenario-3")
        .post(requestUrl)
        .body(RawFileBody("isom-create-scenario3.json")).asJson
        .headers(headers)
        .check(status.is(200))
    )
  val baseScenario4: ScenarioBuilder = scenario("isom-create-scenario-4")
    .exec(
      http("isom-create-scenario-4")
        .post(requestUrl)
        .body(RawFileBody("isom-create-scenario4.json")).asJson
        .headers(headers)
        .check(status.is(200))
    )

  val baseScenario5: ScenarioBuilder = scenario("isom-create-scenario-5")
    .exec(
      http("isom-create-scenario-5")
        .post(requestUrl)
        .body(RawFileBody("isom-create-scenario5.json")).asJson
        .headers(headers)
        .check(status.is(200))
    )

  val baseScenario6: ScenarioBuilder = scenario("isom-create-scenario-6")
    .exec(
      http("isom-create-scenario-6")
        .post(requestUrl)
        .body(RawFileBody("isom-create-scenario6.json")).asJson
        .headers(headers)
        .check(status.is(200))
    )
  //This simulation will reach 100 req/s with a ramp of 10 seconds, then hold this throughput for 120 seconds, jump to 50 req/s and finally hold this throughput for 60 seconds.

  setUp(
    baseScenario1.inject(constantUsersPerSec(users1) during (duration1 seconds)).throttle( //adds 100 users every second , so a total of 3600 users after 120 seconds
      reachRps(20) in (100 seconds), //reachRps(target) in (duration): target a throughput with a ramp over a given duration.
      holdFor(totalDuration seconds), //holdFor(duration): hold the current throughput for a given duration.
      jumpToRps(10),
      holdFor(totalDuration seconds)

    ),
    baseScenario2.inject(constantUsersPerSec(users2) during (duration2 seconds)).throttle(
      reachRps(20) in (100 seconds),
      holdFor(totalDuration seconds),
      jumpToRps(10),
      holdFor(totalDuration seconds)
    ),
    baseScenario3.inject(constantUsersPerSec(users3) during (duration3 seconds)).throttle(
      reachRps(20) in (100 seconds),
      holdFor(totalDuration seconds)
    ),
    baseScenario4.inject(constantUsersPerSec(users4) during (duration4 seconds)).throttle(
      reachRps(20) in (100 seconds),
      holdFor(totalDuration seconds),
      jumpToRps(10),
      holdFor(totalDuration seconds)
    ),
    baseScenario5.inject(constantUsersPerSec(users5) during (duration5 seconds)).throttle(
      reachRps(20) in (100 seconds),
      holdFor(totalDuration seconds),
      jumpToRps(10),
      holdFor(totalDuration seconds)
    ),
    baseScenario6.inject(constantUsersPerSec(users6) during (duration6 seconds)).throttle(
      reachRps(20) in (100 seconds),
      holdFor(totalDuration seconds),
      jumpToRps(10),
      holdFor(totalDuration seconds)
    )
  ).protocols(httpProtocol).assertions(global.successfulRequests.percent.is(throughput))
}