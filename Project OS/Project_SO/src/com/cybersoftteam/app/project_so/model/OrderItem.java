package com.cybersoftteam.app.project_so.model;

public class OrderItem {
	private String id;
	private String name;
	private float quantity;
	private float price;
	public OrderItem(String id, String name, float quantity, float price) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.price = price;
	}
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public OrderItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", name=" + name + ", quantity="
				+ quantity + ", price=" + price + "]";
	}
	
}
