package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;

/**
 * Page Object for Projects page
 */
public class ProjectsPage extends BasePage {

    // Locators
    private static final String PROJECT_LIST = ".project-list, [data-testid='projects-list']";
    private static final String PROJECT_CARD = ".project-card, [data-testid='project-card']";
    private static final String GO_TO_BOARD_BUTTON = "button:has-text('Перейти к доске'), button:has-text('На доску'), [data-testid='go-to-board']";
    private static final String PROJECT_TITLE = ".project-title, .project-name, h3";

    public ProjectsPage(Page page) {
        super(page);
    }

    public void open() {
        page.navigate(BASE_URL + "/projects");
        waitForPageLoad();
    }

    public List<String> getProjectNames() {
        return page.locator(PROJECT_CARD + " " + PROJECT_TITLE).allTextContents();
    }

    public int getProjectCount() {
        return page.locator(PROJECT_CARD).count();
    }

    public boolean isProjectVisible(String projectName) {
        return page.locator(PROJECT_CARD + ":has-text('" + projectName + "')").count() > 0;
    }

    public BoardPage goToProjectBoard(String projectName) {
        Locator projectCard = page.locator(PROJECT_CARD + ":has-text('" + projectName + "')");
        Locator goToBoardBtn = projectCard.locator(GO_TO_BOARD_BUTTON);

        if (goToBoardBtn.isVisible()) {
            goToBoardBtn.click();
        } else {
            // Click on project card itself
            projectCard.click();
        }

        return new BoardPage(page);
    }

    public void clickProject(String projectName) {
        page.locator(PROJECT_CARD + ":has-text('" + projectName + "')").click();
    }
}
