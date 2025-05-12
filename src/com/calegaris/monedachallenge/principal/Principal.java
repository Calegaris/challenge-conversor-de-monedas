package com.calegaris.monedachallenge.principal;

import com.calegaris.monedachallenge.api.ClienteExchangeApi;
import com.calegaris.monedachallenge.menu.MenuConversor;

public class Principal {

    public static void main(String[] args) {
        ClienteExchangeApi cliente = new ClienteExchangeApi();
        MenuConversor menu = new MenuConversor(cliente);
        menu.mostrarMenu();
    }

}
