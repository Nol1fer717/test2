package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;

/**
 * Page Object for Issues (Tasks) list page
 */
public class IssuesPage extends BasePage {

    // Locators
    private static final String SEARCH_INPUT = "input[placeholder*='Поиск']";
    private static final String STATUS_FILTER = "select[name='status'], [data-testid='status-filter'], .status-filter select";
    private static final String BOARD_FILTER = "select[name='board'], [data-testid='board-filter'], .board-filter select";
    private static final String CREATE_TASK_BUTTON = "button:has-text('Создать задачу'), [data-testid='create-issue-btn']";
    private static final String TASK_LIST = ".issue-list, .task-list, [data-testid='issues-list']";
    private static final String TASK_CARD = ".issue-card, .task-card, [data-testid='issue-card']";
    private static final String NO_TASKS_MESSAGE = ":has-text('Задачи не найдены'), :has-text('Нет задач')";

    public IssuesPage(Page page) {
        super(page);
    }

    public void open() {
        page.navigate(BASE_URL + "/issues");
        waitForPageLoad();
    }

    public void searchTask(String query) {
        page.locator(SEARCH_INPUT).fill(query);
        // Wait for search results to update
        page.waitForTimeout(500);
    }

    public void clearSearch() {
        page.locator(SEARCH_INPUT).clear();
        page.waitForTimeout(500);
    }

    public void selectStatusFilter(String status) {
        Locator statusFilter = page.locator(STATUS_FILTER).first();
        if (statusFilter.isVisible()) {
            statusFilter.selectOption(status);
        } else {
            // Try clicking on filter dropdown
            page.locator("text=Статус").click();
            page.locator("text=" + status).click();
        }
        page.waitForTimeout(500);
    }

    public void selectBoardFilter(String board) {
        Locator boardFilter = page.locator(BOARD_FILTER).first();
        if (boardFilter.isVisible()) {
            boardFilter.selectOption(board);
        } else {
            // Try clicking on filter dropdown
            page.locator("text=Доска").click();
            page.locator("text=" + board).click();
        }
        page.waitForTimeout(500);
    }

    public void resetFilters() {
        clearSearch();
        try {
            selectStatusFilter("Все");
            selectBoardFilter("Все");
        } catch (Exception e) {
            // Filters may not exist or have different format
        }
    }

    public void clickCreateTaskButton() {
        page.locator(CREATE_TASK_BUTTON).first().click();
    }

    public void clickOnTask(String taskName) {
        page.locator(TASK_CARD + ":has-text('" + taskName + "')").first().click();
    }

    public List<String> getTaskNames() {
        return page.locator(TASK_CARD).allTextContents();
    }

    public int getTaskCount() {
        return page.locator(TASK_CARD).count();
    }

    public boolean isTaskVisible(String taskName) {
        return page.locator(TASK_CARD + ":has-text('" + taskName + "')").count() > 0;
    }

    public boolean isNoTasksMessageVisible() {
        return page.locator(NO_TASKS_MESSAGE).isVisible();
    }

    public boolean isSearchInputVisible() {
        return page.locator(SEARCH_INPUT).isVisible();
    }

    public CreateTaskModal openCreateTaskModal() {
        clickCreateTaskButton();
        return new CreateTaskModal(page);
    }

    public TaskCardModal openTaskCard(String taskName) {
        clickOnTask(taskName);
        return new TaskCardModal(page);
    }
}
