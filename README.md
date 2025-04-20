# 💰 Sistema de Ahorro Digital Familiar

**Desarrollado por el equipo "Los Proactivos"**

## 📝 Descripción del Proyecto

El *Sistema de Ahorro Digital Familiar* es una aplicación de escritorio desarrollada en Java que tiene como objetivo facilitar la gestión organizada, colaborativa y transparente de los ahorros dentro de una familia. Actualmente, el desarrollo se encuentra en su **fase inicial**, centrado en la implementación de un sistema de **autenticación segura** para controlar el acceso a la plataforma según el rol del usuario.

## 🚀 Funcionalidades

### ✅ Implementadas (Fase Inicial)

- **Ingreso al Sistema:**  
  Permite a los usuarios acceder al sistema mediante sus credenciales (usuario y contraseña). El sistema reconoce el rol del usuario (Administrador, Integrante Familiar, Coordinador de Finanzas) y prepara el entorno para mostrar las funcionalidades futuras según su nivel de acceso.

### 📌 Funcionalidades Planificadas (Etapas Futuras)

- Registro de fuentes de ingreso por integrante.
- Registro de metas financieras familiares.
- Registro de aportes mensuales por miembro.
- Generación del flujo de caja mensual.
- Proyección del flujo de caja.
- Consulta del estado de las metas.
- Cierre de metas y reasignación de saldos.

## 🛠 Tecnologías Utilizadas

| Componente              | Tecnología            |
|-------------------------|-----------------------|
| Lenguaje de programación | Java SE               |
| Interfaz gráfica        | Swing (Java)          |
| Base de datos           | MySQL                 |
| Driver JDBC             | MySQL Connector/J     |
| Gestor de dependencias  | Maven                 |

## 💻 Requisitos del Sistema

- **Java JDK:** 11 o superior  
- **MySQL:** 5.7 o superior  
- **Maven:** Instalado en el entorno de desarrollo  

## 🗂 Estructura del Proyecto (Actual)

```
AHORROFAMILIAR/
├── database/
│   └── bd_ahorro.sql         # Script para crear la base de datos y las tablas iniciales (usuario, rol)
├── src/
│   └── main/
│       └── java/
│           └── com/ahorrofamiliar/
│               ├── models/          # Clases de entidades (Usuario, Rol)
│               ├── utils/           # Clases utilitarias (DatabaseConnection)
│               └── views/           # Interfaz gráfica (LoginView, App)
├── resources/                        # Archivos de configuración (vacío por ahora)
├── target/                           # Archivos generados por Maven
├── pom.xml                           # Archivo de configuración de Maven
├── README.md                         # Este documento
└── .gitignore                        # Archivos ignorados por Git
```

## ⚙️ Configuración Inicial

1. **Clona el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```

2. **Configura la base de datos:**
   - Asegúrate de que el servicio de MySQL esté activo.
   - Ejecuta el script `database/bd_ahorro.sql` para crear la base de datos `bd_ahorro`, la tabla `usuario` y `rol`.

3. **Instala las dependencias:**
   - Al compilar el proyecto con Maven, se descargará automáticamente el conector JDBC necesario desde el `pom.xml`.

## ▶️ Cómo Ejecutar la Aplicación (Fase Inicial)

1. Abre el proyecto en tu IDE (VS Code, IntelliJ, Eclipse, etc.).
2. Navega a la clase principal ubicada en `src/main/java/com/ahorrofamiliar/views/App.java`.
3. Haz clic derecho en la clase y selecciona **"Run"** o **"Debug"**.
4. Ingresa con las credenciales de prueba configuradas en la tabla `usuario`.

> Asegúrate de haber configurado correctamente la conexión a la base de datos en tu clase `DatabaseConnection`.

---

## 📌 Próximas Tareas Inmediatas

- Desarrollo completo de la interfaz de inicio de sesión con `Swing` (`LoginView.java`).
- Implementación del controlador de eventos de la vista (`LoginController.java`).
- Desarrollo del servicio de autenticación (`AuthService.java`) para validar credenciales y roles desde la base de datos.
- Pruebas de conexión mediante la clase `DatabaseConnection`.

---

## 👥 Equipo de Desarrollo

| Nombre completo                   | Rol                  | Participación |
|----------------------------------|-----------------------|---------------|
| **Elizabeth Alondra Aguilar Luza** | Delegada, Documentadora | 100%          |
| **Misael Fernando Challco**        | PMO                     | 100%          |
| **Torres Saavedra Jimmy**          | Integrador, Imagen      | 100%          |
| **Luces Martinez Jeremy**          |                         | 100%          |

---

¡Seguimos construyendo un sistema sólido, accesible y útil para la organización financiera familiar!

> *“Los pequeños esfuerzos diarios construyen grandes logros” – Los Proactivos*

---