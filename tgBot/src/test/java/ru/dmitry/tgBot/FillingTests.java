package ru.dmitry.tgBot;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;;
//Класс для наполнения базы данных категориями и продуктами
@SpringBootTest
class FillingTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void fillMenuWithCategoriesAndProducts() {
        productRepository.deleteAll();
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

        // Создаем продукты для каждой категории (минимум 3 на подкатегорию)
        createProductsForCategory(pizza, List.of(
                new ProductData("Маргарита", "Классическая пицца", 450.0),
                new ProductData("Пепперони", "Острая пицца", 550.0),
                new ProductData("Гавайская", "С ананасами", 500.0)
        ));

        createProductsForCategory(classicRolls, List.of(
                new ProductData("Филадельфия", "С лососем", 320.0),
                new ProductData("Калифорния", "С крабом", 280.0),
                new ProductData("Канада", "С угрем", 350.0)
        ));

        createProductsForCategory(bakedRolls, List.of(
                new ProductData("Запеченный с креветкой", "С сыром", 380.0),
                new ProductData("Запеченный с лососем", "С соусом", 400.0),
                new ProductData("Запеченный с угрем", "Острый", 420.0)
        ));

        // Аналогично для остальных категорий...
        createProductsForCategory(sweetRolls, List.of(
                new ProductData("Банан-шоколад", "Сладкий ролл", 250.0),
                new ProductData("Клубничный", "Со сгущенкой", 230.0),
                new ProductData("Яблочный", "С корицей", 220.0)
        ));

        createProductsForCategory(classicBurgers, List.of(
                new ProductData("Чизбургер", "Классический", 250.0),
                new ProductData("Гамбургер", "Без сыра", 200.0),
                new ProductData("Бургер с беконом", "С беконом", 300.0)
        ));

    }

    private Category createCategory(String name, Category parent) {
        Category category = new Category();
        category.setName(name);
        category.setParent(parent);
        return categoryRepository.save(category);
    }

    private void createProductsForCategory(Category category, List<ProductData> productsData) {
        for (ProductData data : productsData) {
            Product product = new Product();
            product.setName(data.name());
            product.setDescription(data.description());
            product.setPrice(data.price());
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    private record ProductData(String name, String description, Double price) {}
}
