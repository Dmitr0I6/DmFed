package ru.dmitry.tgBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dmitry.tgBot.entity.*;
import ru.dmitry.tgBot.service.EntitiesService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramBotConnection {

    private final EntitiesService entitiesService;
    private TelegramBot bot;

    @Value("${telegram.bot.token}")
    private String botToken;


    private final ConcurrentHashMap<Long, Long> Context = new ConcurrentHashMap<>();

    public TelegramBotConnection(EntitiesService entitiesService) {
        this.entitiesService = entitiesService;
    }

    @PostConstruct
    public void start() {
        bot = new TelegramBot(botToken);
        bot.setUpdatesListener(new TelegramUpdatesListener());
    }

    private class TelegramUpdatesListener implements UpdatesListener {
        @Override
        public int process(List<Update> updates) {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }

        private void processUpdate(Update update) {
            if (update.message() != null) {
                processMessage(update.message());
            } else if (update.callbackQuery() != null) {
                processCallbackQuery(update.callbackQuery());
            }
        }

        private void processMessage(Message message) {
            Long chatId = message.chat().id();
            String text = message.text();

            Client client = entitiesService.getOrCreateClient(
                    chatId,
                    message.from().firstName() + (message.from().lastName() != null ? " " + message.from().lastName() : ""),
                    "-",
                    "-"
            );

            // Создаем заказ для клиента 
            ClientOrder activeOrder = entitiesService.getOrCreateActiveOrder(client);

            if ("/start".equals(text) || "На главное меню".equals(text)) {
                Context.remove(chatId);
                showStartMenu(chatId);
            } else if ("Оформить заказ".equals(text)) {
                handleCheckout(chatId, activeOrder);
            } else {
                handleCategory(chatId, text);
            }
        }

        private void processCallbackQuery(CallbackQuery callbackQuery) {
            Long chatId = callbackQuery.message().chat().id();
            String data = callbackQuery.data();
            Client client = entitiesService.getOrCreateClient(
                    chatId,
                    callbackQuery.from().firstName() + (callbackQuery.from().lastName() != null ? " " + callbackQuery.from().lastName() : ""),
                    "-",
                    "-"
            );
            ClientOrder activeOrder = entitiesService.getOrCreateActiveOrder(client);

            if (data.startsWith("product:")) {
                Long productId = Long.parseLong(data.substring("product:".length()));
                entitiesService.addProductToOrder(activeOrder, productId);
                Product addedProduct = entitiesService.getProductById(productId);
                if (addedProduct != null) {
                    bot.execute(new SendMessage(chatId, "'" + addedProduct.getName() + "' добавлен в заказ"));
                }
                Long currentCategoryId = Context.get(chatId);
                if (currentCategoryId != null) {
                    showCategoryMenu(chatId, currentCategoryId);
                } else {
                    showStartMenu(chatId);
                }
            }
        }

        private ReplyKeyboardMarkup createReplyKeyboardMarkup(List<Category> categories, boolean includeGoToMainMenu) {
            ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(new KeyboardButton[][]{})
                    .resizeKeyboard(true);

            if (!categories.isEmpty()) {
                int buttonsPerRow = 3;
                for (int i = 0; i < categories.size(); i += buttonsPerRow) {
                    List<KeyboardButton> row = new ArrayList<>();
                    for (int j = 0; j < buttonsPerRow && (i + j) < categories.size(); j++) {
                        row.add(new KeyboardButton(categories.get(i + j).getName()));
                    }
                    markup.addRow(row.toArray(new KeyboardButton[0]));
                }
            }

            List<KeyboardButton> mainButtons = new ArrayList<>();
            mainButtons.add(new KeyboardButton("Оформить заказ"));
            if (includeGoToMainMenu) {
                mainButtons.add(new KeyboardButton("На главное меню"));
            }
            markup.addRow(mainButtons.toArray(new KeyboardButton[0]));

            return markup;
        }

        private void showCategoryMenu(Long chatId, Long categoryId) {
            List<Category> subcategories = entitiesService.getCategoriesByParentId(categoryId);
            List<Product> products = entitiesService.getProductsByCategoryIdForDisplay(categoryId);
            ReplyKeyboardMarkup replyMarkup = createReplyKeyboardMarkup(subcategories, true);
            bot.execute(new SendMessage(chatId, "Выберите подкатегорию или товар:")
                    .replyMarkup(replyMarkup));
            if (!products.isEmpty()) {
                InlineKeyboardMarkup inlineMarkup = new InlineKeyboardMarkup();
                for (Product product : products) {
                    InlineKeyboardButton button = new InlineKeyboardButton(String.format("%s. Цена: %.2f руб.", product.getName(), product.getPrice()))
                            .callbackData("product:" + product.getId());
                    inlineMarkup.addRow(button);
                }
                bot.execute(new SendMessage(chatId, "Для выбора товара нажмите на кнопку")
                        .replyMarkup(inlineMarkup));
            } else if (subcategories.isEmpty() && products.isEmpty()) {
                bot.execute(new SendMessage(chatId, "В категории пока ничего нет"));
                Context.remove(chatId);
                showStartMenu(chatId);
            }
        }

        private void showStartMenu(Long chatId) {
            List<Category> categories = entitiesService.getCategoriesByParentId(null);
            ReplyKeyboardMarkup markup = createReplyKeyboardMarkup(categories, false);
            bot.execute(new SendMessage(chatId, "Выберите категорию:")
                    .replyMarkup(markup));
            if (categories.isEmpty()) {
                bot.execute(new SendMessage(chatId, "Нет категории"));
            }
        }

        private void handleCheckout(Long chatId, ClientOrder activeOrder) {
            List<OrderProduct> orderProducts = entitiesService.getOrderProducts(activeOrder);

            if (orderProducts.isEmpty()) {
                bot.execute(new SendMessage(chatId, "Ваш заказ пуст, добавьте товары "));
                Long currentCategoryId = Context.get(chatId);
                if (currentCategoryId != null) {
                    showCategoryMenu(chatId, currentCategoryId);
                } else {
                    showStartMenu(chatId);
                }
                return;
            }

            StringBuilder orderSummary = new StringBuilder("Ваш заказ:\n");
            for (OrderProduct op : orderProducts) {
                orderSummary.append(String.format("%s %dx%.2f=%.2f руб.\n",
                        op.getProduct().getName(),
                        op.getCountProduct(),
                        op.getProduct().getPrice(),
                        op.getProduct().getPrice() * op.getCountProduct()));
            }
            Double total = entitiesService.calculateOrderTotal(activeOrder);
            orderSummary.append(String.format("Итого %.2f руб.", total));

            bot.execute(new SendMessage(chatId, orderSummary.toString()));

            try {
                entitiesService.closeOrder(activeOrder);
                Client client = entitiesService.getClientById(activeOrder.getClient().getId());
                if (client != null) {
                    entitiesService.getOrCreateActiveOrder(client);
                }

                bot.execute(new SendMessage(chatId, "Заказ №" + activeOrder.getId() + " успешно создан"));
                Context.remove(chatId);
                showStartMenu(chatId);
            } catch (IllegalStateException e) {
                bot.execute(new SendMessage(chatId, "Ошибка при оформлении заказа: " + e.getMessage()));
                Long currentCategoryId = Context.get(chatId);
                if (currentCategoryId != null) {
                    showCategoryMenu(chatId, currentCategoryId);
                } else {
                    showStartMenu(chatId);
                }
            }
        }

        private void handleCategory(Long chatId, String categoryName) {
            Long currentContextCategoryId = Context.get(chatId);

            List<Category> categoriesToCheck;
            if (currentContextCategoryId == null) {
                categoriesToCheck = entitiesService.getCategoriesByParentId(null);
            } else {
                categoriesToCheck = entitiesService.getCategoriesByParentId(currentContextCategoryId);
            }

            Optional<Category> selectedCategory = categoriesToCheck.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(categoryName))
                    .findFirst();

            if (selectedCategory.isPresent()) {
                Context.put(chatId, selectedCategory.get().getId());
                showCategoryMenu(chatId, selectedCategory.get().getId());
            } else {
                bot.execute(new SendMessage(chatId, "Неизвестная команда"));
                if (currentContextCategoryId != null) {
                    showCategoryMenu(chatId, currentContextCategoryId);
                } else {
                    showStartMenu(chatId);
                }
            }
        }
    }
}
