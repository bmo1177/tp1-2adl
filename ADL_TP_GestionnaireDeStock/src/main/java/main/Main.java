package main;

import models.Produit;
import models.ProduitConsommable;
import services.GestionProduits;
import services.FileHandler;
import concurrency.SauvegardeThread;
import exceptions.NegativePriceException;
import exceptions.NomInvalidException;

import java.util.Scanner;
import java.util.List;

public class Main {
    private static GestionProduits gestionnaire = new GestionProduits();
    private static Scanner scanner = new Scanner(System.in);
    private static final String FILENAME = "produits.txt";
    
    public static void main(String[] args) {
        boolean continuer = true;
        
        // Initialisation avec quelques produits
        initialiserProduits();
        
        while (continuer) {
            afficherMenu();
            int choix = lireChoix();
            
            try {
                switch (choix) {
                    case 1 -> afficherProduits();
                    case 2 -> ajouterProduit();
                    case 3 -> supprimerProduit();
                    case 4 -> sauvegarderProduits();
                    case 5 -> chargerProduits();
                    case 6 -> trierProduits();
                    case 7 -> filtrerProduits();
                    case 8 -> sauvegarderEnThread();
                    case 9 -> modifierProduit();
                    case 10 -> {
                        continuer = false;
                        System.out.println("Au revoir!");
                    }
                    default -> System.err.println("Choix invalide. Veuillez reessayer.");
                }
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
            }
            
            System.out.println("\nAppuyez sur Entree pour continuer...");
            scanner.nextLine();
        }
        
        scanner.close();
    }
    
    private static void afficherMenu() {
        System.out.println("\n==== GESTIONNAIRE DE STOCK ====");
        System.out.println("1. Afficher les produits");
        System.out.println("2. Ajouter un produit");
        System.out.println("3. Supprimer un produit");
        System.out.println("4. Sauvegarder les produits");
        System.out.println("5. Charger les produits");
        System.out.println("6. Trier les produits");
        System.out.println("7. Filtrer les produits par prix");
        System.out.println("8. Sauvegarder en arriere-plan (Thread)");
        System.out.println("9. Modifier un produit");
        System.out.println("10. Quitter");
        System.out.print("Votre choix: ");
    }
    
    private static int lireChoix() {
        try {
            int choix = scanner.nextInt();
            scanner.nextLine();
            return choix;
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }
    
    private static void initialiserProduits() {
        try {
            gestionnaire.ajouterProduit(new Produit(1, "MacBook 14 Pro 2025 ", 600000));
            gestionnaire.ajouterProduit(new Produit(2, "Printer Epson", 45000));
            gestionnaire.ajouterProduit(new ProduitConsommable(4, "Cartouche CMYK", 680, "2024-09-10"));
        } catch (NegativePriceException | NomInvalidException e) {
            System.err.println("Erreur lors de l'initialisation: " + e.getMessage());
        }
    }
    
    private static void afficherProduits() {
        System.out.println("\n=== LISTE DES PRODUITS ===");
        gestionnaire.afficherProduits();
    }
    
    private static void ajouterProduit() throws NegativePriceException, NomInvalidException {
        System.out.println("\n=== AJOUTER UN PRODUIT ===");
        System.out.print("ID: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 
        
        System.out.print("Nom (lettres uniquement): ");
        String nom = scanner.nextLine();
        
        System.out.print("Prix: ");
        double prix = scanner.nextDouble();
        scanner.nextLine(); 
        
        System.out.print("Est-ce un produit consommable? (o/n): ");
        String reponse = scanner.nextLine();
        
        if (reponse.equalsIgnoreCase("o")) {
            System.out.print("Date d'expiration (AAAA-MM-JJ): ");
            String dateExpiration = scanner.nextLine();
            gestionnaire.ajouterProduit(new ProduitConsommable(id, nom, prix, dateExpiration));
        } else {
            gestionnaire.ajouterProduit(new Produit(id, nom, prix));
        }
        
        System.out.println("Produit ajoute avec success.");
    }
    
    private static void supprimerProduit() {
        System.out.println("\n=== SUPPRIMER UN PRODUIT ===");
        System.out.print("ID du produit a supprimer: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        gestionnaire.supprimerProduit(id);
        System.out.println("Produit supprime avec success (si existant).");
    }
    
    private static void sauvegarderProduits() {
        System.out.println("\n=== SAUVEGARDER LES PRODUITS ===");
        FileHandler.saveToFile(gestionnaire.getProduits(), FILENAME);
    }
    
    private static void chargerProduits() {
        System.out.println("\n=== CHARGER LES PRODUITS ===");
        List<Produit> produits = FileHandler.readFromFile(FILENAME);

        
        gestionnaire = new GestionProduits();
        for (Produit p : produits) {
            gestionnaire.ajouterProduit(p);
        }
    }
    
    private static void trierProduits() {
        System.out.println("\n=== TRIER LES PRODUITS ===");
        System.out.println("1. Trier par prix");
        System.out.println("2. Trier par nom");
        System.out.print("Votre choix: ");
        
        int choix = scanner.nextInt();
        scanner.nextLine(); 
        switch (choix) {
            case 1 -> {
                gestionnaire.trierParPrix();
                System.out.println("Produits tries par prix.");
            }
            case 2 -> {
                gestionnaire.trierParNom();
                System.out.println("Produits tries par nom.");
            }
            default -> System.err.println("Choix invalide.");
        }
    }
    
    private static void filtrerProduits() {
        System.out.println("\n=== FILTRER LES PRODUITS ===");
        System.out.print("Prix minimum: ");
        double seuil = scanner.nextDouble();
        scanner.nextLine(); 
        
        List<Produit> produitsFiltres = gestionnaire.filtrerProduitParPrix(seuil);
        
        System.out.println("\nProduits avec prix > " + seuil + " DA:");
        if (produitsFiltres.isEmpty()) {
            System.out.println("Aucun produit ne correspond au critere.");
        } else {
            produitsFiltres.forEach(Produit::afficher);
        }
    }
    
    private static void sauvegarderEnThread() {
        System.out.println("\n=== SAUVEGARDE EN ARRIERE-PLAN ===");
        SauvegardeThread thread = new SauvegardeThread(gestionnaire.getProduits());
        thread.start();
        System.out.println("Sauvegarde lancée en arrière-plan.");
    }
    
    private static void modifierProduit() throws NegativePriceException, NomInvalidException {
        System.out.println("\n=== MODIFIER UN PRODUIT ===");
        System.out.print("ID du produit a modifier: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Produit produit = gestionnaire.trouverProduitParId(id);
        if (produit == null) {
            System.out.println("Produit non trouvé.");
            return;
        }
        
        System.out.println("Informations actuelles:");
        produit.afficher();
        System.out.println("\nEntrez NEw info (laissez vide pour ne pas modifier):");
        
        System.out.print("New name .. (actuel: " + produit.getNom() + "): ");
        String nouveauNom = scanner.nextLine();
        if (nouveauNom.isEmpty()) {
            nouveauNom = produit.getNom(); 
        }
        
        System.out.print("Nouveau prix (actuel: " + produit.getPrix() + "): ");
        String prixStr = scanner.nextLine();
        double nouveauPrix = prixStr.isEmpty() ? produit.getPrix() : Double.parseDouble(prixStr);
        
        boolean modifie = false;
        
        if (produit instanceof ProduitConsommable) {
            System.out.print("Nouvelle date d'expiration (actuelle: " + 
                    ((ProduitConsommable)produit).getDateExpiration() + "): ");
            String nouvelleDateExp = scanner.nextLine();
            
            if (!nouvelleDateExp.isEmpty()) {
                ((ProduitConsommable)produit).setDateExpiration(nouvelleDateExp);
            }
            
            modifie = gestionnaire.modifierProduit(id, nouveauNom, nouveauPrix);
        } else {
            modifie = gestionnaire.modifierProduit(id, nouveauNom, nouveauPrix);
        }
        
        if (modifie) {
            System.out.println("Produit modifie avec success.");
        } else {
            System.out.println("Échec de la modification.");
        }
    }
}