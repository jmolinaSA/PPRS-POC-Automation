package poc.automation.UI;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import poc.automation.backend.entity.Client;
import poc.automation.backend.entity.Study;

import java.util.List;

public class StudyForm extends FormLayout
{
    public ComboBox<Client> client = new ComboBox<>("CDISC Standard");
    public TextField ID = new TextField("Study ID");
    public TextField studyName = new TextField("Study name");

    public Button save = new Button("Save");
    public Button delete = new Button("Delete");
    public Button close = new Button("Cancel");

    public Binder<Study> binder = new BeanValidationBinder<>(Study.class);

    public StudyForm(List<Client> clients)
    {
        addClassName("client-form");
        ID.getElement().setAttribute("name", "ID");
        studyName.getElement().setAttribute("name", "studyName");
        client.getElement().setAttribute("name", "cdisc");
        save.getElement().setAttribute("name", "save");
        delete.getElement().setAttribute("name", "delete");

        binder.bindInstanceFields(this);
        client.setItems(clients);
        client.setItemLabelGenerator(Client::getName);

        add(studyName, ID, client, createButtonLayout());
    }

    public void setStudy(Study study)
    {
        binder.setBean(study);
    }

    private Component createButtonLayout()
    {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        // Hook buttons to events
        save.addClickListener(clickEvent -> validateAndSave());
        delete.addClickListener(clickEvent -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(clickEvent -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave()
    {
        if(binder.isValid())
        {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    // Events
    public static abstract class StudyFormEvent extends ComponentEvent<StudyForm> {
        private final Study study;

        protected StudyFormEvent(StudyForm source, Study study) {
            super(source, false);
            this.study = study;
        }

        public Study getStudy() {
            return study;
        }
    }

    public static class SaveEvent extends StudyFormEvent {
        SaveEvent(StudyForm source, Study study) {
            super(source, study);
        }
    }

    public static class DeleteEvent extends StudyFormEvent {
        DeleteEvent(StudyForm source, Study study) {
            super(source, study);
        }

    }

    public static class CloseEvent extends StudyFormEvent {
        CloseEvent(StudyForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
