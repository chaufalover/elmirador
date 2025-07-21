package com.proyecto.elmirador.dao;

import java.util.List;

public interface Dao<T, ID> {
    
    String insert(T dato);
    List<T> selectAll(int dato);
    T update(T dato);
    T delete(ID id);

}
