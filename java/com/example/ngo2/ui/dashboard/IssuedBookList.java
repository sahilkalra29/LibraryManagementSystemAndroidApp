package com.example.ngo2.ui.dashboard;

public class IssuedBookList {

    String bookname, isbn, issued_date, return_date, fine;

    public IssuedBookList(String bookname, String isbn, String issued_date, String return_date, String fine) {
        this.bookname = bookname;
        this.isbn = isbn;
        this.issued_date = issued_date;
        this.return_date = return_date;
        this.fine = fine;
    }

    public String getBookname(){
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getIssued_date(){
        return issued_date;
    }

    public void setIssued_date(String issued_date) {
        this.issued_date = issued_date;
    }

    public String getIsbn(){
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getReturn_date(){
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getFine(){
        return fine;
    }

    public void setFine(String fine) { this.fine = fine; }

}
