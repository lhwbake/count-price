package com.bake.price.entity;

public class ProductOrder {
	
	private Product product;
	
	private int productCount;
	
	private Person person;
	
	private double totalPrice;

	public ProductOrder(Product product, int productCount, Person person, double totalPrice) {
		super();
		this.product = product;
		this.productCount = productCount;
		this.person = person;
		this.totalPrice = totalPrice;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return  person.getName() + ":购买" + product.getName() + "-" + productCount + "份-" + totalPrice + "元";
	}
	
	

	
}
