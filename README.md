# Practice Plan Generator

A Spring Boot application for generating practice plans for youth sports coaches. The application enables coaches to create structured practice plans based on sport, focus area, available time, and other parameters.

## Features

- Create practice plans for multiple sports (Basketball, Football, Soccer, etc.)
- Filter drills by focus area (Fundamentals, Offense, Defense)
- Generate practice sections with warmup, stations, position groups, and team time
- Manage equipment requirements for practices
- Track KPIs (Key Performance Indicators) for skill development
- Smart rotation of stations and position-specific activities

## Technology Stack

- Java 17
- Spring Boot 3.2.3
- JPA/Hibernate
- SQL Server
- Flyway for database migrations
- Maven for build management
- Swagger/OpenAPI for API documentation

## Setup Development Environment

### Prerequisites

- Java 17 or higher
- Maven
- Docker

### Database Setup with Docker

1. Pull the SQL Server Docker image:

```bash
docker pull mcr.microsoft.com/mssql/server:2022-latest
```

2. Run SQL Server container:

```bash
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=YourStrongPassword123" \
   -p 1433:1433 --name sql_server \
   -d mcr.microsoft.com/mssql/server:2022-latest
```

3. Connect to SQL Server using SQL Server command-line tools or Azure Data Studio:

```bash
docker exec -it sql_server /opt/mssql-tools/bin/sqlcmd \
   -S localhost -U sa -P YourStrongPassword123
```

4. Create the database and development user:

```sql
-- Create practice plan database
CREATE DATABASE practice_plan;
GO

-- Use the new database
USE practice_plan;
GO

-- Create dev_user login
CREATE LOGIN dev_user WITH PASSWORD = 'P@ssw0rd1234';
GO

-- Create user in database from login
CREATE USER dev_user FOR LOGIN dev_user;
GO

-- Add db_owner role to user
ALTER ROLE db_owner ADD MEMBER dev_user;
GO

-- Create flyway history schema
CREATE SCHEMA flyway_history_schema;
GO

-- Grant necessary permissions
GRANT ALTER ON SCHEMA::flyway_history_schema TO dev_user;
GRANT CREATE TABLE TO dev_user;
GRANT CONTROL ON SCHEMA::dbo TO dev_user;
GO

EXIT
```

### Building and Running the Application

1. Clone the repository:

```bash
git clone https://github.com/yourusername/practice-plan-generator.git
cd practice-plan-generator
```

2. Build with Maven:

```bash
mvn clean install
```

3. Run the application with dev profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

4. The application will be available at `http://localhost:8080`

5. API documentation is available at `http://localhost:8080/swagger-ui.html`

## API Overview

The application provides RESTful APIs for:

- **Drills**: Create and manage drills for different sports and focus areas
- **Practice Plans**: Generate and customize practice plans
- **Sports & Positions**: Manage sports and player positions
- **Focus Areas**: Define focus areas like fundamentals, offense, defense
- **Equipment**: Track equipment needs for drills and practice plans
- **KPIs**: Define and monitor key performance indicators

## Database Structure

The database consists of the following main entities:

- Sports (Basketball, Football, etc.)
- Positions (specific to each sport)
- Focus Areas (Fundamentals, Offense, Defense)
- Categories (within focus areas)
- Subcategories (within categories)
- KPIs (key performance indicators)
- Drills (with links to appropriate sports, positions, focus areas, etc.)
- Practice Plans (composed of selected drills in a specific sequence)
- Equipment (used by drills and tracked in practice plans)

## Docker Troubleshooting

If you encounter issues with the SQL Server Docker container:

1. Check container status:
```bash
docker ps -a
```

2. View container logs:
```bash
docker logs sql_server
```

3. If the container stops unexpectedly, ensure your Docker has enough memory allocated (at least 2GB for SQL Server)

4. Restart the container:
```bash
docker start sql_server
```

5. If you need to remove and recreate the container:
```bash
docker rm sql_server
# Then rerun the docker run command from the setup instructions
```

## Application Configuration

The application uses Spring profiles for different environments:

- **dev**: Development environment with local database
- **prod**: Production environment with configurable database and SSL

Configuration files are in `src/main/resources/application.yml`.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

[Your license information here]
