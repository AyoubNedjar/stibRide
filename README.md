# STIB RIDE

**STIB RIDE** is a Java/JavaFX application designed to help users find routes between metro stations in Brussels.

## Overview

- **Route Finding:** Navigate between a source station and a destination station.
- **Shortcuts Management:** Create, name, and load shortcuts for frequent trips.
- **Network Navigation:** Explore the STIB network map.
- **Language Switching:** Change station names between French and Dutch through the Options menu.

## Setup and Build

To build the project:

1. Ensure Maven is installed on your system.
2. Run the Makefile using the command:

   ```bash
   make

   This will compile the project.

How to Use
Launch the application.
Access the Help menu for detailed instructions on how to use the application.
Notes
Data is managed using an SQL database.
Shortcuts are stored locally in a database.
The station search feature utilizes JavaFX Searchable ComboBox, which may have some display bugs.
The application does not support responsive design.
Development Details
Design Patterns: MVC, Facade, Observable-Observer.
Concurrency: Uses multithreading.
Database Handling: Utilizes DAO, DTO, and repositories.
JavaFX: Views are implemented with FXML.
Testing: Includes unit tests with JUnit5 and Mockito.
Project Management: Maven-based.
Author
Nedjar Ayoub
