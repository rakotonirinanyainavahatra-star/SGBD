import java.time.LocalDate;

public class Affichage {

    public static void main(String[] args) {

        String nomDomaine1 = "int";
        String nomDomaine2 = "VARCHAR(50)";
        String nomDomaine3 = "CHAR(5)";
        String nomDomaine4 = "DECIMAL(10,2)";
        String nomDomaine5 = "DATE";

        String nomDomaine6 = "int";
        String nomDomaine7 = "VARCHAR(50)";
        String nomDomaine8 = "CHAR(5)";
        String nomDomaine9 = "DECIMAL(10,2)";
        String nomDomaine10 = "DATE";

        String projection = "Nom, Prenom";
        String selection = "Nombre >= 20";
        String jointure = "Table.id = Table2.id";

        Domaine domaine1 = Domaine.getDomaineByName(nomDomaine1);
        Domaine domaine2 = Domaine.getDomaineByName(nomDomaine2);
        Domaine domaine3 = Domaine.getDomaineByName(nomDomaine3);
        Domaine domaine4 = Domaine.getDomaineByName(nomDomaine4);
        Domaine domaine5 = Domaine.getDomaineByName(nomDomaine5);

        Domaine domaine6 = Domaine.getDomaineByName(nomDomaine6);
        Domaine domaine7 = Domaine.getDomaineByName(nomDomaine7);
        Domaine domaine8 = Domaine.getDomaineByName(nomDomaine8);
        Domaine domaine9 = Domaine.getDomaineByName(nomDomaine9);
        Domaine domaine10 = Domaine.getDomaineByName(nomDomaine10);

        Attribut attribut1 = new Attribut("id", domaine1);
        Attribut attribut2 = new Attribut("Nom", domaine2);
        Attribut attribut3 = new Attribut("Prenom", domaine3);
        Attribut attribut4 = new Attribut("Nombre", domaine4);
        Attribut attribut5 = new Attribut("Naissance", domaine5);

        Attribut attribut6 = new Attribut("id", domaine6);
        Attribut attribut7 = new Attribut("Nom", domaine7);
        Attribut attribut8 = new Attribut("Prenom", domaine8);
        Attribut attribut9 = new Attribut("Nombre", domaine9);
        Attribut attribut10 = new Attribut("Naissance", domaine10);

        Attribut[] attributs = {attribut1, attribut2, attribut3, attribut4, attribut5};
        Attribut[] attributs2 = {attribut6, attribut7, attribut8, attribut9, attribut10};

        Object[][] valeurs = {
            {1, "Rakoto", "Jeann", null, LocalDate.parse("2002-01-10")},
            {2, "Rabe", "Marie", 20.0, LocalDate.parse("2001-05-15")},
            {3, "Andrianarivo", "Paris", 30.75, LocalDate.parse("2000-12-20")},
            {4, "Rakoto", "Jeann", 25.0, LocalDate.parse("2003-03-05")},
            {5, "Rakotomalala", "Aaron", 18.25, LocalDate.parse("2002-07-30")}
        };

        Object[][] valeurs2 = {
            {1, "Raly", "Jeann", null, LocalDate.parse("2002-01-10")},
            {2, "Ravao", "Marie", 20.0, LocalDate.parse("2001-05-15")},
            {3, "Ranavalona", "Paris", 30.75, LocalDate.parse("2000-12-20")},
            {4, "Joseph", "Jeann", 25.0, LocalDate.parse("2003-03-05")},
            {5, "Rasoa", "Aaron", 18.25, LocalDate.parse("2002-07-30")}
        };

        if (!Relation.checkValeurs(attributs, valeurs)) {
            System.out.println("Les valeurs ne correspondent pas aux types des attributs.");
        }
        else {
            Relation relation = new Relation("Table", attributs, valeurs);
            System.out.println("Structure de table: " + relation.getNom());
            relation.afficherFormeTableau();

            Relation relation2 = new Relation("Table2", attributs2, valeurs2);
            System.out.println("Structure de table: " + relation2.getNom());
            relation2.afficherFormeTableau();

            Relation projectionRelation = relation.projection(projection);

            if (projectionRelation.getAttributs().length > 0 && projectionRelation.getValeurs().length > 0) {
                System.out.println("Projection " + projectionRelation.getNom() + ":");
                projectionRelation.afficherFormeTableau();
            }

            Relation selectionRelation = relation.selection(selection);
            if (selectionRelation.getAttributs().length > 0 && selectionRelation.getValeurs().length > 0) {
                System.out.println("Sélection " + selectionRelation.getNom() + ":");
                selectionRelation.afficherFormeTableau();
            }

            Relation produitCartesienRelation = relation.produitCartesien(relation2);
            if (produitCartesienRelation.getAttributs().length > 0 && produitCartesienRelation.getValeurs().length > 0) {
                System.out.println("Produit cartésien " + produitCartesienRelation.getNom() + ":");
                produitCartesienRelation.afficherFormeTableau();
            }

            Relation jointureRelation = relation.jointure(relation2, jointure);
            if (jointureRelation.getAttributs().length > 0 && jointureRelation.getValeurs().length > 0) {
                System.out.println("Jointure " + jointureRelation.getNom() + ":");
                jointureRelation.afficherFormeTableau();
            }
        }

    }
}