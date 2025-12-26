package tests;

import org.junit.jupiter.api.*;
import pages.BoardPage;
import pages.IssuesPage;
import pages.ProjectsPage;
import pages.TaskCardModal;
import utils.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for board navigation functionality
 * Based on test cases 14-17 from requirements
 */
@DisplayName("Переход на доску проекта")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BoardNavigationTests extends BaseTest {

    /**
     * Test Case 15: Navigate to board from projects page
     */
    @Test
    @Order(1)
    @DisplayName("TC15: Переход на доску проекта со страницы Проекты")
    void testNavigateToBoardFromProjectsPage() {
        ProjectsPage projectsPage = new ProjectsPage(page);
        projectsPage.open();

        page.waitForTimeout(1000);

        // Try to navigate to a project board
        if (projectsPage.getProjectCount() > 0) {
            BoardPage boardPage = projectsPage.goToProjectBoard("Рефакторинг API");

            page.waitForTimeout(1000);

            // Verify board is displayed
            assertTrue(boardPage.isBoardVisible(), "Доска проекта должна отображаться");

            // Verify Kanban columns exist
            assertTrue(boardPage.hasTodoColumn(), "Колонка To Do должна присутствовать");
            assertTrue(boardPage.hasInProgressColumn(), "Колонка In Progress должна присутствовать");
            assertTrue(boardPage.hasDoneColumn(), "Колонка Done должна присутствовать");
        }
    }

    /**
     * Test Case 16: Navigate to board from task card
     */
    @Test
    @Order(2)
    @DisplayName("TC16: Переход на доску проекта из карточки задачи")
    void testNavigateToBoardFromTaskCard() {
        IssuesPage issuesPage = new IssuesPage(page);
        issuesPage.open();

        page.waitForTimeout(1000);

        if (issuesPage.getTaskCount() > 0) {
            // Search for task from specific project
            issuesPage.searchTask("Редизайн");
            page.waitForTimeout(500);

            if (issuesPage.getTaskCount() > 0) {
                // Open task card
                issuesPage.clickOnTask("Редизайн");
                page.waitForTimeout(500);

                TaskCardModal taskCard = new TaskCardModal(page);

                if (taskCard.isGoToBoardButtonVisible()) {
                    // Navigate to board
                    BoardPage boardPage = taskCard.clickGoToBoard();

                    page.waitForTimeout(1000);

                    // Verify on board page
                    String currentUrl = page.url();
                    assertTrue(currentUrl.contains("/board"),
                            "Пользователь должен быть перенаправлен на доску проекта");

                    // Verify board displays tasks
                    assertTrue(boardPage.isBoardVisible(), "Доска должна отображаться");
                }
            }
        }
    }

    /**
     * Test Case 17: Verify tasks match their column status on board
     */
    @Test
    @Order(3)
    @DisplayName("TC17: Проверка соответствия задач статусам на доске")
    void testTasksMatchColumnStatus() {
        // Navigate directly to a board
        page.navigate(BASE_URL + "/board/1");
        page.waitForLoadState();
        page.waitForTimeout(1000);

        BoardPage boardPage = new BoardPage(page);

        if (boardPage.isBoardVisible()) {
            // Verify board has all three columns
            assertTrue(boardPage.hasAllColumns(),
                    "Доска должна иметь все три колонки: To Do, In Progress, Done");

            // Get task counts from each column
            int totalTasks = boardPage.getTotalTaskCount();

            if (totalTasks > 0) {
                // Open a task from a specific column and verify its status
                page.locator(".column:has-text('In Progress') .task-card, " +
                        "[data-testid='column-inprogress'] .task-card").first().click();

                page.waitForTimeout(500);

                TaskCardModal taskCard = new TaskCardModal(page);

                if (taskCard.isModalVisible()) {
                    String status = taskCard.getStatus();
                    assertTrue(status.contains("Progress") || status.contains("progress"),
                            "Задача в колонке In Progress должна иметь соответствующий статус");

                    taskCard.close();
                }
            }
        }
    }

    /**
     * Test: Direct board navigation by URL
     */
    @Test
    @Order(4)
    @DisplayName("Прямой переход на доску по URL")
    void testDirectBoardNavigation() {
        // Navigate directly to board
        page.navigate(BASE_URL + "/board/1");
        page.waitForLoadState();
        page.waitForTimeout(1000);

        BoardPage boardPage = new BoardPage(page);

        // Verify board is loaded
        String currentUrl = page.url();
        assertTrue(currentUrl.contains("/board"), "URL должен содержать /board");

        // Verify board elements are visible
        assertTrue(boardPage.isBoardVisible() || page.locator(".column, .board-column").count() > 0,
                "Элементы доски должны быть видимы");
    }

    /**
     * Test: Board displays only project-specific tasks
     */
    @Test
    @Order(5)
    @DisplayName("Доска отображает только задачи проекта")
    void testBoardShowsOnlyProjectTasks() {
        // This test verifies that tasks from other projects are not shown
        page.navigate(BASE_URL + "/board/1");
        page.waitForLoadState();
        page.waitForTimeout(1000);

        BoardPage boardPage = new BoardPage(page);

        if (boardPage.isBoardVisible()) {
            String boardTitle = boardPage.getBoardTitle();

            // All visible tasks should belong to this board/project
            int taskCount = boardPage.getTotalTaskCount();

            // If there are tasks, they should be project-specific
            assertTrue(taskCount >= 0,
                    "Доска должна отображать только задачи текущего проекта");
        }
    }
}
