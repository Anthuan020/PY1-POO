package indicadorTipoDeCambioDolar;

import java.util.ArrayList;
import java.util.Random;


public class generadorPin
{
    private ArrayList<Integer> IDs = new ArrayList<>();
    
    

    public void generarPin(){
        Random random = new Random();

        // Mínimo y máximo valores de 9 dígitos dentro del rango de Integer
        int minimo = 100000000; // 9 dígitos mínimo
        int maximo = 999999999; // 9 dígitos máximo

        // Generar un número aleatorio entre el mínimo y máximo (incluye 9 dígitos)
        int numeroAleatorio = random.nextInt((maximo - minimo) + 1) + minimo;

        IDs.add(numeroAleatorio);
        
        System.out.println("Número aleatorio de 9 dígitos: " + numeroAleatorio);
    }
    
    public ArrayList getIDs(){
        return this.IDs;
    }
    
}
