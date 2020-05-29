package com.isom.service.test

import com.typesafe.config._
import io.gatling.core.Predef.{Simulation, _}
import io.gatling.http.Predef.{HttpHeaderValues, http}
import io.gatling.http.protocol.HttpProtocolBuilder
import io.gatling.jdbc.Predef.jdbcFeeder

class ISOMGetLoadTest extends Simulation {
  val conf = ConfigFactory.load()
  val baseUrl = conf.getString("isom-baseUrl")
  val apiUri = conf.getString("isom-apiUri")
  val throughput = conf.getInt("isom-throughputValue")
  val dbUrl = conf.getString("isom-dbUrl")
  val dbUsername = conf.getString("isom-dbUserName")
  val dbPassword = conf.getString("isom-dbPassword")

  val httpProtocol: HttpProtocolBuilder = http.baseUrl(baseUrl)

  val headers = Map("Content-Type" -> HttpHeaderValues.ApplicationJson,
    "Accept" -> HttpHeaderValues.ApplicationJson
  )

  val baseScenario1 = scenario("isom-get-request-1")
    .feed(jdbcFeeder(dbUrl, dbUsername, dbPassword, "SELECT fo_id FROM id_table").circular)
    .exec(
      http("isom-get-request-1")
        .get(baseUrl + apiUri + "/${fo_id}")
    )

  setUp(
    baseScenario1.inject(constantUsersPerSec(10) during (10))
  ).protocols(httpProtocol)
    .assertions(global.successfulRequests.percent.is(throughput))
    .assertions(global.responseTime.max.lt(2000))
}