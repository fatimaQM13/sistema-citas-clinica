# Sistema de Gestión de Citas Médicas para Clínica

Sistema de administración médica desarrollado en Python con arquitectura orientada a objetos y ejecución interactiva en consola.

## Integrantes del Proyecto
- Jesús Alejandro Aranda Reyes
- Fátima Guadalupe Quirino Morales (fatimaQM13)

---

## Módulos y Reglas del Sistema

### 1. Pacientes
- Registro de pacientes con nombre y teléfono.
- Listado general y actualización de datos de contacto.
- Activación y desactivación lógica del paciente.
- Historial de citas médicas asociadas.
- **Validación:** No se permite eliminar un paciente que cuente con citas programadas activas.

### 2. Especialidades
- Registro de especialidades médicas.
- Visualización de especialidades disponibles.
- Actualización y eliminación de registros (con validación de dependencia).

### 3. Médicos
- Registro de médicos asociados a una especialidad existente.
- Cambio de estado (Activo / Inactivo).
- Visualización de citas programadas por médico.
- **Validación:** No se permite asignar citas a médicos inactivos.
- **Validación:** No se permite eliminar un médico que tenga citas activas.

### 4. Consultorios y Citas Médicas
- Programación, consulta, modificación y cancelación de citas médicas.
- **Validación de fechas:** No se permite registrar ni reprogramar citas en fechas u horas anteriores al momento actual.
- **Validación de médico:** Garantiza que un médico no tenga dos citas en el mismo horario.
- **Validación de consultorio:** Garantiza que un consultorio no sea asignado simultáneamente a diferentes pacientes.
- Registro de consultas realizadas y contador total del sistema.

---

## Estructura del Proyecto
```text
sistema-citas-clinica/
│
├── .gitignore       # Exclusión de temporales y entornos
├── README.md        # Documentación del proyecto
├── modelos.py       # Clases: Especialidad, Paciente, Medico, Cita
├── sistema.py       # Lógica del negocio, colecciones y validaciones
└── main.py          # Interfaz de consola interactiva por menús

## Interfaz Web
El proyecto cuenta con un dashboard visual interactivo desarrollado con Tailwind CSS. Para ejecutarlo:
1. Abrir directamente el archivo `index.html` en cualquier navegador web moderno (Chrome, Edge, Firefox).
2. O ejecutar en terminal:
```bash
start index.html
