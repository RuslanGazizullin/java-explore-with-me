# java-explore-with-me

Pull request: https://github.com/RuslanGazizullin/java-explore-with-me/pull/1

Основной сервис:
API основного сервиса разделён на три части. Первая — публичная, доступна без регистрации любому пользователю сети. Вторая — закрытая, доступна только авторизованным пользователям. Третья — административная, для администраторов сервиса.

Сервис статистики:
Второй сервис, статистики, призван собирать информацию. Во-первых, о количестве обращений пользователей к спискам событий и, во-вторых, о количестве запросов к подробной информации о событии. На основе этой информации формируется статистика о работе приложения.

Специцикация основного сервиса: https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-main-service-spec.json

Спецификация сервиса статистики: https://raw.githubusercontent.com/yandex-praktikum/java-explore-with-me/main/ewm-stats-service-spec.json

Для работы со спецификациями понадобится редактор Swagger: https://editor-next.swagger.io/

Запуск приложения происходит из командной строки командой docker-compose up.

Схема базы данных основного сервиса: ![main service DB schema](https://user-images.githubusercontent.com/97000877/204021013-437f6bb7-d839-4d55-b1c4-c73396106862.png)

Схема базы данных сервиса статистики: ![stats service DB schema](https://user-images.githubusercontent.com/97000877/204021037-84e3618a-9c43-4819-9c33-7adf6aaebf9e.png)
