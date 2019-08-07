package com.example.hellobookstore.db;


import org.litepal.crud.DataSupport;

public class Book extends DataSupport {

	private String bookName;
	private String bookCatalog;
	private String bookTags;
	private String bookAbstract;
	private String bookContent;
	private String imageUrl;
	private String reading;
	private String bookOnline;
	private String bookBytime;

	public Book(String bookName, String bookCatalog, String bookTags, String bookAbstract, String bookContent, String imageUrl, String reading, String bookOnline, String bookBytime) {
		this.bookName = bookName;
		this.bookCatalog = bookCatalog;
		this.bookTags = bookTags;
		this.bookAbstract = bookAbstract;
		this.bookContent = bookContent;
		this.imageUrl = imageUrl;
		this.reading = reading;
		this.bookOnline = bookOnline;
		this.bookBytime = bookBytime;
	}

	public Book() {
	}

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getBookCatalog() {
		return bookCatalog;
	}

	public void setBookCatalog(String bookCatalog) {
		this.bookCatalog = bookCatalog;
	}

	public String getBookTags() {
		return bookTags;
	}

	public void setBookTags(String bookTags) {
		this.bookTags = bookTags;
	}

	public String getBookAbstract() {
		return bookAbstract;
	}

	public void setBookAbstract(String bookAbstract) {
		this.bookAbstract = bookAbstract;
	}

	public String getBookContent() {
		return bookContent;
	}

	public void setBookContent(String bookContent) {
		this.bookContent = bookContent;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getReading() {
		return reading;
	}

	public void setReading(String reading) {
		this.reading = reading;
	}

	public String getBookOnline() {
		return bookOnline;
	}

	public void setBookOnline(String bookOnline) {
		this.bookOnline = bookOnline;
	}

	public String getBookBytime() {
		return bookBytime;
	}

	public void setBookBytime(String bookBytime) {
		this.bookBytime = bookBytime;
	}
}