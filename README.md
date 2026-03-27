# Hello World — Spring Boot WAR CI/CD Demo

A minimal Spring Boot application packaged as a **WAR** file, designed for a
CI/CD pipeline showcase: pull code → build → generate WAR → deploy on Tomcat.

---

## Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| GET | `/hello-world/api/hello` | Hello World response |
| GET | `/hello-world/api/health` | Health check (UP/DOWN) |
| GET | `/hello-world/api/info` | App version & server info |

---

## Build Locally

**Prerequisites:** Java 17+, Maven 3.8+

```bash
# Run tests
mvn clean test

# Build WAR (skipping tests)
mvn clean package -DskipTests

# WAR file location
ls target/hello-world.war
```

## Run Locally (embedded Tomcat)

```bash
mvn spring-boot:run
# Visit: http://localhost:8080/hello-world/api/hello
```

---

## Deploy on Tomcat

### Option A — Copy WAR manually
```bash
cp target/hello-world.war /opt/tomcat/webapps/
# Tomcat auto-deploys on file drop
```

### Option B — Tomcat Manager API (used in Jenkinsfile)
```bash
curl -u admin:password \
     -T target/hello-world.war \
     "http://localhost:8080/manager/text/deploy?path=/hello-world&update=true"
```

---

## CI/CD Pipeline (Jenkins)

See `Jenkinsfile` in the project root. The pipeline stages are:

1. **Checkout** — pulls code from the configured SCM repo
2. **Build & Test** — `mvn clean test`, publishes JUnit results
3. **Package WAR** — `mvn package`, archives `hello-world.war`
4. **Deploy to Tomcat** — pushes WAR via Tomcat Manager API
5. **Smoke Test** — hits `/api/health` to confirm successful deployment

### Jenkins Setup Checklist
- [ ] Configure **JDK 17** in Jenkins → Global Tool Configuration
- [ ] Configure **Maven 3** in Jenkins → Global Tool Configuration
- [ ] Add a **Username/Password** credential with ID `tomcat-deployer`
- [ ] Update `TOMCAT_URL` in the `Jenkinsfile` environment block
- [ ] Ensure Tomcat `conf/tomcat-users.xml` has a user with role `manager-script`

### Tomcat User Setup (`conf/tomcat-users.xml`)
```xml
<role rolename="manager-script"/>
<user username="deployer" password="yourpassword" roles="manager-script"/>
```

---

## Key WAR Configuration Points

| What | Where | Why |
|------|-------|-----|
| `<packaging>war</packaging>` | `pom.xml` | Tells Maven to produce a WAR |
| `spring-boot-starter-tomcat` scope=`provided` | `pom.xml` | Excludes embedded Tomcat from WAR |
| `extends SpringBootServletInitializer` | `HelloWorldApplication.java` | Lets external Tomcat bootstrap Spring |
| `configure()` override | `HelloWorldApplication.java` | Required WAR entry point for Tomcat |
