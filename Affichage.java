import java.time.LocalDate;

public class Affichage {

    public static void main(String[] args) {

        String nomDomaine1 = "int";
        String nomDomaine2 = "VARCHAR(50)";
        String nomDomaine3 = "CHAR(5)";
        String nomDomaine4 = "DECIMAL(10,2)";
        String nomDomaine5 = "DATE";

        String projection = "Nom";

        Domaine domaine1 = Domaine.getDomaineByName(nomDomaine1);
        Domaine domaine2 = Domaine.getDomaineByName(nomDomaine2);
        Domaine domaine3 = Domaine.getDomaineByName(nomDomaine3);
        Domaine domaine4 = Domaine.getDomaineByName(nomDomaine4);
        Domaine domaine5 = Domaine.getDomaineByName(nomDomaine5);

        Attribut attribut1 = new Attribut("id", domaine1);
        Attribut attribut2 = new Attribut("Nom", domaine2);
        Attribut attribut3 = new Attribut("Prenom", domaine3);
        Attribut attribut4 = new Attribut("Nombre", domaine4);
        Attribut attribut5 = new Attribut("Naissance", domaine5);

        Attribut[] attributs = {attribut1, attribut2, attribut3, attribut4, attribut5};

        Object[][] valeurs = {
            {1, "Rakoto", "Jeann", 15.5, LocalDate.parse("2002-01-10")},
            {2, "Rabe", "Marie", 20.0, LocalDate.parse("2001-05-15")},
            {3, "Andrianarivo", "Paris", 30.75, LocalDate.parse("2000-12-20")},
            {4, "Rasoa", "Lucie", 25.0, LocalDate.parse("2003-03-05")},
            {5, "Rakotomalala", "Aaron", 18.25, LocalDate.parse("2002-07-30")}
        };

        if (!Relation.checkValeurs(attributs, valeurs)) {
            System.out.println("Les valeurs ne correspondent pas aux types des attributs.");
        }
        else {
            Relation relation = new Relation("Table", attributs, valeurs);
            System.out.println("Structure de table: " + relation.getNom());
            relation.afficherFormeTableau();

            Relation projectionRelation = Relation.getRelationByName(relation, projection);

            if (projectionRelation.getAttributs().length > 0 && projectionRelation.getValeurs().length > 0) {
                projectionRelation.afficherFormeTableau();
            }
        }

    }
}