# Vehicle Management System (Java)

A terminal-based Java project built from the original vehicle/OOP program and expanded into a small, usable application.

## Features

- Add standard cars and luxury cars
- List all registered vehicles
- Search by vehicle registration number
- Remove a vehicle
- Start/stop a vehicle
- Display vehicle features
- Saves records to `data/vehicles.csv` so they remain after restarting
- Demonstrates abstraction, inheritance, interfaces, method overloading/overriding, polymorphism, threads, `wait()` / `notifyAll()`, and an optional deadlock demo
- Rejects duplicate registration numbers and invalid manufacturing years

## Requirements

- Java JDK 17 or newer
- Terminal / Command Prompt

Check Java is installed:

```bash
java -version
javac -version
```

## Run in VS Code or a terminal

Open the project folder, then run the following from its root directory.

### Windows

```bat
javac -d out src\vehicle\Vehicle.java src\vehicle\VehicleFeatures.java src\vehicle\Car.java src\vehicle\LuxuryCar.java src\main\Main.java
java -cp out main.Main
```

### macOS / Linux

```bash
javac -d out src/vehicle/Vehicle.java src/vehicle/VehicleFeatures.java src/vehicle/Car.java src/vehicle/LuxuryCar.java src/main/Main.java
java -cp out main.Main
```

Use the numbered menu to interact with the application. The `data` folder and saved file are created automatically when the app saves records.

## Optional deadlock demonstration

Run this separately:

```bash
java -cp out main.Main --deadlock-demo
```

The demo intentionally creates a deadlock to illustrate the concept. It uses daemon threads so it does not keep the process alive indefinitely. Do not use this pattern in production applications.

## Project structure

```text
vehicle-management-system/
├── src/
│   ├── main/
│   │   └── Main.java
│   └── vehicle/
│       ├── Vehicle.java
│       ├── VehicleFeatures.java
│       ├── Car.java
│       └── LuxuryCar.java
├── data/                 # Created automatically at runtime
├── .gitignore
└── README.md
```

## OOP concepts demonstrated

| Concept | Where |
|---|---|
| Abstraction | `Vehicle` is abstract |
| Inheritance | `Car extends Vehicle`, `LuxuryCar extends Car` |
| Interface | `Car implements VehicleFeatures` |
| Method overriding | `displayVehicleInfo()` and `showFeatures()` |
| Method overloading | `displayVehicleInfo()` methods in `Vehicle` |
| Polymorphism | Vehicle list stores `Car` and `LuxuryCar` objects |
| Multithreading | Feature and start methods run in separate threads |
| Synchronization | `Signal` uses synchronized methods and `wait()` / `notifyAll()` |
| Persistence | Vehicle details are saved to a local file |

## Data and privacy

Vehicle records are stored locally in `data/vehicles.csv`. The app does not connect to a server or upload data. Avoid adding real personal information to public repositories.
