
package models;

import exceptions.NegativePriceException;
import exceptions.NomInvalidException;

public class Produit extends ProduitGenerique {
    private int id;
    private String nom;
    private double prix;
    private static final double TAUX_TAXE = 0.19; // TVA à 19%

    public Produit(int id, String nom, double prix) throws NegativePriceException, NomInvalidException {
        if (prix < 0)
            throw new NegativePriceException("Le prix ne peut pas être négatif !");
        if (!nom.matches("[a-zA-Z]+"))
            throw new NomInvalidException("Le nom doit contenir seulement des lettres de l'alphabet.");
        this.id = id;
        this.nom = nom;
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) throws NomInvalidException {
        if (!nom.matches("[a-zA-Z]+"))
            throw new NomInvalidException("Le nom doit contenir seulement des lettres de l'alphabet.");
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) throws NegativePriceException {
        if (prix < 0) {
            throw new NegativePriceException("Le prix ne peut pas être négatif !");
        }
        this.prix = prix;
    }
    
    public void validateName() throws NomInvalidException {
        if (!this.nom.matches("[a-zA-Z]+"))
            throw new NomInvalidException("Le nom doit contenir seulement des lettres de l'alphabet.");
    }

    @Override
    public double calculerPrixAvecTaxe() {
        return prix * (1 + TAUX_TAXE);
    }

    @Override
    public void afficher() {
        System.out.println("Produit(" + id + "): [" + nom + " - " + prix + " DA]");
        System.out.println("Prix avec taxe: " + calculerPrixAvecTaxe() + " DA");
    }
}