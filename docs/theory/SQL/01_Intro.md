# Введение в SQL

SQL (Structured Query Language) — декларативный язык применяемый для создания, модификации и управления данными в БД посредством СУБД. В конкретной СУБД, язык SQL может иметь специфичную реализацию.

## ERD

ERD (Entity-Relationship Diagram) — это концептуальная графическая модель данных, которая используется для визуализации, описания и анализа структуры данных или информационного окружения в системе.

![ERD](/res/imgs/erd.png)

## DDL

DDL (Data Definition Language) - язык для создания и модификации структуры БД, т.е. для создания/изменения/удаления таблиц и связей.

### Создание/удаление БД

```sql
CREATE DATABASE Test

DROP DATABASE Test
```

### Создание/удаление/изменение таблиц

```sql
create table Person (
    Id int not null identity(1, 1)
  , PersonRoleId int not null constraint DF_Person_PersonRoleId default (1)
  , [Name] nvarchar(255) null
  , [Login] nvarchar(255) not null
  , IsActive bit not null
  , CreatedBy int null
  , CreatedAtUTC datetime not null constraint DF_PersonCreatedAtUTC default getutcdate()
  , ChangedAtUTC datetime not null
  , ChangedBy int null
  , Mail nvarchar(max) null
  , constraint PK_Person primary key clustered (Id)
  , constraint FK_Person_Person_CreatedBy foreign key (CreatedBy) references Person (Id)
  , constraint FK_Person_Person_ChangedBy foreign key (ChangedBy) references Person (Id)
  , constraint FK_Person_PersonRole_PersonRoleId foreign key (PersonRoleId) references PersonRole (Id)
)

drop table Person

alter table Person add Address nvarchar(50) null;
alter table Person alter column [Name] nvarchar(30) not null
```

## DML

DML (Data Manipulation Language) - язык для манипуляции с данными таблиц, т.е. с ее строками. Он позволяет делать выборку данных из таблиц, добавлять новые данные в таблицы, а так же обновлять и удалять существующие данные.

### Вставка/удаление/изменеие строк

```sql
INSERT Employees
       (ID, Position, Department)
VALUES (1000, N'Директор', N'Администрация'),
       (1001, N'Программист', N'ИТ'),

INSERT INTO Products
DEFAULT VALUES
```