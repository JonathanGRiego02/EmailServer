package pgv.emailserver;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CreacionUsuarios {

    private static final String Usuario_Correo = "C:/XAMPP/MERCURYMAIL/MAIL/";

    // Método para crear la contraseña en el archivo PASSWD.PM
    public static void crearContraseña(String usuario, String contraseña) throws IOException {
        String Ruta_Pass = Usuario_Correo + usuario + "/PASSWD.PM";  // Cambiado a PASSWD.PM
        File userDir = new File(Usuario_Correo + usuario);

        // Crear el directorio del usuario si no existe
        if (!userDir.exists()) {
            userDir.mkdirs();
        }

        // Crear el contenido del archivo PASSWD.PM
        String passwdContent = "POP3_access: " + contraseña + "\n" + "APOP_secret: \n";

        // Escribir el contenido en el archivo PASSWD.PM
        Files.write(Paths.get(Ruta_Pass), passwdContent.getBytes(), StandardOpenOption.CREATE);
        System.out.println("Contraseña para el usuario " + usuario + " almacenada correctamente.");
    }

    public static void agregarUsuarioAlArchivo(String usuario, String nombreCompleto) throws IOException {
        String archivoUsuarios = "C:\\xampp\\MercuryMail\\MAIL\\PMAIL.USR";
        String tipoUsuario = "U";

        String contenidoUsuario = tipoUsuario + ";" + usuario + ";" + nombreCompleto + "\n";
        Files.write(Paths.get(archivoUsuarios), contenidoUsuario.getBytes(), StandardOpenOption.APPEND);
        System.out.println("Usuario " + usuario + " agregado correctamente a PMAIL.USR.");
    }

    public static void reiniciarMercury() {
        try {
            // Detener el proceso de Mercury
            System.out.println("Deteniendo el servidor Mercury...");
            ProcessBuilder stopBuilder = new ProcessBuilder("taskkill", "/IM", "Mercury.exe", "/F");
            Process stopProcess = stopBuilder.start();
            stopProcess.waitFor();
            System.out.println("Servidor Mercury detenido.");

            // Esperar un poco antes de reiniciar el servidor
            Thread.sleep(2000);  // Espera de 2 segundos para asegurarse de que el proceso se haya detenido completamente

            // Iniciar el servidor Mercury
            System.out.println("Iniciando el servidor Mercury...");
            ProcessBuilder startBuilder = new ProcessBuilder("C:\\xampp\\MercuryMail\\Mercury.exe");
            Process startProcess = startBuilder.start();
            startProcess.waitFor();
            System.out.println("Servidor Mercury iniciado.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error al intentar reiniciar el servidor Mercury.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Ingrese su usuario:");
        String usuario = sc.nextLine();

        System.out.println("Ingrese su password:");
        String password = sc.nextLine();

        System.out.println("Ingrese su nombre completo:");
        String nombreCompleto = sc.nextLine();

        try {
            // Verificar si el directorio del usuario existe, si no, crearlo
            File correo = new File(Usuario_Correo + usuario);
            if (!correo.exists()) {
                correo.mkdirs();
                System.out.println("El usuario se ha creado correctamente.");
            }

            // Llamar a la función para crear la contraseña y guardar los archivos
            crearContraseña(usuario, password);
            agregarUsuarioAlArchivo(usuario, nombreCompleto);

            // Llamar al método para reiniciar el servicio de Mercury
            reiniciarMercury();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
