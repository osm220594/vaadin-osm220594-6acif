package com.teppichcenter.views.teppich;

import com.teppichcenter.domain.Color;
import com.teppichcenter.domain.Teppich;
import com.teppichcenter.services.TeppichService;
import com.teppichcenter.views.MainLayout;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@PageTitle(value = "Teppich erfassen")
@Route(value = "teppich/:teppichId?/create", layout = MainLayout.class)
public class TeppichCreateView extends VerticalLayout implements BeforeEnterObserver {

    public static final String PRODUCT_ID_PARAM = "teppichId";

    private final TextField bezeichnung = new TextField("Bezeichnung");
    private final NumberField preis = new NumberField("Preis");
    private final IntegerField menge = new IntegerField("Menge");
    private final Select<Color> colorSelect = new Select<>();




    private final Button save = new Button("Speichern", LineAwesomeIcon.SAVE.create(), e -> onSave());
    private final Button cancel = new Button("Abbruch", LineAwesomeIcon.SAVE.create(), e -> onCancel());

    private final Binder<Teppich> binder = new Binder<>(Teppich.class, true);
    private final TeppichService teppichService;

    private Teppich derzeitigesTeppich;


    public TeppichCreateView() {
        log.debug("constructor ");
        this.teppichService = TeppichService.getInstance();
        init();
    }


    private void init() {
        log.debug("init ");

        FormLayout formLayout = new FormLayout(bezeichnung,colorSelect,preis, menge);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonPanel = new HorizontalLayout(save, cancel);

        colorSelect.setLabel("Farbauswahl");
        colorSelect.setItemLabelGenerator(new ItemLabelGenerator<Color>() {
            @Override
            public String apply(Color color) {
                return color.getLabel();
            }
        });





        // Beispiel: NullColor
//        Color nullColor = new Color("keine Farbe definiert", 'x');

        binder.forField(colorSelect)
                .bind(new ValueProvider<Teppich, Color>() {
                    @Override
                    public Color apply(Teppich teppich) {

                        log.debug("apply teppich={}", teppich);
                        Optional<Color> optional = teppichService.findColorsByChar(teppich.getFarbe());
                        if (optional.isPresent()) {
                            return optional.get();
                        }
//                                log.warn("Farbe nicht mehr in der DB vorhanden! {}", optional);
//                                Notification.show("System-Error - Leider wurde die Farbe nicht gefunden");
//                                return nullColor;
                        return null;
                    }
                }, new Setter<Teppich, Color>() {
                    @Override
                    public void accept(Teppich teppich, Color color) {
                        // Null Prüfungen durchführen
                        teppich.setFarbe(color.getLabel());
                    }
                });


        add(formLayout);
        add(buttonPanel);

        loadData();
    }

    private void loadData() {
        List<Color> colorList = teppichService.findAllColors();
        colorSelect.setItems(colorList);
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------


    private void onSave() {

        try {

//            Teppich teppich = new Teppich();
            binder.writeBean(derzeitigesTeppich);

            log.debug("teppich={}", derzeitigesTeppich);
            boolean success = teppichService.save(derzeitigesTeppich);
            if (success) {
                Notification.show("Teppich mit dem Namen " + derzeitigesTeppich.getBezeichnung() + " erfolgreich gespeichert");

                getUI().ifPresent(new Consumer<UI>() {
                    @Override
                    public void accept(UI ui) {
                        log.debug("nav to {}", TeppichCreateView.class);
                        ui.navigate( TeppichGridView.class );
                    }
                });

            } else {
                Notification.show("Fehler - Teppich mit der Bezeichnung " + derzeitigesTeppich.getBezeichnung() + " konnte nicht gespeichert werden.");
            }
        } catch (ValidationException e) {
            log.warn("Benutzereingabe falsch. " + e.getMessage());
            Notification.show(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            Notification.show(e.getMessage());
        }
    }


    public void setTeppich(Teppich teppich) {

        if (teppich == null)
            teppich = new Teppich();
        this.derzeitigesTeppich = teppich;
        log.debug("{}", teppich);
        binder.readBean(teppich);
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

        event.getRouteParameters().getLong(TeppichCreateView.PRODUCT_ID_PARAM)
                .ifPresentOrElse(new Consumer<Long>() {
                    @Override
                    public void accept(Long teppichId) {
                        Optional<Teppich> optional = teppichService.findById(teppichId);

                        optional.ifPresentOrElse(new Consumer<Teppich>() {
                            @Override
                            public void accept(Teppich teppich) {
                                setTeppich(teppich);
                            }
                        }, new Runnable() {
                            @Override
                            public void run() {
                                setTeppich(new Teppich());
                            }
                        });
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        setTeppich(new Teppich());
                    }
                });
    }
}