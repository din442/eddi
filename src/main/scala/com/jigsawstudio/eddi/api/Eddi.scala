package com.jigsawstudio.eddi.api

import scalikejdbc._

// initialize JDBC driver & connection pool

class Eddi {
  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:postgres:data_catalog:hello", "user", "pass")


}
