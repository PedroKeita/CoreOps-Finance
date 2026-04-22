# Migration Conventions

## Naming pattern

`V{version}__{description}.sql`

## Rules
- `V` uppercase, followed by version number
- Double underscore `__` between version and description
- Description in snake_case, short and descriptive
- Never edit or delete an existing migration
- One migration per logical change

## Examples
| File | Description |
|------|-------------|
| `V1__init.sql` | Initial schema — users table |
| `V2__add_audit_log.sql` | Audit log table |
| `V3__add_api_catalog.sql` | API catalog table |

## Rollback strategy
Flyway Community does not support automatic rollback.
To undo a migration, create a new version:
- Problem in `V3__add_api_catalog.sql`
- Fix: create `V4__fix_api_catalog.sql` with the correction

## Flyway history
The `flyway_schema_history` table tracks all executed migrations.
Never manually edit this table.