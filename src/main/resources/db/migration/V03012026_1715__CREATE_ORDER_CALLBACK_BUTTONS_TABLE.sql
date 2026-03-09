CREATE TABLE order_callback_buttons (
    id           VARCHAR(64) PRIMARY KEY,
    button_type  VARCHAR(32)  NOT NULL,
    label        VARCHAR(255) NOT NULL,
    payload_json jsonb        NOT NULL
);

-- Fraction options (f1–f8)
INSERT INTO order_callback_buttons (id, button_type, label, payload_json)
VALUES ('f1', 'FRACTION', '0–1 • 300₽', '{
  "frac": "0-1",
  "name": "Гумус",
  "price": "300"
}'),
       ('f2', 'FRACTION', '1–3 • 340₽', '{
         "frac": "1-3",
         "name": "Мелкая",
         "price": "340"
       }'),
       ('f3', 'FRACTION', '2–3 • 350₽', '{
         "frac": "2-3",
         "name": "Мелкая",
         "price": "350"
       }'),
       ('f4', 'FRACTION', '3–5 • 450₽', '{
         "frac": "3-5",
         "name": "Универсальная",
         "price": "450"
       }'),
       ('f5', 'FRACTION', '2–6 • 460₽', '{
         "frac": "2-6",
         "name": "Средняя",
         "price": "460"
       }'),
       ('f6', 'FRACTION', '4–8 • 500₽', '{
         "frac": "4-8",
         "name": "Премиум",
         "price": "500"
       }'),
       ('f7', 'FRACTION', '6–12 • 520₽', '{
         "frac": "6-12",
         "name": "Крупная",
         "price": "520"
       }'),
       ('f8', 'FRACTION', '15–25 • 750₽', '{
         "frac": "15-25",
         "name": "Гигантская",
         "price": "750"
       }');

-- Delivery options
INSERT INTO order_callback_buttons (id, button_type, label, payload_json)
VALUES ('d1', 'DELIVERY', '🚚 Доставка', '{
  "method": "🚚 Доставка"
}'),
       ('d2', 'DELIVERY', '📍 Самовывоз (Малино)', '{
         "method": "📍 Самовывоз (Малино)"
       }');

-- Edit menu options (target = OrderStep enum name)
INSERT INTO order_callback_buttons (id, button_type, label, payload_json)
VALUES ('e_menu', 'EDIT', '✏️ Изменить', '{
  "target": "EDIT_MENU"
}'),
       ('e_frac', 'EDIT', '🧩 Фракцию', '{
         "target": "FRACTION"
       }'),
       ('e_qty', 'EDIT', '🔢 Количество', '{
         "target": "QUANTITY"
       }'),
       ('e_del', 'EDIT', '🚚 Получение/адрес', '{
         "target": "DELIVERY_METHOD"
       }'),
       ('e_phone', 'EDIT', '📞 Телефон', '{
         "target": "PHONE"
       }'),
       ('e_back', 'EDIT', '↩️ Назад', '{
         "target": "CONFIRMATION"
       }');

-- Cancel
INSERT INTO order_callback_buttons (id, button_type, label, payload_json)
VALUES ('cancel', 'CANCEL', '❌ Отмена', '{}');
