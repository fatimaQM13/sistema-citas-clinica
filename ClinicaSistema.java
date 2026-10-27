import java.time.LocalDateTime;
import java.util.*;

public class ClinicaSistema {
    private Map<Integer, Especialidad> especialidades = new HashMap<>();
    private Map<Integer, Medico> medicos = new HashMap<>();
    private Map<Integer, Paciente> pacientes = new HashMap<>();
    private Map<Integer, Cita> citas = new HashMap<>();

    private int nextEspId = 1;
    private int nextMedId = 1;
    private int nextPacId = 1;
    private int nextCitaId = 1;

    // --- 2. ESPECIALIDADES ---
    public Especialidad agregarEspecialidad(String nombre) {
        Especialidad e = new Especialidad(nextEspId, nombre.trim());
        especialidades.put(nextEspId++, e);
        return e;
    }

    public List<Especialidad> listarEspecialidades() {
        return new ArrayList<>(especialidades.values());
    }

    public Especialidad obtenerEspecialidad(int id) {
        return especialidades.get(id);
    }

    public boolean actualizarEspecialidad(int id, String nuevoNombre) {
        Especialidad e = especialidades.get(id);
        if (e != null) {
            e.setNombre(nuevoNombre.trim());
            return true;
        }
        return false;
    }

    public String eliminarEspecialidad(int id) {
        for (Medico m : medicos.values()) {
            if (m.getEspecialidad().getId() == id) {
                return "Error: No se puede eliminar, existen medicos con esta especialidad.";
            }
        }
        if (especialidades.remove(id) != null) {
            return "Especialidad eliminada correctamente.";
        }
        return "Error: Especialidad no encontrada.";
    }

    // --- 3. MÉDICOS ---
    public String agregarMedico(String nombre, int idEspecialidad) {
        Especialidad esp = obtenerEspecialidad(idEspecialidad);
        if (esp == null) return "Error: La especialidad indicada no existe.";
        Medico m = new Medico(nextMedId, nombre.trim(), esp);
        medicos.put(nextMedId++, m);
        return "Medico registrado con exito con ID #" + m.getId();
    }

    public List<Medico> listarMedicos() {
        return new ArrayList<>(medicos.values());
    }

    public Medico obtenerMedico(int id) {
        return medicos.get(id);
    }

    public boolean actualizarMedico(int id, String nuevoNombre, Integer nuevaEspId) {
        Medico m = medicos.get(id);
        if (m == null) return false;
        if (nuevoNombre != null && !nuevoNombre.isEmpty()) m.setNombre(nuevoNombre.trim());
        if (nuevaEspId != null) {
            Especialidad esp = obtenerEspecialidad(nuevaEspId);
            if (esp != null) m.setEspecialidad(esp);
        }
        return true;
    }

    public boolean cambiarEstadoMedico(int id, boolean activo) {
        Medico m = medicos.get(id);
        if (m == null) return false;
        m.setActivo(activo);
        return true;
    }

    public String eliminarMedico(int id) {
        Medico m = medicos.get(id);
        if (m == null) return "Error: Medico no encontrado.";
        for (Cita c : citas.values()) {
            if (c.getMedico().getId() == id && c.getEstado().equals("PROGRAMADA")) {
                return "Error: No se puede eliminar un medico con citas activas (programadas).";
            }
        }
        medicos.remove(id);
        return "Medico eliminado con exito.";
    }

    public List<Cita> citasDelMedico(int id) {
        List<Cita> res = new ArrayList<>();
        for (Cita c : citas.values()) {
            if (c.getMedico().getId() == id) res.add(c);
        }
        return res;
    }

    // --- 1. PACIENTES ---
    public Paciente agregarPaciente(String nombre, String telefono) {
        Paciente p = new Paciente(nextPacId, nombre.trim(), telefono.trim());
        pacientes.put(nextPacId++, p);
        return p;
    }

    public List<Paciente> listarPacientes() {
        return new ArrayList<>(pacientes.values());
    }

    public Paciente obtenerPaciente(int id) {
        return pacientes.get(id);
    }

    public boolean actualizarPaciente(int id, String nombre, String telefono) {
        Paciente p = pacientes.get(id);
        if (p == null) return false;
        if (nombre != null && !nombre.isEmpty()) p.setNombre(nombre.trim());
        if (telefono != null && !telefono.isEmpty()) p.setTelefono(telefono.trim());
        return true;
    }

    public boolean cambiarEstadoPaciente(int id, boolean activo) {
        Paciente p = pacientes.get(id);
        if (p == null) return false;
        p.setActivo(activo);
        return true;
    }

    public String eliminarPaciente(int id) {
        Paciente p = pacientes.get(id);
        if (p == null) return "Error: Paciente no encontrado.";
        for (Cita c : citas.values()) {
            if (c.getPaciente().getId() == id && c.getEstado().equals("PROGRAMADA")) {
                return "Error: No se puede eliminar un paciente con citas activas (programadas).";
            }
        }
        pacientes.remove(id);
        return "Paciente eliminado con exito.";
    }

    public List<Cita> citasDelPaciente(int id) {
        List<Cita> res = new ArrayList<>();
        for (Cita c : citas.values()) {
            if (c.getPaciente().getId() == id) res.add(c);
        }
        return res;
    }

    // --- 4. CITAS Y CONSULTORIOS ---
    public String registrarCita(int idPac, int idMed, String consultorio, LocalDateTime fechaHora) {
        Paciente p = obtenerPaciente(idPac);
        if (p == null) return "Error: El paciente no existe.";
        if (!p.isActivo()) return "Error: El paciente se encuentra inactivo.";

        Medico m = obtenerMedico(idMed);
        if (m == null) return "Error: El medico no existe.";
        if (!m.isActivo()) return "Error: No se puede asignar cita a medicos inactivos.";

        if (fechaHora.isBefore(LocalDateTime.now())) {
            return "Error: No se puede registrar una cita con fecha/hora anterior a la actual.";
        }

        for (Cita c : citas.values()) {
            if (c.getEstado().equals("PROGRAMADA") && c.getFechaHora().isEqual(fechaHora)) {
                if (c.getMedico().getId() == idMed) {
                    return "Error: El medico ya tiene una cita asignada en ese mismo horario.";
                }
                if (c.getConsultorio().equalsIgnoreCase(consultorio.trim())) {
                    return "Error: El consultorio '" + consultorio + "' ya esta ocupado en ese horario.";
                }
            }
        }

        Cita nueva = new Cita(nextCitaId, p, m, consultorio.trim().toUpperCase(), fechaHora);
        citas.put(nextCitaId++, nueva);
        return "Cita programada con exito con ID #" + nueva.getId();
    }

    public List<Cita> listarCitas() {
        return new ArrayList<>(citas.values());
    }

    public String actualizarCita(int idCita, LocalDateTime nuevaFecha, String nuevoCons) {
        Cita c = citas.get(idCita);
        if (c == null) return "Error: Cita no encontrada.";
        if (!c.getEstado().equals("PROGRAMADA")) return "Error: Solo se pueden modificar citas programadas.";

        LocalDateTime fechaValidar = (nuevaFecha != null) ? nuevaFecha : c.getFechaHora();
        String consValidar = (nuevoCons != null && !nuevoCons.isEmpty()) ? nuevoCons.trim().toUpperCase() : c.getConsultorio();

        if (nuevaFecha != null && nuevaFecha.isBefore(LocalDateTime.now())) {
            return "Error: La nueva fecha no puede ser anterior a la actual.";
        }

        for (Cita otra : citas.values()) {
            if (otra.getId() != idCita && otra.getEstado().equals("PROGRAMADA") && otra.getFechaHora().isEqual(fechaValidar)) {
                if (otra.getMedico().getId() == c.getMedico().getId()) {
                    return "Error: Empalme de horario con otra cita del medico.";
                }
                if (otra.getConsultorio().equalsIgnoreCase(consValidar)) {
                    return "Error: El consultorio '" + consValidar + "' ya esta ocupado en ese horario.";
                }
            }
        }

        if (nuevaFecha != null) c.setFechaHora(nuevaFecha);
        if (nuevoCons != null && !nuevoCons.isEmpty()) c.setConsultorio(consValidar);
        return "Cita modificada con exito.";
    }

    public boolean cancelarCita(int id) {
        Cita c = citas.get(id);
        if (c != null) {
            c.setEstado("CANCELADA");
            return true;
        }
        return false;
    }

    public boolean completarCita(int id) {
        Cita c = citas.get(id);
        if (c != null) {
            c.setEstado("REALIZADA");
            return true;
        }
        return false;
    }

    public long totalConsultasRealizadas() {
        return citas.values().stream().filter(c -> c.getEstado().equals("REALIZADA")).count();
    }
}