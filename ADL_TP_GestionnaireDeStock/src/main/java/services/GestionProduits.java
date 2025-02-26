package services;

import models.Produit;
import models.ProduitConsommable;
import exceptions.NegativePriceException;
import exceptions.NomInvalidException;
import java.util.*;
import java.util.stream.Collectors;

public class GestionProduits {
    private List<Produit> produits = new ArrayList<>();

    public void ajouterProduit(Produit produit) {
        produits.add(produit);
    }
    
    public void supprimerProduit(int id) {
        produits.removeIf(p -> p.getId() == id);
    }

    public List<Produit> filtrerProduitParPrix(double seuil) {
        return produits.stream()
                    .filter(p -> p.getPrix() > seuil)
                    .collect(Collectors.toList());
    }

    public void afficherProduits() {
        if (produits.isEmpty()) {
            System.out.println("Aucun produit disponible.");
        } else {
            produits.forEach(Produit::afficher);
        }
    }

    public List<Produit> getProduits() {
        return produits;
    }
    
    public void trierParPrix() {
        produits.sort(Comparator.comparing(Produit::getPrix));
    }
    
    public void trierParNom() {
        produits.sort(Comparator.comparing(Produit::getNom));
    }
    
    public Produit trouverProduitParId(int id) {
        return produits.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }
    
    public boolean modifierProduit(int id, String nouveauNom, double nouveauPrix) 
            throws NegativePriceException, NomInvalidException {
        Produit produit = trouverProduitParId(id);
        if (produit != null) {
            if (nouveauNom != null && !nouveauNom.isEmpty()) {
                produit.setNom(nouveauNom);
            }
            
            if (nouveauPrix >= 0) {
                produit.setPrix(nouveauPrix);
            } else {
                throw new NegativePriceException("Le prix ne peut pas être négatif !");
            }
            return true;
        }
        return false;
    }
    
    public boolean modifierProduitConsommable(int id, String nouveauNom, double nouveauPrix, String nouvelleDateExpiration) 
            throws NegativePriceException, NomInvalidException {
        Produit produit = trouverProduitParId(id);
        if (produit != null && produit instanceof ProduitConsommable) {

            boolean modified = modifierProduit(id, nouveauNom, nouveauPrix);
         
            if (nouvelleDateExpiration != null && !nouvelleDateExpiration.isEmpty()) {
                ((ProduitConsommable) produit).setDateExpiration(nouvelleDateExpiration);
            }
            
            return modified;
        }
        return false;
    }
}
