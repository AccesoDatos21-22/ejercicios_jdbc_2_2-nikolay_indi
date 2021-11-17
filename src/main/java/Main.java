import org.iesinfantaelena.dao.Cafes;
import org.iesinfantaelena.dao.Libros;
import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;

import java.util.HashMap;
import java.util.List;

public class Main {

    public static void leerLista(List<Libro> lista){
        for(int i = 0; i < lista.size(); i++){
            int isbn = lista.get(i).getISBN();
            String titulo = lista.get(i).getTitulo();
            String autor = lista.get(i).getAutor();
            String editorial = lista.get(i).getEditorial();
            int paginas = lista.get(i).getPaginas();
            int copias = lista.get(i).getCopias();

            System.out.println(isbn + ", " + titulo + ", " + autor + ", " + editorial + ", " + paginas + ", " + copias);
        }
    }

    public static void leerCampos(String[] campos){
        for(int i = 0; i < campos.length; i++){
            System.out.println(campos[i]);
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
            Libro libro_1 = new Libro(12345,"Sistemas Operativos","Tanembaun","Informatica",156,3);
            Libro libro_2 = new Libro(12453,"Minix","Stallings","Informatica",345,4);
            Libro libro_3 = new Libro(1325,"Linux","Richard Stallman","FSF",168,10);
            Libro libro_4 = new Libro(1725,"Java","Juan Garcia","Programacion",245,9);

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
            libro_2 = new Libro(12453,"Minix","Stallings","Informatica",345,10);
            libros.actualizarCopias(libro_2);

            System.out.println();

            HashMap<Integer, Integer> listaActu = new HashMap<Integer, Integer>();

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

        } catch (AccesoDatosException e) {
            e.printStackTrace();
        }
    }
}