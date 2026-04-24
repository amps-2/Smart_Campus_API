/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.dao;

/**
 *
 * @author Welcome
 */

import com.example.model.mainInterface;
import java.util.List;
import java.util.Map;

public class GenericDAO<T extends mainInterface> {

    private final Map<String, T> items;

    public GenericDAO(Map<String, T> items) {
        this.items = items;
    }

    public List<T> getAll() {
        return new java.util.ArrayList<>(items.values());
    }

    public T getById(String id) {
        return items.get(id);
    }

    public boolean exists(String id) {
        return items.containsKey(id);
    }

    public void add(T item) {
        items.put(item.getId(), item);
    }

    public void update(T updatedItem) {
        if (items.containsKey(updatedItem.getId())) {
            items.put(updatedItem.getId(), updatedItem);
        }
    }

    public void delete(String id) {
        items.remove(id);
    }
}