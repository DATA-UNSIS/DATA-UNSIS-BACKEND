>[!NOTE]
>## NULL en GeneralQueryRequest
>Para el GeneralQueryRequest validar cuando
>el campo semester y major sea null 
>### null=todos:todos los semestres:todas las carreras:ambos sexos

>[!IMPORTANT]
>## Creación de ramas con GitFlow
>### Instalación de GitFlow
>```bash
>sudo apt install git-flow
>```
>### Uso de GitFlow
>```bash
>/directorio/del/proyecto$ git flow init
>```
>**Te saldrá algo así**
>```bash
>Inicializado repositorio Git vacío en /home/chay/new/.git/
>No branches exist yet. Base branches must be created now.
>Branch name for production releases: [master] 
>Branch name for "next release" development: [develop] 
>
>How to name your supporting branch prefixes?
>Feature branches? [feature/] 
>Bugfix branches? [bugfix/] 
>Release branches? [release/] 
>Hotfix branches? [hotfix/] 
>Support branches? [support/] 
>Version tag prefix? [] 
>Hooks and filters directory? [/home/chay/new/.git/hooks] 
>```
>Le vas dando enter a todo lo que te pregunte

>[!WARNING]
>Si en **Release branches? [release/]** te da error escribe **dev** y da enter

>[!TIP]
>#### Creación de ramas con comandos
>**Para implementar nueva funcionalidad desde la dev (feature)**
>```bash
>/directorio/del/proyecto$ git flow feature start NUB-X/ENUM-TITLE (ej: NUB-3/civil-state)
>```
>**Para resolver bug en la dev (bug normal):**
>```bash 
>/directorio/del/proyecto$ git flow bugfix start NUB-X/ENUM-TITLE (ej: NUB-3/civil-state)
>```
>**Para resolver bug en master (bug de producción)**
>```bash
>/directorio/del/proyecto$ git flow hotfix start NUB-X/ENUM-TITLE (ej: NUB-3/civil-state)
>```

>[!CAUTION]
>**Si quieres realizar una contribución a este repositorio**
>
>El repositorio cuenta con protección de ramas en master y dev
>La rama dev es en la que trabajaremos con Git Flow, puedes clonar este proyecto
>y realizar lo siguiente para hacer tu contribución

>[!TIP]
>Clona el repositorio, crea tu rama con gitflow y al terminar lo que deseas aportar
>haz un **Pull Request** al repositorio con tu rama antes creada, alguno de nuestros
>colaboradores revisara tu propuesta de implementación y puede aprobarla, rechazarla
>o proporcionarte comentarios de mejora para poder hacer el merge en dev

>[!CAUTION]
>Asegurate de que  tu **Pull Request** este apuntando a dev, de lo contrario será
>descartado automáticamente.

>[!IMPORTANT]
>### Estructuras de commits, con conventional commits y el código de tarea en jira
>git commit -m "feat: #NUB-13 se cambia el README"
>feat(nueva feature), fix(bug), refactor(mejorar código), build(dependencias), chore(cambio que no afecte funcionalidad o código muerto(ej: quitar un import que da warnings porque no se usa))
## 📋 Documentación y test

- [📖 README](README.md) - Información general del proyecto
- [🧪 Guía de Testing](TEST-BACKEND.md) - Pruebas del backend
## 🧪 Testing

Para ver la guía completa de pruebas, consulta [TEST-BACKEND.md](TEST-BACKEND.md)

### Prueba rápida
```bash
curl -X POST http://localhost:8080/api/query \
  -H "Content-Type: application/json" \
  -d '{"titles": ["MUNICIPALITY_DISTRIBUTION"]}'