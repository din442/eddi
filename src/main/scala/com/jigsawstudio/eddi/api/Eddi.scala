package com.jigsawstudio.eddi.api

import com.jigsawstudio.eddi.model.DataSet
import scalikejdbc._

// initialize JDBC driver & connection pool

object Eddi extends App {
  Class.forName("org.postgresql.Driver")
  ConnectionPool.singleton("jdbc:postgresql://127.0.0.1:5432/data_catalog", "jigsaw_app", "jigsaw")
  implicit val session = AutoSession
  println("creating tablke")
  // DataSet.createSql.execute().apply()
  val d = DataSet.getByName("datasets")
  println(d)}
