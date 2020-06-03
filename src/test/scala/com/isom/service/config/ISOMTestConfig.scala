package com.isom.service.config

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder;

class ISOMTestConfig extends Simulation {

  val conf = ConfigFactory.load()
  val baseUrl = conf.getString("isom-baseUrl")
  val apiUri = conf.getString("isom-apiUri")
  val requestUrl = baseUrl + apiUri
  val throughput = conf.getInt("isom-throughput")
  val maxResponseTime = conf.getInt("isom-max-response-time")

  val dbUrl = conf.getString("isom-dbUrl")
  val dbUsername = conf.getString("isom-dbUserName")
  val dbPassword = conf.getString("isom-dbPassword")
  val insertSQLQuery = conf.getString("isom-insertSQLQuery")
  val getSQLQuery = conf.getString("isom-getSQLQuery")
  val idColumnName = conf.getString("isom-id-column")

  val getScenarioName = "isom-get-scenario"
  val createScenarioName = "isom-create-scenario"
  val createScenarioCsvFileName = "isom-create-scenarios.csv"
  val createScenarioExpression = "${isom-create-scenario-name}"
  val createScenarioRequestPath = "isom-create-scenarios-requests/"
  val httpProtocol: HttpProtocolBuilder = http.baseUrl(baseUrl)

  val headers = Map("Content-Type" -> HttpHeaderValues.ApplicationJson,
    "Accept" -> HttpHeaderValues.ApplicationJson
  )
}