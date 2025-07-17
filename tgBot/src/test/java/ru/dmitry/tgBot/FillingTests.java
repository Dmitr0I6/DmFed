package ru.dmitry.tgBot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.dmitry.tgBot.entity.*;
import ru.dmitry.tgBot.repository.*;


//Класс для заполнения всех таблиц бд тестовыми данными
@SpringBootTest
class FillingTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientOrderRepository clientOrderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Test
    void fillMenuWithCategoriesAndProducts() {
        orderProductRepository.deleteAll();
        clientOrderRepository.deleteAll();
        productRepository.deleteAll();
        clientRepository.deleteAll();
        categoryRepository.deleteAll();

        // Создаем основные категории (parent = null)
        Category pizza = createCategory("Пицца", null);
        Category rolls = createCategory("Роллы", null);
        Category burgers = createCategory("Бургеры", null);
        Category drinks = createCategory("Напитки", null);

        // Создаем подкатегории для Роллов
        Category classicRolls = createCategory("Классические роллы", rolls);
        Category bakedRolls = createCategory("Запеченные роллы", rolls);
        Category sweetRolls = createCategory("Сладкие роллы", rolls);
        Category rollSets = createCategory("Наборы", rolls);

        // Создаем подкатегории для Бургеров
        Category classicBurgers = createCategory("Классические бургеры", burgers);
        Category spicyBurgers = createCategory("Острые бургеры", burgers);

        // Создаем подкатегории для Напитков
        Category soda = createCategory("Газированные напитки", drinks);
        Category energyDrinks = createCategory("Энергетические напитки", drinks);
        Category juices = createCategory("Соки", drinks);
        Category otherDrinks = createCategory("Другие напитки", drinks);

        // Создаем продукты для каждой категории
        Product margarita = createProduct("Маргарита", "Классическая пицца", 450.0, pizza);
        Product pepperoni = createProduct("Пепперони", "Острая пицца", 550.0, pizza);
        Product hawaiian = createProduct("Гавайская", "С ананасами", 500.0, pizza);

        Product philadelphia = createProduct("Филадельфия", "С лососем", 320.0, classicRolls);
        Product california = createProduct("Калифорния", "С крабом", 280.0, classicRolls);
        Product canada = createProduct("Канада", "С угрем", 350.0, classicRolls);

        Product bakedShrimp = createProduct("Запеченный с креветкой", "С сыром", 380.0, bakedRolls);
        Product bakedSalmon = createProduct("Запеченный с лососем", "С соусом", 400.0, bakedRolls);
        Product bakedEel = createProduct("Запеченный с угрем", "Острый", 420.0, bakedRolls);

        Product bananaChocolate = createProduct("Банан-шоколад", "Сладкий ролл", 250.0, sweetRolls);
        Product strawberry = createProduct("Клубничный", "Со сгущенкой", 230.0, sweetRolls);
        Product apple = createProduct("Яблочный", "С корицей", 220.0, sweetRolls);

        Product cheeseburger = createProduct("Чизбургер", "Классический", 250.0, classicBurgers);
        Product hamburger = createProduct("Гамбургер", "Без сыра", 200.0, classicBurgers);
        Product baconBurger = createProduct("Бургер с беконом", "С беконом", 300.0, classicBurgers);

        // Создаем клиентов
        Client client1 = createClient(1001L, "Иван Петров", "+79123456789", "ул. Ленина, 10");
        Client client2 = createClient(1002L, "Анна Сидорова", "+79234567890", "пр. Мира, 25");
        Client client3 = createClient(1003L, "Сергей Иванов", "+79345678901", "ул. Гагарина, 5");

        // Создаем заказы
        ClientOrder order1 = createOrder(client1, 1200.0, 1);
        ClientOrder order2 = createOrder(client2, 850.0, 1);
        ClientOrder order3 = createOrder(client3, 1530.0, 2);

        // Добавляем продукты в заказы
        addProductToOrder(order1, margarita.getName(), 2);
        addProductToOrder(order1, philadelphia.getName(), 1);
        addProductToOrder(order1, cheeseburger.getName(), 1);

        addProductToOrder(order2, pepperoni.getName(), 1);
        addProductToOrder(order2, california.getName(), 2);

        addProductToOrder(order3, bakedEel.getName(), 3);
        addProductToOrder(order3, baconBurger.getName(), 2);
        addProductToOrder(order3, bananaChocolate.getName(), 1);
    }

    private void addProductToOrder(ClientOrder order, String productName, Integer count) {
        Product productOpt = productRepository.getProductByName(productName);
        if (productOpt != null) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setClientOrder(order);
            orderProduct.setProduct(productOpt);
            orderProduct.setCountProduct(count.longValue());
            orderProductRepository.save(orderProduct);

            // Обновляем общую сумму заказа
            double total = order.getTotal() + (productOpt.getPrice() * count);
            order.setTotal(total);
            clientOrderRepository.save(order);
        }
    }

    private Client createClient(Long externalId, String fullName, String phoneNumber, String address) {
        Client client = new Client();
        client.setExternalId(externalId);
        client.setFullName(fullName);
        client.setPhoneNumber(phoneNumber);
        client.setAddress(address);
        return clientRepository.save(client);
    }

    private ClientOrder createOrder(Client client, Double total, Integer status) {
        ClientOrder order = new ClientOrder();
        order.setClient(client);
        order.setTotal(total);
        order.setStatus(status);
        return clientOrderRepository.save(order);
    }

    private Category createCategory(String name, Category parent) {
        Category category = new Category();
        category.setName(name);
        category.setParent(parent);
        return categoryRepository.save(category);
    }

    private Product createProduct(String name, String description, Double price, Category category) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        return productRepository.save(product);
    }
}