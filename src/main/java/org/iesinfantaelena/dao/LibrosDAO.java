package org.iesinfantaelena.dao;

import java.util.List;

import org.iesinfantaelena.modelo.Libro;
import org.iesinfantaelena.modelo.AccesoDatosException;

/**
 *
 * @author Carlos
 * @date 23/10/2021
 * @version 1.0
 * @license GPLv3
 */
public interface LibrosDAO {

    public List<Libro> verCatalogo() throws AccesoDatosException;

    public void actualizarCopias(Libro libro) throws AccesoDatosException;

    public void anadirLibro(Libro libro) throws AccesoDatosException;

    public void borrar(Libro libro) throws AccesoDatosException;

    public void obtenerLibro(int ISBN) throws AccesoDatosException;

    public String[] getCamposLibro() throws AccesoDatosException;


}
