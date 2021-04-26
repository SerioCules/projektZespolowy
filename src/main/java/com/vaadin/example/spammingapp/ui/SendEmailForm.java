package com.vaadin.example.spammingapp.ui;

import com.vaadin.example.spammingapp.backend.entity.Contact;
import com.vaadin.example.spammingapp.backend.entity.SEmial;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public class SendEmailForm extends FormLayout {

    private Contact contact;
    DateTimePicker date = new DateTimePicker();

    TextField emailTopic = new TextField("Email Topic");
    TextField msgBody = new TextField("Message Body");
    EmailField toEmail = new EmailField("To Email");
/*    TextField sEmail = new TextField("Sender email on gmail domain");
    TextField sPassword = new TextField("Gmail Password");*/
    ComboBox<SEmial> sEmial = new ComboBox<>("Emails");

    Button save = new Button("Send");
    Button delete = new Button("Delete");
    Button close = new Button("Cancel");

        Binder<Contact> binder = new BeanValidationBinder<>(Contact.class);

    public SendEmailForm(List<SEmial> senders) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);
/*        binder.bind(messageBody, "msgBody");
        binder.bind(emailTopic, "emailTopic");
        binder.bind(toEmail, "toEmail");*/
/*     !!!   sEmail.setValue(senders.get(0).toString());*/
        sEmial.setItems(senders);
        sEmial.setItemLabelGenerator(SEmial::getEmail);
        date.setLabel("Choose date and time to send message");

        add(emailTopic,
                msgBody,
                toEmail,
                /*
                sEmail,
                sPassword,
                */
                sEmial,
                date,
                createButtonsLayout());
    }

    public void setContact(Contact contact){
        this.contact=contact;
        binder.setBean(contact);
    }
    public Date convertToDateViaSqlTimestamp(LocalDateTime dateToConvert) {
        return java.sql.Timestamp.valueOf(dateToConvert);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
        Div value = new Div();
        value.setText("Select Value");
        date.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                value.setText("No date time selected");
            } else {
                value.setText("Selected date time: " + event.getValue());
            }
        });

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(contact);
            fireEvent(new SaveEvent(this, contact));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<SendEmailForm> {
        private Contact contact;

        protected ContactFormEvent(SendEmailForm source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(SendEmailForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(SendEmailForm source, Contact contact) {
            super(source, contact);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(SendEmailForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}

