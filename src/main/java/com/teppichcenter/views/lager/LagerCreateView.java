package com.teppichcenter.views.lager;
import com.teppichcenter.domain.Color;
import com.teppichcenter.domain.Lager;
import com.teppichcenter.domain.Teppich;
import com.teppichcenter.services.LagerService;
import com.teppichcenter.services.TeppichService;
import com.teppichcenter.views.MainLayout;
import com.teppichcenter.views.teppich.TeppichCreateView;
import com.teppichcenter.views.teppich.TeppichGridView;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


@Slf4j
@PageTitle(value = "Lager erfassen")
@Route(value = "lager/:lagerId?/create", layout = MainLayout.class)

public class LagerCreateView extends VerticalLayout implements BeforeEnterObserver{


    public static final String PRODUCT_ID_PARAM = "teppichId";

    private TextField lagerort = new TextField("Lagerort");

    private final Button save = new Button("Speichern", LineAwesomeIcon.SAVE.create(), e -> onSave());
    private final Button cancel = new Button("Abbruch", LineAwesomeIcon.SAVE.create(), e -> onCancel());

    private final Binder<Lager> binder = new Binder<>(Lager.class, true);
    private final LagerService lagerService;

    private Lager derzeitigesLager;

    public LagerCreateView() {
        log.debug("constructor ");
        this.lagerService = LagerService.getInstance();
        init();
    }


    private void init() {
        log.debug("init ");
        FormLayout formLayout = new FormLayout(lagerort);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonPanel = new HorizontalLayout(save, cancel);

        add(formLayout);
        add(buttonPanel);
    }



    // -- ACTIONS ------------------------------------------------------------------------------------------------------

    private void onSave() {

        try {

//            Teppich teppich = new Teppich();
            binder.writeBean(derzeitigesLager);

            log.debug("lager={}", derzeitigesLager);
            boolean success = lagerService.save(derzeitigesLager);
            if (success) {
                Notification.show("Lager mit dem Namen " + derzeitigesLager.getName() + " erfolgreich gespeichert");

                getUI().ifPresent(new Consumer<UI>() {
                    @Override
                    public void accept(UI ui) {
                        log.debug("nav to {}", TeppichCreateView.class);
                        ui.navigate( TeppichGridView.class );
                    }
                });

            } else {
                Notification.show("Fehler - Lager mit dem Namen " + derzeitigesLager.getName() + " konnte nicht gespeichert werden.");
            }
        } catch (ValidationException e) {
            log.warn("Benutzereingabe falsch. " + e.getMessage());
            Notification.show(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            Notification.show(e.getMessage());
        }
    }


    public void setLager(Lager lager) {

        if (lager == null)
            lager = new Lager();
        this.derzeitigesLager = lager;
        log.debug("{}", lager);
        binder.readBean(lager);
    }

    private void onCancel() {
        getUI().ifPresent(new Consumer<UI>() {
            @Override
            public void accept(UI ui) {
                ui.navigate(TeppichGridView.class);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {

        log.debug("beforeEnter {}", event);

        event.getRouteParameters().getLong(LagerCreateView.PRODUCT_ID_PARAM)
                .ifPresentOrElse(new Consumer<Long>() {
                    @Override
                    public void accept(Long teppichId) {
                        Optional<Lager> optional = lagerService.findById(teppichId);

                        optional.ifPresentOrElse(new Consumer<Lager>() {
                            @Override
                            public void accept(Lager lager) {
                                setLager(lager);
                            }
                        }, new Runnable() {
                            @Override
                            public void run() {
                                setLager(new Lager());
                            }
                        });
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        setLager(new Lager());
                    }
                });
    }
}