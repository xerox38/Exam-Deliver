package delivery;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Delivery {
	
	private SortedSet<String> categories;
	private SortedMap<String,Restaurant> restaurants;
	private ArrayList<Order> orders;
    private int id=0;
	
    public Delivery() {
        categories = new TreeSet<String>();
        restaurants = new TreeMap<>();
        orders = new ArrayList<Order>();
    }
    
	// R1
	
    /**
     * adds one category to the list of categories managed by the service.
     * 
     * @param category name of the category
     * @throws DeliveryException if the category is already available.
     */
	public void addCategory (String category) throws DeliveryException {
		if (categories.contains(category)) {
			throw new DeliveryException("Category already present: " + category);
		} 
		categories.add(category);
	}
	
	/**
	 * retrieves the list of defined categories.
	 * 
	 * @return list of category names
	 */
	public List<String> getCategories() {
		return new ArrayList<>(categories);
	}
	
	public void addRestaurant (String name, String category) throws DeliveryException {
		if (!categories.contains(category)) {
			throw new DeliveryException("Category not present: " + category);
		}
		restaurants.put(name,new Restaurant (name, category));
	}
	
	/**
	 * retrieves an ordered list by name of the restaurants of a given category. 
	 * It returns an empty list in there are no restaurants in the selected category 
	 * or the category does not exist.
	 * 
	 * @param category name of the category
	 * @return sorted list of restaurant names
	 */
	public List<String> getRestaurantsForCategory(String category) {
		return restaurants.values().stream()
				.filter(r -> r.getCategory().equals(category))
				.map(Restaurant::getName)
				.collect(Collectors.toList());
	}
	
	// R2
	
	/**
	 * adds a dish to the list of dishes of a restaurant. 
	 * Every dish has a given price.
	 * 
	 * @param name             name of the dish
	 * @param restaurantName   name of the restaurant
	 * @param price            price of the dish
	 * @throws DeliveryException if the dish name already exists
	 */
	public void addDish(String name, String restaurantName, float price) throws DeliveryException {
		
		Restaurant r = restaurants.get(restaurantName);
        
		r.addDish(name, price);
	}
	
	/**
	 * returns a map associating the name of each restaurant with the 
	 * list of dish names whose price is in the provided range of price (limits included). 
	 * If the restaurant has no dishes in the range, it does not appear in the map.
	 * 
	 * @param minPrice  minimum price (included)
	 * @param maxPrice  maximum price (included)
	 * @return map restaurant -> dishes
	 */
	public Map<String,List<String>> getDishesByPrice(float minPrice, float maxPrice) {
	    return restaurants.values().stream()
	            .flatMap(r->r.getDishes().stream())
                .filter(d->d.priceInRange(minPrice,maxPrice))
	            .collect(Collectors.groupingBy(d -> d.getRestaurant().getName(),
	                                 Collectors.mapping(Dish::getName,Collectors.toList())))
	            ; 
	}
	
	/**
	 * retrieve the ordered list of the names of dishes sold by a restaurant. 
	 * If the restaurant does not exist or does not sell any dishes 
	 * the method must return an empty list.
	 *  
	 * @param restaurantName   name of the restaurant
	 * @return alphabetically sorted list of dish names 
	 */
	public List<String> getDishesForRestaurant(String restaurantName) {
		Restaurant r;
		r = restaurants.get(restaurantName);
		if( r == null ) return new ArrayList<>();
		return r.getDishes().stream()
		        .sorted()
		        .map(Dish::getName)
		        .collect(Collectors.toList());
	}
	
	/**
	 * retrieves the list of all dishes sold by all restaurants belonging to the given category. 
	 * If the category is not defined or there are no dishes in the category 
	 * the method must return and empty list.
	 *  
	 * @param category     the category
	 * @return 
	 */
	public List<String> getDishesByCategory(String category) {
		return restaurants.values().stream()
			.filter(r->r.getCategory().equals(category))
			.flatMap(r->r.getDishes().stream())
			.distinct()
			.map( Dish::getName )
			.collect(Collectors.toList());
	}
	
	//R3
	
	/**
	 * creates a delivery order. 
	 * Each order may contain more than one product with the related quantity. 
	 * The delivery time is indicated with a number in the range 8 to 23. 
	 * The delivery distance is expressed in kilometers. 
	 * Each order is assigned a progressive order ID, the first order has number 1.
	 * 
	 * @param dishNames        names of the dishes
	 * @param quantities       relative quantity of dishes
	 * @param customerName     name of the customer
	 * @param restaurantName   name of the restaurant
	 * @param deliveryTime     time of delivery (8-23)
	 * @param deliveryDistance distance of delivery
	 * 
	 * @return order ID
	 */
	public int addOrder(String dishNames[], int quantities[], String customerName, String restaurantName, int deliveryTime, int deliveryDistance) {
		Restaurant r = restaurants.get(restaurantName);
		Order o = new Order(++id, dishNames, quantities, customerName, r, deliveryTime, deliveryDistance);
		orders.add(o);
		return id;
	}
	
	/**
	 * retrieves the IDs of the orders that satisfy the given constraints.
	 * Only the  first {@code maxOrders} (according to the orders arrival time) are returned
	 * they must be scheduled to be delivered at {@code deliveryTime} 
	 * whose {@code deliveryDistance} is lower or equal that {@code maxDistance}. 
	 * Once returned by the method the orders must be marked as assigned 
	 * so that they will not be considered if the method is called again. 
	 * The method returns an empty list if there are no orders (not yet assigned) 
	 * that meet the requirements.
	 * 
	 * @param deliveryTime required time of delivery 
	 * @param maxDistance  maximum delivery distance
	 * @param maxOrders    maximum number of orders to retrieve
	 * @return list of order IDs
	 */
	public List<Integer> scheduleDelivery(int deliveryTime, int maxDistance, int maxOrders) {
		List<Order> deliveredOrders = orders.stream()
//				.filter(o->!o.isDelivered())
//				.filter(o->o.getDeliveryTime()==deliveryTime)
//				.filter(o->o.getDeliveryDistance()<=maxDistance)
				// OR
				.filter(o->!o.isDelivered()
					     && o.getDeliveryTime()==deliveryTime
				         && o.getDeliveryDistance()<=maxDistance)
				.limit(maxOrders)
				.collect(Collectors.toList());
		if(deliveredOrders!=null)
			deliveredOrders.forEach(o->o.setDelivered(true));
		return deliveredOrders.stream()
				.map(Order::getId)
				.collect(Collectors.toList());
	}
	
	/**
	 * retrieves the number of orders that still need to be assigned
	 * @return the unassigned orders count
	 */
	public int getPendingOrders() {
		return (int) orders.stream()
				.filter(o->!o.isDelivered())
				.count();
	}
	
	// R4
	/**
	 * records a rating (a number between 0 and 5) of a restaurant.
	 * Ratings outside the valid range are discarded.
	 * 
	 * @param restaurantName   name of the restaurant
	 * @param rating           rating
	 */
	public void setRatingForRestaurant(String restaurantName, int rating) {
		if(rating>=0 || rating<=5) {
			Restaurant r = restaurants.get(restaurantName);
			if( r != null ) {
				r.addRating(rating);
			}
		}
	}
	
	/**
	 * retrieves the ordered list of restaurant. 
	 * 
	 * The restaurant must be ordered by decreasing average rating. 
	 * The average rating of a restaurant is the sum of all rating divided by the number of ratings.
	 * 
	 * @return ordered list of restaurant names
	 */
	public List<String> restaurantsAverageRating() {
		return restaurants.values().stream()
		.filter(Restaurant::hasRatings)
		.sorted(Comparator.comparingDouble(Restaurant::getAvgRating).reversed())
		.map(Restaurant::getName)
		.collect(Collectors.toList());
	}
	
	//R5
	/**
	 * returns a map associating each category to the number of orders placed to any restaurant in that category. 
	 * Also categories whose restaurants have not received any order must be included in the result.
	 * 
	 * @return map category -> order count
	 */
	public Map<String,Long> ordersPerCategory() {
//		Map<String,Long> res = orders.stream()
//				.collect(Collectors.groupingBy(
//					o -> o.getRestaurant().getCategory(),
//					Collectors.counting()
//				));
//		categories.forEach(c->res.putIfAbsent(c, 0L));
//		return res;
		// OR (with  Java >= 9)
		return
		restaurants.values().stream()
		.collect(Collectors.groupingBy(
				Restaurant::getCategory,
				Collectors.flatMapping(
						r -> orders.stream()
							.filter(o -> r == o.getRestaurant()),
						Collectors.counting()
						)
				));
	}
	
	/**
	 * retrieves the name of the restaurant that has received the higher average rating.
	 * 
	 * @return restaurant name
	 */
	public String bestRestaurant() {
		return restaurants.values().stream()
		        .max(Comparator.comparingDouble(Restaurant::getAvgRating))
		        .get().getName();
	}
}
