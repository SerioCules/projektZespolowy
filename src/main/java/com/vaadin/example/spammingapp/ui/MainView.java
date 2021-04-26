package com.vaadin.example.spammingapp.ui;

import com.vaadin.example.spammingapp.backend.entity.Contact;
import com.vaadin.example.spammingapp.backend.service.ContactService;
import com.vaadin.example.spammingapp.backend.service.FeedbackController;
import com.vaadin.example.spammingapp.backend.service.SemailService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("")
@CssImport("./styles/shared-styles.css")
public class MainView extends VerticalLayout {

    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    private SendEmailForm form;

    private ContactService contactService;
    private FeedbackController feedbackController;

    public MainView(ContactService contactService,
                    SemailService semailService, FeedbackController feedbackController) {
        this.contactService = contactService;
        this.feedbackController = feedbackController;

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        getToolBar();

        form = new SendEmailForm(semailService.findAll());
        form.addListener(SendEmailForm.SaveEvent.class, this::saveContact);
        form.addListener(SendEmailForm.DeleteEvent.class, this::deleteContact);
        form.addListener(SendEmailForm.CloseEvent.class, e ->closeEditor());

        Div content = new Div(grid, form);
        content.addClassName("content");
        content.setSizeFull();

        add(getToolBar(), content);
        updateList();
        closeEditor();
    }

    private void deleteContact(SendEmailForm.DeleteEvent evt) {
        contactService.delete(evt.getContact());
        updateList();
        closeEditor();
    }

    private void saveContact(SendEmailForm.SaveEvent evt) {
        contactService.save(evt.getContact());
        feedbackController.sendFeedback(evt.getContact());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }


    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.removeColumnByKey("semail_id");
        grid.setColumns("emailTopic", "msgBody", "toEmail");
/*        grid.addColumn(contact -> {
            SEmial company = contact.getSemail_id();

            return company == null ? "-" : company.getEmail();
        }).setHeader("From Emial");*/
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(evt -> editContact(evt.getValue()));
    }

    private void editContact(Contact contact) {
        if(contact == null){
            closeEditor();
        }else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolBar() {
        filterText.setPlaceholder("Filter by topic...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Anchor logout = new Anchor("/logout", "Log out");

        Button addContactButton = new Button("Send Msg", click -> sendMsg());
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton, logout);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void sendMsg() {
        grid.asSingleSelect().clear();
            editContact(new Contact());
    }

    private void updateList() {
        grid.setItems(contactService.findAll(filterText.getValue()));
    }
}
