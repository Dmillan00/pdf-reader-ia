# 📄 Asistente de IA para la Lectura de Documentos PDF

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-blue?logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green)]()

Aplicación web desarrollada con **Spring Boot** que permite a los usuarios subir un PDF e interactuar con él a través de un modelo de IA **Ollama** ejecutado localmente.

## 📋 Tabla de Contenidos

- [✨ Características](#-características)
- [🛠️ Tecnologías](#️-tecnologías)
- [⚙️ Requisitos Previos](#️-requisitos-previos)
- [📦 Instalación](#-instalación)
- [🚀 Ejecución](#-ejecución)
- [📖 Funcionamiento](#-funcionamiento)
- [📁 Estructura del Proyecto](#-estructura-del-proyecto)
- [👨‍💻 Autor](#-autor)

---

## ✨ Características

- 🔒 **Privacidad Local**: El asistente funciona usando un modelo IA predescargado sin necesidad de internet. Procesa información únicamente basada en el modelo local y en los documentos que subas.

- 🔐 **Sesión Privada**: Cada usuario recibe una sesión única mediante `HttpSession` con historial de conversación personal.

- 📚 **Procesamiento de Documentos**: Los PDFs se fragmentan automáticamente y se convierten en vectores para búsqueda semántica.

- 💬 **Chat Inteligente**: Respuestas contextuales basadas en el contenido de tu PDF.

---

## 🛠️ Tecnologías

| Capa | Tecnología |
|------|-----------|
| **Backend** | Java 21, Spring Boot 3.x, Spring AI |
| **Base de Datos** | PostgreSQL 16 + extensión `pgvector` (Docker) |
| **Modelo IA** | Ollama (Llama3) |
| **Frontend** | HTML5, Thymeleaf, Bootstrap 5, Font Awesome |

---

## ⚙️ Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **Java 21** o superior 
- **Docker y Docker Compose** 
- **Ollama** con modelo Llama3 

### Verificar instalaciones

```bash
java --version
docker --version
ollama --version
```

---

## 📦 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Dmillan00/pdf-reader.git
cd pdf-reader
```

### 2. Levantar la Base de Datos

La aplicación usa **PostgreSQL** con la extensión `pgvector` para almacenar vectores. Asegúrate de tener Docker abierto y ejecuta:

```bash
docker-compose up -d
```

Esto levantará un contenedor de PostgreSQL en segundo plano.

### 3. Configurar las Variables de Entorno

Edita `src/main/resources/application.properties` según tu entorno:

#### Base de Datos (PostgreSQL + pgvector)

```properties
# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/pdf_reader
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true
```

#### Modelo de IA (Ollama)

```properties
# Ollama Configuration
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=llama3
spring.ai.ollama.embedding.enabled=true
spring.ai.ollama.embedding.model=nomic-embed-text
```

> **⚠️ Nota**: Asegúrate de que Ollama se esté ejecutando localmente. Descarga el modelo ejecutando:
> ```bash
> ollama run llama3
> ```

---

## 🚀 Ejecución

### Opción 1: Desde tu IDE (Recomendado)

1. Abre el proyecto en **IntelliJ IDEA** o **Eclipse**
2. Localiza la clase principal (`Application.java` o similar con método `main`)
3. Haz clic en el botón verde ▶️ **Run**
4. Una vez iniciado, accede a: **http://localhost:8080**

### Opción 2: Desde Línea de Comandos

```bash
# Si usas Maven
mvn spring-boot:run

# Si usas Gradle
gradle bootRun
```

### Verificar que todo funciona

La consola mostrará:

```
Started Application in X.XXX seconds
INFO: Tomcat started on port(s): 8080 (http) with context path ''
```

Luego accede a **http://localhost:8080** en tu navegador.

---

## 📖 Funcionamiento

1. El usuario sube un PDF a través de la interfaz web
2. El backend procesa el PDF dividiéndolo en fragmentos (chunks)
3. Cada fragmento se convierte en un vector (embedding) usando Ollama
4. Los vectores se almacenan en la base de datos PostgreSQL (pgvector)
5. Cuando el usuario realiza una pregunta, se buscan los fragmentos más relevantes semánticamente
6. La pregunta + fragmentos relevantes se envían al modelo Llama3
7. La IA genera una respuesta basada en el contexto del documento

---

## 🔧 Solución de Problemas

### PostgreSQL no se conecta

```bash
# Verifica que el contenedor está corriendo
docker ps

# Ver logs del contenedor
docker logs postgres_container_name
```

### Ollama no responde

```bash
# Verifica que Ollama está corriendo
ollama list

# Reinicia Ollama
ollama serve
```

### Puerto 8080 ya en uso

Cambia el puerto en `application.properties`:

```properties
server.port=8081
```

---

## 📝 Licencia

Este proyecto está bajo la licencia **MIT**. Ver archivo `LICENSE` para más detalles.

---

## 👨‍💻 Autor

**Daniel Millán**

- GitHub: [@Dmillan00](https://github.com/Dmillan00)
- LinkedIn: [Daniel Millán](https://www.linkedin.com/in/danielmillan0/)
- Email: [danielmillan222@gmail.com](mailto:danielmillan222@gmail.com)

---


