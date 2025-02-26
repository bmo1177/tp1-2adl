
package concurrency;

import java.util.List;
import models.Produit;

public class SauvegardeThread extends Thread {
    private List<Produit> produits;
    
    public SauvegardeThread(List<Produit> produits) {
        this.produits = produits;
    }
    
    @Override
    public void run() {
        try {
           System.out.println("Sauvegarde en cours....");
           Thread.sleep(2000); // Simule un délai de 2 secondes
           System.out.println("Produits sauvegardés: ");
           produits.forEach(Produit::afficher);
        } catch (InterruptedException e) {
            System.err.println("Erreur pendant la sauvegarde: " + e.getMessage());
        }
    }
}