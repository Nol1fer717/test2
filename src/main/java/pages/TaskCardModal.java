package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Page Object for Task Card Modal (Edit Task)
 */
public class TaskCardModal extends BasePage {

    // Locators
    private static final String MODAL = ".modal, [role='dialog'], [data-testid='edit-issue-modal']";
    private static final String TITLE_FIELD = "[data-testid='task-title'], .task-title, h2, h3";
    private static final String DESCRIPTION_FIELD = "[data-testid='task-description'], .task-description";
    private static final String PROJECT_FIELD = "[data-testid='task-project'], .task-project";
    private static final String PRIORITY_FIELD = "[data-testid='task-priority'], .task-priority";
    private static final String STATUS_SELECT = "select[name='status'], [data-testid='status-select']";
    private static final String ASSIGNEE_FIELD = "[data-testid='task-assignee'], .task-assignee";
    private static final String GO_TO_BOARD_BUTTON = "button:has-text('Перейти на доску'), button:has-text('На доску'), [data-testid='go-to-board-btn']";
    private static final String UPDATE_BUTTON = "button:has-text('Обновить'), button:has-text('Сохранить'), [data-testid='update-btn']";
    private static final String CLOSE_BUTTON = "button:has-text('Закрыть'), [data-testid='close-btn'], .close-btn";

    public TaskCardModal(Page page) {
        super(page);
        waitForModal();
    }

    private void waitForModal() {
        page.waitForSelector(MODAL, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));
    }

    public String getTitle() {
        return page.locator(TITLE_FIELD).first().textContent();
    }

    public String getDescription() {
        Locator desc = page.locator(DESCRIPTION_FIELD).first();
        return desc.isVisible() ? desc.textContent() : "";
    }

    public String getProject() {
        Locator project = page.locator(PROJECT_FIELD).first();
        return project.isVisible() ? project.textContent() : "";
    }

    public String getPriority() {
        Locator priority = page.locator(PRIORITY_FIELD).first();
        return priority.isVisible() ? priority.textContent() : "";
    }

    public String getStatus() {
        Locator statusSelect = page.locator(STATUS_SELECT).first();
        if (statusSelect.isVisible()) {
            return statusSelect.inputValue();
        }
        return "";
    }

    public void setStatus(String status) {
        Locator statusSelect = page.locator(STATUS_SELECT).first();
        if (statusSelect.isVisible()) {
            statusSelect.selectOption(status);
        }
    }

    public String getAssignee() {
        Locator assignee = page.locator(ASSIGNEE_FIELD).first();
        return assignee.isVisible() ? assignee.textContent() : "";
    }

    public boolean isGoToBoardButtonVisible() {
        return page.locator(GO_TO_BOARD_BUTTON).count() > 0 &&
               page.locator(GO_TO_BOARD_BUTTON).first().isVisible();
    }

    public BoardPage clickGoToBoard() {
        page.locator(GO_TO_BOARD_BUTTON).first().click();
        return new BoardPage(page);
    }

    public void clickUpdate() {
        page.locator(UPDATE_BUTTON).first().click();
    }

    public void close() {
        Locator closeBtn = page.locator(CLOSE_BUTTON).first();
        if (closeBtn.isVisible()) {
            closeBtn.click();
        } else {
            // Try pressing Escape
            page.keyboard().press("Escape");
        }
    }

    public boolean isModalVisible() {
        return page.locator(MODAL).isVisible();
    }

    public boolean hasAllRequiredFields() {
        return page.locator(TITLE_FIELD).count() > 0;
    }
}
