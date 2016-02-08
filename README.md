# MariaDB boolean errors

The MariaDB Connector/J driver maps mysql
boolean/tinyint1 values differently than the standard
mysql driver. The results are inconsistent with the mysql
[datatypes documentation](http://dev.mysql.com/doc/refman/5.7/en/numeric-type-overview.html),
and lead to incorrect results.

## prereqs

- local mysql installation with default root/no password accessible account on localhost
- java, sbt

## setup

- start local mysqld instance
- Run `./create-db.sh` to create test db, tables, data in local mysql instance

## run tests
- `sbt run`
- to change db driver, edit build.sbt and then run again.

## explanation of output

- the database table `examples` has three columns (bigint, boolean, varchar)
- the ExampleRow object has four fields
  - `id: Long` - the value of the bigint column
  - `booleanValue: Boolean` - the value of the boolean column, read using
    jdbc or anorms read boolean functions
  - `intValue: Int` - the value of the boolean column, read using
    jdbc or anorm read int functions
  - `expected: String` - the value of the expected column, has string
    representation of what the booleanValue should contain

## output with mysql connector/j:

```
[info] Running Examples
using driver: MySQL Connector Java (mysql-connector-java-5.1.36 ( Revision: 4fc1f969f740409a4e03750316df2c0e429f3dc8 ))
test: jdbc statement
OK    ExampleRow(1,false,0,false)
OK    ExampleRow(2,true,1,true)
OK    ExampleRow(3,true,2,true)
OK    ExampleRow(4,false,0,false)
OK    ExampleRow(5,true,1,true)

test: jdbc prepared statement
OK    ExampleRow(1,false,0,false)
OK    ExampleRow(2,true,1,true)
OK    ExampleRow(3,true,2,true)
OK    ExampleRow(4,false,0,false)
OK    ExampleRow(5,true,1,true)

test: anorm fixed
OK    ExampleRow(1,false,0,false)
OK    ExampleRow(2,true,1,true)
OK    ExampleRow(3,true,1,true)
OK    ExampleRow(4,false,0,false)
OK    ExampleRow(5,true,1,true)

test: anorm interpolation
OK    ExampleRow(1,false,0,false)
OK    ExampleRow(2,true,1,true)
OK    ExampleRow(3,true,1,true)
OK    ExampleRow(4,false,0,false)
OK    ExampleRow(5,true,1,true)
```

## output using mariadb connector/j:

```
[info] Running Examples
using driver: MariaDB connector/J (1.3.4)
test: jdbc statement
OK    ExampleRow(1,false,0,false)
OK    ExampleRow(2,true,1,true)
ERR   ExampleRow(3,false,2,true)
OK    ExampleRow(4,false,0,false)
OK    ExampleRow(5,true,1,true)

test: jdbc prepared statement
OK    ExampleRow(1,false,0,false)
OK    ExampleRow(2,true,1,true)
ERR   ExampleRow(3,false,2,true)
OK    ExampleRow(4,false,0,false)
OK    ExampleRow(5,true,1,true)

test: anorm fixed
OK    ExampleRow(1,false,0,false)
OK    ExampleRow(2,true,1,true)
OK    ExampleRow(3,true,1,true)
OK    ExampleRow(4,false,0,false)
OK    ExampleRow(5,true,1,true)

test: anorm interpolation
ERR   ExampleRow(1,true,1,false)
OK    ExampleRow(2,true,1,true)
OK    ExampleRow(3,true,1,true)
ERR   ExampleRow(4,true,1,false)
OK    ExampleRow(5,true,1,true)
```

