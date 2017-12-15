package com.example.lupe.siin;

/**
 * Created by lupe on 11/12/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * An {@link TramoAdapter} knows how to create a list item layout for each Tramo
 * in the data source (a list of {@link Tramo} objects).
 * En otras palabras acá se crea como se va a mostrar un item en el list view
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class TramoAdapter extends ArrayAdapter<Tramo> {

    /**
     * Constructs a new {@link TramoAdapter}.
     *
     * @param context of the app
     * @param Tramos  is the list of Tramos, which is the data source of the adapter
     */
    public TramoAdapter(Context context, List<Tramo> Tramos) {
        super(context, 0, Tramos);
    }

    /**
     * Returns a list item view that displays information about the Tramo at the given position
     * in the list of Tramos.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.tramo_list_item, parent, false);
        }

        // Find the Tramo at the given position in the list of Tramos
        Tramo currentTramo = getItem(position);

        // Find the TextView with view ID titulo
        TextView tramoView = (TextView) listItemView.findViewById(R.id.tituloTramo);
        // Display the magnitude of the current Tramo in that TextView
        tramoView.setText(currentTramo.getId());

        TextView mcoordenadas = (TextView) listItemView.findViewById(R.id.coordenadas);
        String todaslascoordenadas = "";
        for (int i = 0; i < currentTramo.getCoordenadas().size(); i++) {
            todaslascoordenadas += currentTramo.getCoordenadas().get(i).toString() + "\n";
        }
        mcoordenadas.setText(todaslascoordenadas);
        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}