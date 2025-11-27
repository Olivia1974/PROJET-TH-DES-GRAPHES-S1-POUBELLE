import modele.graphe.*;
import modele.graphe.modele.entites.*;
import algorithmes.base.*;
import utils.*;

/**
 * Main de test pour l'algorithme de Dijkstra
 * Test du Thème 1 - Problématique 1 - Hypothèse 1 (P1-Hyp1)
 *
 * @author Evie - Thème 1
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║   TEST DIJKSTRA - COLLECTE DES ENCOMBRANTS (P1-Hyp1)  ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();

        try {
            // ========== ÉTAPE 1 : CHARGER LE GRAPHE ==========
            System.out.println("📂 Chargement du graphe HO1...");
            Graphe graphe = LecteurGraphe.chargerDepuisFichier("data/HO1.txt");
            System.out.println("✅ Graphe chargé avec succès !");
            System.out.println(graphe);
            System.out.println();

            // ========== ÉTAPE 2 : DÉFINIR LE CENTRE DE TRAITEMENT ==========
            Sommet centreTraitement = graphe.getSommet("A");
            if (centreTraitement == null) {
                System.out.println("❌ Erreur : Le sommet A n'existe pas dans le graphe");
                return;
            }
            System.out.println("🏢 Centre de traitement : " + centreTraitement.getId());
            System.out.println();

// ========== ÉTAPE 3 : CRÉER DES HABITATIONS DE TEST ==========
            System.out.println("🏠 Création des habitations de test...");

// Habitation 1 : M. Dupont sur l'arête (B-D) à 50m de B
            Arete areteB_D = graphe.getArete(graphe.getSommet("B"), graphe.getSommet("D"));
            if (areteB_D == null) {
                System.out.println("❌ Arête B-D introuvable");
                return;
            }
            System.out.println("  ℹ️  Arête B-D : longueur = " + areteB_D.getPoids() + "m");
            Habitation dupont = new Habitation("M. Dupont", areteB_D, graphe.getSommet("B"), 50);
            System.out.println("  ✅ " + dupont);

// Habitation 2 : Mme Martin sur l'arête (O-N) à 80m de O
            Arete areteO_N = graphe.getArete(graphe.getSommet("O"), graphe.getSommet("N"));
            if (areteO_N == null) {
                System.out.println("❌ Arête O-N introuvable");
                return;
            }
            System.out.println("  ℹ️  Arête O-N : longueur = " + areteO_N.getPoids() + "m");
            Habitation martin = new Habitation("Mme Martin", areteO_N, graphe.getSommet("O"), 80);
            System.out.println("  ✅ " + martin);

// Habitation 3 : M. Bernard sur l'arête (K-L) à 60m de K
            Arete areteK_L = graphe.getArete(graphe.getSommet("K"), graphe.getSommet("L"));
            if (areteK_L == null) {
                System.out.println("❌ Arête K-L introuvable");
                return;
            }
            System.out.println("  ℹ️  Arête K-L : longueur = " + areteK_L.getPoids() + "m");
            Habitation bernard = new Habitation("M. Bernard", areteK_L, graphe.getSommet("K"), 60);
            System.out.println("  ✅ " + bernard);
            System.out.println();

            // ========== ÉTAPE 4 : EXÉCUTER DIJKSTRA ==========
            System.out.println("🔍 Exécution de Dijkstra depuis " + centreTraitement.getId() + "...");
            Dijkstra dijkstra = new Dijkstra(graphe);
            dijkstra.executer(centreTraitement);
            System.out.println("✅ Dijkstra terminé !");
            System.out.println();

            // ========== ÉTAPE 5 : TEST 1 - CHEMIN VERS UNE INTERSECTION ==========
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.println("TEST 1 : Plus court chemin vers une intersection");
            System.out.println("═══════════════════════════════════════════════════════");

            Sommet destination1 = graphe.getSommet("N");
            if (destination1 != null) {
                double distance = dijkstra.getDistance(destination1);
                System.out.println("Destination : " + destination1.getId());
                System.out.println("Distance : " + distance + " m");

                if (dijkstra.estAccessible(destination1)) {
                    System.out.print("Chemin : ");
                    for (Sommet s : dijkstra.getChemin(destination1)) {
                        System.out.print(s.getId() + " → ");
                    }
                    System.out.println("✓");
                } else {
                    System.out.println("❌ Inaccessible");
                }
            }
            System.out.println();

            // ========== ÉTAPE 6 : TEST 2 - CHEMINS VERS LES HABITATIONS ==========
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.println("TEST 2 : Plus courts chemins vers les habitations");
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.println();

            // Test M. Dupont
            dijkstra.afficherCheminVersHabitation(dupont);

            // Test Mme Martin
            dijkstra.afficherCheminVersHabitation(martin);

            // Test M. Bernard
            dijkstra.afficherCheminVersHabitation(bernard);

            // ========== ÉTAPE 7 : AFFICHER TOUS LES RÉSULTATS ==========
            System.out.println("═══════════════════════════════════════════════════════");
            System.out.println("TOUS LES CHEMINS DEPUIS LE CENTRE DE TRAITEMENT");
            System.out.println("═══════════════════════════════════════════════════════");
            dijkstra.afficherResultats();

            System.out.println();
            System.out.println("✅ TESTS TERMINÉS AVEC SUCCÈS !");

        } catch (Exception e) {
            System.err.println("❌ ERREUR : " + e.getMessage());
            e.printStackTrace();
        }
    }
}