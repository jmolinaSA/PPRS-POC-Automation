package poc.automation.UI;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;
import poc.automation.backend.entity.Client;
import poc.automation.backend.entity.Study;
import poc.automation.backend.entity.service.ClientService;
import poc.automation.backend.entity.service.StudyService;


@Component
@Route("")
@PageTitle("MVP-02 PPRS POC Automation")
@CssImport("./styles/shared-styles.css")
@UIScope
public class ListView extends VerticalLayout
{
    public StudyForm form;
    public StudyService studyService;
    public Grid<Study> grid = new Grid<>(Study.class);
    public TextField filterText = new TextField();

    public ListView(StudyService studyService, ClientService clientService)
    {
        this.studyService = studyService;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

        form = new StudyForm(clientService.findAll());

        // Hoop up events when saving, deleting
        form.addListener(StudyForm.SaveEvent.class, this::saveContact);
        form.addListener(StudyForm.DeleteEvent.class, this::deleteContact);
        form.addListener(StudyForm.CloseEvent.class, e -> closedEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();

        // Hiding form when no client is selected
        closedEditor();
    }

    private void deleteContact(StudyForm.DeleteEvent evt)
    {
        studyService.delete(evt.getStudy());
        updateList();
        closedEditor();
    }

    private void saveContact(StudyForm.SaveEvent evt)
    {
        studyService.save(evt.getStudy());
        updateList();
        closedEditor();
    }

    private HorizontalLayout getToolBar()
    {
        filterText.setPlaceholder("Filter by study name... ");
        filterText.getElement().setAttribute("name", "filter");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add study");
        addContactButton.getElement().setAttribute("name", "addStudy");
        addContactButton.addClickListener(click -> addStudy());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addStudy()
    {
        grid.asSingleSelect().clear();
        editStudy(new Study());
    }

    private void configureGrid()
    {
        grid.addClassName("contact-grid");
        grid.setSizeFull();

        grid.removeAllColumns();

        grid.addColumn(Study::getStudyName).setHeader("Study name");
        grid.addColumn(Study::getID).setHeader("Study ID");
        grid.addColumn(study -> {
            Client client = study.getClient();
            return client == null ? "-" : client.getName();
        }).setHeader("CDISC Standard");
        grid.addColumn(Study::getCreatedOn).setHeader("Created On");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Listener from whenever a row is selected and show in editor
        grid.asSingleSelect().addValueChangeListener(evt -> editStudy(evt.getValue()));
    }

    private void editStudy(Study study)
    {
        // If we select a row, get contact. Otherwise, get null value
        if (study == null)
            closedEditor();
        else
            form.setStudy(study);
            form.setVisible(true);
            addClassName("editing");
    }

    private void closedEditor()
    {
        form.setStudy(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList()
    {
        grid.setItems(studyService.findAll(filterText.getValue()));
    }
}
