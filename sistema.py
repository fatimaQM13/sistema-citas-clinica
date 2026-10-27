from datetime import datetime
from modelos import Especialidad, Paciente, Medico, Cita

class ClinicaSistema:
    def __init__(self):
        self.especialidades = {}
        self.medicos = {}
        self.pacientes = {}
        self.citas = {}
        self._next_esp_id = 1
        self._next_med_id = 1
        self._next_pac_id = 1
        self._next_cita_id = 1

    # ==========================================
    # 2. ESPECIALIDADES
    # ==========================================
    def agregar_especialidad(self, nombre: str):
        esp = Especialidad(self._next_esp_id, nombre.strip())
        self.especialidades[esp.id] = esp
        self._next_esp_id += 1
        return esp

    def listar_especialidades(self):
        return list(self.especialidades.values())

    def obtener_especialidad(self, id_esp: int):
        return self.especialidades.get(id_esp)

    def actualizar_especialidad(self, id_esp: int, nuevo_nombre: str):
        if id_esp in self.especialidades:
            self.especialidades[id_esp].nombre = nuevo_nombre.strip()
            return True, "Especialidad actualizada correctamente."
        return False, "Especialidad no encontrada."

    def eliminar_especialidad(self, id_esp: int):
        for med in self.medicos.values():
            if med.especialidad.id == id_esp:
                return False, "No se puede eliminar: tiene médicos asignados."
        if id_esp in self.especialidades:
            del self.especialidades[id_esp]
            return True, "Especialidad eliminada."
        return False, "Especialidad no encontrada."

    # ==========================================
    # 3. MÉDICOS
    # ==========================================
    def agregar_medico(self, nombre: str, id_esp: int):
        esp = self.obtener_especialidad(id_esp)
        if not esp:
            return None, "Error: La especialidad indicada no existe."
        med = Medico(self._next_med_id, nombre.strip(), esp)
        self.medicos[med.id] = med
        self._next_med_id += 1
        return med, "Médico registrado exitosamente."

    def listar_medicos(self):
        return list(self.medicos.values())

    def obtener_medico(self, id_med: int):
        return self.medicos.get(id_med)

    def actualizar_medico(self, id_med: int, nuevo_nombre: str = None, id_esp: int = None):
        med = self.obtener_medico(id_med)
        if not med:
            return False, "Médico no encontrado."
        if nuevo_nombre:
            med.nombre = nuevo_nombre.strip()
        if id_esp:
            esp = self.obtener_especialidad(id_esp)
            if not esp:
                return False, "Especialidad inválida."
            med.especialidad = esp
        return True, "Médico actualizado correctamente."

    def cambiar_estado_medico(self, id_med: int, activo: bool):
        med = self.obtener_medico(id_med)
        if not med:
            return False, "Médico no encontrado."
        med.activo = activo
        estado_str = "activo" if activo else "inactivo"
        return True, f"Médico marcado como {estado_str}."

    def eliminar_medico(self, id_med: int):
        med = self.obtener_medico(id_med)
        if not med:
            return False, "Médico no encontrado."
        for cita in self.citas.values():
            if cita.medico.id == id_med and cita.estado == "PROGRAMADA":
                return False, "No se puede eliminar un médico con citas activas (programadas)."
        del self.medicos[id_med]
        return True, "Médico eliminado con éxito."

    def citas_del_medico(self, id_med: int):
        return [c for c in self.citas.values() if c.medico.id == id_med]

    # ==========================================
    # 1. PACIENTES
    # ==========================================
    def agregar_paciente(self, nombre: str, telefono: str):
        pac = Paciente(self._next_pac_id, nombre.strip(), telefono.strip())
        self.pacientes[pac.id] = pac
        self._next_pac_id += 1
        return pac

    def listar_pacientes(self):
        return list(self.pacientes.values())

    def obtener_paciente(self, id_pac: int):
        return self.pacientes.get(id_pac)

    def actualizar_paciente(self, id_pac: int, nombre: str = None, telefono: str = None):
        pac = self.obtener_paciente(id_pac)
        if not pac:
            return False, "Paciente no encontrado."
        if nombre:
            pac.nombre = nombre.strip()
        if telefono:
            pac.telefono = telefono.strip()
        return True, "Paciente actualizado correctamente."

    def desactivar_paciente(self, id_pac: int):
        pac = self.obtener_paciente(id_pac)
        if not pac:
            return False, "Paciente no encontrado."
        pac.activo = False
        return True, "Paciente desactivado correctamente."

    def activar_paciente(self, id_pac: int):
        pac = self.obtener_paciente(id_pac)
        if not pac:
            return False, "Paciente no encontrado."
        pac.activo = True
        return True, "Paciente activado correctamente."

    def eliminar_paciente(self, id_pac: int):
        pac = self.obtener_paciente(id_pac)
        if not pac:
            return False, "Paciente no encontrado."
        for cita in self.citas.values():
            if cita.paciente.id == id_pac and cita.estado == "PROGRAMADA":
                return False, "No se puede eliminar un paciente que tenga cita activa (programada)."
        del self.pacientes[id_pac]
        return True, "Paciente eliminado con éxito."

    def citas_del_paciente(self, id_pac: int):
        return [c for c in self.citas.values() if c.paciente.id == id_pac]

    # ==========================================
    # 4. CITAS Y CONSULTORIOS
    # ==========================================
    def registrar_cita(self, id_pac: int, id_med: int, consultorio: str, fecha_hora: datetime):
        paciente = self.obtener_paciente(id_pac)
        if not paciente:
            return None, "Error: Paciente no existe."
        if not paciente.activo:
            return None, "Error: El paciente está inactivo."

        medico = self.obtener_medico(id_med)
        if not medico:
            return None, "Error: Médico no existe."
        if not medico.activo:
            return None, "Error: No se puede asignar cita a médicos inactivos."

        if fecha_hora < datetime.now():
            return None, "Error: No se puede registrar una cita con fecha y hora anterior a la actual."

        for c in self.citas.values():
            if c.estado == "PROGRAMADA" and c.fecha_hora == fecha_hora:
                if c.medico.id == id_med:
                    return None, "Error: El médico ya tiene una cita asignada en ese mismo horario."
                if c.consultorio.strip().upper() == consultorio.strip().upper():
                    return None, f"Error: El consultorio '{consultorio}' ya se encuentra ocupado en ese horario."

        nueva_cita = Cita(self._next_cita_id, paciente, medico, consultorio.strip().upper(), fecha_hora)
        self.citas[nueva_cita.id] = nueva_cita
        self._next_cita_id += 1
        return nueva_cita, "Cita programada con éxito."

    def actualizar_cita(self, id_cita: int, nueva_fecha_hora: datetime = None, nuevo_consultorio: str = None):
        cita = self.citas.get(id_cita)
        if not cita:
            return False, "Cita no encontrada."
        if cita.estado != "PROGRAMADA":
            return False, f"Solo se pueden actualizar citas programadas (Estado actual: {cita.estado})."

        fecha_a_validar = nueva_fecha_hora or cita.fecha_hora
        consultorio_a_validar = nuevo_consultorio.strip().upper() if nuevo_consultorio else cita.consultorio

        if nueva_fecha_hora and nueva_fecha_hora < datetime.now():
            return False, "Error: La nueva fecha no puede ser anterior a la actual."

        for c in self.citas.values():
            if c.id != id_cita and c.estado == "PROGRAMADA" and c.fecha_hora == fecha_a_validar:
                if c.medico.id == cita.medico.id:
                    return False, "Error: Conflicto de horario con otra cita del médico."
                if c.consultorio == consultorio_a_validar:
                    return False, f"Error: El consultorio '{consultorio_a_validar}' ya está ocupado en ese horario."

        if nueva_fecha_hora:
            cita.fecha_hora = nueva_fecha_hora
        if nuevo_consultorio:
            cita.consultorio = consultorio_a_validar
        return True, "Cita actualizada correctamente."

    def eliminar_cita(self, id_cita: int):
        cita = self.citas.get(id_cita)
        if not cita:
            return False, "Cita no encontrada."
        cita.estado = "CANCELADA"
        return True, "Cita cancelada/eliminada correctamente."

    def completar_cita(self, id_cita: int):
        cita = self.citas.get(id_cita)
        if not cita:
            return False, "Cita no encontrada."
        cita.estado = "REALIZADA"
        return True, "Consulta marcada como realizada."

    def total_consultas_realizadas(self):
        return sum(1 for c in self.citas.values() if c.estado == "REALIZADA")