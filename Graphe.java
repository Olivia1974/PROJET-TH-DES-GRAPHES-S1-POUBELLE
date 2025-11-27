package modele.graphe;

import java.util.*;


public class Graphe {

    public enum TypeHypothese {
        HO1,  // Toutes rues bidirectionnelles 1 voie
        HO2,  // Toutes rues orientées
        HO3   // Mix
    }

    private TypeHypothese typeHypothese;
    private Map<String, Sommet> sommets;
    private List<Arete> aretes;
    private Map<Sommet, List<Arete>> adjacence;

    public Graphe(TypeHypothese typeHypothese) {
        this.typeHypothese = typeHypothese;
        this.sommets = new HashMap<>();
        this.aretes = new ArrayList<>();
        this.adjacence = new HashMap<>();
    }

    // Getters
    public TypeHypothese getTypeHypothese() { return typeHypothese; }
    public int getNbSommets() { return sommets.size(); }
    public int getNbAretes() { return aretes.size(); }
    public Collection<Sommet> getSommets() { return sommets.values(); }
    public List<Arete> getAretes() { return new ArrayList<>(aretes); }
    public Sommet getSommet(String id) { return sommets.get(id); }

    // Ajout de sommets
    public boolean ajouterSommet(Sommet sommet) {
        if (sommet == null) {
            throw new IllegalArgumentException("Le sommet ne peut pas être null");
        }
        if (sommets.containsKey(sommet.getId())) {
            return false;
        }
        sommets.put(sommet.getId(), sommet);
        adjacence.put(sommet, new ArrayList<>());
        return true;
    }

    // Ajout d'arêtes
    public void ajouterArete(Arete arete) {
        if (arete == null) {
            throw new IllegalArgumentException("L'arête ne peut pas être null");
        }

        Sommet s1 = arete.getSommet1();
        Sommet s2 = arete.getSommet2();

        if (!sommets.containsKey(s1.getId())) ajouterSommet(s1);
        if (!sommets.containsKey(s2.getId())) ajouterSommet(s2);

        validerArete(arete);

        aretes.add(arete);
        adjacence.get(s1).add(arete);
        adjacence.get(s2).add(arete);
    }

    private void validerArete(Arete arete) {
        switch (typeHypothese) {
            case HO1:
                if (!arete.isBidirectionnelle() || arete.getNbVoies() != 1) {
                    throw new IllegalArgumentException(
                            "HO1: arêtes doivent être bidirectionnelles avec 1 voie");
                }
                break;
            case HO2:
                if (arete.isBidirectionnelle()) {
                    throw new IllegalArgumentException(
                            "HO2: arêtes doivent être orientées");
                }
                break;
            case HO3:
                break;
        }
    }

    // Voisinage
    public List<Arete> getAretesAdjacentes(Sommet sommet) {
        return new ArrayList<>(adjacence.getOrDefault(sommet, new ArrayList<>()));
    }

    public List<Sommet> getSuccesseurs(Sommet sommet) {
        List<Sommet> successeurs = new ArrayList<>();
        List<Arete> aretesAdj = adjacence.getOrDefault(sommet, new ArrayList<>());

        for (Arete arete : aretesAdj) {
            Sommet autre = arete.getAutreSommet(sommet);
            if (arete.peutAllerDe(sommet, autre)) {
                successeurs.add(autre);
            }
        }
        return successeurs;
    }

    public int getDegre(Sommet sommet) {
        return adjacence.getOrDefault(sommet, new ArrayList<>()).size();
    }

    public Arete getArete(Sommet s1, Sommet s2) {
        List<Arete> aretesS1 = adjacence.getOrDefault(s1, new ArrayList<>());
        for (Arete arete : aretesS1) {
            if (arete.contient(s2)) {
                return arete;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("===== GRAPHE ").append(typeHypothese).append(" =====\n");
        sb.append("Sommets : ").append(getNbSommets()).append("\n");
        sb.append("Arêtes : ").append(getNbAretes()).append("\n");
        return sb.toString();
    }
}