package com.topografia.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.Function;

public class TableFilter<T> {

    private final ObservableList<T> datos;
    private final FilteredList<T> filtrados;

    public TableFilter(ObservableList<T> datos) {
        this.datos = datos;
        this.filtrados = new FilteredList<>(datos, p -> true);
    }

    /**
     * Conecta búsqueda a un TextField + ComboBox + TableView.
     *
     * @param txtBuscar TextField de búsqueda
     * @param cbFiltro  ComboBox con opciones de filtro
     * @param tabla     TableView donde mostrar
     * @param funciones Funciones de acceso para cada filtro (en el mismo orden que cbFiltro)
     */
    @SafeVarargs
    public final void conectar(TextField txtBuscar, ComboBox<String> cbFiltro,
                               TableView<T> tabla, Function<T, String>... funciones) {

        txtBuscar.textProperty().addListener((obs, old, nuevo) -> {
            filtrados.setPredicate(item -> {
                if (nuevo == null || nuevo.isEmpty()) return true;
                String lower = nuevo.toLowerCase();
                int index = cbFiltro.getSelectionModel().getSelectedIndex();
                if (index >= 0 && index < funciones.length) {
                    String valor = funciones[index].apply(item);
                    return valor != null && valor.toLowerCase().contains(lower);
                }
                return true;
            });
        });

        SortedList<T> ordenada = new SortedList<>(filtrados);
        ordenada.comparatorProperty().bind(tabla.comparatorProperty());
        tabla.setItems(ordenada);
    }

    public ObservableList<T> getDatos() {
        return datos;
    }
}

