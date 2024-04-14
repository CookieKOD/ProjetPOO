import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class Livre {
    private String titre;
    private String auteur;
    private String isbn;
    private int anneePublication;

    public Livre(String titre, String auteur, String isbn, int anneePublication) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.anneePublication = anneePublication;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    @Override
    public String toString() {
        return titre + " par " + auteur;
    }
}

class Utilisateur {
    private String nom;
    private int idUtilisateur;
    private ArrayList<Livre> livresEmpruntes;

    public Utilisateur(String nom, int idUtilisateur) {
        this.nom = nom;
        this.idUtilisateur = idUtilisateur;
        this.livresEmpruntes = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public ArrayList<Livre> getLivresEmpruntes() {
        return livresEmpruntes;
    }

    public void emprunterLivre(Livre livre) {
        livresEmpruntes.add(livre);
    }

    public void retournerLivre(Livre livre) {
        livresEmpruntes.remove(livre);
    }

    public int nombreLivresEmpruntes() {
        return livresEmpruntes.size();
    }
}

class Bibliothèque {
    private ArrayList<Livre> listeLivres;
    private HashMap<Utilisateur, ArrayList<Livre>> empruntsUtilisateurs;
    private ArrayList<Utilisateur> listeUtilisateurs;

    public Bibliothèque() {
        listeLivres = new ArrayList<>();
        empruntsUtilisateurs = new HashMap<>();
        listeUtilisateurs = new ArrayList<>();
    }

    public void ajouterLivre(Livre livre) {
        listeLivres.add(livre);
    }

    public void supprimerLivre(Livre livre) {
        listeLivres.remove(livre);
        // Supprimer le livre de tous les emprunts utilisateurs
        for (ArrayList<Livre> livres : empruntsUtilisateurs.values()) {
            livres.remove(livre);
        }
    }

    public Livre rechercherLivreParISBN(String isbn) {
        for (Livre livre : listeLivres) {
            if (livre.getIsbn().equalsIgnoreCase(isbn)) {
                return livre;
            }
        }
        return null;
    }

    public Livre rechercherLivre(String recherche) {
        for (Livre livre : listeLivres) {
            if (livre.getTitre().equalsIgnoreCase(recherche) ||
                livre.getAuteur().equalsIgnoreCase(recherche) ||
                livre.getIsbn().equalsIgnoreCase(recherche)) {
                return livre;
            }
        }
        return null;
    }

    public void enregistrerEmprunt(Utilisateur utilisateur, Livre livre) {
        ArrayList<Livre> livresEmpruntes = empruntsUtilisateurs.getOrDefault(utilisateur, new ArrayList<>());
        livresEmpruntes.add(livre);
        empruntsUtilisateurs.put(utilisateur, livresEmpruntes);
    }

    public void enregistrerRetour(Utilisateur utilisateur, Livre livre) {
        if (empruntsUtilisateurs.containsKey(utilisateur)) {
            empruntsUtilisateurs.get(utilisateur).remove(livre);
        }
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        listeUtilisateurs.add(utilisateur);
    }

    public void supprimerUtilisateur(Utilisateur utilisateur) {
        listeUtilisateurs.remove(utilisateur);
    }

    public Utilisateur rechercherUtilisateurParID(int id) {
        for (Utilisateur utilisateur : listeUtilisateurs) {
            if (utilisateur.getIdUtilisateur() == id) {
                return utilisateur;
            }
        }
        return null;
    }

    public ArrayList<Utilisateur> getListeUtilisateurs() {
        return listeUtilisateurs;
    }

    public ArrayList<Livre> getListeLivresDisponibles() {
        ArrayList<Livre> livresDisponibles = new ArrayList<>();
        for (Livre livre : listeLivres) {
            boolean emprunte = false;
            for (ArrayList<Livre> livres : empruntsUtilisateurs.values()) {
                if (livres.contains(livre)) {
                    emprunte = true;
                    break;
                }
            }
            if (!emprunte) {
                livresDisponibles.add(livre);
            }
        }
        return livresDisponibles;
    }

    public ArrayList<Livre> getLivresEmpruntes(Utilisateur utilisateur) {
        return empruntsUtilisateurs.getOrDefault(utilisateur, new ArrayList<>());
    }
}

class GestionLivres {
    private Bibliothèque bibliothèque;
    private Scanner scanner;

    public GestionLivres(Bibliothèque bibliothèque, Scanner scanner) {
        this.bibliothèque = bibliothèque;
        this.scanner = scanner;
    }

    public void afficherMenu() {
        int choix;
        do {
            System.out.println("\n***** Gestion des Livres *****");
            System.out.println("1. Ajouter un livre");
            System.out.println("2. Supprimer un livre");
            System.out.println("3. Rechercher un livre");
            System.out.println("4. Afficher la liste des livres disponibles");
            System.out.println("5. Retour au menu principal");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    ajouterLivre();
                    break;
                case 2:
                    supprimerLivre();
                    break;
                case 3:
                    rechercherLivre();
                    break;
                case 4:
                    afficherListeLivresDisponibles();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez choisir une option valide.");
            }
        } while (choix != 5);
    }

    public void ajouterLivre() {
        System.out.println("\n***** Ajouter un livre *****");
        System.out.print("Titre : ");
        String titre = scanner.nextLine();
        System.out.print("Auteur : ");
        String auteur = scanner.nextLine();
        System.out.print("ISBN : ");
        String isbn = scanner.nextLine();
        System.out.print("Année de publication : ");
        int anneePublication = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Livre livre = new Livre(titre, auteur, isbn, anneePublication);
        bibliothèque.ajouterLivre(livre);
        System.out.println("Livre ajouté avec succès !");
    }

    public void supprimerLivre() {
        System.out.println("\n***** Supprimer un livre *****");
        System.out.print("ISBN du livre à supprimer : ");
        String isbn = scanner.nextLine();
        Livre livre = bibliothèque.rechercherLivreParISBN(isbn);
        if (livre != null) {
            bibliothèque.supprimerLivre(livre);
            System.out.println("Livre supprimé avec succès !");
        } else {
            System.out.println("Livre non trouvé !");
        }
    }

    public void rechercherLivre() {
        System.out.println("\n***** Rechercher un livre *****");
        System.out.print("Entrez le titre, l'auteur ou l'ISBN du livre : ");
        String recherche = scanner.nextLine();
        Livre livre = bibliothèque.rechercherLivre(recherche);
        if (livre != null) {
            System.out.println("Livre trouvé : " + livre);
        } else {
            System.out.println("Livre non trouvé !");
        }
    }

    public void afficherListeLivresDisponibles() {
        System.out.println("\n***** Liste des livres disponibles *****");
        ArrayList<Livre> livresDisponibles = bibliothèque.getListeLivresDisponibles();
        for (Livre livre : livresDisponibles) {
            System.out.println(livre);
        }
    }
}

class GestionUtilisateurs {
    private Bibliothèque bibliothèque;
    private Scanner scanner;

    public GestionUtilisateurs(Bibliothèque bibliothèque, Scanner scanner) {
        this.bibliothèque = bibliothèque;
        this.scanner = scanner;
    }

    public void afficherMenu() {
        int choix;
        do {
            System.out.println("\n***** Gestion des Utilisateurs *****");
            System.out.println("1. Ajouter un utilisateur");
            System.out.println("2. Supprimer un utilisateur");
            System.out.println("3. Afficher la liste des utilisateurs");
            System.out.println("4. Vérifier l'éligibilité d'un utilisateur");
            System.out.println("5. Afficher les livres empruntés par un utilisateur");
            System.out.println("6. Retour au menu principal");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    ajouterUtilisateur();
                    break;
                case 2:
                    supprimerUtilisateur();
                    break;
                case 3:
                    afficherListeUtilisateurs();
                    break;
                case 4:
                    verifierEligibiliteUtilisateur();
                    break;
                case 5:
                    afficherLivresEmpruntesParUtilisateur();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez choisir une option valide.");
            }
        } while (choix != 6);
    }

    public void ajouterUtilisateur() {
        System.out.println("\n***** Ajouter un utilisateur *****");
        System.out.print("Nom de l'utilisateur : ");
        String nom = scanner.nextLine();
        System.out.print("ID de l'utilisateur : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Utilisateur utilisateur = new Utilisateur(nom, id);
        bibliothèque.ajouterUtilisateur(utilisateur);
        System.out.println("Utilisateur ajouté avec succès !");
    }

    public void supprimerUtilisateur() {
        System.out.println("\n***** Supprimer un utilisateur *****");
        System.out.print("ID de l'utilisateur à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Utilisateur utilisateur = bibliothèque.rechercherUtilisateurParID(id);
        if (utilisateur != null) {
            bibliothèque.supprimerUtilisateur(utilisateur);
            System.out.println("Utilisateur supprimé avec succès !");
        } else {
            System.out.println("Utilisateur non trouvé !");
        }
    }

    public void afficherListeUtilisateurs() {
        System.out.println("\n***** Liste des utilisateurs *****");
        ArrayList<Utilisateur> utilisateurs = bibliothèque.getListeUtilisateurs();
        for (Utilisateur utilisateur : utilisateurs) {
            System.out.println(utilisateur.getIdUtilisateur() + " - " + utilisateur.getNom());
        }
    }

    public void verifierEligibiliteUtilisateur() {
        System.out.println("\n***** Vérifier l'éligibilité d'un utilisateur *****");
        System.out.print("ID de l'utilisateur : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Utilisateur utilisateur = bibliothèque.rechercherUtilisateurParID(id);
        if (utilisateur != null) {
            if (utilisateur.nombreLivresEmpruntes() < 3) {
                System.out.println("L'utilisateur est éligible pour emprunter des livres.");
            } else {
                System.out.println("L'utilisateur a déjà emprunté le nombre maximum de livres.");
            }
        } else {
            System.out.println("Utilisateur non trouvé !");
        }
    }

    public void afficherLivresEmpruntesParUtilisateur() {
        System.out.println("\n***** Afficher les livres empruntés par un utilisateur *****");
        System.out.print("ID de l'utilisateur : ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Utilisateur utilisateur = bibliothèque.rechercherUtilisateurParID(id);
        if (utilisateur != null) {
            ArrayList<Livre> livresEmpruntes = bibliothèque.getLivresEmpruntes(utilisateur);
            if (!livresEmpruntes.isEmpty()) {
                System.out.println("Livres empruntés par " + utilisateur.getNom() + " :");
                for (Livre livre : livresEmpruntes) {
                    System.out.println(livre);
                }
            } else {
                System.out.println("Aucun livre emprunté par cet utilisateur.");
            }
        } else {
            System.out.println("Utilisateur non trouvé !");
        }
    }
}

class GestionEmprunts {
    private Bibliothèque bibliothèque;
    private Scanner scanner;

    public GestionEmprunts(Bibliothèque bibliothèque, Scanner scanner) {
        this.bibliothèque = bibliothèque;
        this.scanner = scanner;
    }

    public void afficherMenu() {
        int choix;
        do {
            System.out.println("\n***** Gestion des Emprunts *****");
            System.out.println("1. Enregistrer un emprunt");
            System.out.println("2. Enregistrer un retour");
            System.out.println("3. Retour au menu principal");
            System.out.print("Votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    enregistrerEmprunt();
                    break;
                case 2:
                    enregistrerRetour();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez choisir une option valide.");
            }
        } while (choix != 3);
    }

    public void enregistrerEmprunt() {
        System.out.println("\n***** Enregistrer un emprunt *****");
        System.out.print("ID de l'utilisateur : ");
        int idUtilisateur = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Utilisateur utilisateur = bibliothèque.rechercherUtilisateurParID(idUtilisateur);
        if (utilisateur != null) {
            System.out.println("Liste des livres disponibles :");
            ArrayList<Livre> livresDisponibles = bibliothèque.getListeLivresDisponibles();
            for (int i = 0; i < livresDisponibles.size(); i++) {
                System.out.println((i + 1) + ". " + livresDisponibles.get(i));
            }
            System.out.print("Choisissez le numéro du livre à emprunter : ");
            int choixLivre = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne
            if (choixLivre >= 1 && choixLivre <= livresDisponibles.size()) {
                Livre livreEmprunte = livresDisponibles.get(choixLivre - 1);
                bibliothèque.enregistrerEmprunt(utilisateur, livreEmprunte);
                System.out.println("Emprunt enregistré avec succès !");
            } else {
                System.out.println("Numéro de livre invalide !");
            }
        } else {
            System.out.println("Utilisateur non trouvé !");
        }
    }

    public void enregistrerRetour() {
        System.out.println("\n***** Enregistrer un retour *****");
        System.out.print("ID de l'utilisateur : ");
        int idUtilisateur = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Utilisateur utilisateur = bibliothèque.rechercherUtilisateurParID(idUtilisateur);
        if (utilisateur != null) {
            ArrayList<Livre> livresEmpruntes = bibliothèque.getLivresEmpruntes(utilisateur);
            if (!livresEmpruntes.isEmpty()) {
                System.out.println("Liste des livres empruntés par " + utilisateur.getNom() + " :");
                for (int i = 0; i < livresEmpruntes.size(); i++) {
                    System.out.println((i + 1) + ". " + livresEmpruntes.get(i));
                }
                System.out.print("Choisissez le numéro du livre à retourner : ");
                int choixLivre = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne
                if (choixLivre >= 1 && choixLivre <= livresEmpruntes.size()) {
                    Livre livreRetourne = livresEmpruntes.get(choixLivre - 1);
                    bibliothèque.enregistrerRetour(utilisateur, livreRetourne);
                    System.out.println("Retour enregistré avec succès !");
                } else {
                    System.out.println("Numéro de livre invalide !");
                }
            } else {
                System.out.println("Aucun livre emprunté par cet utilisateur.");
            }
        } else {
            System.out.println("Utilisateur non trouvé !");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Bibliothèque bibliothèque = new Bibliothèque();
        Scanner scanner = new Scanner(System.in);
        GestionLivres gestionLivres = new GestionLivres(bibliothèque, scanner);
        GestionUtilisateurs gestionUtilisateurs = new GestionUtilisateurs(bibliothèque, scanner);
        GestionEmprunts gestionEmprunts = new GestionEmprunts(bibliothèque, scanner);

        int choix;

        do {
            afficherMenuPrincipal();
            choix = scanner.nextInt();
            scanner.nextLine(); // consommer la nouvelle ligne

            switch (choix) {
                case 1:
                    gestionLivres.afficherMenu();
                    break;
                case 2:
                    gestionUtilisateurs.afficherMenu();
                    break;
                case 3:
                    gestionEmprunts.afficherMenu();
                    break;
                case 4:
                    System.out.println("Au revoir !");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez choisir une option valide.");
                    break;
            }
        } while (choix != 4);

        scanner.close();
    }

    public static void afficherMenuPrincipal() {
        System.out.println("\n***** Menu Principal *****");
        System.out.println("1. Gestion des Livres");
        System.out.println("2. Gestion des Utilisateurs");
        System.out.println("3. Gestion des Emprunts");
        System.out.println("4. Quitter");
        System.out.print("Votre choix : ");
    }
}
