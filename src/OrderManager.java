
import java.util.*;

public class OrderManager {
    /* DAO LAYER FUNCTIONS */
    public static class Dao {

        private static HashMap<Long, User> userHashMap;
        private static HashMap<Long, Order> orderHashMap;
        private static HashMap<String, Product> productHashMap;

        static {
            userHashMap = new HashMap<>();
            orderHashMap = new HashMap<>();
            productHashMap = new HashMap<>();
        }

        public static User getUser(long id) {
            for(User u : Dao.userHashMap.values()){
                if(u.getId().equals(id)){
                    return u;
                }else{
                    System.out.println("User not found.");
                }
            }
            return null;
        }

        static User addUser(User user) {
            return Dao.userHashMap.put(user.getId(), user);
        }

        public static Product getProduct(String name) {
            for(Product prod : Dao.productHashMap.values()){
                if(prod.getName().equalsIgnoreCase(name)){
                    return prod;
                }else{
                    System.out.println("Product not found.");
                }
            }
            return null;
        }

        static Product addProduct(Product product) {
            return Dao.productHashMap.put(product.name, product);
        }

        public static Order getOrder(long id) {
            for(Order ord : Dao.orderHashMap.values()){
                if(ord.getUserId().equals(id)){
                    return ord;
                }else{
                    System.out.println("Order not found.");
                }
            }
            return null;
        }
        static Order addOrders(Order order) {
            return Dao.orderHashMap.put(order.getId(), order);
        }
    }

    public static class User {
        String name;
        String email;	//email has to be unique, otherwise don't create new user
        Long id;			//each new user gets an application generated Id.

        String getName() {
            return name;
        }
        void setName(String name) {
            this.name = name;
        }

        String getEmail() {
            return email;
        }
        void setEmail(String email) {
            this.email = email;
        }

        Long getId() {
            return id;
        }
        void setId(Long id) {
            this.id = id;
        }
    }

    public static class Product {
        String name;				//name has to be unique
        Long inventoryQty;		//inventoryQty can not be negative.

        String getName() {
            return name;
        }
        void setName(String name) {
            this.name = name;
        }

        Long getInventoryQty() {
            return inventoryQty;
        }
        void setInventoryQty(Long inventoryQty) {
            this.inventoryQty = inventoryQty;
        }

        @Override
        public String toString() {
            return "Product{" +
                    "name='" + name + '\'' +
                    ", inventoryQty=" + inventoryQty +
                    '}';
        }

        //when creating new product if product wth this name already exists, then just add inventoryQty.
        //when order is placed on this product, its inventoryQty will get reduced.
    }

    public static class OrderItem {
        String name;
        Long qty;

        String getName() {
            return name;
        }
        void setName(String name) {
            this.name = name;
        }

        Long getQty() {
            return qty;
        }
        void setQty(Long qty) {
            this.qty = qty;
        }
    }

    public static class Order {
        Long id; //each new order gets an application generated Id. Sequentially.
        List<OrderItem> orderItems;
        Long userId; // Id of user from User entity who creates the order

        Long getId() {
            return id;
        }
        void setId(Long id) {
            this.id = id;
        }

        List<OrderItem> getOrderItems() {
            return orderItems;
        }
        void setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }

        public Long getUserId() {
            return userId;
        }
        void setUserId(Long userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "id=" + id +
                    ", orderItems=" + orderItems +
                    ", userId=" + userId +
                    '}';
        }
    }

    public static void main(String[] args) {
        String name;
        long qty;
        String username;
        String email;
        long userId ;
        List<OrderItem> items = new ArrayList<>();

        while(true){
            System.out.println("Order Management System: ");
            System.out.println("-------------------------------------------");
            System.out.println("Press 1 to add User");
            System.out.println("Press 2 to add Product");
            System.out.println("Press 3 to add Order");
            System.out.println("Press 4 to print InventoryStatus");
            System.out.println("Press 5 to print Orders");
            System.out.println("Print 6 to show Users");
            System.out.println("Print 7 to Exit");
            System.out.println("-------------------------------------------");

            Scanner sc = new Scanner(System.in);
            int input = sc.nextInt();
            switch(input){
                case 1 :
                    sc.nextLine();
                    System.out.println("Enter Username : ");
                    username = sc.nextLine();
                    System.out.println("Enter email : ");
                    email = sc.nextLine();
                    addUser(username, email);
                    break;
                case 2 :
                    sc.nextLine();
                    System.out.println("Enter Product-name : ");
                    name = sc.nextLine();
                    System.out.println("Enter qty : ");
                    qty = sc.nextLong();
                    addProduct(name, qty);
                    break;
                case 3 :
                    System.out.println("Enter userId : ");
                    userId = sc.nextLong();
                    System.out.println("Enter number of items (less than or equal to "+Dao.productHashMap.size()+"): ");
                    long index = sc.nextLong();
                    while(index > Dao.productHashMap.size()){
                        System.out.println("Enter number of items again: ");
                        index = sc.nextLong();
                    }
                    while(index>0){
                        OrderItem o = new OrderItem();
                        sc.nextLine();
                        System.out.println(index+". Enter Product-name : ");
                        String o_name = sc.nextLine();
                        o.setName(o_name);
                        System.out.println("Enter qty : ");
                        Long o_qty = sc.nextLong();
                        o.setQty(o_qty);
                        items.add(o);
                        index--;
                    }
                    addOrder(userId, items);
                    break;
                case 4 :
                    showInventoryStatus();
                    break;
                case 5 :
                    showOrderDetails();
                    break;
                case 6 :
                    showUsers();
                    break;
                case 7 :
                    System.exit(-1);
                    break;
                default:
                    System.out.println("Enter Valid inputs.");
            }
        }
    }

    /* API LAYER FUNCTIONS */

    private static void addProduct(String name, Long qty) {

        Product product = new Product();
        if(Dao.productHashMap.values().isEmpty()){
            product.setName(name);
            product.setInventoryQty(qty);
            Product p = Dao.addProduct(product);
            if(p != null){
                System.out.println("Product is not added.");
            }else{
                System.out.println("Product is added.");
            }
        }else{
            for (Product p: Dao.productHashMap.values()) {
                product.setName(name);
                if(p.getName().equalsIgnoreCase(name)){
                    long inventoryQty = p.getInventoryQty();
                    product.setInventoryQty(inventoryQty + qty);
                }else{
                    product.setInventoryQty(qty);
                }
                Dao.addProduct(product);
                System.out.println("Product is added.");
            }
        }
    }

    private static void addOrder(long userId, List<OrderItem> items) {
        Order order = new Order();
        List<OrderItem> orderList = new ArrayList<>();
        User u = Dao.getUser(userId);
        if(u != null){
            for(OrderItem item : items){
                Product p = Dao.getProduct(item.getName());
                if(p != null){
                    long qty = p.getInventoryQty() - item.getQty();
                    if(qty >= 0){
                        orderList.add(item);
                        addProduct(p.name, -(item.getQty()));
                    }else{
                        System.out.println("A product inside OrderItem is not available.");
                        return;
                    }
                }
            }
            if(orderList.size() == items.size()){
                order.setId(Dao.orderHashMap.size()+1L);
                order.setOrderItems(orderList);
                order.setUserId(userId);
                Dao.addOrders(order);
            }
        }
    }

    private static void addUser(String name, String email) {
        User user = new User();
        Iterator it = Dao.userHashMap.values().iterator();
        if(!it.hasNext()){
            user.setName(name);
            user.setEmail(email);
            user.setId(Dao.userHashMap.size()+1L);
            User us = Dao.addUser(user);
            if(us != null){
                System.out.println("User is not added.");
            }else{
                System.out.println("User is added.");
            }
        }else{
            while(it.hasNext()){
                synchronized (Dao.class) {
                    User u = (User) it.next();
                    if (!u.getEmail().equalsIgnoreCase(email)) {
                        user.setName(name);
                        user.setEmail(email);
                        user.setId(Dao.userHashMap.size() + 1L);
                        Dao.addUser(user);
                        System.out.println("User is added.");
                    } else {
                        System.out.println("User already present with this email." + email);
                    }
                }
            }
        }
    }

    private static void showInventoryStatus() {
        for(Product p : Dao.productHashMap.values()){
            System.out.println("Product Name : "+p.getName() +" | Qty : "+ p.getInventoryQty());
            System.out.println("-----------------------------------------------------");
        }
    }

    private static void showOrderDetails() {
        for(Order o : Dao.orderHashMap.values()){
            System.out.print("Order Id " + o.getId()+" | Items inside order : ");
            for(OrderItem item : o.getOrderItems()){
                System.out.println("Item Name : "+item.getName()+" | qty : "+item.getQty());
            }
            System.out.println("-----------------------------------------------------");
        }
    }

    private static void showUsers() {
        for(User u : Dao.userHashMap.values()){
            System.out.println("User_id: "+u.getId()+" | UserName : "+u.getName() +" | Email_id : "+ u.getEmail());
            System.out.println("-----------------------------------------------------");
        }
    }

}
