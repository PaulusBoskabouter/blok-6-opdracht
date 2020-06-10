public class Variant implements Comparable<Variant> {

    /**
     * @author Paul Verhoeven
     * @version jan 30, 2019
     */

    private int allelId;
    private int position;
    private int pathogenicity;
    private int geneId;
    private int diseaseID;

    private String type;
    private String alternateAllele;
    private String disease;
    private String referenceAllele;
    private String chromosome;

    public Variant(int allelId, int position, int pathogenicity,
                   int geneId, int diseaseID,
                   String type, String alternateAllele, String disease,
                   String referenceAllele, String chromosome) {
        // Variant Constructor.
        this.allelId = allelId;
        this.position = position;
        this.pathogenicity = pathogenicity;
        this.geneId = geneId;
        this.diseaseID = diseaseID;
        this.type = type;
        this.alternateAllele = alternateAllele;
        this.disease = disease;
        this.referenceAllele = referenceAllele;
        this.chromosome = chromosome;
    }

    // Getters for all the Variant variables.
    public int getAllelId() {
        return allelId;
    }

    public int getPosition() {
        return position;
    }

    public int getPathogenicity() {
        return pathogenicity;
    }

    public int getGeneId() {
        return geneId;
    }

    public String getType() {
        return type;
    }

    public String getAlternateAllele() {
        return alternateAllele;
    }

    public String getDisease() {
        return disease;
    }

    public String getReferenceAllele() {
        return referenceAllele;
    }

    public String getChromosome() {
        return chromosome;
    }

    public int getDiseaseID() {
        return diseaseID;
    }
    
    /** 
     *To string method for object Variant. 
     */
    public String toString() {
        return "Variant{" +
                "allelId=" + allelId +
                ", position=" + position +
                ", pathogenicity=" + pathogenicity +
                ", geneId=" + geneId +
                ", diseaseID=" + diseaseID +
                ", type='" + type + '\'' +
                ", alternateAllele='" + alternateAllele + '\'' +
                ", disease='" + disease + '\'' +
                ", referenceAllele='" + referenceAllele + '\'' +
                ", chromosome='" + chromosome + '\'' +
                '}';
    }

    @Override
    /**
     * A short function comparing Variant objects to eachother to order the Arraylist. 
     */
    public int compareTo(Variant other) {
        int i = Integer.compare(pathogenicity, other.pathogenicity);
        if (i != 0) return i;
        i = chromosome.compareTo(other.chromosome);
        if (i != 0) return i;
        return Integer.compare(position, other.position);
    }
}

