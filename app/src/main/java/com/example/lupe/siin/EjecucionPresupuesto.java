package com.example.lupe.siin;

/**
 * Created by lupe on 28/11/17.
 */

public class EjecucionPresupuesto {
    public final String fecha;
    public final String programado_devengado;
    public final String reprogramado_devengado;
    public final String presupuesto_aprobado_devengado;
    public final String presupuesto_vigente_devengado;
    public final String programado_pagado;
    public final String reprogramado_pagado;
    public final String presupuesto_aprobado_pagado;
    public final String presupuesto_vigente_pagado;

    /**
     * Construye un nuevo {@link EjecucionPresupuesto}.
     */

    public EjecucionPresupuesto(String fecha, String programado_devengado, String reprogramado_devengado, String presupuesto_aprobado_devengado, String presupuesto_vigente_devengado, String programado_pagado, String reprogramado_pagado, String presupuesto_aprobado_pagado, String presupuesto_vigente_pagado) {
        this.fecha = fecha;
        this.programado_devengado = programado_devengado;
        this.reprogramado_devengado = reprogramado_devengado;
        this.presupuesto_aprobado_devengado = presupuesto_aprobado_devengado;
        this.presupuesto_vigente_devengado = presupuesto_vigente_devengado;
        this.programado_pagado = programado_pagado;
        this.reprogramado_pagado = reprogramado_pagado;
        this.presupuesto_aprobado_pagado = presupuesto_aprobado_pagado;
        this.presupuesto_vigente_pagado = presupuesto_vigente_pagado;
    }
}
