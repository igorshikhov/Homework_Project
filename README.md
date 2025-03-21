# Homework_Project

Учебный проект курса OTUS

Предназначен для демонстрации использования возможностей VK MAPS API.

Приложение получает список "точек интереса" и отображает их на карте.

Критерий выбора "точек интереса" задается в настройках.

Для получения данных используется сервис https://maps.vk.com/api/places

Для получения образа карты используется сервис https://maps.vk.com/api/staticmap/png

Два режима просмотра "точек интереса" - списком и на карте - между которыми можно переключаться.

Карта позиционируется относилельно выбранного элемента списка (он помещается в центр).

Из-за отсутствия ключа приложения используется тестовый доступ к сервисам (с ограничениями).

TODO:

добавить хранение настроек в файловой системе.

добавить содание собственных "точек интереса" и хранение их в локальной базе (Room).

при наличии ключа приложения подключить возможности управления картой с помощью инструментов API.







