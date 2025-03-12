# Practice Plan Generator

Application to generate practice plans for youth sports.

## Overview

This application allows coaches to:

- Define sports, positions, and focus areas (fundamentals, offense, defense)
- Create categories and subcategories for organizing performance indicators
- Define key performance indicators (KPIs) for measuring athlete skills
- Create and organize drills that target specific KPIs
- Track equipment needed for drills
- Create practice plans with organized sequences of drills
- Get a summary of equipment needed for practice plans

## Technology Stack

- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- MySQL Database
- Maven
- Lombok
- MapStruct
- SpringDoc OpenAPI (Swagger)

## Database Schema

The application uses the following database tables:

- `sports`: Different sports
- `positions`: Positions within each sport
- `focus_areas`: Areas of focus (fundamentals, offense, defense)
- `categories`: Categories of skills within each sport and focus area
- `subcategories`: Sub-Categories within each category
- `kpis`: Key Performance Indicators to measure
- `equipment`: Equipment items used in drills
- `drills`: Training exercises targeting specific KPIs
- `drill_sports`: Association between drills and the sports they apply to
- `drill_positions`: Association between drills and the positions they apply to
- `drill_kpis`: Association between drills and the KPIs they improve (with impact level)
- `drill_equipment`: Equipment needed for each drill (with quantity)
- `practice_plans`: Organized plans for training sessions
- `practice_plan_drills`: Association between practice plans and drills (with sequence and duration)

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

### Database Setup

1. Create a MySQL database:

```sql
CREATE DATABASE sports_kpi_dev;
CREATE USER 'dev_user'@'localhost' IDENTIFIED BY 'dev_password';
GRANT ALL PRIVILEGES ON sports_kpi_dev.* TO 'dev_user'@'localhost';
FLUSH PRIVILEGES;
```

2. The application will automatically create the database schema using `schema.sql` when it starts in development mode.

### Building the Application

```bash
mvn clean package
```

### Running the Application

```bash
# Development mode
java -jar target/sports-kpi-tracker-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

# Production mode
java -jar target/sports-kpi-tracker-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## API Documentation

Once the application is running, you can access the Swagger UI to explore and test the API endpoints:

```
http://localhost:8080/swagger-ui.html
```

## API Endpoints

The application provides the following API endpoints:

### Sports Management
- `GET /api/sports`: Get all sports
- `GET /api/sports/{id}`: Get a sport by ID
- `POST /api/sports`: Create a new sport
- `PUT /api/sports/{id}`: Update a sport
- `DELETE /api/sports/{id}`: Delete a sport

### Positions Management
- `GET /api/positions`: Get all positions
- `GET /api/positions/{id}`: Get a position by ID
- `GET /api/positions/sport/{sportId}`: Get positions by sport ID
- `POST /api/positions`: Create a new position
- `PUT /api/positions/{id}`: Update a position
- `DELETE /api/positions/{id}`: Delete a position

### Focus Areas Management
- `GET /api/focus-areas`: Get all focus areas
- `GET /api/focus-areas/{id}`: Get a focus area by ID
- `POST /api/focus-areas`: Create a new focus area
- `PUT /api/focus-areas/{id}`: Update a focus area
- `DELETE /api/focus-areas/{id}`: Delete a focus area

### Categories Management
- `GET /api/categories`: Get all categories
- `GET /api/categories/{id}`: Get a category by ID
- `GET /api/categories/sport/{sportId}`: Get categories by sport ID
- `GET /api/categories/focus-area/{focusAreaId}`: Get categories by focus area ID
- `GET /api/categories/sport/{sportId}/focus-area/{focusAreaId}`: Get categories by sport ID and focus area ID
- `POST /api/categories`: Create a new category
- `PUT /api/categories/{id}`: Update a category
- `DELETE /api/categories/{id}`: Delete a category

### Sub-Categories Management
- `GET /api/subcategories`: Get all subcategories
- `GET /api/subcategories/{id}`: Get a subcategory by ID
- `GET /api/subcategories/category/{categoryId}`: Get subcategories by category ID
- `POST /api/subcategories`: Create a new subcategory
- `PUT /api/subcategories/{id}`: Update a subcategory
- `DELETE /api/subcategories/{id}`: Delete a subcategory

### KPIs Management
- `GET /api/kpis`: Get all KPIs
- `GET /api/kpis/{id}`: Get a KPI by ID
- `GET /api/kpis/subcategory/{subcategoryId}`: Get KPIs by subcategory ID
- `GET /api/kpis/sport/{sportId}`: Get KPIs by sport ID
- `GET /api/kpis/focus-area/{focusAreaId}`: Get KPIs by focus area ID
- `POST /api/kpis`: Create a new KPI
- `PUT /api/kpis/{id}`: Update a KPI
- `DELETE /api/kpis/{id}`: Delete a KPI

### Equipment Management
- `GET /api/equipment`: Get all equipment
- `GET /api/equipment/{id}`: Get an equipment by ID
- `POST /api/equipment`: Create a new equipment
- `PUT /api/equipment/{id}`: Update an equipment
- `DELETE /api/equipment/{id}`: Delete an equipment

### Drills Management
- `GET /api/drills`: Get all drills
- `GET /api/drills/{id}`: Get a drill by ID
- `GET /api/drills/{id}/details`: Get drill details by ID
- `GET /api/drills/focus-area/{focusAreaId}`: Get drills by focus area ID
- `GET /api/drills/sport/{sportId}`: Get drills by sport ID
- `GET /api/drills/position/{positionId}`: Get drills by position ID
- `GET /api/drills/kpi/{kpiId}`: Get drills by KPI ID
- `GET /api/drills/sport/{sportId}/focus-area/{focusAreaId}`: Get drills by sport ID and focus area ID
- `POST /api/drills`: Create a new drill
- `PUT /api/drills/{id}`: Update a drill
- `DELETE /api/drills/{id}`: Delete a drill
- `POST /api/drills/{id}/equipment`: Add equipment to a drill
- `DELETE /api/drills/{drillId}/equipment/{equipmentId}`: Remove equipment from a drill
- `GET /api/drills/{id}/equipment`: Get equipment for a drill

### Practice Plans Management
- `GET /api/practice-plans`: Get all practice plans
- `GET /api/practice-plans/{id}`: Get a practice plan by ID
- `GET /api/practice-plans/sport/{sportId}`: Get practice plans by sport ID
- `GET /api/practice-plans/focus-area/{focusAreaId}`: Get practice plans by focus area ID
- `GET /api/practice-plans/sport/{sportId}/focus-area/{focusAreaId}`: Get practice plans by sport ID and focus area ID
- `POST /api/practice-plans`: Create a new practice plan
- `PUT /api/practice-plans/{id}`: Update a practice plan
- `DELETE /api/practice-plans/{id}`: Delete a practice plan
- `POST /api/practice-plans/{id}/drills`: Add a drill to a practice plan
- `DELETE /api/practice-plans/{practicePlanId}/drills/{practicePlanDrillId}`: Remove a drill from a practice plan
- `PUT /api/practice-plans/{id}/drills/reorder`: Reorder drills in a practice plan
- `GET /api/practice-plans/{id}/equipment`: Get equipment summary for a practice plan

## License

[MIT License](LICENSE)