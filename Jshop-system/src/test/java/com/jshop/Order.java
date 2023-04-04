/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop;

public class Order {
	private String title;
	private String price;
	private String num;

	public Order() {
	}
	public Order(String title, String price, String num) {
		this.title = title;
		this.price = price;
		this.num = num;
	}
	@Override
	public String toString() {
		return "Order [title=" + title + ", price=" + price + ", num=" + num + "]";
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
}
