package lambda.viewcontroller.level;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * This class serves as a specialized {@link Stack} for holding and displaying
 * the level buttons. It divides the level buttons in pages. Each page is a
 * {@link Table} which displays all level buttons of a difficulty. Only one page
 * at the same time is visible.
 * 
 * @author Robert Hochweiss
 */
public class LevelStack extends Stack {

    private List<Table> pages;
    private int currentVisiblePage;

    /**
     * Creates a new LevelStack.
     */
    public LevelStack() {
        pages = new ArrayList<>();
        currentVisiblePage = 0;
    }

    /**
     * Adds a page to the LevelStack.
     * 
     * @param page
     *            the to be added page/table
     */
    public void addPage(Table page) {
        if (page == null) {
            throw new IllegalArgumentException("page cannot be null!");
        }
        pages.add(page);
        add(page);
        if (pages.size() > 1) {
            page.setVisible(false);
        }
    }

    /**
     * Returns the number of pages.
     * 
     * @return the number of pages
     */
    public int size() {
        return pages.size();
    }

    /**
     * Sets one page of the LevelStack visible and the others invisible. The
     * page index start with 0.
     * 
     * @param pageNumber
     *            the index of the page which is to be set visible
     * @throws IllegalArgumentException
     *             if the pageNumber is invalid (out of bounds)
     */
    public void setPageVisible(int pageNumber) {
        if ((pageNumber < 0) || (pageNumber >= size())) {
            throw new IllegalArgumentException(
                    "Invalid LevelStack page number!");
        }
        pages.get(currentVisiblePage).setVisible(false);
        currentVisiblePage = pageNumber;
        pages.get(pageNumber).setVisible(true);
    }

}
