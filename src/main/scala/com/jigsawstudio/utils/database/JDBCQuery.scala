package com.jigsawstudio.utils.database

import java.sql.{Connection, ResultSet}

import com.jigsawstudio.utils.CaseReflections

object JDBCQuery {
  def runQuery(query: String)(implicit connection: Connection): Unit = {
    val stmt = connection.createStatement()
    stmt.executeUpdate(query)
  }

  def queryResult[T: Manifest](sql: String)(implicit connection: Connection): List[T] = {
    rsRowMap(connection
      .createStatement
      .executeQuery(sql)
    ).map { r => CaseReflections.caseFromMap[T](r) }
  }

  def queryResultsMap(sql: String)(implicit connection: Connection): List[Map[String, Any]] = {
    // returns rows returned by query as List of maps
    rsRowMap(
      connection
        .createStatement
        .executeQuery(sql)
    )
  }

  def rsColumns(rs: ResultSet): List[(String, String)] = {
    val m = rs.getMetaData

    {
      1 to m.getColumnCount
      }
      .map { i => (m.getColumnName(i), m.getColumnTypeName(i)) }
      .toList
  }

  def rsRowMap(rs: ResultSet): List[Map[String, Any]] = {
    val columns = rsColumns(rs)
    Iterator.continually(rs.next)
      .takeWhile(_ == true)
      .map { _ => {
        1 to columns.size
        }
        .map { i => (columns(i - 1)._1, rs.getObject(i).asInstanceOf[Any]) }
        .toMap
      }.toList
  }

  def rsRowList(rs: ResultSet): List[List[Any]] = {
    Iterator.continually(rs.next)
      .takeWhile(_ == true)
      .map { _ => {
        1 to rsColumns(rs).size
        }
        .map { i => rs.getObject(i).asInstanceOf[Any] }
        .toList
      }.toList
  }

  def queryRows(sql: String)(implicit connection: Connection): List[List[Any]] = {
    rsRowList(
      connection
        .createStatement
        .executeQuery(sql)
    )
  }
}

object JDBCDiscovery {
  def getSchema(db: String, table: String)(implicit connection: Connection): Unit = {

  }
}