package com.calegaris.monedachallenge.menu;

import com.calegaris.monedachallenge.api.ClienteExchangeApi;
import com.calegaris.monedachallenge.modelos.Moneda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MenuConversor {

    private static final Scanner teclado = new Scanner(System.in);
    private final ClienteExchangeApi cliente;

    public MenuConversor(ClienteExchangeApi cliente){
        this.cliente = cliente;
    }

    private static final Map<Integer, String> codigosMonedas = Map.of(
            1, "USD",
            2, "ARS",
            3, "BRL",
            4, "EUR"
    );

    private static final Map<String, String> nombresMonedas = Map.of(
            "USD", "Dólar Americano",
            "ARS", "Peso Argentino",
            "BRL", "Real Brasieño",
            "EUR", "Euro"
    );


        public void mostrarMenu(){
            int intentos = 0;


            while(intentos < 3){
            System.out.println("\nBienvenidos al Conversor de Monedas");
            System.out.println("Elige la moneda que quieres convertir:");
            System.out.println("1) Dólar Americano");
            System.out.println("2) Peso Argentino");
            System.out.println("3) Real Brasileño");
            System.out.println("4) Euro");
            System.out.println("5) Salir");

            String linea = teclado.nextLine();
            int opcion;

                try {
                    opcion = Integer.parseInt(linea);
                } catch (NumberFormatException e) {
                    opcion = -1;
                }

                if (opcion == 5) {
                    System.out.println("¡Gracias por usar el conversor!");
                    return;
                }

                if (!codigosMonedas.containsKey(opcion)){
                    intentos++;
                    System.out.println("Opción inválida. The quedan " + (3 - intentos));
                    continue;
                }

                String monedaBase = codigosMonedas.get(opcion);
                boolean volverAlMenu = mostrarSubMenu(monedaBase);
                if (!volverAlMenu){
                    System.out.println("¡Gracias por usar el conversor!");
                    return;
                }

            }

            System.out.println("Demasiados intentos inválidos. Fin del programa.");
        }

    private boolean mostrarSubMenu(String monedaBase) {
        List<String> monedasDestino = new ArrayList<>(nombresMonedas.keySet());
        monedasDestino.remove(monedaBase);

        System.out.println("Convertir " + nombresMonedas.get(monedaBase) + " (" + monedaBase + ") a:");

        for (int i = 0; i < monedasDestino.size(); i++) {
            String codigo = monedasDestino.get(i);
            System.out.printf("%d) %s (%s)%n", i + 1, nombresMonedas.get(codigo), codigo);
        }

        System.out.println("4) Salir");

        String linea = teclado.nextLine();
        int opcion;


        try {
            opcion = Integer.parseInt(linea);
        }catch (NumberFormatException e){
            System.out.println("Entrada no válida.");
            return true;
        }

        if (opcion == 5){
            return false;
        }

        if (opcion == 4){
            return true;
        }
        if (opcion >= 1 && opcion <= monedasDestino.size()) {
            String monedaDestino = monedasDestino.get(opcion - 1);
            System.out.printf("Vas a convertir de %s a %s.%n",
                    monedaBase, monedaDestino);

            // Pedir el monto a convertir
            System.out.print("Ingresa el monto a convertir: ");
            double monto;
            try {
                monto = Double.parseDouble(teclado.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Monto inválido. Volviendo al menú principal.");
                return true;
            }

            // Llamada a la API
            try {
                Moneda conversion = cliente.convertirMoneda(monedaBase, monedaDestino);
                double resultado = monto * conversion.conversion_rate();
                System.out.printf("El tipo de cambio es: 1 %s = %.2f %s%n%n",
                        conversion.base_code(),
                        conversion.conversion_rate(),
                        conversion.target_code());
                System.out.printf("Resultado: %.2f %s = %.2f %s%n%n",
                        monto,
                        conversion.base_code(),
                        resultado,
                        conversion.target_code());
            } catch (IOException | InterruptedException e) {
                System.out.println("Error al consultar la API: " + e.getMessage());
            }

            System.out.println("¿Querés hacer otra conversión de monedas? (s/n)");
            String resp = teclado.nextLine();
            return resp.equalsIgnoreCase("s");  // true = repetir submenú; false = volver al principal
        }

        System.out.println("Opción inválida. Volviendo al menú principal.");
        return true;


    }
}

