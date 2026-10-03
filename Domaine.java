/* ensemble de valeurs
*/
import java.time.LocalDate;

public class Domaine {

    private String nom;
    private Object min;
    private Object max;
    private Object[] valeurs;
    private Class classe;

    public Domaine(String nom, Object[] valeurs, Class classe) {
        this.nom = nom;
        this.valeurs = valeurs;
        this.classe = classe;
    }

    public Domaine(String nom, Object min, Object max, Class classe) {
        this.nom = nom;
        this.min = min;
        this.max = max;
        this.classe = classe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Object[] getValeurs() {
        return valeurs;
    }

    public void setValeurs(Object[] valeurs) {
        this.valeurs = valeurs;
    }

    public Object getMin() {
        return this.min;
    }

    public void setMin(Object min) {
        this.min = min;
    }

    public Object getMax() {
        return this.max;
    }

    public void setMax(Object max) {
        this.max = max;
    }

    public Class getClasse() {
        return this.classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

    public static Domaine getDomaineByName(String nomDomaine) {

        if (nomDomaine.equalsIgnoreCase("INT") || nomDomaine.equalsIgnoreCase("INTERGER")) {
            return new Domaine(nomDomaine, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.class);
        }
        else if(nomDomaine.equalsIgnoreCase("FLOAT")){
            return new Domaine(nomDomaine, -Float.MAX_VALUE, Float.MAX_VALUE, Float.class);
        }
        else if(nomDomaine.equalsIgnoreCase("DOUBLE")){
            return new Domaine(nomDomaine, -Double.MAX_VALUE, Double.MAX_VALUE, Double.class);
        }
        else if(nomDomaine.toUpperCase().trim().startsWith("VARCHAR")) {
            double taille = extraireTaille(nomDomaine);
            return new Domaine(nomDomaine, 0, taille, String.class);
        }
        else if(nomDomaine.toUpperCase().trim().startsWith("CHAR")) {
            double taille = extraireTaille(nomDomaine);
            return new Domaine(nomDomaine, taille, taille, String.class);
        }
        else if(nomDomaine.toUpperCase().trim().startsWith("DECIMAL")) {
            int[] parametre = extraireParametre(nomDomaine);
            int precision = parametre[0];
            int echelle = parametre[1];

            double max = Math.pow(10, precision - echelle) - Math.pow(10, -echelle);
            double min = -max;
            return new Domaine(nomDomaine, min, max, Double.class);
        }
        else if(nomDomaine.toUpperCase().trim().startsWith("DATE")) {
            LocalDate minDate = LocalDate.of(1, 1, 1);
            LocalDate maxDate = LocalDate.of(9999, 12, 31);
            return new Domaine(nomDomaine, minDate, maxDate, LocalDate.class);

        }
        else {
            return null;
        }
    }

    public static int extraireTaille(String nomDomaine) {

        if (nomDomaine.contains("(") && nomDomaine.contains(")")) {
            String nomType = nomDomaine.substring(0, nomDomaine.indexOf("("));
            String tailleStr = nomDomaine.substring(nomDomaine.indexOf("(") + 1, nomDomaine.indexOf(")"));
            return Integer.parseInt(tailleStr);
        }
        return 255;
    }

    public static int[] extraireParametre(String nomDomaine) {

        int[] parametre = new int[2];
        if (nomDomaine.contains("(") && nomDomaine.contains(")")) {

            String nomType = nomDomaine.substring(0, nomDomaine.indexOf("("));
            String parametreStr = nomDomaine.substring(nomDomaine.indexOf("(") + 1, nomDomaine.indexOf(")"));

            if (parametreStr.contains(",")) {
                String precisionStr = parametreStr.substring(0, parametreStr.indexOf(","));
                parametre[0] = Integer.parseInt(precisionStr);

                String echelleStr = parametreStr.substring(parametreStr.indexOf(",") + 1);
                parametre[1] = Integer.parseInt(echelleStr);

                return parametre;
            }
        }
        parametre[0] = 0;
        parametre[1] = 0;
        return parametre;
    }
}