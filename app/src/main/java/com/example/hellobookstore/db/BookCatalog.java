package com.example.hellobookstore.db;

import org.litepal.crud.DataSupport;

public class BookCatalog extends DataSupport {

	private String bookCatalogId;
	private String bookCatalogName;

	public BookCatalog(String bookCatalogId, String bookCatalogName) {
		this.bookCatalogId = bookCatalogId;
		this.bookCatalogName = bookCatalogName;
	}

	public String getBookCatalogId() {
		return bookCatalogId;
	}

	public void setBookCatalogId(String bookCatalogId) {
		this.bookCatalogId = bookCatalogId;
	}

	public String getBookCatalogName() {
		return bookCatalogName;
	}

	public void setBookCatalogName(String bookCatalogName) {
		this.bookCatalogName = bookCatalogName;
	}
}
