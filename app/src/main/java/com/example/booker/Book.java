package com.example.booker;

import java.io.Serializable;

public class Book implements Serializable {
    private String title, author, isbn, publisher, genre, edition, language, numberOfPage, securityMoney, ownerId, CoverLink, status, bookId,borrowerId;

    public Book() {
    }

    public Book(String title, String author, String isbn, String publisher, String genre, String edition, String language, String numberOfPage, String securityMoney, String ownerId, String coverLink, String status, String bookId, String borrower) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.genre = genre;
        this.edition = edition;
        this.language = language;
        this.numberOfPage = numberOfPage;
        this.securityMoney = securityMoney;
        this.ownerId = ownerId;
        CoverLink = coverLink;
        this.status = status;
        this.bookId = bookId;
        this.borrowerId = borrower;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getNumberOfPage() {
        return numberOfPage;
    }

    public void setNumberOfPage(String numberOfPage) {
        this.numberOfPage = numberOfPage;
    }

    public String getSecurityMoney() {
        return securityMoney;
    }

    public void setSecurityMoney(String securityMoney) {
        this.securityMoney = securityMoney;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCoverLink() {
        return CoverLink;
    }

    public void setCoverLink(String coverLink) {
        CoverLink = coverLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

}