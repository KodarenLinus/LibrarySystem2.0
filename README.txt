# Library System

Ett bibliotekssystem byggt med **JavaFX** för användargränssnittet och **MySQL** som databashanterare. Projektet är hanterat via **Maven** och innehåller funktioner för att hantera böcker, DVD:er, lån och sökningar i databasen.

---

## Krav

Innan du kan köra projektet behöver du ha följande installerat:

- Java Development Kit (JDK) 11 eller senare  
- Apache Maven  
- MySQL Server  
- En JavaFX-kompatibel IDE (t.ex. IntelliJ IDEA, Eclipse)

---

## Installation av beroenden (steg för steg)

Följ dessa steg för att säkerställa att alla beroenden är korrekt installerade via Maven.

### Steg 1: Kontrollera att Maven är installerat

mvn -v


Om kommandot inte känns igen:  
[Installera Maven](https://maven.apache.org/install.html)

### Steg 2: Lägg till beroenden i `pom.xml`

Se till att följande finns under `<dependencies>`:

#### MySQL JDBC Driver

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>9.2.0</version>
</dependency>


#### JavaFX GUI-stöd

<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>23.0.1</version>
</dependency>

<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>23.0.1</version>
</dependency>

### Steg 3: Lägg till JavaFX Maven-plugin

Lägg till följande i `pom.xml` under `<build><plugins>`:

<plugin>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-maven-plugin</artifactId>
    <version>0.0.8</version>
    <configuration>
        <mainClass>com.mycompany.library_system.Library_System</mainClass>
    </configuration>
</plugin>



### Steg 4: Uppdatera och bygg projektet

Kör följande kommando i terminalen eller använd "Reload Maven Project" i din IDE:

mvn clean install

## Databasinställningar

Ange dina inloggningsuppgifter i DatabaseConnection.java:

String url = "jdbc:mysql://localhost:3306/Din databas";
String user = "root";
String password = "ditt_lösenord";


## Vanliga problem

| Problem | Lösning |
|--------|---------|
| `Cannot connect to database` | Kontrollera databasuppgifter och att MySQL körs |
| `JavaFX runtime components are missing` | Kör via `mvn javafx:run` eller konfigurera JavaFX i din IDE |
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Kontrollera att `mysql-connector-j` är korrekt tillagd i `pom.xml` |




