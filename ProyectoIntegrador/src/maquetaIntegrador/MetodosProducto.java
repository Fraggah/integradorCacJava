package maquetaIntegrador;

import com.jakewharton.fliptables.FlipTableConverters;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MetodosProducto {
    private static List<Producto> objetivos = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        cargarDatos();
        mostrarMenu();
        guardarDatos();
    }

    public static void mostrarMenu() throws IOException {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("Menu");
            System.out.println("===================\n");
            System.out.println("1-Ingresar un nuevo Producto");
            System.out.println("2-Listar registro de Productos");
            System.out.println("3-Buscar Producto");
            System.out.println("4-Eliminar Producto");
            System.out.println("5-Modificar datos de Producto");
            System.out.println("6-Salir");
            System.out.println("\nIngresar una opcion\n");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    registrarProducto(sc);
                    break;
                case 2:
                    listarProductos();
                    break;
                case 3:
                    buscarProducto(sc);
                    break;
                case 4:
                    eliminarProducto(sc);
                    break;
                case 5:
                    modificarProducto(sc);
                    break;
                case 6:
                    System.out.println("Saliendo del programa\n");
                    break;
                default:
                    System.out.println("Opcion invalida\n");
            }
        } while (opcion != 6);
    }

    public static void registrarProducto(Scanner sc) {
           System.out.println("Registro de Producto");
           System.out.println("===================\n");
           System.out.println("Ingrese los siguientes datos:\n");
           sc.nextLine();

           System.out.println("Nombre:");
           String nombre = sc.nextLine();

           String cantidad;
           String precioCompraStr = "";
           double precioCompra = 0;

           // Validar la cantidad
           do {
               System.out.println("Cantidad:");
               cantidad = sc.nextLine();
               if (!cantidad.matches("\\d+")) {
                   System.out.println("La cantidad debe ser un número.");
               }
           } while (!cantidad.matches("\\d+"));

           // Validar el precio de compra
           do {
               System.out.println("Precio de Compra:");
               precioCompraStr = sc.nextLine();
               precioCompraStr = precioCompraStr.replace(",",".");
               if (!precioCompraStr.matches("\\d+(\\.\\d+)?")) {
                   System.out.println("El precio de compra debe ser un número.");
               } else {
                   precioCompra = Double.parseDouble(precioCompraStr);
               }
           } while (!precioCompraStr.matches("\\d+(\\.\\d+)?"));


           double precioVenta = precioCompra * 1.3;

           objetivos.add(new Producto(generarIdUnico(), nombre, cantidad, precioCompra, precioVenta));
           guardarDatos();
           System.out.println("Producto registrado con éxito.");
    }

    public static void listarProductos() {
        // Cargar los datos antes de listarlos
        cargarDatos();
        System.out.println("Listar datos del Producto");
        System.out.println("=======================\n");

        String[] headers = {"ID", "Nombre", "Cantidad", "Precio Compra", "Precio Venta"};
        Object[][] data = new Object[objetivos.size()][headers.length];
        
        DecimalFormat df = new DecimalFormat("0.00");//formato de cadena para mostrar solo 2 numeros decimales
        
        for (int i = 0; i < objetivos.size(); i++) {
            Producto producto = objetivos.get(i);
            data[i][0] = producto.getId();
            data[i][1] = producto.getNombre();
            data[i][2] = producto.getCantidad();
            data[i][3] = df.format(producto.getPrecio_compra());
            data[i][4] = df.format(producto.getPrecio_venta());
        }

        String listado = FlipTableConverters.fromObjects(headers, data);
        String[] caracteresAReemplazar = {"╔", "═", "╤", "╗", "╠", "╧", "╣", "╚", "╝", "╪", "┼","─","╟", "╢"};
         for (String caracter : caracteresAReemplazar) {
            listado = listado.replace(caracter, "-");
        }
        listado = listado.replace("║", "|");
        listado = listado.replace("│", "|");
        System.out.println(listado);                              
    }

    public static void buscarProducto(Scanner sc) {
        System.out.println("Buscar Producto por ID");
        System.out.println("=======================\n");
        System.out.println("Ingresa el ID del Producto:");
        int buscadoId = sc.nextInt();
        Producto productoEncontrado = buscarProductoPorId(buscadoId);

        if (productoEncontrado != null) {
            System.out.println("Producto encontrado:");
            System.out.println("Nombre: " + productoEncontrado.getNombre());
            System.out.println("Cantidad: " + productoEncontrado.getCantidad());
            System.out.println("Precio de Compra: " + productoEncontrado.getPrecio_compra());
            System.out.println("Precio de Venta: " + productoEncontrado.getPrecio_venta());
        } else {
            System.out.println("Producto con ID " + buscadoId + " no encontrado.");
        }
    }

    public static Producto buscarProductoPorId(int id) {
        for (Producto producto : objetivos) {
            if (producto.getId() == id) {
                return producto;
            }
        }
        return null;
    }

    public static void eliminarProducto(Scanner sc) {
        // Cargar los datos antes de eliminar un producto
        cargarDatos();
        System.out.println("Eliminar Producto");
        System.out.println("================\n");
        System.out.println("Eliminar por:");
        System.out.println("1. Id");
        System.out.println("2. Nombre");
        System.out.println("3. Salir/Cancelar");

        int eliminar = sc.nextInt();
        sc.nextLine();

        switch (eliminar) {
            case 1:
                System.out.println("Ingrese el Id del producto");
                String idProducto = sc.nextLine();
                for (Producto producto : objetivos) {
                    if (String.valueOf(producto.getId()).equals(idProducto)) {
                        objetivos.remove(producto);
                        System.out.println("Producto eliminado\n");
                        guardarDatos();  // Guardar los datos después de eliminar un producto
                        break;
                    }
                }
                break;
            case 2:
                System.out.println("Ingrese el nombre del producto");
                String nombreProducto = sc.nextLine();
                for (Producto producto : objetivos) {
                    if (producto.getNombre().equals(nombreProducto)) {
                        objetivos.remove(producto);
                        System.out.println("Producto eliminado\n");
                        guardarDatos();  // Guardar los datos después de eliminar un producto
                        break;
                    }
                }
                break;
            case 3:
                System.out.println("\nOperación cancelada\n");
                break;
            default:
                System.out.println("Opción inválida\n");
        }
    }

    public static void modificarProducto(Scanner sc) {
        // Cargar los datos antes de modificar un producto
        cargarDatos();
        System.out.println("Modificar Producto");
        System.out.println("=================\n");
        System.out.println("Ingrese el Id del Producto");

        String tempCant = "";
        String tempPC = "";
        String tempPV = "";
        String modificar = sc.next();
        String mensaje3 = "No se encontró el Producto\n";
        Producto producto = null;

        for (Producto objetivo : objetivos) {
            if (Integer.valueOf(objetivo.getId()).equals(Integer.parseInt(modificar))) {
                producto = objetivo;
                mensaje3 = "Producto encontrado";
                break;
            }
        }
        System.out.println(mensaje3 + "\n");

        int opcion2 = 0;

        if (producto != null) {
            do {
                System.out.println("1-Modificar Nombre");
                System.out.println("2-Modificar Cantidad");
                System.out.println("3-Modificar Precio de Compra");
                System.out.println("4-Modificar Precio de Venta");
                System.out.println("5-Cancelar");

                opcion2 = sc.nextInt();

                switch (opcion2) {
                    case 1:
                        sc.nextLine();
                        System.out.println("Nombre actual: " + producto.getNombre());
                        System.out.println("Ingrese nuevo nombre");
                        producto.setNombre(sc.nextLine());
                        break;
                    case 2:
                        do{
                            System.out.println("Cantidad actual: " + producto.getCantidad());
                            System.out.println("Ingrese nueva cantidad");                         
                            tempCant = sc.nextLine();
                            if (!tempCant.matches("\\d+")) {
                                System.out.println("La cantidad debe ser un número.");
                            }
                        }while (!tempCant.matches("\\d+"));
                        producto.setCantidad(tempCant);
                        break;
                    case 3:
                        do{
                            System.out.println("Precio de Compra actual " + producto.getPrecio_compra());
                            System.out.println("Ingrese nuevo precio de compra");
                            tempPC = sc.nextLine();
                            if (!tempPC.matches("\\d+")) {
                                System.out.println("El Precio de Compra debe ser un número.");
                            }
                        }while(!tempPC.matches("\\d+"));
                        producto.setPrecio_compra(Double.parseDouble(tempPC));
                        break;
                    case 4:
                        do{
                            System.out.println("Precio de Venta actual " + producto.getPrecio_venta());
                            System.out.println("Ingrese nuevo precio de venta");
                            tempPV = sc.nextLine();
                            if (!tempPV.matches("\\d+")) {
                                System.out.println("El Precio de Venta debe ser un número.");
                            }
                        }while(!tempPV.matches("\\d+"));
                        producto.setPrecio_venta(Double.parseDouble(tempPV));
                        break;
                    case 5:
                        System.out.println("\nOpción cancelada\n");
                        break;
                    default:
                        System.out.println("\nOpción inválida\n");
                }
            } while (opcion2 != 5);
        }

        guardarDatos();  // Guardar los datos después de modificar un producto
    }

    public static int generarIdUnico() {
        int maxId = 0;
        for (Producto producto : objetivos) {
            if (producto.getId() > maxId) {
                maxId = producto.getId();
            }
        }
        return maxId + 1;
    }

    public static void guardarDatos() {
        File file = new File("BaseProductos.txt");
        try {
            FileOutputStream ficheroSalida = new FileOutputStream(file);
            ObjectOutputStream objetoSalida = new ObjectOutputStream(ficheroSalida);
            objetoSalida.writeObject(objetivos);
            System.out.println("Datos Guardados Correctamente\n");
            objetoSalida.close();
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    public static void cargarDatos() {
        File file = new File("BaseProductos.txt");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                objetivos = (List<Producto>) ois.readObject();
                System.out.println("Datos Cargados Correctamente\n");
                ois.close();
            } catch (Exception e) {
                System.out.println("Error al cargar los datos: " + e.getMessage());
            }
        }
    }
}