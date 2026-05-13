package com.rochela.rochelasystem.modulos.produccion.state;

public class LoteStateContext {

    private final boolean requierePasteurizacion;
    private final boolean requiereCloruro;
    private final boolean requiereLavadoDesuerado;
    private final StateResolver resolver;

    public LoteStateContext(boolean requierePasteurizacion,
                            boolean requiereCloruro,
                            boolean requiereLavadoDesuerado,
                            StateResolver resolver) {
        this.requierePasteurizacion = requierePasteurizacion;
        this.requiereCloruro = requiereCloruro;
        this.requiereLavadoDesuerado = requiereLavadoDesuerado;
        this.resolver = resolver;
    }

    public boolean requierePasteurizacion() {
        return requierePasteurizacion;
    }

    public boolean requiereCloruro() {
        return requiereCloruro;
    }

    public boolean requiereLavadoDesuerado() {
        return requiereLavadoDesuerado;
    }

    public StateResolver getResolver() {
        return resolver;
    }
}

