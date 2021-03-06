package org.iesinfantaelena.dao;

import org.iesinfantaelena.modelo.AccesoDatosException;
import org.iesinfantaelena.modelo.Libro;
import org.iesinfantaelena.utils.Utilidades;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @descrition
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Libros {

    /**
     * Definición de atributos para que la clase sea más eficiente
     */

    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pStmt;

    /**
     * Consultas a realizar en la base de datos
     */

    private static final String CREATE_LIBROS_QUERY = "create table libros (isbn integer not null, titulo varchar(50) not null, autor varchar(50) not null, editorial varchar(25) not null, paginas integer not null, copias integer not null, precio double not null, constraint isbn_pk primary key (isbn));";
    private static final String INSERT_LIBRERIA_QUERY = "insert into LIBROS values (?,?,?,?,?,?,?)";
    private static final String SELECT_LIBROS_QUERY = "select * from libros";
    private static final String DELETE_LIBRO_QUERY = "delete from LIBROS WHERE isbn = ?";
    private static final String SEARCH_LIBRO_QUERY = "select * from libros WHERE isbn = ?";
    private static final String UPDATE_LIBRO_QUERY = "update libros set copias = ? where isbn = ?";
    private static final String SELECT_CAMPOS_QUERY = "SELECT * FROM LIBROS LIMIT 1";

    /**
     * Constructor: inicializa conexión
     */

    public Libros() throws AccesoDatosException {
        try {
            this.con = new Utilidades().getConnection();
            liberar();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        }
    }

    /**
     * Método para cerrar la conexión
     */
    public void cerrar() {
        if (con != null) {
            Utilidades.closeConnection(con);
        }
    }


    /**
     * Método para liberar recursos
     */
    private void liberar() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (pStmt != null) {
                pStmt.close();
            }
        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
        }
    }

    /**
     * Método que muestra por pantalla los datos de la tabla libros
     */
    public List<Libro> verCatalogo() throws AccesoDatosException {
        stmt = null;
        rs = null;

        List<Libro> lista = new ArrayList<Libro>();

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            while(rs.next()){
                Libro temporal = new Libro();
                temporal.setISBN(rs.getInt("isbn"));
                temporal.setTitulo(rs.getString("titulo"));
                temporal.setAutor(rs.getString("autor"));
                temporal.setEditorial(rs.getString("editorial"));
                temporal.setPaginas(rs.getInt("paginas"));
                temporal.setCopias(rs.getInt("copias"));
                temporal.setPrecio(rs.getDouble("precio"));

                lista.add(temporal);
            }

        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }

        return lista;
    }

    /**
     * Actualiza el número de copias para un libro
     */

    public void actualizarCopias(Libro libro) throws AccesoDatosException {
        pStmt = null;
        try {
            pStmt = con.prepareStatement(UPDATE_LIBRO_QUERY);
            pStmt.setInt(1, libro.getCopias());
            pStmt.setInt(2, libro.getISBN());

            System.out.println("El libro se a actualizado correctamente");

        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }
    }

    /**
     * Añade un nuevo libro a la BD
     */
    public void anadirLibro(Libro libro) throws AccesoDatosException {
        pStmt = null;

        try {
            pStmt = con.prepareStatement(INSERT_LIBRERIA_QUERY);

            pStmt.setInt(1, libro.getISBN());
            pStmt.setString(2, libro.getTitulo());
            pStmt.setString(3, libro.getAutor());
            pStmt.setString(4, libro.getEditorial());
            pStmt.setInt(5, libro.getPaginas());
            pStmt.setInt(6, libro.getCopias());
            pStmt.setDouble(7,libro.getPrecio());

            pStmt.executeUpdate();

        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }
    }

    /**
     * Borra un libro por ISBN
     */

    public void borrar(Libro libro) throws AccesoDatosException {
        try {
            pStmt = con.prepareStatement(DELETE_LIBRO_QUERY);
            pStmt.setInt(1, libro.getISBN());
            pStmt.executeUpdate();

            System.out.println("El libro " + libro.getTitulo() + " ha sido borrado.");
        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            liberar();
        }
    }

    public void crearTablaLibros() throws AccesoDatosException {
        try {
            pStmt = con.prepareStatement(CREATE_LIBROS_QUERY);
            pStmt.executeUpdate();

        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }
    }

    public void obtenerLibro(int ISBN) throws AccesoDatosException {
        try {
            pStmt = con.prepareStatement(SEARCH_LIBRO_QUERY);
            pStmt.setInt(1, ISBN);
            rs = pStmt.executeQuery();

            if (rs.next()) {
                int isbn = rs.getInt("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
                double precio = rs.getDouble("precio");

                System.out.println("ISBN: " + isbn + " Título: " + titulo + " Autor: " + autor + " Editorial: " + editorial + " Páginas: " + paginas + " Copias: " + copias + " Precio: " + precio);
            }

        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }
    }

    public String[] getCamposLibro() throws AccesoDatosException {
        ResultSetMetaData rsmd = null;
        String[] campos = null;

        try {
            pStmt = con.prepareStatement(SELECT_CAMPOS_QUERY);
            rs = pStmt.executeQuery();
            rsmd = rs.getMetaData();

            int columns = rsmd.getColumnCount();
            campos = new String[columns];
            for (int i = 0; i < columns; i++) {
                campos[i] = rsmd.getColumnLabel(i + 1);
            }
            return campos;

        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");

        } finally {
            liberar();
        }
    }


    /**
     * Para este método utilizo los tipos indicados de ResultSet para poder recorrelo deade la última posición
     * a la primera
     * @throws AccesoDatosException
     */
    public void verCatalogoInverso() throws AccesoDatosException{
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            rs.afterLast();

            while(rs.previous()){
                int isbn = rs.getInt("ISBN");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                String editorial = rs.getString("editorial");
                int paginas = rs.getInt("paginas");
                int copias = rs.getInt("copias");
                double precio = rs.getDouble("precio");

                System.out.println("ISBN: " + isbn + " Título: " + titulo + " Autor: " + autor + " Editorial: " + editorial + " Páginas: " + paginas + " Copias: " + copias + " Precio: " + precio);            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            liberar();
        }

    }

    /**
     * Método para actualizar las copias de los libros
     * @param copias
     */

    public void actualizarCopias(HashMap<Integer, Integer> copias){
        try{
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            while (rs.next()){
                if(copias.containsKey(rs.getInt("isbn"))){
                    int total = rs.getInt("copias") + copias.get(rs.getInt("isbn"));
                    rs.updateInt("copias", total);
                    rs.updateRow();
                }
            }
        } catch(SQLException sqle){
            Utilidades.printSQLException(sqle);
        } finally {
            liberar();
        }
    }

    /**
     * Método para mostrar filas pasadas por un array
     * @param filas
     * @throws AccesoDatosException
     */
    public void verCatalogo(int[] filas) throws AccesoDatosException{
        try{
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            for(int i = 0; i < filas.length; i++){
                if(rs.absolute(filas[i])){
                    int isbn = rs.getInt("isbn");
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    String editorial = rs.getString("editorial");
                    int paginas = rs.getInt("paginas");
                    int copias = rs.getInt("copias");
                    double precio = rs.getDouble("precio");

                    System.out.println("ISBN: " + isbn + " Título: " + titulo + " Autor: " + autor + " Editorial: " + editorial + " Páginas: " + paginas + " Copias: " + copias + " Precio: " + precio);               }
            }
        }catch(SQLException sqle){
            Utilidades.printSQLException(sqle);
        }finally {
            liberar();
        }
    }

    /**
     * Método para rellenar el campo precio de cada libro en base al precio por página pasado.
     * He cambiado el tipo de dato recibido de float a double debido a que se adapta mejor a mi programa.
     * @param precio
     * @throws AccesoDatosException
     */
    public void rellenaPrecio(double precio) throws AccesoDatosException{
        try{
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            while (rs.next()){
                double precioTotal = (rs.getInt("paginas") * precio);
                rs.updateDouble("precio", precioTotal);
                rs.updateRow();
            }
        } catch(SQLException sqle){
            Utilidades.printSQLException(sqle);
        } finally {
            liberar();
        }
    }

    /**
     * En este método actualizo el precio de dos libros en base al precio más alto recibido.
     * Y en caso de que ocurra algún fallo hago un con.rollBack() para poder volver al estado anterior.
     * He cambiado el tipo de dato recibido (precio) de float a double debido a que se adapta mejor a mi código
     * @param isbn1
     * @param isbn2
     * @param precio
     * @throws AccesoDatosException
     */
    public void actualizaPrecio(int isbn1, int isbn2, double precio) throws AccesoDatosException{
        try{
            con.setAutoCommit(false);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            double precioMax = 0;

            while (rs.next()){
                if(rs.getInt("isbn") == isbn1 || rs.getInt("isbn") == isbn2){
                    double precioTotal = rs.getInt("paginas") * precio;
                    if(precioMax < precioTotal){
                        precioMax = precioTotal;
                    }
                }
            }

            rs.beforeFirst();

            while(rs.next()){
                if(rs.getInt("isbn") == isbn1 || rs.getInt("isbn") == isbn2){
                    rs.updateDouble("precio", precioMax);
                    rs.updateRow();
                }
            }

            con.commit();
        } catch(SQLException sqle){
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Utilidades.printSQLException(sqle);
        } finally {
            liberar();
        }
    }

    /**
     * Le he cambiado el nombre al método debido a que el anterior se llama igual
     * En este método actualizo al libro pasado por parámetros le sumo las páginas le sumo las páginas
     * pasadas por parámetro a las que ya tenía y les calculo de nuevo el precio
     * He cambiado el tipo de dato recibido (precio) de float a double debido a que se adapta mejor a mi código
     * @param isbn
     * @param precio
     * @param paginas
     * @throws AccesoDatosException
     */
    public void actualizaPrecioNuevo(int isbn, double precio,  int paginas) throws AccesoDatosException{
        try{
            con.setAutoCommit(false);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            while (rs.next()){
                if(rs.getInt("isbn") == isbn){
                    int totalPaginas = rs.getInt("paginas") + paginas;
                    rs.updateInt("paginas", totalPaginas);
                    rs.updateRow();

                    double nuevoPrecio = rs.getInt("paginas") * precio;
                    rs.updateDouble("precio", nuevoPrecio);
                    rs.updateRow();
                }
            }

            con.commit();
        } catch(SQLException sqle){
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Utilidades.printSQLException(sqle);
        } finally {
            liberar();
        }
    }

    /**
     * Método para buscar un libro y en base a ese libro crear uno nuevo con los mismos datos pero con el isbn pasado
     * por parámetros
     * @param isbn1
     * @param isbn2
     * @throws AccesoDatosException
     */
    public void copiaLibro(int isbn1, int isbn2) throws AccesoDatosException{
        try{
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_LIBROS_QUERY);

            while(rs.next()){
                if(rs.getInt("isbn") == isbn1){
                    String titulo = rs.getString("titulo");
                    String autor = rs.getString("autor");
                    String editorial = rs.getString("editorial");
                    int paginas = rs.getInt("paginas");
                    int copias = rs.getInt("copias");
                    double precio = rs.getDouble("precio");

                    rs.moveToInsertRow();
                    rs.updateInt("isbn", isbn2);
                    rs.updateString("titulo", titulo);
                    rs.updateString("autor", autor);
                    rs.updateString("editorial", editorial);
                    rs.updateInt("paginas", paginas);
                    rs.updateInt("copias", copias);
                    rs.updateDouble("precio", precio);

                    rs.insertRow();
                }
            }
        } catch (SQLException sqle){
            Utilidades.printSQLException(sqle);
        } finally {
            liberar();
        }
    }
}