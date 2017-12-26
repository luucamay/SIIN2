package com.example.lupe.siin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lupe on 19/12/17.
 */


public class ProyectoAdapter extends ArrayAdapter<Proyecto> {

    /**
     * The part of the location string from the USGS service that we use to determine
     * whether or not there is a location offset present ("5km N of Cairo, Egypt").
     */
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructs a new {@link ProyectoAdapter}.
     *
     * @param context   of the app
     * @param proyectos is the list of proyectos, which is the data source of the adapter
     */
    public ProyectoAdapter(Context context, List<Proyecto> proyectos) {
        super(context, 0, proyectos);
    }

    /**
     * Returns a list item view that displays information about the proyecto at the given position
     * in the list of proyectos.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.proyecto_list_item, parent, false);
        }

        // Find the proyecto at the given position in the list of proyectos
        Proyecto currentProyecto = getItem(position);

        // Find the TextView with view ID magnitude
        TextView proyIdView = (TextView) listItemView.findViewById(R.id.proy_id);
        // Display the magnitude of the current proyecto in that TextView
        proyIdView.setText(""+currentProyecto.getProyId());

        // Find the TextView with view ID magnitude
        TextView proySisinView = (TextView) listItemView.findViewById(R.id.proy_sisin);
        // Display the magnitude of the current proyecto in that TextView
        proySisinView.setText(currentProyecto.getProySisin());

        // Find the TextView with view ID magnitude
        TextView proyDescripView = (TextView) listItemView.findViewById(R.id.proy_descrip);
        // Display the magnitude of the current proyecto in that TextView
        proyDescripView.setText(currentProyecto.getProyDescrip());


        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}