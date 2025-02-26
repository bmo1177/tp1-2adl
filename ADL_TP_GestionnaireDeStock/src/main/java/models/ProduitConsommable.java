
package models;

import exceptions.NegativePriceException;
import exceptions.NomInvalidException;

public class ProduitConsommable extends Produit {
    private String dateExpiration;
    private static final double TAUX_TAXE_ALIMENTAIRE = 0.09; // TVA 9% pour produits alimentaire

    public ProduitConsommable(int id, String nom, double prix, String dateExpiration) throws NegativePriceException, NomInvalidException {
        super(id, nom, prix);
        this.dateExpiration = dateExpiration;
    }

    @Override
    public double calculerPrixAvecTaxe() {
        return getPrix() * (1 + TAUX_TAXE_ALIMENTAIRE);
    }

    @Override
    public void afficher() {
        super.afficher();
        System.out.println(" ---> Expire le: " + dateExpiration);
    }

    public String getDateExpiration() {
        return dateExpiration;
    }
    
    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }
}
