package com.isom.service.test

import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

import scala.concurrent.duration._;

class ISOMLoadTest extends Simulation {

  val headers = Map("Content-Type" -> HttpHeaderValues.ApplicationJson,
    "Accept" -> HttpHeaderValues.ApplicationJson
  )

  val baseScenario: ScenarioBuilder = scenario("isom-create-request")
    .exec(
      http("isom-create-request")
        .post(Configuration.t_baseUrl + Configuration.t_apiUri)
        .body(RawFileBody("requestBody.json")).asJson
        .headers(headers)
        .check(status.is(200))
    )

  val httpProtocol: HttpProtocolBuilder = http.baseUrl(Configuration.t_baseUrl)

  object Configuration {
    val t_concurrency: Int = Integer.getInteger("users", 10).toInt
    val t_duration: Int = Integer.getInteger("duration", 1).toInt
    val t_throughput: Int = Integer.getInteger("throughput", 100).toInt
    val t_baseUrl: String = System.getProperty("host_url", "")
    val t_apiUri: String = System.getProperty("request_url", "")
  }

  //This simulation will reach 100 req/s with a ramp of 10 seconds, then hold this throughput for 120 seconds, jump to 50 req/s and finally hold this throughput for 60 seconds.

  setUp(
    baseScenario.inject(constantUsersPerSec(100) during (2 minutes)).throttle(
      reachRps(100) in (15 seconds), //reachRps(target) in (duration): target a throughput with a ramp over a given duration.
      holdFor(30 seconds), //holdFor(duration): hold the current throughput for a given duration.
      jumpToRps(50), //jumpToRps(target): jump immediately to a given targeted throughput.
      holdFor(15 seconds) //holdFor(duration): hold the current throughput for a given duration.
    )
  ).protocols(httpProtocol).assertions(global.successfulRequests.percent.is(Configuration.t_throughput))
}