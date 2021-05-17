# Лабораторная 6
Разделить программу из лабораторной работы №5 на клиентский и серверный модули. Серверный модуль должен осуществлять выполнение команд по управлению коллекцией. Клиентский модуль должен в интерактивном режиме считывать команды, передавать их для выполнения на сервер и выводить результаты выполнения.

Необходимо выполнить следующие требования:

Операции обработки объектов коллекции должны быть реализованы с помощью Stream API с использованием лямбда-выражений.
Объекты между клиентом и сервером должны передаваться в сериализованном виде.
Объекты в коллекции, передаваемой клиенту, должны быть отсортированы по размеру
Клиент должен корректно обрабатывать временную недоступность сервера.
Обмен данными между клиентом и сервером должен осуществляться по протоколу TCP
Для обмена данными на сервере необходимо использовать сетевой канал
Для обмена данными на клиенте необходимо использовать потоки ввода-вывода
Сетевые каналы должны использоваться в неблокирующем режиме.
