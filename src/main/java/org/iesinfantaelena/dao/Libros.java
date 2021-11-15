package org.iesinfantaelena.dao;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.iesinfantaelena.modelo.Libro;
import org.iesinfantaelena.utils.Utilidades;
import org.iesinfantaelena.modelo.AccesoDatosException;


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

    private static final String CREATE_LIBROS_QUERY = "create table libros (isbn integer not null, titulo varchar(50) not null, autor varchar(50) not null, editorial varchar(25) not null, paginas integer not null, copias integer not null, constraint isbn_pk primary key (isbn));";
    private static final String INSERT_LIBRERIA_QUERY = "insert into LIBROS values (?,?,?,?,?,?)";
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
        rs = null;
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
        pStmt = null;

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
        pStmt = null;

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
        pStmt = null;
        rs = null;

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
                System.out.println(isbn + ", " + titulo + ", "
                        + autor + ", " + editorial + ", " + paginas + ", " + copias);
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
        pStmt = null;
        rs = null;

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
}