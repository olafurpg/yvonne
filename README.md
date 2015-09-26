# Yvonne - photo contests

Template: https://github.com/tototoshi/sbt-slick-codegen-example

## How to run

```
$ export DB_DEFAULT_URL="jdbc:h2:/tmp/example.db"
$ export DB_DEFAULT_USER="sa"
$ export DB_DEFAULT_PASSWORD=""
$ sbt
> flyway/flywayMigrate # flyway configuration is in another subproject to avoid conflict between migration and compilation
> project server
> compile
> run
```
