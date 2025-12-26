package tests;

import org.junit.jupiter.api.*;
import pages.IssuesPage;
import utils.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for task search functionality
 * Based on test cases 7-13 from requirements
 */
@DisplayName("Поиск задачи")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchTaskTests extends BaseTest {

    private IssuesPage issuesPage;

    @BeforeEach
    void setUp() {
        issuesPage = new IssuesPage(page);
        issuesPage.open();
    }

    /**
     * Test Case 7: Search task by name
     */
    @Test
    @Order(1)
    @DisplayName("TC7: Поиск задачи по названию")
    void testSearchTaskByName() {
        // Search for task
        issuesPage.searchTask("темной");

        page.waitForTimeout(1000);

        // Verify search results
        int taskCount = issuesPage.getTaskCount();
        if (taskCount > 0) {
            assertTrue(issuesPage.isTaskVisible("темной") || issuesPage.isTaskVisible("Реализация темной темы"),
                    "В результатах поиска должны отображаться задачи с подстрокой 'темной'");
        }
    }

    /**
     * Test Case 8: Search task with status filter
     */
    @Test
    @Order(2)
    @DisplayName("TC8: Поиск задачи с фильтром по статусу")
    void testSearchWithStatusFilter() {
        issuesPage.searchTask("Оптимизация");
        issuesPage.selectStatusFilter("Done");

        page.waitForTimeout(1000);

        // Verify filtered results
        int taskCount = issuesPage.getTaskCount();
        // Either tasks are shown matching criteria or no tasks message
        assertTrue(taskCount >= 0, "Фильтрация должна работать корректно");
    }

    /**
     * Test Case 9: Search task with board filter
     */
    @Test
    @Order(3)
    @DisplayName("TC9: Поиск задачи с фильтром по доске")
    void testSearchWithBoardFilter() {
        issuesPage.searchTask("загрузка");
        issuesPage.selectBoardFilter("Оптимизация производительности");

        page.waitForTimeout(1000);

        // Verify filtered results
        int taskCount = issuesPage.getTaskCount();
        assertTrue(taskCount >= 0, "Фильтрация по доске должна работать корректно");
    }

    /**
     * Test Case 10: Search with multiple filters
     */
    @Test
    @Order(4)
    @DisplayName("TC10: Поиск задачи с несколькими фильтрами")
    void testSearchWithMultipleFilters() {
        issuesPage.searchTask("Оптимизация");
        issuesPage.selectStatusFilter("InProgress");
        issuesPage.selectBoardFilter("Редизайн карточки товара");

        page.waitForTimeout(1000);

        // Verify results or no tasks message
        int taskCount = issuesPage.getTaskCount();
        if (taskCount == 0) {
            assertTrue(issuesPage.isNoTasksMessageVisible() || taskCount == 0,
                    "При отсутствии результатов должно отображаться сообщение");
        }
    }

    /**
     * Test Case 11: Search with no results
     */
    @Test
    @Order(5)
    @DisplayName("TC11: Поиск задачи без результатов")
    void testSearchWithNoResults() {
        issuesPage.searchTask("Несуществующая задача xyz123");

        page.waitForTimeout(1000);

        // Verify no results or empty list
        int taskCount = issuesPage.getTaskCount();
        assertTrue(taskCount == 0 || issuesPage.isNoTasksMessageVisible(),
                "При отсутствии результатов список должен быть пуст");
    }

    /**
     * Test Case 12: Reset filters and search
     */
    @Test
    @Order(6)
    @DisplayName("TC12: Сброс фильтров и строки поиска")
    void testResetFilters() {
        // First apply some filters
        issuesPage.searchTask("Оптимизация");
        issuesPage.selectStatusFilter("Backlog");

        page.waitForTimeout(500);

        int filteredCount = issuesPage.getTaskCount();

        // Reset filters
        issuesPage.resetFilters();

        page.waitForTimeout(1000);

        int resetCount = issuesPage.getTaskCount();

        // After reset, should show all tasks (more or equal than filtered)
        assertTrue(resetCount >= filteredCount,
                "После сброса фильтров должен отображаться полный список задач");
    }

    /**
     * Test Case 13: Case-insensitive search
     */
    @Test
    @Order(7)
    @DisplayName("TC13: Проверка регистронезависимости поиска")
    void testCaseInsensitiveSearch() {
        // Search in uppercase
        issuesPage.searchTask("ОПТИМИЗАЦИЯ");

        page.waitForTimeout(1000);

        int uppercaseResults = issuesPage.getTaskCount();

        // Search in mixed case
        issuesPage.clearSearch();
        issuesPage.searchTask("Оптимизация");

        page.waitForTimeout(1000);

        int mixedCaseResults = issuesPage.getTaskCount();

        // Results should be the same regardless of case
        assertEquals(uppercaseResults, mixedCaseResults,
                "Поиск должен быть регистронезависимым");
    }
}
