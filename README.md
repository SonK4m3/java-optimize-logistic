# java-optimize-logistic

## Core Logic for Optimizing Logistic System

### Main.java

The `Main.java` file is the entry point of the application. It initializes the logistic system by creating depots, customers, and vehicles. It then uses the `VRPSolver` to find an optimal solution for the Vehicle Routing Problem (VRP) using the Tabu Search Algorithm.

#### Description

- **Input**: Lists of depots, customers, and vehicles.
- **Output**: An optimal solution for the VRP, displayed in the log file.

#### Core Logic

1. Initialize the display to log the output.
2. Create lists of depots, customers, and vehicles.
3. Display the locations of depots, customers, and vehicles.
4. Initialize the `VRPSolver` with a specified Tabu list size.
5. Solve the VRP using the Tabu Search Algorithm and display the initial and optimal solutions.
6. Measure and log the execution time of the solving task.

### Solver.java

The `Solver.java` file defines a generic solver that uses a specified planning algorithm to solve a given planning solution.

#### Description

- **Input**: An initial planning solution and a planning algorithm.
- **Output**: The solved planning solution.

#### Core Logic

1. Initialize the solver with or without a planning algorithm.
2. Validate the initial solution and algorithm.
3. Use the specified algorithm to solve the initial solution.
4. Return the solved planning solution.

## Managing Logistic System Using Spring

### OptPlanApplication.java

The `OptPlanApplication.java` file is the main class for the Spring Boot application. It configures and runs the application, enabling caching and asynchronous processing.

#### Description

- **Goal**: To manage and optimize the logistic system using Spring Boot.
- **Requirements**:
  - Java 8 or higher
  - Spring Boot 2.5.4 or higher
- **Stack**:
  - Spring Data JPA
  - PostgreSQL
  - Spring Security
  - JWT (JSON Web Token)
  - Spring Validation

#### Core Logic

1. Enable caching and asynchronous processing.
2. Configure component scanning for the specified packages.
3. Run the Spring Boot application.
