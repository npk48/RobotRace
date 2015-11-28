package robotrace;

/**
* Materials that can be used for the robots.
*/
public enum Material {

    /** 
     * Gold material properties.
     * Modify the default values to make it look like gold.
     */
    GOLD (
        new float[] {0.24725f,0.2245f,0.0645f,1f},
        new float[] {0.34615f, 0.3143f, 0.0903f, 1.0f},
        new float[] {0.797357f, 0.723991f, 0.208006f, 1.0f},
        83.2f),

    /**
     * Silver material properties.
     * Modify the default values to make it look like silver.
     */
    SILVER (
        new float[] {0.23125f,0.23125f,0.23125f,1f},
        new float[] {0.2775f, 0.2775f, 0.2775f, 1.0f},
        new float[] {0.773911f, 0.773911f, 0.773911f, 1.0f},
        89.6f),

    /** 
     * Wood material properties.
     * Modify the default values to make it look like wood.
     */
    WOOD (
        new float[] {0.411764f,0.2f,0.0f,1f},
        new float[] {0.411764f,0.2f,0.0f,1f},
        new float[] {0.0f, 0.0f, 0.0f, 1.0f},
        83.2f),

    /**
     * Orange material properties.
     * Modify the default values to make it look like orange.
     */
    ORANGE (
        new float[] {1.0f, 0.5f,0.0f,1f},
        new float[] {1.0f, 0.5f, 0.0f, 1.0f},
        new float[] {0.6f, 0.6f, 0.5f, 1.0f},
        32f);

    float[] ambient;

    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] ambient,float[] diffuse, float[] specular, float shininess) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}
