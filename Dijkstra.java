package algorithmes.base;

import modele.graphe.*;
import modele.graphe.modele.entites.*;
import java.util.*;


public class Dijkstra {

    private Graphe graphe;
    private Sommet source;

    // Résultats du calcul
    private Map<Sommet, Double> distances;      // Distance minimale depuis la source
    private Map<Sommet, Sommet> predecesseurs;  // Prédécesseur dans le plus court chemin
    private Set<Sommet> visites;                // Sommets déjà visités

    public Dijkstra(Graphe graphe) {
        this.graphe = graphe;
        this.distances = new HashMap<>();
        this.predecesseurs = new HashMap<>();
        this.visites = new HashSet<>();
    }


    public void executer(Sommet source) {
        this.source = source;
        initialiser();

        // File de priorité pour choisir le sommet avec la plus petite distance
        PriorityQueue<SommetDistance> filePriorite = new PriorityQueue<>();
        filePriorite.offer(new SommetDistance(source, 0.0));

        while (!filePriorite.isEmpty()) {
            SommetDistance current = filePriorite.poll();
            Sommet sommetCourant = current.sommet;

            // Si déjà visité, on passe au suivant
            if (visites.contains(sommetCourant)) {
                continue;
            }

            // Marquer comme visité
            visites.add(sommetCourant);

            // Explorer les successeurs
            for (Sommet successeur : graphe.getSuccesseurs(sommetCourant)) {
                if (!visites.contains(successeur)) {
                    relaxer(sommetCourant, successeur, filePriorite);
                }
            }
        }
    }


    private void initialiser() {
        distances.clear();
        predecesseurs.clear();
        visites.clear();

        // Initialiser toutes les distances à l'infini
        for (Sommet sommet : graphe.getSommets()) {
            distances.put(sommet, Double.POSITIVE_INFINITY);
            predecesseurs.put(sommet, null);
        }

        // Distance de la source = 0
        distances.put(source, 0.0);
    }

    private void relaxer(Sommet u, Sommet v, PriorityQueue<SommetDistance> filePriorite) {
        Arete arete = graphe.getArete(u, v);
        if (arete == null) return;

        double nouvelleDistance = distances.get(u) + arete.getPoids();

        // Si on trouve un chemin plus court vers v
        if (nouvelleDistance < distances.get(v)) {
            distances.put(v, nouvelleDistance);
            predecesseurs.put(v, u);
            filePriorite.offer(new SommetDistance(v, nouvelleDistance));
        }
    }


    public double getDistance(Sommet destination) {
        return distances.getOrDefault(destination, Double.POSITIVE_INFINITY);
    }


    public boolean estAccessible(Sommet destination) {
        return distances.containsKey(destination) &&
                distances.get(destination) < Double.POSITIVE_INFINITY;
    }


    public List<Sommet> getChemin(Sommet destination) {
        List<Sommet> chemin = new ArrayList<>();

        if (!estAccessible(destination)) {
            return chemin; // Chemin vide si inaccessible
        }

        // Reconstruire le chemin en remontant les prédécesseurs
        Sommet courant = destination;
        while (courant != null) {
            chemin.add(0, courant); // Ajouter au début
            courant = predecesseurs.get(courant);
        }

        return chemin;
    }


    public List<Arete> getAretesChemins(Sommet destination) {
        List<Arete> aretes = new ArrayList<>();
        List<Sommet> chemin = getChemin(destination);

        for (int i = 0; i < chemin.size() - 1; i++) {
            Sommet u = chemin.get(i);
            Sommet v = chemin.get(i + 1);
            Arete arete = graphe.getArete(u, v);
            if (arete != null) {
                aretes.add(arete);
            }
        }

        return aretes;
    }


    public void afficherResultats() {
        System.out.println("=== DIJKSTRA depuis " + source.getId() + " ===");
        System.out.println();

        for (Sommet sommet : graphe.getSommets()) {
            if (sommet.equals(source)) continue;

            double dist = getDistance(sommet);
            if (dist == Double.POSITIVE_INFINITY) {
                System.out.println(sommet.getId() + " : INACCESSIBLE");
            } else {
                System.out.println(sommet.getId() + " : distance = " + dist);
                List<Sommet> chemin = getChemin(sommet);
                System.out.print("  Chemin : ");
                for (int i = 0; i < chemin.size(); i++) {
                    System.out.print(chemin.get(i).getId());
                    if (i < chemin.size() - 1) System.out.print(" → ");
                }
                System.out.println();
            }
        }
    }

    // MÉTHODES POUR GÉRER LES HABITATIONS


    public static class ResultatChemin {
        public List<Sommet> chemin;          // Chemin jusqu'à l'intersection la plus proche
        public double distanceTotale;        // Distance totale incluant le détour sur la rue
        public Sommet intersectionArrivee;   // L'intersection par laquelle on arrive donc le sommet

        public ResultatChemin(List<Sommet> chemin, double distanceTotale, Sommet intersectionArrivee) {
            this.chemin = chemin;
            this.distanceTotale = distanceTotale;
            this.intersectionArrivee = intersectionArrivee;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Distance totale : ").append(String.format("%.2f", distanceTotale)).append(" m\n");
            sb.append("Chemin : ");
            for (int i = 0; i < chemin.size(); i++) {
                sb.append(chemin.get(i).getId());
                if (i < chemin.size() - 1) sb.append(" → ");
            }
            sb.append(" → Habitation");
            return sb.toString();
        }
    }

    public ResultatChemin getCheminVersHabitation(Habitation destination) {
        Arete areteDestination = destination.getArete();
        Sommet sommet1 = areteDestination.getSommet1();
        Sommet sommet2 = areteDestination.getSommet2();

        // Vérifier si on peut aller vers chaque extrémité
        boolean peutAllerS1 = estAccessible(sommet1);
        boolean peutAllerS2 = estAccessible(sommet2);

        if (!peutAllerS1 && !peutAllerS2) {
            // Habitation inaccessible
            return new ResultatChemin(new ArrayList<>(), Double.POSITIVE_INFINITY, null);
        }

        // Calculer les distances vers les 2 extrémités + distance sur la rue
        double dist1 = peutAllerS1 ?
                getDistance(sommet1) + destination.getDistanceDepuisSommet(sommet1) :
                Double.POSITIVE_INFINITY;

        double dist2 = peutAllerS2 ?
                getDistance(sommet2) + destination.getDistanceDepuisSommet(sommet2) :
                Double.POSITIVE_INFINITY;

        // Choisir le chemin le plus court
        if (dist1 <= dist2) {
            return new ResultatChemin(getChemin(sommet1), dist1, sommet1);
        } else {
            return new ResultatChemin(getChemin(sommet2), dist2, sommet2);
        }
    }


    public void afficherCheminVersHabitation(Habitation habitation) {
        System.out.println("=== CHEMIN VERS " + habitation.getNom() + " ===");
        System.out.println("Localisation : " + habitation);
        System.out.println();

        ResultatChemin resultat = getCheminVersHabitation(habitation);

        if (resultat.distanceTotale == Double.POSITIVE_INFINITY) {
            System.out.println("INACCESSIBLE");
        } else {
            System.out.println("" + resultat);
        }
        System.out.println();
    }


    private static class SommetDistance implements Comparable<SommetDistance> {
        Sommet sommet;
        double distance;

        SommetDistance(Sommet sommet, double distance) {
            this.sommet = sommet;
            this.distance = distance;
        }

        @Override
        public int compareTo(SommetDistance autre) {
            return Double.compare(this.distance, autre.distance);
        }
    }
}