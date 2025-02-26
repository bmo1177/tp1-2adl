package services;

import models.Produit;
import models.ProduitConsommable;
import exceptions.NegativePriceException;
import exceptions.NomInvalidException;

import java.io.*;
import java.util.*;

public class FileHandler {
    public static void saveToFile(List<Produit> produits, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Produit p : produits) {
                if (p instanceof ProduitConsommable) {
                    bw.write(p.getId() + "," + p.getNom() + "," + p.getPrix() + "," +
                            ((ProduitConsommable) p).getDateExpiration() + "\n");
                } else {
                    bw.write(p.getId() + "," + p.getNom() + "," + p.getPrix() + ",NULL\n");
                }
            }
            System.out.println("Produits sauvegardes dans " + filename);
        } catch (IOException e) {
            System.err.println("Erreur de sauvegarde fichier: " + e.getMessage());
        }
    }

    public static List<Produit> readFromFile(String filename) {
        List<Produit> produits = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String nom = parts[1];
                double prix = Double.parseDouble(parts[2]);

                if (!parts[3].equals("NULL")) {
                    produits.add(new ProduitConsommable(id, nom, prix, parts[3]));
                } else {
                    produits.add(new Produit(id, nom, prix));
                }
            }
            System.out.println("Produits charges depuis " + filename);
        } catch (IOException | NumberFormatException | NegativePriceException | NomInvalidException e) {
            System.err.println("Erreur de lecture fichier: " + e.getMessage());
        }
        return produits;
    }
}