package com.example.ngo2.ui.catalogue;

public class CatalogueList {

    String bookname, category, author, isbn, total_quantity, issued_quantity;

    public CatalogueList(String bookname, String category, String author, String isbn, String total_quantity, String issued_quantity) {
        this.bookname = bookname;
        this.category = category;
        this.author = author;
        this.isbn = isbn;
        this.total_quantity = total_quantity;
        this.issued_quantity = issued_quantity;
    }

    public String getBookname(){
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn(){
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTotal_quantity(){
        return total_quantity;
    }

    public void setTotal_quantity(String total_quantity) {
        this.total_quantity = total_quantity;
    }

    public String getIssued_quantity(){
        return issued_quantity;
    }

    public void setIssued_quantity(String issued_quantity) {
        this.issued_quantity = issued_quantity;
    }

}
