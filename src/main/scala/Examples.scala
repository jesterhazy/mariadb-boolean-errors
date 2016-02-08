import java.sql.{DriverManager, ResultSet, Connection}

import anorm._

object Examples {
  def main(args: Array[String]): Unit = {
    val url = "jdbc:mysql://localhost:3306/test_db?user=root"
    implicit val c = DriverManager.getConnection(url)

    println(s"using driver: ${c.getMetaData.getDriverName} (${c.getMetaData.getDriverVersion})")

    runTest(c, "jdbc statement", loadUsingJdbcStatement)
    runTest(c, "jdbc prepared statement", loadUsingJdbcPreparedStatement)
    runTest(c, "anorm fixed", loadUsingAnormFixedQuery)
    runTest(c, "anorm interpolation", loadUsingAnormSqlInterpolation)

    c.close()
  }


  def runTest(c: Connection, label: String, f: (Connection, Long) => ExampleRow): Unit = {
    println("test: " + label)
    1 to 5 map { f(c, _) } foreach { verify(_) }
    println()
  }

  def loadUsingAnormSqlInterpolation(c: Connection, id: Long): ExampleRow = {
    // anorm SQL interpolation
    SQL"select * from examples where id = $id".as(anormParser.single)(c)
  }

  def loadUsingAnormFixedQuery(c: Connection, id: Long): ExampleRow = {
    // scala string interpolation only
    SQL(s"select * from examples where id = $id").as(anormParser.single)(c)
  }

  def loadUsingJdbcStatement(c: Connection, id: Long): ExampleRow = {
    val rs = c.createStatement().executeQuery(s"select * from examples where id = $id")
    extractExampleRow(rs)
  }

  def loadUsingJdbcPreparedStatement(c: Connection, id: Long): ExampleRow = {
    val ps = c.prepareStatement("select * from examples where id = ?")
    ps.setLong(1, id)
    val rs = ps.executeQuery()
    extractExampleRow(rs)
  }

  def extractExampleRow(rs: ResultSet): ExampleRow = {
    rs.first()
    ExampleRow(rs.getLong("id"), rs.getBoolean("value"), rs.getInt("value"), rs.getString("expected"))
  }

  def verify(example: ExampleRow): Unit = {
    val result = if (example.booleanValue.toString == example.expected) "OK " else "ERR"
    println(s"$result   $example")
  }

  val anormParser: RowParser[ExampleRow] = {
    import SqlParser._
    get[Long]("id") ~
    get[Boolean]("value") ~
    get[Int]("value") ~
    get[String]("expected") map {
      case i ~ vb ~ vi ~ e => ExampleRow(i, vb, vi, e)
    }
  }
}

case class ExampleRow(id: Long, booleanValue: Boolean, intValue: Int, expected: String)


