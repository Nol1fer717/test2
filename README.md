# Task Tracker E2E Tests

Автоматизированные E2E тесты для таск-трекера Avito с использованием Java + Playwright.

## Описание

Проект содержит автоматизированные тесты для проверки основных пользовательских сценариев:
- Создание задачи
- Поиск задачи
- Открытие карточки задачи
- Переход на доску проекта

**URL тестируемого приложения:** https://avito-tech-internship-psi.vercel.app/issues

## Технологии

- **Java 17** - язык программирования
- **Playwright** - фреймворк для E2E тестирования
- **JUnit 5** - тестовый фреймворк
- **Maven** - система сборки

## Структура проекта

```
├── pom.xml                           # Maven конфигурация
├── src/
│   ├── main/java/pages/              # Page Object классы
│   │   ├── BasePage.java             # Базовый класс страницы
│   │   ├── IssuesPage.java           # Страница списка задач
│   │   ├── CreateTaskModal.java      # Модальное окно создания задачи
│   │   ├── TaskCardModal.java        # Карточка задачи
│   │   ├── ProjectsPage.java         # Страница проектов
│   │   └── BoardPage.java            # Kanban доска
│   └── test/java/
│       ├── tests/                    # Тестовые классы
│       │   ├── CreateTaskTests.java  # Тесты создания задачи
│       │   ├── SearchTaskTests.java  # Тесты поиска
│       │   ├── TaskCardTests.java    # Тесты карточки задачи
│       │   └── BoardNavigationTests.java # Тесты навигации по доске
│       └── utils/
│           └── BaseTest.java         # Базовый класс теста
├── TESTCASES.md                      # Документация тест-кейсов
├── BUGS.md                           # Найденные баги (если есть)
└── README.md                         # Этот файл
```

## Требования

- Java 17 или выше
- Maven 3.6 или выше
- Доступ к интернету (для загрузки зависимостей и доступа к тестируемому сайту)

## Установка

1. Клонировать репозиторий:
```bash
git clone <repository-url>
cd test2
```

2. Установить зависимости Maven:
```bash
mvn install -DskipTests
```

3. Установить браузеры Playwright:
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

## Запуск тестов

### Запуск всех тестов:
```bash
mvn test
```

### Запуск конкретного тестового класса:
```bash
# Тесты создания задачи
mvn test -Dtest=CreateTaskTests

# Тесты поиска
mvn test -Dtest=SearchTaskTests

# Тесты карточки задачи
mvn test -Dtest=TaskCardTests

# Тесты навигации по доске
mvn test -Dtest=BoardNavigationTests
```

### Запуск конкретного теста:
```bash
mvn test -Dtest=CreateTaskTests#testCreateTaskWithRequiredFields
```

### Запуск в режиме с видимым браузером (не headless):
Измените в `BaseTest.java`:
```java
.setHeadless(false)
```

## Тест-кейсы

Подробное описание тест-кейсов находится в файле [TESTCASES.md](TESTCASES.md).

### Покрытие сценариев:

| Сценарий | Количество тестов |
|----------|-------------------|
| Создание задачи | 6 |
| Поиск задачи | 7 |
| Открытие карточки задачи | 4 |
| Переход на доску проекта | 5 |
| **Всего** | **22** |

## Page Object Model

Проект использует паттерн Page Object для организации кода:

- **BasePage** - базовый класс с общими методами для всех страниц
- **IssuesPage** - страница списка задач с поиском и фильтрами
- **CreateTaskModal** - модальное окно создания новой задачи
- **TaskCardModal** - модальное окно просмотра/редактирования задачи
- **ProjectsPage** - страница со списком проектов
- **BoardPage** - Kanban доска проекта

## Отчёт о багах

Если в процессе тестирования обнаружены баги, они документируются в файле [BUGS.md](BUGS.md).

## Автор

Автоматизированные тесты созданы для Avito Tech Internship.
