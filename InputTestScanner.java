import java.util.Scanner;

public class InputTestScanner{
    public static void main(String args[]){
        int bulan_lahir; 
        Scanner baca = new Scanner(System.in); 
        System.out.println("Masukkan bulan lahir");
        
        bulan_lahir = baca.nextInt(); 
        if (bulan_lahir == 1)
            System.out.println("Aquarius");
        else if (bulan_lahir == 2 )
            System.out.println("Pisces");
        else System.out.println("Salah Input");

    }
}