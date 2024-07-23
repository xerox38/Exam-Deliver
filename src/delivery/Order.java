package delivery;

public class Order {
	String dishNames[];
	int quantities[];
	String customerName;
	Restaurant restaurant;
	int deliveryTime;
	int deliveryDistance;
	private int id;
	
	boolean delivered = false;
	
	public Order(int id, String[] dishNames, int[] quantities, String customerName, Restaurant restaurant, int deliveryTime,
			int deliveryDistance) {
	    this.id = id;
		this.dishNames = dishNames;
		this.quantities = quantities;
		this.customerName = customerName;
		this.restaurant = restaurant;
		this.deliveryTime = deliveryTime;
		this.deliveryDistance = deliveryDistance;
	}

	public String[] getDishNames() {
		return dishNames;
	}

	public int[] getQuantities() {
		return quantities;
	}

	public String getCustomerName() {
		return customerName;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}
	
	public String getRestaurantName() {
		return restaurant.getName();
	}

	public int getDeliveryTime() {
		return deliveryTime;
	}

	public int getDeliveryDistance() {
		return deliveryDistance;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

    public int getId() {
        return id;
    }
	
	
}
