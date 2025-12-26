package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

/**
 * Page Object for Create Task Modal
 */
public class CreateTaskModal extends BasePage {

    // Locators
    private static final String MODAL = ".modal, [role='dialog'], [data-testid='create-issue-modal']";
    private static final String TITLE_INPUT = "input[name='title'], input[placeholder*='Название'], [data-testid='title-input']";
    private static final String DESCRIPTION_INPUT = "textarea[name='description'], textarea[placeholder*='Описание'], [data-testid='description-input']";
    private static final String PROJECT_SELECT = "select[name='project'], [data-testid='project-select']";
    private static final String PRIORITY_SELECT = "select[name='priority'], [data-testid='priority-select']";
    private static final String STATUS_FIELD = "input[name='status'], [data-testid='status-field'], .status-field";
    private static final String ASSIGNEE_SELECT = "select[name='assignee'], [data-testid='assignee-select']";
    private static final String CREATE_BUTTON = "button:has-text('Создать'), [data-testid='create-btn']";
    private static final String CANCEL_BUTTON = "button:has-text('Отмена'), [data-testid='cancel-btn']";

    public CreateTaskModal(Page page) {
        super(page);
        waitForModal();
    }

    private void waitForModal() {
        page.waitForSelector(MODAL, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));
    }

    public void setTitle(String title) {
        Locator titleInput = page.locator(TITLE_INPUT).first();
        titleInput.clear();
        titleInput.fill(title);
    }

    public void setDescription(String description) {
        Locator descInput = page.locator(DESCRIPTION_INPUT).first();
        if (descInput.isVisible()) {
            descInput.clear();
            descInput.fill(description);
        }
    }

    public void selectProject(String project) {
        Locator projectSelect = page.locator(PROJECT_SELECT).first();
        if (projectSelect.isVisible()) {
            projectSelect.selectOption(project);
        } else {
            // Try clicking approach
            page.locator("text=Проект").click();
            page.locator("option:has-text('" + project + "'), li:has-text('" + project + "')").click();
        }
    }

    public void selectPriority(String priority) {
        Locator prioritySelect = page.locator(PRIORITY_SELECT).first();
        if (prioritySelect.isVisible()) {
            prioritySelect.selectOption(priority);
        } else {
            page.locator("text=Приоритет").click();
            page.locator("option:has-text('" + priority + "'), li:has-text('" + priority + "')").click();
        }
    }

    public void selectAssignee(String assignee) {
        Locator assigneeSelect = page.locator(ASSIGNEE_SELECT).first();
        if (assigneeSelect.isVisible()) {
            assigneeSelect.selectOption(assignee);
        } else {
            page.locator("text=Исполнитель").click();
            page.locator("option:has-text('" + assignee + "'), li:has-text('" + assignee + "')").click();
        }
    }

    public String getStatusValue() {
        Locator statusField = page.locator(STATUS_FIELD).first();
        if (statusField.isVisible()) {
            return statusField.inputValue();
        }
        return page.locator("text=Backlog").textContent();
    }

    public boolean isStatusDisabled() {
        Locator statusField = page.locator(STATUS_FIELD).first();
        if (statusField.isVisible()) {
            return statusField.isDisabled() ||
                   statusField.getAttribute("readonly") != null ||
                   statusField.getAttribute("disabled") != null;
        }
        // Check if status select is disabled
        Locator statusSelect = page.locator("select[name='status']").first();
        if (statusSelect.count() > 0) {
            return statusSelect.isDisabled();
        }
        return true; // Assume disabled if not interactive
    }

    public boolean isCreateButtonEnabled() {
        return page.locator(CREATE_BUTTON).first().isEnabled();
    }

    public void clickCreate() {
        page.locator(CREATE_BUTTON).first().click();
    }

    public void clickCancel() {
        page.locator(CANCEL_BUTTON).first().click();
    }

    public void createTask(String title, String project, String priority, String assignee) {
        setTitle(title);
        selectProject(project);
        selectPriority(priority);
        selectAssignee(assignee);
        clickCreate();
    }

    public void createTaskWithDescription(String title, String description, String project,
                                          String priority, String assignee) {
        setTitle(title);
        setDescription(description);
        selectProject(project);
        selectPriority(priority);
        selectAssignee(assignee);
        clickCreate();
    }

    public boolean isModalVisible() {
        return page.locator(MODAL).isVisible();
    }
}
