package com.example.lupe.siin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FormFragment extends Fragment {

    private TextView midTramo;
    private TextView mdistancia;
    private TextView mpoblacion;
    private TextView mtramo;
    private TextView mproyid;
    private TextView midsubproyecto;


    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.info_window_form_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        midTramo = (TextView) view.findViewById(R.id.idTramo);
        // Get back arguments
        String sIdTramo = getArguments().getString("tIdTramo", "");
        midTramo.setText(midTramo.getText().toString() + " " + sIdTramo);

        int sobjectid = getArguments().getInt("tOBJECTID", 0);
        int sobjectid1 = getArguments().getInt("tOBJECTID_1", 0);
        int sobjectid2 = getArguments().getInt("tOBJECTID_2", 0);
        float sshapeleng = getArguments().getFloat("tShape_Leng", 0);

        mdistancia = (TextView) view.findViewById(R.id.distancia);
        float sdistancia = getArguments().getFloat("tDistancia", 0);
        mdistancia.setText(mdistancia.getText().toString() + " " + sdistancia);

        mpoblacion = (TextView) view.findViewById(R.id.poblacion);
        String spoblacion = getArguments().getString("tPoblacion", "");
        mpoblacion.setText(mpoblacion.getText().toString() + " " + spoblacion);

        mtramo = (TextView) view.findViewById(R.id.tramo);
        String stramo = getArguments().getString("tTramo", "");
        mtramo.setText(mtramo.getText().toString() + " " + stramo);

        mproyid = (TextView) view.findViewById(R.id.proyId);
        final int sproyid = getArguments().getInt("tProyId", 0);
        mproyid.setText(mproyid.getText().toString() + " " + sproyid);

        midsubproyecto = (TextView) view.findViewById(R.id.idSubproyecto);
        final int sidsubproyecto = getArguments().getInt("tIdSubproyecto", 0);
        midsubproyecto.setText(midsubproyecto.getText().toString() + " " + sidsubproyecto);

        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //haciendo un intent para abrir otra activity
                Intent myIntent = new Intent(getContext(), FichaActivity.class);
                myIntent.putExtra("tProyId",sproyid);
                myIntent.putExtra("tIdSubproyecto",sidsubproyecto);
                startActivity(myIntent);
            }
        });

        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SeguimientoActivity.class);
                intent.putExtra("tProyId",sproyid);
                intent.putExtra("tIdSubproyecto",sidsubproyecto);
                startActivity(intent);
            }
        });

    }

    public static FormFragment newInstance(String tIdTramo,
                                           int tOBJECTID_1,
                                           int tOBJECTID,
                                           float tDistancia,
                                           int tOBJECTID_2,
                                           String tPoblacion,
                                           String tTramo,
                                           float tShape_Leng,
                                           int tProyId,
                                           int tIdSubproyecto) {
        FormFragment fragmentForm = new FormFragment();
        Bundle args = new Bundle();
        args.putString("tIdTramo", tIdTramo);
        args.putInt("tOBJECTID_1", tOBJECTID_1);
        args.putInt("tOBJECTID", tOBJECTID);
        args.putInt("tOBJECTID_2", tOBJECTID_2);
        args.putFloat("tDistancia", tDistancia);
        args.putString("tPoblacion", tPoblacion);
        args.putString("tTramo", tTramo);
        args.putFloat("tShape_Leng", tShape_Leng);
        args.putInt("tProyId", tProyId);
        args.putInt("tIdSubproyecto", tIdSubproyecto);

        //args.putInt("someInt", someInt);
        fragmentForm.setArguments(args);
        return fragmentForm;
    }
}
