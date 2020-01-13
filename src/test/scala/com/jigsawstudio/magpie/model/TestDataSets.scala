package com.jigsawstudio.magpie.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import com.jigsawstudio.utils._
import org.scalatest.FunSuite
import org.scalatest.matchers.must.Matchers
import scalikejdbc.IsolationLevel.Default

import scala.util.{Failure, Success, Try}


class TestDataSets extends FunSuite with Matchers {
  test("tables are created successfully") {
    implicit val connection = JDBCConnection.getConnection(
      "postgresql",
      "127.0.0.1",
      5432,
      "data_catalog",
      "jigsaw_app",
      "jigsaw")

    Try(Migration.createTables) match {
      case Success(_) => "success"
      case Failure(e) => println(e)
    }
    connection.close()
  }

  test("insert works successfully") {
    val schema = """[{"field1": "int"}, {"field2": "varchar(20)"}]"""

    implicit val connection = JDBCConnection.getConnection(
      "postgresql",
      "127.0.0.1",
      5432,
      "data_catalog",
      "jigsaw_app",
      "jigsaw")


    val sql = "select * from datasets limit 1"
    val s = connection.createStatement()
    val r = s.executeQuery(sql)
    val m = r.getMetaData
    val columns = {1 to m.getColumnCount}.map { i =>
      (m.getColumnName(i), m.getColumnTypeName(i))
    }.toMap
    println(columns)
    connection.close()

  }

  test("resultset successfully") {
    implicit val connection = JDBCConnection.getConnection(
      "postgresql",
      "127.0.0.1",
      5432,
      "data_catalog",
      "jigsaw_app",
      "jigsaw")

    val om = new ObjectMapper() with ScalaObjectMapper
    om.registerModule(DefaultScalaModule)
    val ctime = JDBCConnection.currentTimestamp

    val sql = """select column_name,data_type,character_maximum_length
from information_schema.columns
where table_name = 'datasets';"""
    val s = JDBCQuery.queryRows(sql)
    val schema = s.map { f => Map("name" -> f.head, "type" -> f(1), "size" -> f(2)) }

    val defn = Map(
      "id" -> None,
      "name" -> "data_catalog.data_sets",
      "type" -> "db_table",
      "schema" -> om.writeValueAsString(schema),
      "path" -> "data_catalog.datasets",
      "isPartitioned" -> false,
      "owner" -> "uuid-adin",
      "version" -> 1,
      "properties" -> "{}",
      "createdAt" -> ctime,
      "updatedAt" -> ctime
    )

    val b = CaseReflections.fromMap[DataSet](defn)
    println(defn)
    println(b)
    Try(b.insert(connection)) match {
      case Success(_) =>
      case Failure(e) =>
        println(e.getMessage)
    }
    //connection.commit()
    connection.close()


  }

  test("query dataset") {
    implicit val connection = JDBCConnection.getConnection(
      "postgresql",
      "127.0.0.1",
      5432,
      "data_catalog",
      "jigsaw_app",
      "jigsaw")

  }

}
