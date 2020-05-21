package com.isom.service.test

import com.typesafe.config._
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

import scala.concurrent.duration._

class ISOMGetLoadTest extends Simulation {


  val conf = ConfigFactory.load()
  val baseUrl = conf.getString("baseUrl")
  val apiUri = conf.getString("apiUri")
  val throughput = conf.getInt("throughputValue")
  val users = conf.getInt("s1-users")
  val duration = conf.getInt("s1-duration")


  val httpProtocol: HttpProtocolBuilder = http.baseUrl(baseUrl)

  val headers = Map("Content-Type" -> HttpHeaderValues.ApplicationJson,
    "Accept" -> HttpHeaderValues.ApplicationJson
  )

  val baseScenario1: ScenarioBuilder = scenario("isom-create-request-1")
    .exec(
      http("isom-create-request-1")
        .post(baseUrl + apiUri)
        .body(RawFileBody("isom-create-scenario1.json")).asJson
        .headers(headers)
        .check(status.is(200))
    )


  //This simulation will reach 100 req/s with a ramp of 10 seconds, then hold this throughput for 120 seconds, jump to 50 req/s and finally hold this throughput for 60 seconds.

  setUp(
    baseScenario1.inject(constantUsersPerSec(100) during (10 seconds)).throttle( //adds 100 users every second , so a total of 3600 users after 120 seconds
      reachRps(10) in (10 seconds), //reachRps(target) in (duration): target a throughput with a ramp over a given duration.
      holdFor(60 seconds) //holdFor(duration): hold the current throughput for a given duration.
    )).protocols(httpProtocol).assertions(global.successfulRequests.percent.is(throughput))
}