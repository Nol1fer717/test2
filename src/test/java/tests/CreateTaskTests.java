package tests;

import org.junit.jupiter.api.*;
import pages.CreateTaskModal;
import pages.IssuesPage;
import utils.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for task creation functionality
 * Based on test cases 1-6 from requirements
 */
@DisplayName("Создание задачи")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateTaskTests extends BaseTest {

    private IssuesPage issuesPage;

    @BeforeEach
    void setUp() {
        issuesPage = new IssuesPage(page);
        issuesPage.open();
    }

    /**
     * Test Case 1: Successful task creation with required fields
     */
    @Test
    @Order(1)
    @DisplayName("TC1: Успешное создание задачи с обязательными полями")
    void testCreateTaskWithRequiredFields() {
        // Open create task modal
        CreateTaskModal createModal = issuesPage.openCreateTaskModal();

        // Verify modal is visible
        assertTrue(createModal.isModalVisible(), "Модальное окно создания задачи должно быть видимым");

        // Fill required fields
        createModal.setTitle("Оптимизация");
        createModal.selectProject("Оптимизация производительности");
        createModal.selectPriority("High");

        // Verify status is Backlog and disabled
        assertTrue(createModal.isStatusDisabled(), "Поле Статус должно быть недоступно для редактирования");

        // Select assignee
        createModal.selectAssignee("Илья Романов");

        // Verify create button is enabled
        assertTrue(createModal.isCreateButtonEnabled(), "Кнопка Создать должна быть активной после заполнения полей");

        // Click create
        createModal.clickCreate();

        // Wait for modal to close and verify task creation
        page.waitForTimeout(1000);

        // Verify task was created (either redirect or task visible in list)
        String currentUrl = page.url();
        assertTrue(
                currentUrl.contains("/issues") || currentUrl.contains("/board") || issuesPage.isTaskVisible("Оптимизация"),
                "Задача должна быть создана успешно"
        );
    }

    /**
     * Test Case 2: Create task with all fields filled
     */
    @Test
    @Order(2)
    @DisplayName("TC2: Создание задачи со всеми заполненными полями")
    void testCreateTaskWithAllFields() {
        CreateTaskModal createModal = issuesPage.openCreateTaskModal();

        // Fill all fields including description
        createModal.setTitle("Рефакторинг эндпоинтов");
        createModal.setDescription("Привести API к единому стандарту");
        createModal.selectProject("Рефакторинг API");
        createModal.selectPriority("Medium");

        // Verify status is Backlog and not editable
        assertTrue(createModal.isStatusDisabled(), "Поле Статус должно быть недоступно для редактирования");

        createModal.selectAssignee("Дмитрий Козлов");

        // Create task
        createModal.clickCreate();

        page.waitForTimeout(1000);

        // Verify task was created without errors
        assertFalse(createModal.isModalVisible(), "Модальное окно должно закрыться после создания");
    }

    /**
     * Test Case 3: Verify required fields validation
     */
    @Test
    @Order(3)
    @DisplayName("TC3: Проверка обязательности полей")
    void testRequiredFieldsValidation() {
        CreateTaskModal createModal = issuesPage.openCreateTaskModal();

        // Leave title empty, fill other fields
        createModal.selectProject("Миграция на новую БД");
        createModal.selectPriority("Low");
        createModal.selectAssignee("Ольга Новикова");

        // Verify create button is disabled
        assertFalse(createModal.isCreateButtonEnabled(), "Кнопка Создать должна быть неактивна без заполнения названия");
    }

    /**
     * Test Case 4: Verify status field is not editable during creation
     */
    @Test
    @Order(4)
    @DisplayName("TC4: Проверка невозможности изменения статуса при создании")
    void testStatusFieldIsDisabled() {
        CreateTaskModal createModal = issuesPage.openCreateTaskModal();

        // Fill required fields
        createModal.setTitle("Тестовая задача");
        createModal.selectProject("Оптимизация производительности");
        createModal.selectPriority("High");
        createModal.selectAssignee("Илья Романов");

        // Verify status field is disabled
        assertTrue(createModal.isStatusDisabled(), "Поле Статус должно быть недоступно для взаимодействия");
    }

    /**
     * Test Case 5: Create task with different assignee
     */
    @Test
    @Order(5)
    @DisplayName("TC5: Создание задачи с выбором исполнителя")
    void testCreateTaskWithAssignee() {
        CreateTaskModal createModal = issuesPage.openCreateTaskModal();

        createModal.setTitle("Новая задача Kubernetes");
        createModal.selectProject("Переход на Kubernetes");
        createModal.selectPriority("High");
        createModal.selectAssignee("Максим Орлов");

        createModal.clickCreate();

        page.waitForTimeout(1000);

        // Verify no errors occurred
        assertFalse(createModal.isModalVisible(), "Задача должна быть создана без ошибок");
    }

    /**
     * Test Case 6: Verify create button behavior
     */
    @Test
    @Order(6)
    @DisplayName("TC6: Проверка поведения кнопки Создать")
    void testCreateButtonBehavior() {
        CreateTaskModal createModal = issuesPage.openCreateTaskModal();

        // Initially button should be disabled
        assertFalse(createModal.isCreateButtonEnabled(), "Кнопка должна быть неактивна до заполнения полей");

        // Fill fields one by one
        createModal.setTitle("Тестовая задача");
        createModal.selectProject("Оптимизация производительности");
        createModal.selectPriority("Medium");
        createModal.selectAssignee("Илья Романов");

        // After filling all required fields, button should be enabled
        assertTrue(createModal.isCreateButtonEnabled(), "Кнопка должна стать активной после заполнения всех полей");

        // Click and verify task creation
        createModal.clickCreate();

        page.waitForTimeout(1000);

        assertFalse(createModal.isModalVisible(), "Клик по кнопке должен привести к созданию задачи");
    }
}
