package com.teppichcenter.views.teppich;

import com.teppichcenter.domain.Lager;
import com.teppichcenter.domain.Teppich;
import com.teppichcenter.services.ApplicationService;
import com.teppichcenter.services.ApplicationService2;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.function.ValueProvider;
import com.teppichcenter.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.selection.SelectionEvent;
import com.vaadin.flow.data.selection.SelectionListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.vaadin.lineawesome.LineAwesomeIcon;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@PageTitle("TeppicheView")
@Route(value = "teppiche/list", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class TeppichGridView extends VerticalLayout {

    private final ApplicationService service;
    private final ApplicationService2 service2;
    private final Grid<Teppich> grid = new Grid<>(Teppich.class, false);
    private final Grid<Lager> grid2 = new Grid<>(Lager.class, false);

    public TeppichGridView(@Autowired ApplicationService service, ApplicationService2 service2) {
        this.service = service;
        this.service2 = service2;
        init();
    }

    private void init() {

        setHeightFull();

        // -- TOOLBAR -----------------------------------------------------
        HorizontalLayout toolbar = new HorizontalLayout();

        Button delete = new Button("Delete", LineAwesomeIcon.TRASH_SOLID.create(), event -> onDelete() );
        delete.setEnabled(false);


        // -- GRID --------------------------------------------------------
        grid.addColumn(Teppich::getId)
                .setSortProperty("id")
                .setHeader("ID");
        grid.addColumn(Teppich::getBezeichnung)
                .setSortProperty("bezeichnung")
                .setHeader("Bezeichnung");
        grid.addColumn(Teppich::getFarbe)
                .setSortProperty("farbe")
                .setHeader("Farbe");
        grid.addColumn(Teppich::getPreis)
                .setSortProperty("preis")
                .setHeader("Preis");
        grid.addColumn(Teppich::getMenge)
                .setSortProperty("menge")
                .setHeader("Menge");
        grid.addColumn(Teppich::getFkLager)
                        .setSortProperty("fkLager")
                                .setHeader("Lagerort");

        grid.addComponentColumn(new ValueProvider<Teppich, Component>() {
            @Override
            public Component apply(Teppich teppich) {
                return new HorizontalLayout(new Button(LineAwesomeIcon.EDIT_SOLID.create(), new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> event) {
                        onEdit( teppich );
                    }
                }));
            }
        });

        grid.addComponentColumn((Teppich teppich) -> {
            Button button = new Button(LineAwesomeIcon.TRASH_SOLID.create(), event -> onDeleteTeppichEinzelnd(teppich));
            return new HorizontalLayout(button);
        });


        grid2.addColumn(Lager::getId).setSortProperty("id").setHeader("ID");
        grid2.addColumn(Lager::getName).setSortProperty("name").setHeader("Lager");
        grid2.addComponentColumn((Lager lager) -> {
            Button button = new Button(LineAwesomeIcon.TRASH_SOLID.create(), event -> onDeleteLagerEinzelnd(lager));
            return new HorizontalLayout(button);
        });





        grid.setSelectionMode( Grid.SelectionMode.MULTI );
        grid.addSelectionListener(new SelectionListener<Grid<Teppich>, Teppich>() {

            @Override
            public void selectionChange(SelectionEvent<Grid<Teppich>, Teppich> event) {
                Set<Teppich> teppichSet = event.getAllSelectedItems();

                // Aktivieren/Deaktivieren des Buttons abhängig von der Tabellenauswahl - IF/ELSE VARIANTE
//                if(personSet != null && personSet.size() > 0) {
//                    delete.setEnabled(true);
//                } else {
//                    delete.setEnabled(false);
//                }
                // Aktivieren/Deaktivieren des Buttons abhängig von der Tabellenauswahl
                delete.setEnabled( (teppichSet != null) && (!teppichSet.isEmpty()) );
            }
        });



        grid.setHeightFull();

        toolbar.add(delete);

        add(toolbar);
        add(grid);
        add(grid2);
        load();
    }


    private void load() {
        grid.setItems( query -> {
            PageRequest springPageRequest = VaadinSpringDataHelpers.toSpringPageRequest(query);
            return service.findAll( springPageRequest );
        });

        grid2.setItems( query -> {
            PageRequest springPageRequest = VaadinSpringDataHelpers.toSpringPageRequest(query);
            return service2.findAll( springPageRequest );
        });
    }


    // -- ACTIONS ------------------------------------------------------------------------------------------------------
    private void onEdit(Teppich teppich) {
        Long id = teppich.getId();
        getUI().ifPresent(new Consumer<UI>() {
            @Override
            public void accept(UI ui) {
                ui.navigate( TeppichCreateView.class, new RouteParam( TeppichCreateView.PRODUCT_ID_PARAM, id ));
            }
        });

    }

    private void onDelete() {
        try {
            Set<Teppich> selectedTeppiches = grid.getSelectedItems();
            StringBuilder infos = new StringBuilder();
            selectedTeppiches.forEach(
                    teppich -> infos.append( teppich.toString() ).append("\n")
            );
            service.deleteTeppiche(selectedTeppiches);
            load();
            Notification.show("Gelöscht:" + infos.toString());
        } catch (Exception e) {
            log.error("error deleting carpet. Message={}", e.getMessage(), e);
            Notification.show("Fehler: " + e.getMessage());
        }
    }

//    private void onDelete2() {
//        try {
//            Set<Lager> selectedLagers = grid2.getSelectedItems();
//            StringBuilder infos = new StringBuilder();
//            selectedLagers.forEach(
//                    lager -> infos.append( lager.toString() ).append("\n")
//            );
//            service2.deleteLager(selectedLagers);
//            load();
//            Notification.show("Gelöscht:" + infos.toString());
//        } catch (Exception e) {
//            log.error("error deleting carpet. Message={}", e.getMessage(), e);
//            Notification.show("Fehler: " + e.getMessage());
//        }
//    }

    private void onDeleteTeppichEinzelnd(Teppich teppich) {
        try {
            service.deleteTeppiche(Set.of(teppich));
            load();
            Notification.show("Gelöscht: " + teppich.toString());
        } catch (Exception e) {
            log.error("error deleting carpet. Message={}", e.getMessage(), e);
            Notification.show("Fehler: " + e.getMessage());
        }
    }


    private void onDeleteLagerEinzelnd(Lager lager) {
        try {
            service2.deleteLager(Set.of(lager));
            load();
            Notification.show("Gelöscht: " + lager.toString());
        } catch (Exception e) {
            log.error("Fehler beim Löschen von Lager. Message={}", e.getMessage(), e);
            Notification.show("Fehler: " + e.getMessage());
        }
    }
}