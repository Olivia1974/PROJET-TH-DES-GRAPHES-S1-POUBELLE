package modele.graphe;

// les aretes sont les rues entre chaque sommet ( intersections)
public class Arete {

    private final Sommet sommet1;
    private final Sommet sommet2;
    private double poids; // distance entre deux intersections
    private String nom;
    private boolean bidirectionnelle; // sens unique ou pas??
    private int nbVoies; // ramassage en une fois ou deux ?

    public Arete(Sommet sommet1, Sommet sommet2, double poids, String nom,
                 boolean bidirectionnelle, int nbVoies) {
        if (sommet1 == null || sommet2 == null) {
            throw new IllegalArgumentException("Les sommets ne peuvent pas être null");
        }
        if (sommet1.equals(sommet2)) {
            throw new IllegalArgumentException("Pas de boucle (graphe simple)");
        }
        if (poids < 0) {
            throw new IllegalArgumentException("Le poids doit être >= 0");
        }

        this.sommet1 = sommet1;
        this.sommet2 = sommet2;
        this.poids = poids;
        this.nom = (nom != null) ? nom : "";
        this.bidirectionnelle = bidirectionnelle;
        this.nbVoies = nbVoies;
    }

    public Arete(Sommet sommet1, Sommet sommet2, double poids) {
        this(sommet1, sommet2, poids, "", true, 1);
    }

    // Getters pour acceder aux valeurs
    public Sommet getSommet1() { return sommet1; }
    public Sommet getSommet2() { return sommet2; }
    public double getPoids() { return poids; }
    public String getNom() { return nom; }
    public boolean isBidirectionnelle() { return bidirectionnelle; }
    public int getNbVoies() { return nbVoies; }

    public boolean ramassageCompletEnUnPassage() {
        return bidirectionnelle && nbVoies == 1;
    }

    // Setters pour modifier
    public void setPoids(double poids) { this.poids = poids; }
    public void setNom(String nom) { this.nom = nom; }
    public void setBidirectionnelle(boolean bidirectionnelle) {
        this.bidirectionnelle = bidirectionnelle;
    }
    public void setNbVoies(int nbVoies) { this.nbVoies = nbVoies; }


    public boolean peutAllerDe(Sommet depart, Sommet arrivee) {
        if (depart.equals(sommet1) && arrivee.equals(sommet2)) {
            return true;
        }
        if (depart.equals(sommet2) && arrivee.equals(sommet1)) {
            return bidirectionnelle;
        }
        return false;
    }

    public Sommet getAutreSommet(Sommet sommet) {
        if (sommet.equals(sommet1)) return sommet2;
        if (sommet.equals(sommet2)) return sommet1;
        throw new IllegalArgumentException("Le sommet n'appartient pas à cette arête");
    }

    public boolean contient(Sommet sommet) {
        return sommet1.equals(sommet) || sommet2.equals(sommet);
    }

    @Override
    public String toString() {
        String fleche = bidirectionnelle ? " <--> " : " --> ";
        return sommet1.getId() + fleche + sommet2.getId() + " (poids: " + poids + ")";
    }
}