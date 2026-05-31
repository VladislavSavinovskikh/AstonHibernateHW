package com.hibernatehw;

import com.hibernatehw.service.UserService;
import com.hibernatehw.ui.UI;
import com.hibernatehw.util.HibernateUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class App {
    static void main() {
        log.info("=== ЗАПУСК ПРИЛОЖЕНИЯ ===");
        try {
            UserService userService = new UserService();
            UI ui = new UI(userService);
            ui.run();
        } catch (Exception e) {
            log.error("Критическая ошибка приложения: {}", e.getMessage(), e);
        } finally {
            log.debug("Закрытие ресурсов...");
            HibernateUtil.shutdown();
            log.info("=== ЗАВЕРШЕНИЕ РАБОТЫ ===");
        }
    }
}
