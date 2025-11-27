package utils;

import modele.graphe.*;
import java.io.*;
import java.util.*;


public class LecteurGraphe {

    public static Graphe chargerDepuisFichier(String cheminFichier) throws IOException {

        // Ouvrir le fichier en lecture
        BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));

        Graphe graphe = null;  // Le graphe qu'on va construire
        String ligne;

        // Indicateurs de section (pour savoir où on est dans le fichier)
        boolean sectionSommets = false;
        boolean sectionAretes = false;

        // Lire le fichier ligne par ligne
        while ((ligne = reader.readLine()) != null) {
            ligne = ligne.trim();  // Enlever les espaces au début/fin

            // Ignorer les lignes vides et les commentaires (#)
            if (ligne.isEmpty() || ligne.startsWith("#")) {
                continue;
            }

            // DÉTECTION DU TYPE D'HYPOTHÈSE
            if (ligne.startsWith("TYPE:")) {
                // Exemple : "TYPE: HO1"
                String type = ligne.substring(5).trim();  // Récupère "HO1"

                Graphe.TypeHypothese typeHyp;
                switch (type) {
                    case "HO1":
                        typeHyp = Graphe.TypeHypothese.HO1;
                        break;
                    case "HO2":
                        typeHyp = Graphe.TypeHypothese.HO2;
                        break;
                    case "HO3":
                        typeHyp = Graphe.TypeHypothese.HO3;
                        break;
                    default:
                        throw new IllegalArgumentException("Type inconnu : " + type);
                }

                // Créer le graphe avec le bon type
                graphe = new Graphe(typeHyp);
                continue;
            }

            // DÉTECTION DES SECTIONS
            if (ligne.equals("SOMMETS:")) {
                sectionSommets = true;
                sectionAretes = false;
                continue;
            }

            if (ligne.equals("ARETES:")) {
                sectionSommets = false;
                sectionAretes = true;
                continue;
            }

            // Vérifier que le graphe a été créé (TYPE: doit être en premier)
            if (graphe == null) {
                throw new IllegalArgumentException(
                        "Le fichier doit commencer par TYPE: HO1/HO2/HO3"
                );
            }

            // LECTURE DES SOMMETS
            if (sectionSommets) {
                // Exemple de ligne : "G"
                Sommet sommet = new Sommet(ligne);
                graphe.ajouterSommet(sommet);
            }

            // LECTURE DES ARÊTES
            if (sectionAretes) {
                // Séparer la ligne par des espaces
                // Exemple : "G H 100" → ["G", "H", "100"]
                String[] parts = ligne.split("\\s+");

                if (parts.length < 3) {
                    throw new IllegalArgumentException("Format invalide : " + ligne);
                }

                String idSommet1 = parts[0];  // "G"
                String idSommet2 = parts[1];  // "H"
                double poids = Double.parseDouble(parts[2]);  // 100

                // Récupérer les sommets depuis le graphe
                Sommet s1 = graphe.getSommet(idSommet1);
                Sommet s2 = graphe.getSommet(idSommet2);

                // Vérifier qu'ils existent
                if (s1 == null) {
                    throw new IllegalArgumentException("Sommet inconnu : " + idSommet1);
                }
                if (s2 == null) {
                    throw new IllegalArgumentException("Sommet inconnu : " + idSommet2);
                }

                // Créer l'arête selon le type d'hypothèse
                Arete arete;

                if (graphe.getTypeHypothese() == Graphe.TypeHypothese.HO1) {
                    // HO1 : bidirectionnelle + 1 voie (par défaut)
                    String nom = (parts.length > 3) ? parts[3] : "";
                    arete = new Arete(s1, s2, poids, nom, true, 1);

                } else {
                    // HO2 et HO3 : besoin de spécifier bidirectionnel et nbVoies
                    // Format : "G H 100 false 2 [nom]"
                    if (parts.length < 5) {
                        throw new IllegalArgumentException(
                                "Format HO2/HO3 attendu : sommet1 sommet2 poids bidirectionnel nbVoies [nom]"
                        );
                    }

                    boolean bidirectionnel = Boolean.parseBoolean(parts[3]);
                    int nbVoies = Integer.parseInt(parts[4]);
                    String nom = (parts.length > 5) ? parts[5] : "";

                    arete = new Arete(s1, s2, poids, nom, bidirectionnel, nbVoies);
                }

                // Ajouter l'arête au graphe
                graphe.ajouterArete(arete);
            }
        }

        reader.close();

        // Vérifier qu'on a bien construit un graphe
        if (graphe == null) {
            throw new IllegalArgumentException("Fichier vide ou format invalide");
        }

        return graphe;
    }


    public static void afficherResume(Graphe graphe, String nomFichier) {
        System.out.println("=====================================");
        System.out.println("Fichier chargé : " + nomFichier);
        System.out.println("Type : " + graphe.getTypeHypothese());
        System.out.println("Sommets : " + graphe.getNbSommets());
        System.out.println("Arêtes : " + graphe.getNbAretes());
        System.out.println("=====================================\n");
    }
}