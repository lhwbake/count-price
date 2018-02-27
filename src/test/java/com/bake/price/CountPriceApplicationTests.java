package com.bake.price;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bake.price.entity.Person;
import com.bake.price.entity.Product;
import com.bake.price.entity.ProductOrder;
import com.bake.price.service.CountService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountPriceApplicationTests {

	@Test
	public void contextLoads() {
		
		/*订餐人数*/
		final int personNum;
		
		/*配送费*/
		final double distributionCosts = 5;
				
		/*会员优惠*/
		final double memberDiscounts = 0;
		
		/*在线立减优惠*/
		final double onlineDiscounts = 27;
		
		/*折  */
		final double discounts = 0;
		
		/*修正因子*/
		double correctionFactors = 0;
		
		/*用户*/
		Map<String, Person> persons = new HashMap<String, Person>();
		getPersons(persons, "mn", false);
		getPersons(persons, "mz", false);
		getPersons(persons, "lhw", false);
		
		/*
		 * 购买商品
		 */
		Map<String, Product> products = new HashMap<String, Product>();
	/*	getProducts(products,"肥牛素菜米线",20);
		getProducts(products,"肥牛素菜米线",20);
		getProducts(products,"肉夹馍",6);
		getProducts(products,"肉夹馍",6);*/
		
		//getProducts(products,"小醋溜土豆丝",13);
		//getProducts(products,"扁豆焖面",17);
		//getProducts(products,"小宫保鸡丁",19);
		getProducts(products,"芹菜水饺",29.88);
		getProducts(products,"素三鲜水饺",17.88);
		getProducts(products,"豆角水饺",29.88);
		
		/*
		 * 订单
		 */
		List<ProductOrder> productOrders = new ArrayList<ProductOrder>();
		getProductOrders(productOrders, products, persons,"mz","素三鲜水饺",1);
		getProductOrders(productOrders, products, persons,"mn","芹菜水饺",1);
		getProductOrders(productOrders, products, persons,"lhw","豆角水饺",1);
		
		personNum = persons.size();
		
		//CountService CountService = new CountService(personNum,distributionCosts,memberDiscounts,onlineDiscounts,discounts, correctionFactors);
		CountService CountService = new CountService(personNum,distributionCosts,onlineDiscounts,discounts);
		Map<String,Double> result = CountService.count(persons, products, productOrders);
		System.out.println(result);
	}
	/**
	 * 商品列表
	 * @param products
	 * @param name
	 * @param price
	 */
	public void getProducts(Map<String, Product> products, String name, double price){
		Product product = new Product(name,price);
		products.put(product.getName(), product);
	}
	
	/**
	 * 用户列表
	 * @param persons
	 * @param name
	 * @param isMember
	 * @return
	 */
	public void getPersons(Map<String, Person> persons, String name, boolean isMember){
		Person person = new Person(name,isMember);
		persons.put(person.getName(),person);
	}
	
	/**
	 * 订单列表
	 * @param productOrders
	 * @param persons
	 * @param products
	 * @param personName
	 * @param productName
	 * @param count
	 */
	public void getProductOrders(List<ProductOrder> productOrders, Map<String, Product> products, Map<String, Person> persons, String personName, String productName, int count){
		ProductOrder orderMn = new ProductOrder(products.get(productName),count, persons.get(personName),products.get(productName).getPrice()*count);
		productOrders.add(orderMn);
	}

}
