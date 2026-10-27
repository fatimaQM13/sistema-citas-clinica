import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Especialidad {
    private int id;
    private String nombre;

    public Especialidad(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre;
    }
}

class Paciente {
    private int id;
    private String nombre;
    private String telefono;
    private boolean activo;

    public Paciente(int id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.activo = true;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre + " | Tel: " + telefono + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }
}

class Medico {
    private int id;
    private String nombre;
    private Especialidad especialidad;
    private boolean activo;

    public Medico(int id, String nombre, Especialidad especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.activo = true;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Especialidad getEspecialidad() { return especialidad; }
    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return "[" + id + "] Dr(a). " + nombre + " | Especialidad: " + especialidad.getNombre() + " | Estado: " + (activo ? "Activo" : "Inactivo");
    }
}

class Cita {
    private int id;
    private Paciente paciente;
    private Medico medico;
    private String consultorio;
    private LocalDateTime fechaHora;
    private String estado; // PROGRAMADA, REALIZADA, CANCELADA

    public Cita(int id, Paciente paciente, Medico medico, String consultorio, LocalDateTime fechaHora) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.consultorio = consultorio;
        this.fechaHora = fechaHora;
        this.estado = "PROGRAMADA";
    }

    public int getId() { return id; }
    public Paciente getPaciente() { return paciente; }
    public Medico getMedico() { return medico; }
    public String getConsultorio() { return consultorio; }
    public void setConsultorio(String consultorio) { this.consultorio = consultorio; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return "Cita #" + id + " | Fecha: " + fechaHora.format(dtf) +
               " | Consultorio: " + consultorio +
               " | Paciente: " + paciente.getNombre() +
               " | Medico: " + medico.getNombre() +
               " | Estado: " + estado;
    }
}