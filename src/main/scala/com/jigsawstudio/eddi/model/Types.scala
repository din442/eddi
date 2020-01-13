package com.jigsawstudio.magpie.model

trait DataSetType {
  def dataSetTypes = List("db_table", "s3", "hdfs", "hive", "azure_blob")
}

trait SerializationType {
  def serializationTypes = List("csv", "delimited", "parquet", "orc", "text")
}

trait CompressionType {
  def compressionTypes = List("bzip", "gzip", "zip", "lzo", "snappy")
}

object FieldTypes {
  def isQuotable(t: String): Boolean =
    t.toLowerCase match {
      case "string" | "text" | "json" => true
      case s if s.startsWith("character varying") => true
      case s if s.startsWith("varchar") => true
      case s if s.startsWith("timestamp") | s.startsWith("date") => true
      case _ => false
    }
}