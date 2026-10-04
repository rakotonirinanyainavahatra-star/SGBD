public class Relation {

    private String nom;
    private Attribut[] attributs;
    private Object[][] valeurs;

    public Relation (String nom, Attribut[] attributs, Object[][] valeurs) {
        this.nom = nom;
        this.attributs = attributs;
        this.valeurs = valeurs;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Attribut[] getAttributs() {
        return this.attributs;
    }

    public void setAttributs(Attribut[] attributs) {
        this.attributs = attributs;
    }

    public Object[][] getValeurs() {
        return this.valeurs;
    }

    public void setValeurs(Object[][] valeurs) {
        this.valeurs = valeurs;
    }

    public static boolean checkValeurs(Attribut[] attributs, Object[][] valeurs) {

        for(int i = 0; i < valeurs.length; i++) {
            Object[] ligne = valeurs[i];

            for(int j = 0; j < ligne.length; j++) {

                Object val = ligne[j];
                Domaine domaineTemporaire = attributs[j].getDomaine();

                if(val.getClass() != domaineTemporaire.getClasse() || val == null) {
                    System.out.println("La valeur " + val + " n'est pas du type attendu pour l'attribut " + attributs[j].getNom());
                    return false;
                }
                else if (val instanceof Number) {
                    double v = ((Number) val).doubleValue();
                    double min = ((Number) domaineTemporaire.getMin()).doubleValue();
                    double max = ((Number) domaineTemporaire.getMax()).doubleValue();

                    if (v < min || v > max) {
                        System.out.println("La valeur " + v + " n'est pas dans l'intervalle [" + min + ", " + max + "]");
                        return false;
                    }
                }
                else if (val instanceof String) {
                    int longueur = ((String) val).length();
                    int minTaille = ((Number) domaineTemporaire.getMin()).intValue();
                    int maxTaille = ((Number) domaineTemporaire.getMax()).intValue();

                    if (longueur < minTaille || longueur > maxTaille) {
                        System.out.println("La valeur " + val + " n'est pas dans l'intervalle [" + minTaille + ", " + maxTaille + "]");
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void afficherFormeTableau() {

        Attribut[] attributs = this.getAttributs();
        Object[][] valeurs = this.getValeurs();

        StringBuilder bordure = new StringBuilder("+");
        StringBuilder entete = new StringBuilder("|");
        StringBuilder ligneTexte = new StringBuilder();

        for(Attribut attr: attributs) {
            bordure.append("-----------------+");
            entete.append(String.format(" %-15s |", attr.getNom()));
        }

        for(Object[] ligne: valeurs) {
            ligneTexte.append("|");
            for(Object val: ligne) {
                ligneTexte.append(String.format(" %-15s |", val));
            }
            ligneTexte.append("\n");
        }

        System.out.println(bordure.toString());
        System.out.println(entete.toString());
        System.out.println(bordure.toString());
        System.out.print(ligneTexte.toString());
        System.out.println(bordure.toString());

    }

    public static String[] extraireProjection(String projection) {
        return projection.trim().split(",");
    }

    public static Object[][] gestionDoublons(Object[][] valeurs) {

        for (int i = 0; i < valeurs.length; i++) {

            for (int j = i + 1; j < valeurs.length; j++) {

                boolean doublon = true;
                Object[] ligne = valeurs[i];

                for (int k = 0; k < ligne.length; k++) {

                    if (!valeurs[i][k].equals(valeurs[j][k])) {
                        doublon = false;
                        break;
                    }

                }
                if (doublon) {

                    Object[][] nouvellesValeurs = new Object[valeurs.length - 1][valeurs[i].length];
                    int index = 0;

                    for (int l = 0; l < valeurs.length; l++) {

                        if (l != j) {
                            nouvellesValeurs[index++] = valeurs[l];
                        }

                    }
                    return gestionDoublons(nouvellesValeurs);
                }
            }
        }
        return valeurs;
    }

    public static Relation projection(Relation relation, String projection) {

        Attribut[] attributs = relation.getAttributs();
        Object[][] valeurs = relation.getValeurs();

        String[] tableauProjection = Relation.extraireProjection(projection);

        Attribut[] attributsProjection = new Attribut[tableauProjection.length];
        Object[][] valeursProjection = new Object[valeurs.length][tableauProjection.length];

        for (int k = 0; k < tableauProjection.length; k++) {

            String nomColonne = tableauProjection[k];
            for (int i = 0; i < attributs.length; i++) {

                if (attributs[i].getNom().equals(nomColonne.trim())) {
                    attributsProjection[k] = attributs[i];

                    for (int j = 0; j < valeurs.length; j++) {
                        valeursProjection[j][k] = valeurs[j][i];
                    }
                    break;
                }
            }
        }
        Object[][] valeursSansDoublons = Relation.gestionDoublons(valeursProjection);
        return new Relation(relation.getNom(), attributsProjection, valeursSansDoublons);
    }
}