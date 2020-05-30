package com.isom.service.db

import java.sql.{Connection, DriverManager}

import com.isom.service.config.ISOMTestConfig
import io.gatling.core.Predef._
import io.gatling.core.structure.ScenarioBuilder
import io.gatling.http.Predef.{http, _}
import io.gatling.http.request.builder.HttpRequestBuilder.toActionBuilder

import scala.concurrent.duration._

class ISOMDBConnection extends Simulation {

  var isomConfig: ISOMTestConfig = new ISOMTestConfig

  var connection: Connection = _

  def getDBConnection(): Unit = {
    connection = DriverManager.getConnection(isomConfig.dbUrl, isomConfig.dbUsername, isomConfig.dbPassword)
  }

  def closeDBConnection(): Unit = {
    connection.close()
  }

  def writeFOToDatabase(id: String) = {
    val statement = connection.prepareStatement(isomConfig.insertSQLQuery)
    statement.setString(1, id)
    statement.executeUpdate()
  }

}