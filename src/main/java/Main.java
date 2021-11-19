import org.iesinfantaelena.dao.Cafes;
import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

import java.util.HashMap;
import java.util.List;

public class Main {

    public static void leerLista(List<Libro> lista){
        for (Libro libro : lista) {
            int isbn = libro.getISBN();
            String titulo = libro.getTitulo();
            String autor = libro.getAutor();
            String editorial = libro.getEditorial();
            int paginas = libro.getPaginas();
            int copias = libro.getCopias();
            double precio = libro.getPrecio();

            System.out.println("ISBN: " + isbn + " Título: " + titulo + " Autor: " + autor + " Editorial: " + editorial + " Páginas: " + paginas + " Copias: " + copias + " Precio: " + precio);
        }
    }

    public static void leerCampos(String[] campos){
        for (String campo : campos) {
            System.out.println(campo);
        }
    }

    public static void main(String[] args) {


        try {
            Cafes cafes = new Cafes();
            cafes.insertar("Cafetito", 150, 1.0f, 100,1000);
            cafes.insertar("Cafe tacilla", 150, 2.0f, 100,1000);
            cafes.verTabla();
            cafes.buscar("tacilla");
            cafes.cafesPorProveedor(150);
            cafes.borrar("Cafe tacilla");
            cafes.verTabla();

            System.out.println();

            Libros libros = new Libros();
            libros.crearTablaLibros();

            System.out.println("Añado los libros en la base de datos \n");
            Libro libro_1 = new Libro(12345,"Sistemas Operativos","Tanembaun","Informatica",156,3,0.0);
            Libro libro_2 = new Libro(12453,"Minix","Stallings","Informatica",345,4,0.0);
            Libro libro_3 = new Libro(1325,"Linux","Richard Stallman","FSF",168,10,0.0);
            Libro libro_4 = new Libro(1725,"Java","Juan Garcia","Programacion",245,9,0.0);

            libros.anadirLibro(libro_1);
            libros.anadirLibro(libro_2);
            libros.anadirLibro(libro_3);
            libros.anadirLibro(libro_4);

            System.out.println("Borro el primer libro");
            libros.borrar(libro_1);

            System.out.println();

            System.out.println("Catálogo de libros disponibles");
            leerLista(libros.verCatalogo());

            System.out.println();

            System.out.println("Obtengo el libro con el isbn = 1325");
            libros.obtenerLibro(1325);

            System.out.println();

            System.out.println("Obtengo los nombres de los campos de la base de datos");
            leerCampos(libros.getCamposLibro());

            System.out.println();

            System.out.println("Actualizo las copias del libro_2");
            libro_2 = new Libro(12453,"Minix","Stallings","Informatica",345,10,0.0);
            libros.actualizarCopias(libro_2);

            System.out.println();

            HashMap<Integer, Integer> listaActu = new HashMap<>();

            System.out.println("Actualizo con HashMap");
            listaActu.put(12453, 1);
            listaActu.put(1325, 1);
            listaActu.put(1725, 1);

            libros.actualizarCopias(listaActu);
            
            System.out.println();

            System.out.println("Catálogo de libros disponibles");
            leerLista(libros.verCatalogo());

            System.out.println();

            System.out.println("Catálogo de libros disponibles inverso");
            libros.verCatalogoInverso();

            System.out.println();

            System.out.println("Buscar filas, mediante un array el cual contiene las filas deseadas");
            int[] filas = {3,2};
            libros.verCatalogo(filas);

            System.out.println();

            System.out.println("Actualizo el precio de los libros, pasando el precio por página");
            libros.rellenaPrecio(0.10);
            leerLista(libros.verCatalogo());

            System.out.println("");

            System.out.println("Actualizo el precio de los libros (mediante el isbn que he pasado) junto al precio por página");
            libros.actualizaPrecio(1725,1325,0.2);
            leerLista(libros.verCatalogo());
        } catch (AccesoDatosException e) {
            e.printStackTrace();
        }
    }
}