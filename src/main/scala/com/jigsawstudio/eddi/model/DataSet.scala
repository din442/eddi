package com.jigsawstudio.magpie.model

import java.sql.{Connection, Timestamp}

import com.jigsawstudio.utils.{JDBCQuery => JQ}
import com.jigsawstudio.utils.{JDBCConnection => JC}
import com.jigsawstudio.utils.CaseReflections._

case class DataSetField(name: String, `type`: String, size: Option[Int], nullable: Option[Boolean])
case class DataSetSchema(fields: List[DataSetField])

object DataSet {
  def getByName(name: String)(implicit con: Connection): DataSet = {
    val sql =
      s"""
SELECT * FROM datasets WHERE name = '${name}'
      """
    val rs = JQ.queryResultsMap(sql)
    fromMap[DataSet](rs.head)
  }
}
case class DataSet(id: Option[Int],
                   name: String,
                   `type`: String,
                   schema: String,
                   path: String,
                   isPartitioned: Boolean,
                   owner: String,
                   version: Int,
                   properties: String,
                   createdAt: Timestamp,
                   updatedAt: Timestamp) extends DataSetType {

  require(dataSetTypes contains `type`, s"unrecognized type: ${`type`}")

  lazy val schemaMap = jsonMapper.readValue[DataSetSchema](schema)
  lazy val propertiesMap = jsonMapper.readValue[Map[String, Any]](properties)


  def insert(implicit con: Connection): Unit = {
    val cTime = JC.currentTimestamp
    val sql =
      s"""
INSERT INTO datasets(name, type, schema, path, ispartitioned, owner, version, properties, created, updated)
VALUES('${name}', '${`type`}', '${schema}', '${path}', ${isPartitioned}, '${owner}', ${version}, '${properties}', '${cTime}', '${cTime}')
       """

    JQ.runQuery(sql)
  }
}




