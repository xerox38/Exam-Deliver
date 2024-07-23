package delivery;

class Dish implements Comparable<Dish>{
	private String name;
	private float price;
	private Restaurant r;
	
	Dish (String name, float price, Restaurant r ) {
		this.name=name;
		this.price = price;
		this.r = r;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	Restaurant getRestaurant() {
	    return r;
	}
	
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public boolean priceInRange(double min, double max) {
		return price >= min && price <= max;
	}
	@Override
	public int compareTo(Dish o) {
		// TODO Auto-generated method stub
		return name.compareTo(o.name);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return ((Dish) obj).getName().equals(this.name);
	}
	
	
}
