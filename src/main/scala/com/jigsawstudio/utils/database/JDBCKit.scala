package com.jigsawstudio.utils.database

import java.sql.{Connection, DriverManager, Timestamp}

object JDBCConnection {
  def currentTimestamp = new Timestamp(System.currentTimeMillis)

  def drivers = Map("postgresql" -> "org.postgresql.Driver")

  def uri(driver: String, host: String, port: Int, database: String) =
    s"""jdbc:${driver}://${host}:${port.toString}/${database}"""

  def getConnection(driver: String,
                    host: String,
                    port: Int,
                    database: String,
                    user: String,
                    password: String): Connection = {
    require(drivers contains driver, s"unknown driver ${driver}")

    Class.forName(drivers(driver))
    DriverManager.getConnection(
      uri(
        driver,
        host,
        port,
        database
      ),
      user,
      password);
  }
}

