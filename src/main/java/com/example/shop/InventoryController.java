package com.example.shop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@RestController
public class InventoryController {

    ArrayList<Item> inventory = new ArrayList<Item>(
            Arrays.asList(
                    new Item(1, "Keyboard", 29.99, 76),
                    new Item(2, "Mouse", 19.99, 43),
                    new Item(3, "Monitor", 79.99, 7),
                    new Item(4, "PC", 749.99, 2),
                    new Item(5, "Headphones", 19.99, 14)
            ));

    @GetMapping("/items")
    public ArrayList<Item> getInventory() {
        return inventory;
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable("id") Long id) {
        Item itemToShow = findItem(id);

        if (itemToShow == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item does not exist");
        } else {
            return itemToShow;
        }
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@RequestBody Item required) {
        Long newID = inventory.get(inventory.size() - 1).getId() + 1;
        Item newItem = new Item(newID, required.getName(), required.getPrice(), required.getCount());
        inventory.add(newItem);
        return newItem;
    }

    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable("id") Long id, @RequestBody Map<String, String> required) {

        Item itemToUpdate = findItem(id);

        if (itemToUpdate == null) {
            throw new ResponseStatusException((HttpStatus.NOT_FOUND), "Item does not exist");
        } else {
            for (String jsonKey : required.keySet()) {
                if (jsonKey.equals("price")) {
                    itemToUpdate.setPrice(Double.parseDouble(required.get(jsonKey)));
                } else if (jsonKey.equals("count")) {
                    itemToUpdate.setCount(Integer.parseInt(required.get(jsonKey)));
                }
            }
            return itemToUpdate;
        }
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable("id") Long id) {

        Item itemToDelete = findItem(id);

        if (itemToDelete == null) {
            throw new ResponseStatusException((HttpStatus.NOT_FOUND), "Item does not exist");
        } else {
            inventory.remove(itemToDelete);
        }
    }

    private Item findItem(Long id) {
        return inventory.stream()
                .filter(item -> id.equals(item.getId()))
                .findAny()
                .orElse(null);
    }
}
