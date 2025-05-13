
# 🚀 Ahorro Familiar

¡Bienvenido a **Ahorro Familiar**! 🎉  
Esta aplicación de escritorio en **Java Swing** + **MySQL** te ayuda a gestionar metas de ahorro, aportes y usuarios de forma sencilla, práctica y colaborativa. Perfecto para familias, grupos de amigos o equipos de trabajo.

---

## ✨ Características principales

- 🔐 **Usuarios y roles**  
  - Administra quién puede ver y hacer qué (Administrador, Integrante, Coordinador).  
- 🏆 **Metas de ahorro**  
  - Crea y sigue tus objetivos: monto, fecha límite y progreso.  
- 💸 **Aportes**  
  - Registra cada contribución: monto, fecha y fuente de ingreso.  
- 📊 **Fuentes de ingreso**  
  - Define de dónde viene el dinero (salario, regalos, ventas, etc.).  
- 🚦 **Control de estado**  
  - Activa o inhabilita usuarios con un clic.  
- 🖼️ **Interfaz amigable**  
  - Menús claros y formularios intuitivos en Java Swing.

---

## 📂 Estructura del proyecto

```

/database
└─ script\_bd\_ahorro.sql      # SQL para crear BD y tablas
/pom.xml                       # Dependencias y configuración Maven
/src
/main
/java
/com/ahorrofamiliar
/dao      # Clases para acceder a la BD
/dto      # Objetos de transferencia de datos
/models   # Entidades: Usuario, Meta, Aporte…
/service  # Lógica de negocio
/utils    # Helpers y conexión a BD
/views    # Formularios y componentes Swing

````

---

## ⚙️ Requisitos

- **Java JDK 17+**  
- **MySQL Server 8.x**  
- **Maven** (recomendado, aunque puedes compilar con tu IDE)  
- Librerías externas:
  - `mysql-connector-j-8.0.33.jar`  
  - `AbsoluteLayout-RELEASE120.jar`  
  - `protobuf-java-3.21.9.jar`

---

## 🚀 Instalación rápida

1. **Clona el repo**  
   ```bash
   git clone https://github.com/Ourtbitch/PROYECTOFAMILIARJAVA.git
   cd PROYECTOFAMILIARJAVA
````

2. **Importa el proyecto**

   * En NetBeans (o tu IDE favorito) como proyecto Maven.
3. **Crea la base de datos**

   * Ejecuta `/database/script_bd_ahorro.sql` en tu MySQL.
   * Ajusta usuario/contra en `DatabaseConnection.java`.
4. **Compila y corre**

   ```bash
   mvn clean install
   ```

   * O directamente ejecuta `LoginView.java` desde el IDE.

---

## 🎯 Primeros pasos de uso

1. **Login**:

   * Si eres nuevo, regístrate con tu rol (Admin, Integrante o Coordinador).
2. **Menú principal**:

   * Elige entre Usuarios, Metas, Aportes o Fuentes de Ingreso.
3. **Crear meta**:

   * Define nombre, monto objetivo y fecha límite.
4. **Registrar aporte**:

   * Selecciona la meta y añade tu contribución.
5. **Gestionar usuarios**:

   * Edita datos, roles o inhabilita a quien ya no participe.

---

## 🤝 Contribuye y colabora

¡Me encantaría ver tus mejoras!

1. Haz un **fork** del proyecto.
2. Crea una rama nueva:

   ```bash
   git checkout -b feature/mi-idea
   ```
3. Realiza tus cambios y haz **commit**.
4. Abre un **pull request** describiendo qué has añadido o corregido.

---

## 📄 Licencia

Este proyecto está bajo **Licencia MIT**.
Consulta el archivo `LICENSE` para más detalles.

---
