package com.jigsawstudio.utils.database

import java.sql.ResultSet
import com.jigsawstudio.utils.CaseReflections._

trait JDBCTable[T] {
  def apply(m: Map[String, Any])(implicit T: Manifest[T]) = caseFromMap[T](m)
}
