# Run

```bash
mvnd spring-boot:run
```

# Build

```bash
mvnd package -'Dmaven.test.skip=true
```

# Db diff

Po zbudowaniu

```bash
mvnd liquibase:diff
```

Przenieść changeset z generated-changelog.xml do changelog.xml