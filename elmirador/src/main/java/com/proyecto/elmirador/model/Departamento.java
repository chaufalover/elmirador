package com.proyecto.elmirador.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Departamento {
    private int idDpto;
    private String numDpto;

    @Override
    public String toString() {
        return numDpto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Departamento that = (Departamento) o;
        return idDpto == that.idDpto;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idDpto);
    }

}
