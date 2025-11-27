package modele.graphe.modele.entites;

import modele.graphe.*;


public class Habitation {

    private String nom;                      // nom du particulier (ex: "M. Segado")
    private Arete arete;                     // L'arête (rue) dans laquelle se trouve l'habitation
    private Sommet sommetReference;          // Le sommet depuis lequel on mesure la distance
    private double distanceDepuisSommet;     // Distance en mètres depuis le sommet duquel on vient


    public Habitation(String nom, Arete arete, Sommet sommetReference, double distanceDepuisSommet) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (arete == null) {
            throw new IllegalArgumentException("L'arête ne peut pas être null");
        }
        if (sommetReference == null) {
            throw new IllegalArgumentException("Le sommet de référence ne peut pas être null");
        }
        if (!arete.contient(sommetReference)) {
            throw new IllegalArgumentException("Le sommet de référence doit être une extrémité de l'arête");
        }
        if (distanceDepuisSommet < 0 || distanceDepuisSommet > arete.getPoids()) {
            throw new IllegalArgumentException("La distance doit être entre 0 et la longueur de l'arête");
        }

        this.nom = nom;
        this.arete = arete;
        this.sommetReference = sommetReference;
        this.distanceDepuisSommet = distanceDepuisSommet;
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public Arete getArete() {
        return arete;
    }

    public Sommet getSommetReference() {
        return sommetReference;
    }

    public double getDistanceDepuisSommet() {
        return distanceDepuisSommet;
    }


    public Sommet getAutreSommet() {
        return arete.getAutreSommet(sommetReference);
    }

//Là on calcule la distance
    public double getDistanceDepuisSommet(Sommet depart) {
        if (depart.equals(sommetReference)) {
            // Départ depuis le sommet de référence
            return distanceDepuisSommet;
        } else if (depart.equals(getAutreSommet())) {
            // Départ depuis l'autre extrémité
            return arete.getPoids() - distanceDepuisSommet;
        }
        // Le sommet n'est pas sur cette arête
        return Double.POSITIVE_INFINITY;
    }


    public Sommet[] getExtremites() {
        return new Sommet[] { arete.getSommet1(), arete.getSommet2() };
    }

    @Override
    public String toString() {
        return nom + " (sur rue " + arete.getSommet1().getId() + "-" + arete.getSommet2().getId() +
                " à " + distanceDepuisSommet + "m de " + sommetReference.getId() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Habitation autre = (Habitation) obj;
        return nom.equals(autre.nom);
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }
}