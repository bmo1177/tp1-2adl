package models;

import exceptions.NegativePriceException;
import exceptions.NomInvalidException;
import interfaces.Affichable;

public abstract class ProduitGenerique implements Affichable {
    
    public abstract double calculerPrixAvecTaxe();
    
    @Override
    public void afficher() {
        System.out.println("Prix avec taxe est: " + this.calculerPrixAvecTaxe());
    }
}
