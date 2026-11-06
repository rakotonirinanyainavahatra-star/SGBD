import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Relation {

    private String nom;
    private Attribut[] attributs;
    private Object[][] valeurs;
    private static final String[] OPERATEURS = {">=", "<=", "!=", ">", "<", "="};

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

    private static String trouverOperateur(String condition) {
        for (String op : OPERATEURS) {
            if (condition.contains(op)) {
                return op;
            }
        }
        return null;
    }

    private static boolean comparer(int cmp, String operateur) {
        switch (operateur) {
            case ">":  return cmp > 0;
            case "<":  return cmp < 0;
            case ">=": return cmp >= 0;
            case "<=": return cmp <= 0;
            case "=":  return cmp == 0;
            case "!=": return cmp != 0;
            default:   return false;
        }
    }

    public static boolean checkValeurs(Attribut[] attributs, Object[][] valeurs) {

        for(int i = 0; i < valeurs.length; i++) {
            Object[] ligne = valeurs[i];

            for(int j = 0; j < ligne.length; j++) {

                Object val = ligne[j];
                Domaine domaineTemporaire = attributs[j].getDomaine();

                if (val == null) {
                    continue;
                }

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
                
                if (val == null){
                    val = "NULL";
                }
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

    public static String[] extraireProjection(String projection, String separateur) {
        return projection.trim().split(separateur);
    }

    public Relation gestionDoublons() {

        Object[][] valeurs = this.getValeurs();
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
                            nouvellesValeurs[index] = valeurs[l];
                            index++;
                        }

                    }
                    Relation result = new Relation(this.getNom(), this.getAttributs(), nouvellesValeurs);
                    return result.gestionDoublons();
                }
            }
        }
        return this;
    }

    public Relation projection(String projection) {

        Attribut[] attributs = this.getAttributs();
        Object[][] valeurs = this.getValeurs();

        String[] tableauProjection = Relation.extraireProjection(projection, ",");

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
        Relation projectionRelation = new Relation(this.getNom(), attributsProjection, valeursProjection);
        Relation valeursSansDoublons = projectionRelation.gestionDoublons();
        return valeursSansDoublons;
    }

    public Relation selection(String condition) {

        String operateur = trouverOperateur(condition);
        if (operateur == null) {
            System.out.println("Opérateur non trouvé dans la condition : " + condition);
            return null;
        }

        int positionOperateur = condition.indexOf(operateur);
        String nomAttribut = condition.substring(0, positionOperateur).trim();
        String valeurConditionStr = condition.substring(positionOperateur + operateur.length()).trim();

        Attribut[] attributs = this.getAttributs();
        Object[][] valeurs = this.getValeurs();

        int index = -1;
        Comparable valeurCondition = null;

        for (int i = 0; i < attributs.length; i++) {

            if (attributs[i].getNom().equals(nomAttribut)) {
                
                String texte = valeurConditionStr;
                Class<?> classeAttribut = attributs[i].getDomaine().getClasse();

                if (classeAttribut == Integer.class) {
                    valeurCondition = Integer.parseInt(texte);
                } else if (classeAttribut == Float.class) {
                    valeurCondition = Float.parseFloat(texte);
                } else if (classeAttribut == Double.class) {
                    valeurCondition = Double.parseDouble(texte);
                } else if (classeAttribut == String.class) {
                    valeurCondition = texte;
                } else if (classeAttribut == LocalDate.class) {
                    valeurCondition = LocalDate.parse(texte);
                } else {
                    System.out.println("Type de données non pris en charge pour la sélection.");
                    return null;
                }
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("Attribut introuvable : " + nomAttribut);
            return null;
        }

        List<Object[]> lignesSelectionnees = new ArrayList<>();

        for (int j = 0; j < valeurs.length; j++) {
            Comparable valeurLigne = (Comparable) valeurs[j][index];

            if (valeurLigne == null) {
                continue;
            }

            if (comparer(valeurLigne.compareTo(valeurCondition), operateur)) {
                lignesSelectionnees.add(valeurs[j]);
            }
        }

        Object[][] valeursSelectionnees = lignesSelectionnees.toArray(new Object[0][]);

        return new Relation(this.getNom(), this.getAttributs(), valeursSelectionnees);
    }

    public Relation produitCartesien(Relation autreRelation) {

        Attribut[] attributs1 = this.getAttributs();
        Object[][] valeurs1 = this.getValeurs();

        Attribut[] attributs2 = autreRelation.getAttributs();
        Object[][] valeurs2 = autreRelation.getValeurs();

        Attribut[] attributsResultat = new Attribut[attributs1.length + attributs2.length];
        System.arraycopy(attributs1, 0, attributsResultat, 0, attributs1.length);
        System.arraycopy(attributs2, 0, attributsResultat, attributs1.length, attributs2.length);

        Object[][] valeursResultat = new Object[valeurs1.length * valeurs2.length][attributsResultat.length];

        int index = 0;
        for (Object[] ligne1 : valeurs1) {
            for (Object[] ligne2 : valeurs2) {
                Object[] ligneResultat = new Object[attributsResultat.length];
                System.arraycopy(ligne1, 0, ligneResultat, 0, ligne1.length);
                System.arraycopy(ligne2, 0, ligneResultat, ligne1.length, ligne2.length);
                valeursResultat[index++] = ligneResultat;
            }
        }

        return new Relation(this.getNom() + "_x_" + autreRelation.getNom(), attributsResultat, valeursResultat);
    }
}