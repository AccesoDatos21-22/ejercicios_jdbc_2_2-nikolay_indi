package org.iesinfantaelena.modelo;

public class Libro {
    private int ISBN;
    private String titulo ;
    private String autor;
    private String editorial ;
    private int paginas ;
    private int copias ;
    private double precio;

    /**
     * Constructor por defecto
     */
    public Libro() {

    }

    /**
     * Constructor con precio
     * @param ISBN
     * @param titulo
     * @param autor
     * @param editorial
     * @param paginas
     * @param copias
     * @param precio
     */
    public Libro(int ISBN,String titulo, String autor, String editorial, int paginas, int copias, double precio) {
        this.ISBN = ISBN;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.paginas = paginas;
        this.copias = copias;
        this.precio = precio;
    }


    /**
     *Getters y Setters de la clase
     */
    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int iSBN) {
        ISBN = iSBN;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public int getCopias() {
        return copias;
    }

    public void setCopias(int copias) {
        this.copias = copias;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
