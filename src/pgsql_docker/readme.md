# Как запустить базу данных локально в докере

У меня тут спрашивали про использование странных СУБД в домашке, да и у себя я постгресс так и не отладил, так что я решил немного облегчить всем жизнь и показать как развернуть постгрес локально в докере.

**Дисклеймер**
Не используйте БД в докере на проде, в целом это работает, но считается дурным тоном по ряду причин.

## Установка Docker desktop

Просто следуйте этим инструкциям для установки докера

### Windows

https://docs.docker.com/desktop/install/windows-install/

P.S. Для винды используйте WSL2, не помню есть ли там по ходу установки ссылка на инструкцию, так что вот: https://docs.docker.com/desktop/wsl/

### Mac

https://docs.docker.com/desktop/install/mac-install/

## Docker

Тут можно почитать неплохую серию статей про то что такое докер и как им пользоваться, возможно когда-нибудь рассмотрим подробнее

https://habr.com/ru/companies/ruvds/articles/438796/

## Запускаем PostgreSQL в Docker

Использовать будем официальный образ постгреса с докерхаба, там же, кстати, есть инструкция как им пользоваться

https://hub.docker.com/_/postgres

Официальный образ постгреса позволяет настраивать кучу параметров, но для быстрого старта большинство из них можно оставить как есть, но вот пароль суперпользователя придётся задать явно:

`docker run --name test-pg-13.3 -p 5432:5432 -e POSTGRES_USER=testpguser -e POSTGRES_PASSWORD=testpgpass -e POSTGRES_DB=testdb -d postgres:13.3`

Также нам нужно сделать эту БД доступной извне, чтобы к ней могли подключаться приложения и IDE. Для этого нужно выставить наружу порт. Пользователя и базу данных не обязательно указывать явно, но так понятнее.

Но куда сейчас сохраняются наши данные? Докер конечно создает автоматически volume для контейнера с бд, но у такого подхода есть ряд недостатков. Хорошей практикой является полностью ручное управление физическим размещением создаваемых баз данных. Для этого нам нужно подмонтировать соответствующий каталог (куда будут сохраняться данные) в контейнер и при необходимости переопределить переменную окружения PGDATA:

`docker run --name test-pg-13.3 -p 5432:5432 -e POSTGRES_USER=testpguser -e POSTGRES_PASSWORD=testpgpass -e POSTGRES_DB=testdb -e PGDATA=/var/lib/postgresql/data/pgdata -d -v "/absolute/path/to/directory-with-data":/var/lib/postgresql/data -v "/absolute/path/to/directory-with-init-scripts":/docker-entrypoint-initdb.d postgres:13.3`

Уже выглядит страшненько, ну, обычно однострочниками никто и не оперирует, а используют докер композ. По идее это надо уже для управления портянками контейнеров, но можно и одним, проще же.

https://docs.docker.com/compose/compose-file/

```
version: "3.9"
services:
  postgres:
    image: postgres:16.1-alpine3.18
    environment:
      POSTGRES_DB: "testdb"
      POSTGRES_USER: "testpguser"
      POSTGRES_PASSWORD: "testpgpass"
      PGDATA: "/var/lib/postgresql/data/pgdata"
    volumes:
      - ../2. Init Database:/docker-entrypoint-initdb.d
      - testdb-data:/var/lib/postgresql/data
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U testpguser -d testdb"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s
    restart: unless-stopped

volumes:
  testdb-data:
```

В целом, тут все то же самое что и в однострочнике, но еще добавился хелсчек с автоматическим перезапуском контейнера. С помощью докер композ мы можем управлять нашим контейнером вот так:

`docker-compose --project-name="test-pg" up -d`

`docker-compose --project-name="test-pg" down`

Но на самом деле можно наверн расписать это все и в докерфайле (потом добавлю сюда)