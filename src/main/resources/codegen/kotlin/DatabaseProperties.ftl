database=${databaseParameter.name}
database.name=
database.driver=${databaseParameter.driver}
database.url=${databaseParameter.url}
database.username=
database.password=
database.originator=

<#if queryDatabaseParameter?has_content>
query.database=${queryDatabaseParameter.name}
query.database.name=
query.database.driver=${queryDatabaseParameter.driver}
query.database.url=${queryDatabaseParameter.url}
query.database.username=
query.database.password=
query.database.originator=
</#if>
