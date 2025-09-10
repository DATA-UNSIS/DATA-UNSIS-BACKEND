# 🧪 Pruebas del Backend DATA-UNSIS

![Status](https://img.shields.io/badge/Status-Active-success) ![Version](https://img.shields.io/badge/Version-1.0-blue) ![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen)

---

## 🚀 Cómo ejecutar los tests

Para ejecutar las pruebas del backend, sigue estos pasos:

1. **Configurar la base de datos** PostgreSQL
2. **Ejecutar el proyecto** con `./mvnw spring-boot:run`
3. **Probar los endpoints** usando los ejemplos de abajo

---

## 📋 Casos de Prueba

| 🔍 Caso | 🎯 Descripción | ⚡ Estado |
|---------|----------------|-----------|
| Test 1  | Todos los parámetros especificados | ✅ Pasando |
| Test 2  | Solo carreras y semestres | ✅ Pasando |
| Test 3  | Valores nulos | ✅ Pasando |

---

## 🛠️ Herramientas usadas

- **Spring Boot** - Framework principal
- **PostgreSQL** - Base de datos
- **Maven** - Gestión de dependencias
- **Swagger** - Documentación API

---
## 💻 Curls para Testing backend en shell (Consola)

> [!CAUTION]
> ⚠️ **Importante:** Se tiene que mandar por lo menos un title siempre

### 🔹 Test 1: Todos los parámetros especificados

> [!TIP]
> **Descripción:** Todas las carreras, titles, semestres especificando y también especificando un sexo 
> ```bash
> curl -X 'POST' \
>   'http://localhost:8080/data-unsis/api/execute-general-query' \
>   -H 'accept: */*' \
>   -H 'Content-Type: application/json' \
>   -d '{
>   "titles": [
>     "MAJOR_DISTRIBUTION", "ECONOMIC_LEVEL", "SCHOLARSHIPS_REQUESTED", "HOUSEHOLD_SERVICES", "CIVIL_STATE", "MUNICIPALITY_DISTRIBUTION", "SEMESTER_DISTRIBUTION", "SEX_DISTRIBUTION",
>     "TYPE_INSTITUTION_PROCEDENCY", "STATE_DISTRIBUTION"
>   ],
>   "majors": ["Licenciatura en Administración Municipal", "Licenciatura en Administración Pública", "Licenciatura en Ciencias Biomédicas", "Licenciatura en Ciencias Empresariales",
>               "Licenciatura en Enfermería", "Licenciatura en Informática", "Licenciatura en Medicina", "Licenciatura en Nutrición", "Licenciatura en Odontología"],
>   "semesters": ["Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto", "Séptimo", "Octavo", "Noveno", "Décimo", "Onceavo", "Doceavo"],
>   "sexo": "Mujer"
> }'
> ```

### 🔹 Test 2: Sin filtro de sexo

> [!TIP]
> **Descripción:** Todas las carreras, titles, semestres especificando y ambos sexos con null 
> ```bash
> curl -X 'POST' \
>   'http://localhost:8080/data-unsis/api/execute-general-query' \
>   -H 'accept: */*' \
>   -H 'Content-Type: application/json' \
>   -d '{
>   "titles": [
>     "MAJOR_DISTRIBUTION", "ECONOMIC_LEVEL", "SCHOLARSHIPS_REQUESTED", "HOUSEHOLD_SERVICES", "CIVIL_STATE", "MUNICIPALITY_DISTRIBUTION", "SEMESTER_DISTRIBUTION", "SEX_DISTRIBUTION",
>     "TYPE_INSTITUTION_PROCEDENCY", "STATE_DISTRIBUTION"
>   ],
>   "majors": ["Licenciatura en Administración Municipal", "Licenciatura en Administración Pública", "Licenciatura en Ciencias Biomédicas", "Licenciatura en Ciencias Empresariales",
>               "Licenciatura en Enfermería", "Licenciatura en Informática", "Licenciatura en Medicina", "Licenciatura en Nutrición", "Licenciatura en Odontología"],
>   "semesters": ["Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto", "Séptimo", "Octavo", "Noveno", "Décimo", "Onceavo", "Doceavo"],
>   "sexo": null
> }'
> ```

### 🔹 Test 3: Sin filtros

> [!TIP]
> **Descripción:** Todos los titles, carreras, semestres, sexos con null 
> ```bash
> curl -X 'POST' \
>   'http://localhost:8080/data-unsis/api/execute-general-query' \
>   -H 'accept: */*' \
>   -H 'Content-Type: application/json' \
>   -d '{
>   "titles": [
>     "MAJOR_DISTRIBUTION", "ECONOMIC_LEVEL", "SCHOLARSHIPS_REQUESTED", "HOUSEHOLD_SERVICES", "CIVIL_STATE", "MUNICIPALITY_DISTRIBUTION", "SEMESTER_DISTRIBUTION", "SEX_DISTRIBUTION",
>     "TYPE_INSTITUTION_PROCEDENCY", "STATE_DISTRIBUTION"
>   ],
>   "majors": null,
>   "semesters": null,
>   "sexo": null
> }'
> ```

---
>## 🌐 JSON para hacer testing desde Swagger

> [!CAUTION]
> ⚠️ **Importante:** Se tiene que mandar por lo menos un title siempre

### 📝 Test 1: Parámetros completos

> [!TIP]
> **Todas las carreras, titles, semestres especificando y también especificando un sexo**
> ```json
> {
>   "titles": [
>     "MAJOR_DISTRIBUTION", "ECONOMIC_LEVEL", "SCHOLARSHIPS_REQUESTED", "HOUSEHOLD_SERVICES", "CIVIL_STATE", "MUNICIPALITY_DISTRIBUTION", "SEMESTER_DISTRIBUTION", "SEX_DISTRIBUTION",
>     "TYPE_INSTITUTION_PROCEDENCY", "STATE_DISTRIBUTION"
>   ],
>   "majors": ["Licenciatura en Administración Municipal", "Licenciatura en Administración Pública", "Licenciatura en Ciencias Biomédicas", "Licenciatura en Ciencias Empresariales",
>               "Licenciatura en Enfermería", "Licenciatura en Informática", "Licenciatura en Medicina", "Licenciatura en Nutrición", "Licenciatura en Odontología"],
>   "semesters": ["Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto", "Séptimo", "Octavo", "Noveno", "Décimo", "Onceavo", "Doceavo"],
>   "sexo": "Mujer"
> }
> ```

### 📝 Test 2: Sin filtro de sexo

> [!TIP]
> **Todas las carreras, titles, semestres especificando y ambos sexos con null**
> ```json
> {
>   "titles": [
>     "MAJOR_DISTRIBUTION", "ECONOMIC_LEVEL", "SCHOLARSHIPS_REQUESTED", "HOUSEHOLD_SERVICES", "CIVIL_STATE", "MUNICIPALITY_DISTRIBUTION", "SEMESTER_DISTRIBUTION", "SEX_DISTRIBUTION",
>     "TYPE_INSTITUTION_PROCEDENCY", "STATE_DISTRIBUTION"
>   ],
>   "majors": ["Licenciatura en Administración Municipal", "Licenciatura en Administración Pública", "Licenciatura en Ciencias Biomédicas", "Licenciatura en Ciencias Empresariales",
>               "Licenciatura en Enfermería", "Licenciatura en Informática", "Licenciatura en Medicina", "Licenciatura en Nutrición", "Licenciatura en Odontología"],
>   "semesters": ["Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto", "Séptimo", "Octavo", "Noveno", "Décimo", "Onceavo", "Doceavo"],
>   "sexo": null
> }
> ```

### 📝 Test 3: Sin filtros

> [!TIP]
> **Todos los titles, carreras, semestres, sexos con null**
> ```json
> {
>   "titles": [
>     "MAJOR_DISTRIBUTION", "ECONOMIC_LEVEL", "SCHOLARSHIPS_REQUESTED", "HOUSEHOLD_SERVICES", "CIVIL_STATE", "MUNICIPALITY_DISTRIBUTION", "SEMESTER_DISTRIBUTION", "SEX_DISTRIBUTION",
>     "TYPE_INSTITUTION_PROCEDENCY", "STATE_DISTRIBUTION"
>   ],
>   "majors": null,
>   "semesters": null,
>   "sexo": null
> }
> ```

---

## 📊 Endpoints Disponibles
------------------------------------------------------------------------------------------
| 🎯 Endpoint                               | 📝 Descripción              | 🔧 Método     |
|-------------------------------------------|-----------------------------|---------------|
| `/data-unsis/api/execute-general-query`   | Ejecuta consultas generales | POST          |
| `/data-unsis/api/execute-dynamic-query`   | Ejecuta consultas dynamicas | POST          |
| `/swagger-ui`                             | Documentación interactiva   | GET           |
------------------------------------------------------------------------------------------
## 🎨 Títulos Disponibles

- 📈 `MAJOR_DISTRIBUTION` - Distribución por carrera
- 💰 `ECONOMIC_LEVEL` - Nivel económico  
- 🎓 `SCHOLARSHIPS_REQUESTED` - Becas solicitadas
- 🏠 `HOUSEHOLD_SERVICES` - Servicios del hogar
- 👥 `CIVIL_STATE` - Estado civil
- 🏙️ `MUNICIPALITY_DISTRIBUTION` - Distribución por municipio
- 📚 `SEMESTER_DISTRIBUTION` - Distribución por semestre
- ⚤ `SEX_DISTRIBUTION` - Distribución por sexo
- 🏫 `TYPE_INSTITUTION_PROCEDENCY` - Tipo de institución
- 📍 `STATE_DISTRIBUTION` - Distribución por estado