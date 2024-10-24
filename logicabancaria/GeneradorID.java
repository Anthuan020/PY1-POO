package logicabancaria;

import java.util.ArrayList;
import java.util.Random;

public class GeneradorID {
    private static ArrayList<Integer> IDs = new ArrayList<>(); // Lista estática

    public static int generarID() { // Método estático
        Random random = new Random();
        int id;

        do {
            // Mínimo y máximo valores de 9 dígitos dentro del rango de Integer
            int minimo = 100000000; // 9 dígitos mínimo
            int maximo = 999999999; // 9 dígitos máximo

            // Generar un número aleatorio entre el mínimo y máximo (incluye 9 dígitos)
            id = random.nextInt((maximo - minimo) + 1) + minimo;
        } while (IDs.contains(id)); // Verificar que el ID no esté ya en la lista

        IDs.add(id); // Agregar el ID a la lista
        return id; // Retornar el ID generado
    }

    public static ArrayList<Integer> getIDs() { // Método estático
        return IDs; // Retornar la lista de IDs generados
    }
}
