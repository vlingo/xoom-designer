database=${databaseParameter.type}
database.name=${databaseParameter.name}
database.driver=${databaseParameter.driver}
database.url=${databaseParameter.url}
database.username=${databaseParameter.username}
database.password=${databaseParameter.password}
database.originator=${databaseParameter.originator}

<#if queryDatabaseParameter?has_content>
query.database=${queryDatabaseParameter.type}
query.database.name=${queryDatabaseParameter.name}
query.database.driver=${queryDatabaseParameter.driver}
query.database.url=${queryDatabaseParameter.url}
query.database.username=${queryDatabaseParameter.username}
query.database.password=${queryDatabaseParameter.password}
query.database.originator=${queryDatabaseParameter.originator}
</#if>