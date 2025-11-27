package modele.graphe;

import java.util.Objects;

public class Sommet {

    // L'identifiant unique du sommet (ex: "A", "B", "G"...)
    // On met "final" car on ne change JAMAIS l'ID d'un sommet une fois qu'on l'a créé
    private final String id;

    // Le nom descriptif (ex: "Intersection Rue A / Rue B")
    // On peut le changer, donc pas "final"
    private String nom;


    public Sommet(String id, String nom) {
        this.id = id;
        this.nom = nom;
    }


    public Sommet(String id) {
        this(id, id);  // Appelle le constructeur du dessus avec nom=id
    }

    // GETTERS (pour récupérer les valeurs pcq là on est en pv)

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    // SETTERS (pour modifier les valeurs)

    public void setNom(String nom) {
        this.nom = nom;
    }
    // Pas de setId() car l'ID ne change JAMAIS !


    @Override
    public boolean equals(Object obj) {
        // Si c'est le même objet en mémoire → TRUE
        if (this == obj) return true;

        // Si obj n'est pas un Sommet → FALSE
        if (!(obj instanceof Sommet)) return false;

        // Compare les ID
        return id.equals(((Sommet) obj).id);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return id;
    }
}