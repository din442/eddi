package com.jigsawstudio.eddi.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import com.jigsawstudio.eddi.model.DataSet
import com.jigsawstudio.utils._
import com.jigsawstudio.utils.database.{JDBCConnection, JDBCQuery}
import org.scalatest.FunSuite
import org.scalatest.matchers.must.Matchers
import scalikejdbc._

import scala.util.{Failure, Success, Try}


class TestDataSets extends FunSuite with Matchers {
  test("tables are created successfully") {
    Class.forName("org.postgresql.Driver")
    ConnectionPool.singleton("jdbc:postgresql://127.0.0.1:5432/data_catalog", "jigsaw_app", "jigsaw")

      println("creating tablke")
      // DataSet.createSql.execute().apply()
    DB readOnly { implicit session =>
      val d = DataSet.getByName("data_catalog.data_sets")
      println(d)
    }
 //
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
      "name" -> "datasets",
      "type" -> "db_table",
      "schema" -> om.writeValueAsString(schema),
      "path" -> "data_catalog.datasets",
      "isPartitioned" -> false,
      "owner" -> "uuid-adin",
      "version" -> 1,
      "properties" -> """{"database": "data_catalog"}""",
      "created" -> ctime
    )

    val b = CaseReflections.caseFromMap[DataSet](defn)
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
