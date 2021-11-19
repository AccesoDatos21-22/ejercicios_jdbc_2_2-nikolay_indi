package org.iesinfantaelena.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.iesinfantaelena.utils.Utilidades;
import org.iesinfantaelena.modelo.AccesoDatosException;

/**
 * @descrition
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */

public class Cafes   {

    /**
     * Definición de atributos para que la clase sea más eficiente
     */
    private static Connection con;
    private static ResultSet rs;
    private static PreparedStatement pStmt;
    private static Statement stmt;

    /**
     * Consultas a realizar en la base de datos
     */
    private static final String SELECT_CAFES_QUERY = "select CAF_NOMBRE, PROV_ID, PRECIO, VENTAS, TOTAL from CAFES";
    private static final String SEARCH_CAFE_QUERY = "select * from CAFES WHERE CAF_NOMBRE= ?";
    private static final String INSERT_CAFE_QUERY = "insert into CAFES values (?,?,?,?,?)";
    private static final String DELETE_CAFE_QUERY = "delete from CAFES WHERE CAF_NOMBRE = ?";
    private static final String SEARCH_CAFES_PROVEEDOR = "select * from CAFES,PROVEEDORES WHERE CAFES.PROV_ID= ? AND CAFES.PROV_ID=PROVEEDORES.PROV_ID";
    private static final String CREATE_TABLE_PROVEEDORES ="create table if not exists proveedores (PROV_ID integer NOT NULL, PROV_NOMBRE varchar(40) NOT NULL, CALLE varchar(40) NOT NULL, CIUDAD varchar(20) NOT NULL, PAIS varchar(2) NOT NULL, CP varchar(5), PRIMARY KEY (PROV_ID));";
    private static final String CREATE_TABLE_CAFES ="create table if not exists CAFES (CAF_NOMBRE varchar(32) NOT NULL, PROV_ID int NOT NULL, PRECIO numeric(10,2) NOT NULL, VENTAS integer NOT NULL, TOTAL integer NOT NULL, PRIMARY KEY (CAF_NOMBRE), FOREIGN KEY (PROV_ID) REFERENCES PROVEEDORES(PROV_ID));";

    /**
     * Constructor: inicializa conexión
     */
    public Cafes() {
        /**
         * Los siguientes atributos los he comentado debido a que me parecia más eficiente llamar directamente
         * al método liberar (en el bloque finally), pero solo los he comentado debido a que en uno de los ejercicios pedías que lo
         * hiciésemos de esta forma
         */
        con = null;

        /*
        rs = null;
        pStmt = null;
        stmt = null;
         */

        try {
            con = new Utilidades().getConnection();
            stmt = con.createStatement();

            stmt.executeUpdate(CREATE_TABLE_PROVEEDORES);
            stmt.executeUpdate(CREATE_TABLE_CAFES);

            stmt.executeUpdate("insert into proveedores values(49, 'PROVerior Coffee', '1 Party Place', 'Mendocino', 'CA', '95460');");
            stmt.executeUpdate("insert into proveedores values(101, 'Acme, Inc.', '99 mercado CALLE', 'Groundsville', 'CA', '95199');");
            stmt.executeUpdate("insert into proveedores values(150, 'The High Ground', '100 Coffee Lane', 'Meadows', 'CA', '93966');");
        } catch (IOException e) {
            System.err.println(e.getMessage());

        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
            liberar();
        }
    }

    /**
     * Método que muestra por pantalla los datos de la tabla cafes
     */
    public void verTabla() throws AccesoDatosException {
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(SELECT_CAFES_QUERY);

            while (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total);
            }
        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }

    }

    /**
     * Método que busca un cafe por nombre y muestra sus datos
     */
    public void buscar(String nombre) throws AccesoDatosException {
        try {
            pStmt = con.prepareStatement(SEARCH_CAFE_QUERY);
            pStmt.setString(1, nombre);

            rs = pStmt.executeQuery();

            if (rs.next()) {
                String coffeeName = rs.getString("CAF_NOMBRE");
                int supplierID = rs.getInt("PROV_ID");
                float PRECIO = rs.getFloat("PRECIO");
                int VENTAS = rs.getInt("VENTAS");
                int total = rs.getInt("TOTAL");
                System.out.println(coffeeName + ", " + supplierID + ", "
                        + PRECIO + ", " + VENTAS + ", " + total);
            }

        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }

    }

    /**
     * Mótodo para insertar una fila
     */
    public void insertar(String nombre, int provid, float precio, int ventas,
                         int total) throws AccesoDatosException {

        try {
            pStmt = con.prepareStatement(INSERT_CAFE_QUERY);

            pStmt.setString(1, nombre);
            pStmt.setInt(2, provid);
            pStmt.setFloat(3, precio);
            pStmt.setInt(4, ventas);
            pStmt.setInt(5, total);

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
     * Método para borrar una fila dado un nombre de café
     */
    public void borrar(String nombre) throws AccesoDatosException {
        try {
            pStmt = con.prepareStatement(DELETE_CAFE_QUERY);
            pStmt.setString(1, nombre);
            pStmt.executeUpdate();

            System.out.println("El café " + nombre + " ha sido borrado.");
        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
            throw new AccesoDatosException(
                    "Ocurrió un error al acceder a los datos");
        } finally {
            liberar();
        }

    }

    /**
     * Método al que se le pasa el id del proveedor y busca los productos
     * que son suyos
     */
    public void cafesPorProveedor(int provid) throws AccesoDatosException {
        pStmt = null;
        rs = null;

        try {
            pStmt = con.prepareStatement(SEARCH_CAFES_PROVEEDOR);
            pStmt.setInt(1, provid);
            rs = pStmt.executeQuery();

            while(rs.next()) {
                String var4 = rs.getString("CAF_NOMBRE");
                int var5 = rs.getInt("PROV_ID");
                float var6 = rs.getFloat("PRECIO");
                int var7 = rs.getInt("VENTAS");
                int var8 = rs.getInt("TOTAL");
                String var9 = rs.getString("PROV_NOMBRE");
                String var10 = rs.getString("CALLE");
                String var11 = rs.getString("CIUDAD");
                String var12 = rs.getString("PAIS");
                int var13 = rs.getInt("CP");
                System.out.println(var4 + ", " + var5 + ", " + var6 + ", " + var7 + ", " + var8 + ",Y el proveedor es:" + var9 + "," + var10 + "," + var11 + "," + var12 + "," + var13);
            }
        } catch (SQLException var23) {
            Utilidades.printSQLException(var23);
            throw new AccesoDatosException("Ocurrió un error al acceder a los datos");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (pStmt != null) {
                    pStmt.close();
                }
            } catch (SQLException var21) {
                Utilidades.printSQLException(var21);
            }
        }
    }

    /**
     * Método para cerrar la conexión hacia la base de datos
     */
    public void cerrar() {
        if (con != null) {
            Utilidades.closeConnection(con);
        }
    }

    /**
     * Método para liberar recursos utilizados
     */
    private void liberar() {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
            if (pStmt != null) {
                pStmt.close();
                pStmt = null;
            }
        } catch (SQLException sqle) {
            Utilidades.printSQLException(sqle);
        }
    }

    public void transferencia(String cafe1, String cafe2) throws AccesoDatosException{
        try {
            con.setAutoCommit(false);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(SELECT_CAFES_QUERY);

            int ventas = 0;

            while(rs.next()){
                if(rs.getString("CAF_NOMBRE").equals(cafe1)){
                    ventas = rs.getInt("ventas");
                }
            }

            rs.beforeFirst();

            while(rs.next()){
                if(rs.getString("CAF_NOMBRE").equals(cafe2)){
                    rs.updateInt("ventas", rs.getInt("ventas")+ventas);
                    rs.updateRow();
                }
            }

            rs.beforeFirst();

            while(rs.next()){
                if(rs.getString("CAF_NOMBRE").equals(cafe1)){
                    rs.updateInt("ventas", 0);
                    rs.updateRow();
                }
            }

            con.commit();
        } catch( SQLException sqle){
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Utilidades.printSQLException(sqle);
        }
    }
}
