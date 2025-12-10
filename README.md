# Package Sorting System

Sorts packages into stacks based on dimensions and weight.

## Sorting Rules

Packages are classified as:

- **Bulky**: volume ≥ 1,000,000 cm³ OR any dimension ≥ 150 cm
- **Heavy**: mass ≥ 20 kg

Stack assignment:

- `STANDARD` → neither bulky nor heavy
- `SPECIAL` → bulky OR heavy (but not both)
- `REJECTED` → both bulky AND heavy

## Run

```bash
mvn test        # Run tests
mvn exec:java   # Run demo
```

## Prerequisites

- Java 11+
- Maven 3.6+

Install on macOS:

```bash
brew install openjdk@11 maven
```
