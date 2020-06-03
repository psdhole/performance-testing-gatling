package com.isom.service.config

import com.typesafe.config.ConfigFactory
import io.gatling.core.Predef._
import io.gatling.http.Predef.{http, _}
import io.gatling.http.protocol.HttpProtocolBuilder;

class ISOMTestConfig extends Simulation {

  val conf = ConfigFactory.load()
  val baseUrl = conf.getString("isom-baseUrl")
  val apiUri = conf.getString("isom-apiUri")
  val requestUrl=baseUrl+apiUri
  val throughput = conf.getInt("isom-throughput")
  val maxResponseTime = conf.getInt("isom-max-response-time")

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

  val dbUrl = conf.getString("isom-dbUrl")
  val dbUsername = conf.getString("isom-dbUserName")
  val dbPassword = conf.getString("isom-dbPassword")
  val insertSQLQuery = conf.getString("isom-insertSQLQuery")
  val getSQLQuery = conf.getString("isom-getSQLQuery")
  val idColumnName = conf.getString("isom-id-column")

  val getScenarioName ="isom-get-scenario"
  val createScenarioName ="isom-create-scenario"
  val scenarioCsvFileName= "isom-create-scenarios.csv"
  val scenarioExpression="${isom-create-scenario-name}"

  val httpProtocol: HttpProtocolBuilder = http.baseUrl(baseUrl)

  val headers = Map("Content-Type" -> HttpHeaderValues.ApplicationJson,
    "Accept" -> HttpHeaderValues.ApplicationJson
  )
}