package com.exemplo.apiprodutos.controller;


import com.exemplo.apiprodutos.model.Produto;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private List<Produto> listaProdutos = new ArrayList<>(Arrays.asList(
        new Produto(1L, "Notebook", 2500.00),
        new Produto(2L, "Mouse", 70.00),
        new Produto(3L, "Teclado", 120.00)
    ));

    @GetMapping
    public List<Produto> getTodosProdutos() {
        return listaProdutos;
    }

    @GetMapping("/{id}")
    public Produto getProdutoPorId(@PathVariable Long id) {
        return listaProdutos.stream()
            .filter(p -> p.getId().equals(id))
            .findFirst()
            .orElse(null);
    }
}
