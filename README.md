# bark-bot

Добро пожаловать в «Золото Сибири»
Здесь вы можете:

- выбрать фракцию коры лиственницы под ваши задачи
- узнать стоимость
- оставить заявку на доставку

Нажмите «Старт», чтобы посмотреть каталог и цены.

## After cloning repo

1. Settings → Tools → Actions on Save → Reformat code + Optimize imports
2. Commit window → Commit settings → Reformat code + Optimize imports
3. Copy `.env.example` to `.env` and fill in the values
3. Edit configurations
    1. Add new Maven configuration with command `clean install`
    2. Add `clean install` to Before launch section of main configuration
    3. Add `.env` file to Environment variables section of main configuration
    4. Add local profile to active profiles section of main configuration