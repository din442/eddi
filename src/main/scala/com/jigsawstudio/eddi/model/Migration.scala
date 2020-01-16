package com.jigsawstudio.eddi.model

import com.jigsawstudio.eddi.model.{DataSet, DataSetVersions}
import scalikejdbc.{AutoSession, ConnectionPool}

object Migration {
  def main(args: Array[String]) {
    Class.forName("org.postgresql.Driver")
    ConnectionPool.singleton("jdbc:postgresql://127.0.0.1:5432/data_catalog", "jigsaw_app", "jigsaw")
    implicit val session = AutoSession

    println("dropping datasets")
    DataSet.dropSql.execute.apply
    DataSetVersions.dropSql.execute.apply
    println("creating datasets")
    DataSet.createSql.execute.apply
    DataSetVersions.createSql.execute.apply
  }
}
