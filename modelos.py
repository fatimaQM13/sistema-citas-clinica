from datetime import datetime

class Especialidad:
    def __init__(self, id_esp: int, nombre: str):
        self.id = id_esp
        self.nombre = nombre

    def __str__(self):
        return f"[{self.id}] {self.nombre}"


class Paciente:
    def __init__(self, id_paciente: int, nombre: str, telefono: str):
        self.id = id_paciente
        self.nombre = nombre
        self.telefono = telefono
        self.activo = True

    def __str__(self):
        estado = "Activo" if self.activo else "Inactivo"
        return f"[{self.id}] {self.nombre} | Tel: {self.telefono} | Estado: {estado}"


class Medico:
    def __init__(self, id_medico: int, nombre: str, especialidad: Especialidad):
        self.id = id_medico
        self.nombre = nombre
        self.especialidad = especialidad
        self.activo = True

    def __str__(self):
        estado = "Activo" if self.activo else "Inactivo"
        return f"[{self.id}] Dr(a). {self.nombre} | Especialidad: {self.especialidad.nombre} | Estado: {estado}"


class Cita:
    def __init__(self, id_cita: int, paciente: Paciente, medico: Medico, consultorio: str, fecha_hora: datetime):
        self.id = id_cita
        self.paciente = paciente
        self.medico = medico
        self.consultorio = consultorio
        self.fecha_hora = fecha_hora
        self.estado = "PROGRAMADA"

    def __str__(self):
        return (f"Cita #{self.id} | Fecha: {self.fecha_hora.strftime('%Y-%m-%d %H:%M')} | "
                f"Consultorio: {self.consultorio} | Paciente: {self.paciente.nombre} | "
                f"Médico: {self.medico.nombre} | Estado: {self.estado}")
