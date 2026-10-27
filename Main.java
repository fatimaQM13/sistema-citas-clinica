import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        ClinicaSistema sistema = new ClinicaSistema();
        precargarDatos(sistema);

        while (true) {
            System.out.println("\n========================================");
            System.out.println("  SISTEMA CLINICO - GESTION INTEGRAL");
            System.out.println("========================================");
            System.out.println("1. Pacientes");
            System.out.println("2. Especialidades");
            System.out.println("3. Medicos");
            System.out.println("4. Citas y Consultorios");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            String op = scanner.nextLine().trim();

            switch (op) {
                case "1": menuPacientes(sistema); break;
                case "2": menuEspecialidades(sistema); break;
                case "3": menuMedicos(sistema); break;
                case "4": menuCitas(sistema); break;
                case "0":
                    System.out.println("Cerrando sistema. Hasta luego!");
                    return;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private static LocalDateTime pedirFecha() {
        while (true) {
            System.out.print("Ingrese fecha y hora (formato yyyy-MM-dd HH:mm, ej. 2026-11-05 10:00): ");
            String ent = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(ent, dtf);
            } catch (DateTimeParseException e) {
                System.out.println("Formato incorrecto. Intente nuevamente.");
            }
        }
    }

    private static void menuPacientes(ClinicaSistema sis) {
        while (true) {
            System.out.println("\n--- 1. MENU PACIENTES ---");
            System.out.println("1. Agregar Paciente");
            System.out.println("2. Mostrar Pacientes");
            System.out.println("3. Actualizar Paciente");
            System.out.println("4. Eliminar Paciente (no con cita activa)");
            System.out.println("5. Desactivar Paciente");
            System.out.println("6. Activar Paciente");
            System.out.println("7. Mostrar citas de cierto paciente");
            System.out.println("0. Regresar");
            System.out.print("Opcion: ");
            String op = scanner.nextLine().trim();

            if (op.equals("1")) {
                System.out.print("Nombre completo: ");
                String nom = scanner.nextLine();
                System.out.print("Telefono: ");
                String tel = scanner.nextLine();
                Paciente p = sis.agregarPaciente(nom, tel);
                System.out.println("Paciente registrado con ID #" + p.getId());
            } else if (op.equals("2")) {
                sis.listarPacientes().forEach(System.out::println);
            } else if (op.equals("3")) {
                try {
                    System.out.print("ID del paciente: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nuevo nombre (Enter para omitir): ");
                    String nom = scanner.nextLine();
                    System.out.print("Nuevo telefono (Enter para omitir): ");
                    String tel = scanner.nextLine();
                    boolean ok = sis.actualizarPaciente(id, nom, tel);
                    System.out.println(ok ? "Paciente actualizado." : "No encontrado.");
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("4")) {
                try {
                    System.out.print("ID a eliminar: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.println(sis.eliminarPaciente(id));
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("5")) {
                try {
                    System.out.print("ID a desactivar: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.println(sis.cambiarEstadoPaciente(id, false) ? "Desactivado." : "No encontrado.");
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("6")) {
                try {
                    System.out.print("ID a activar: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.println(sis.cambiarEstadoPaciente(id, true) ? "Activado." : "No encontrado.");
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("7")) {
                try {
                    System.out.print("ID del paciente: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    var citas = sis.citasDelPaciente(id);
                    if (citas.isEmpty()) System.out.println("Sin citas.");
                    else citas.forEach(System.out::println);
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("0")) break;
        }
    }

    private static void menuEspecialidades(ClinicaSistema sis) {
        while (true) {
            System.out.println("\n--- 2. MENU ESPECIALIDADES ---");
            System.out.println("1. Agregar Especialidad");
            System.out.println("2. Mostrar Especialidades");
            System.out.println("3. Actualizar");
            System.out.println("4. Eliminar");
            System.out.println("0. Regresar");
            System.out.print("Opcion: ");
            String op = scanner.nextLine().trim();

            if (op.equals("1")) {
                System.out.print("Nombre de especialidad: ");
                String n = scanner.nextLine();
                Especialidad e = sis.agregarEspecialidad(n);
                System.out.println("Registrada con ID #" + e.getId());
            } else if (op.equals("2")) {
                sis.listarEspecialidades().forEach(System.out::println);
            } else if (op.equals("3")) {
                try {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nuevo nombre: ");
                    String n = scanner.nextLine();
                    System.out.println(sis.actualizarEspecialidad(id, n) ? "Actualizada." : "No encontrada.");
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("4")) {
                try {
                    System.out.print("ID: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    System.out.println(sis.eliminarEspecialidad(id));
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("0")) break;
        }
    }

    private static void menuMedicos(ClinicaSistema sis) {
        while (true) {
            System.out.println("\n--- 3. MENU MEDICOS ---");
            System.out.println("1. Agregar Medico (con especialidad)");
            System.out.println("2. Mostrar Medicos");
            System.out.println("3. Actualizar Medico");
            System.out.println("4. Eliminar Medico (no con cita activa)");
            System.out.println("5. Cambiar Estado (Activo/Inactivo)");
            System.out.println("6. Mostrar citas del medico");
            System.out.println("0. Regresar");
            System.out.print("Opcion: ");
            String op = scanner.nextLine().trim();

            if (op.equals("1")) {
                System.out.print("Nombre del medico: ");
                String nom = scanner.nextLine();
                System.out.println("Especialidades disponibles:");
                sis.listarEspecialidades().forEach(System.out::println);
                try {
                    System.out.print("ID de Especialidad: ");
                    int idE = Integer.parseInt(scanner.nextLine());
                    System.out.println(sis.agregarMedico(nom, idE));
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("2")) {
                sis.listarMedicos().forEach(System.out::println);
            } else if (op.equals("3")) {
                try {
                    System.out.print("ID de Medico: ");
                    int idM = Integer.parseInt(scanner.nextLine());
                    System.out.print("Nuevo nombre (Enter para omitir): ");
                    String nom = scanner.nextLine();
                    System.out.print("Nuevo ID Especialidad (Enter para omitir): ");
                    String sE = scanner.nextLine();
                    Integer idE = sE.isEmpty() ? null : Integer.parseInt(sE);
                    System.out.println(sis.actualizarMedico(idM, nom, idE) ? "Actualizado." : "No encontrado.");
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("4")) {
                try {
                    System.out.print("ID a eliminar: ");
                    int idM = Integer.parseInt(scanner.nextLine());
                    System.out.println(sis.eliminarMedico(idM));
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("5")) {
                try {
                    System.out.print("ID del medico: ");
                    int idM = Integer.parseInt(scanner.nextLine());
                    System.out.print("Marcar activo? (s/n): ");
                    boolean act = scanner.nextLine().trim().equalsIgnoreCase("s");
                    System.out.println(sis.cambiarEstadoMedico(idM, act) ? "Estado actualizado." : "No encontrado.");
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("6")) {
                try {
                    System.out.print("ID del medico: ");
                    int idM = Integer.parseInt(scanner.nextLine());
                    var citas = sis.citasDelMedico(idM);
                    if (citas.isEmpty()) System.out.println("Sin citas.");
                    else citas.forEach(System.out::println);
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("0")) break;
        }
    }

    private static void menuCitas(ClinicaSistema sis) {
        while (true) {
            System.out.println("\n--- 4. MENU CITAS Y CONSULTORIOS ---");
            System.out.println("1. Registrar Cita (valida horario medico, consultorio y medico activo)");
            System.out.println("2. Mostrar Todas las Citas");
            System.out.println("3. Actualizar Cita");
            System.out.println("4. Cancelar / Eliminar Cita");
            System.out.println("5. Completar Cita (Consulta Realizada)");
            System.out.println("6. Total Consultas Realizadas");
            System.out.println("0. Regresar");
            System.out.print("Opcion: ");
            String op = scanner.nextLine().trim();

            if (op.equals("1")) {
                try {
                    System.out.print("ID Paciente: ");
                    int idP = Integer.parseInt(scanner.nextLine());
                    System.out.print("ID Medico: ");
                    int idM = Integer.parseInt(scanner.nextLine());
                    System.out.print("Consultorio: ");
                    String cons = scanner.nextLine();
                    LocalDateTime fh = pedirFecha();
                    System.out.println(sis.registrarCita(idP, idM, cons, fh));
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("2")) {
                var c = sis.listarCitas();
                if (c.isEmpty()) System.out.println("No hay citas registradas.");
                else c.forEach(System.out::println);
            } else if (op.equals("3")) {
                try {
                    System.out.print("ID de la Cita: ");
                    int idC = Integer.parseInt(scanner.nextLine());
                    System.out.print("Desea cambiar fecha? (s/n): ");
                    LocalDateTime fh = scanner.nextLine().equalsIgnoreCase("s") ? pedirFecha() : null;
                    System.out.print("Nuevo consultorio (Enter para omitir): ");
                    String cons = scanner.nextLine();
                    System.out.println(sis.actualizarCita(idC, fh, cons));
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("4")) {
                try {
                    System.out.print("ID de Cita: ");
                    int idC = Integer.parseInt(scanner.nextLine());
                    System.out.println(sis.cancelarCita(idC) ? "Cita cancelada." : "No encontrada.");
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("5")) {
                try {
                    System.out.print("ID de Cita realizada: ");
                    int idC = Integer.parseInt(scanner.nextLine());
                    System.out.println(sis.completarCita(idC) ? "Consulta marcada como realizada." : "No encontrada.");
                } catch (Exception e) { System.out.println("Dato invalido."); }
            } else if (op.equals("6")) {
                System.out.println("Total de consultas realizadas: " + sis.totalConsultasRealizadas());
            } else if (op.equals("0")) break;
        }
    }

    private static void precargarDatos(ClinicaSistema sis) {
        Especialidad e1 = sis.agregarEspecialidad("Cardiologia");
        Especialidad e2 = sis.agregarEspecialidad("Pediatria");
        sis.agregarEspecialidad("Medicina General");
        sis.agregarMedico("Carlos Mendoza", e1.getId());
        sis.agregarMedico("Ana Torres", e2.getId());
        sis.agregarPaciente("Juan Perez", "8711234567");
        sis.agregarPaciente("Maria Garcia", "8719876543");
    }
}