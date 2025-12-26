package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;

/**
 * Page Object for Kanban Board page
 */
public class BoardPage extends BasePage {

    // Locators
    private static final String BOARD_CONTAINER = ".board, .kanban-board, [data-testid='board']";
    private static final String COLUMN = ".column, .board-column, [data-testid='board-column']";
    private static final String TODO_COLUMN = ".column:has-text('To Do'), [data-testid='column-todo']";
    private static final String IN_PROGRESS_COLUMN = ".column:has-text('In Progress'), [data-testid='column-inprogress']";
    private static final String DONE_COLUMN = ".column:has-text('Done'), [data-testid='column-done']";
    private static final String TASK_CARD = ".task-card, .issue-card, [data-testid='task-card']";
    private static final String BOARD_TITLE = ".board-title, h1, h2";

    public BoardPage(Page page) {
        super(page);
    }

    public void open(String boardId) {
        page.navigate(BASE_URL + "/board/" + boardId);
        waitForPageLoad();
    }

    public void waitForBoardLoad() {
        page.waitForSelector(BOARD_CONTAINER, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));
    }

    public String getBoardTitle() {
        return page.locator(BOARD_TITLE).first().textContent();
    }

    public boolean hasTodoColumn() {
        return page.locator(TODO_COLUMN).count() > 0 ||
               page.locator(":has-text('To Do')").count() > 0 ||
               page.locator(":has-text('Backlog')").count() > 0;
    }

    public boolean hasInProgressColumn() {
        return page.locator(IN_PROGRESS_COLUMN).count() > 0 ||
               page.locator(":has-text('In Progress')").count() > 0;
    }

    public boolean hasDoneColumn() {
        return page.locator(DONE_COLUMN).count() > 0 ||
               page.locator(":has-text('Done')").count() > 0;
    }

    public boolean hasAllColumns() {
        return hasTodoColumn() && hasInProgressColumn() && hasDoneColumn();
    }

    public List<String> getTodoTasks() {
        return page.locator(TODO_COLUMN + " " + TASK_CARD).allTextContents();
    }

    public List<String> getInProgressTasks() {
        return page.locator(IN_PROGRESS_COLUMN + " " + TASK_CARD).allTextContents();
    }

    public List<String> getDoneTasks() {
        return page.locator(DONE_COLUMN + " " + TASK_CARD).allTextContents();
    }

    public int getTotalTaskCount() {
        return page.locator(TASK_CARD).count();
    }

    public boolean isTaskInColumn(String taskName, String columnName) {
        String columnSelector;
        switch (columnName.toLowerCase()) {
            case "to do":
            case "todo":
            case "backlog":
                columnSelector = TODO_COLUMN;
                break;
            case "in progress":
            case "inprogress":
                columnSelector = IN_PROGRESS_COLUMN;
                break;
            case "done":
                columnSelector = DONE_COLUMN;
                break;
            default:
                columnSelector = COLUMN + ":has-text('" + columnName + "')";
        }
        return page.locator(columnSelector + " " + TASK_CARD + ":has-text('" + taskName + "')").count() > 0;
    }

    public TaskCardModal openTaskCard(String taskName) {
        page.locator(TASK_CARD + ":has-text('" + taskName + "')").first().click();
        return new TaskCardModal(page);
    }

    public boolean isTaskVisible(String taskName) {
        return page.locator(TASK_CARD + ":has-text('" + taskName + "')").count() > 0;
    }

    public boolean isBoardVisible() {
        return page.locator(BOARD_CONTAINER).isVisible() ||
               page.locator(COLUMN).count() > 0;
    }
}
