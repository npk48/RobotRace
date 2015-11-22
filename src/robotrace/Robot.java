package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import java.nio.*;
import com.jogamp.common.nio.Buffers;

/**
* Represents a Robot, to be implemented according to the Assignments.
*/
class Robot {
    
    /** The position of the robot. */
    public Vector position = new Vector(0, 0, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;
    
    /** Model Vertex, Index, Texture Coord */
    private int VertexPtr;
    private FloatBuffer Vertices;
    private int IndexPtr;
    private ShortBuffer Indices;
    private int TexcoordPtr;
    private FloatBuffer Texcoords;
    private int NormalPtr;
    private FloatBuffer Normals;
    
    /*Decleration variables of head*/
    private int HeadVertexPtr;
    private FloatBuffer HeadVertices;
    private int HeadIndexPtr;
    private ShortBuffer HeadIndices;
    private int HeadTexcoordPtr;
    private FloatBuffer HeadTexcoords;
    private int HeadNormalPtr;
    private FloatBuffer HeadNormals;
    
    /*Declearation variables of torso*/
    private int TorsoVertexPtr;
    private FloatBuffer TorsoVertices;
    private int TorsoIndexPtr;
    private ShortBuffer TorsoIndices;
    private int TorsoTexcoordPtr;
    private FloatBuffer TorsoTexcoords;
    private int TorsoNormalPtr;
    private FloatBuffer TorsoNormals;
    
    /*Declearation variables of left arm*/
    private int LeftArmVertexPtr;
    private FloatBuffer LeftArmVertices;
    private int LeftArmIndexPtr;
    private ShortBuffer LeftArmIndices;
    private int LeftArmTexcoordPtr;
    private FloatBuffer LeftArmTexcoords;
    private int LeftArmNormalPtr;
    private FloatBuffer LeftArmNormals;
    
    /*Declearation variables of right arm*/
    private int RightArmVertexPtr;
    private FloatBuffer RightArmVertices;
    private int RightArmIndexPtr;
    private ShortBuffer RightArmIndices;
    private int RightArmTexcoordPtr;
    private FloatBuffer RightArmTexcoords;
    private int RightArmNormalPtr;
    private FloatBuffer RightArmNormals;
    
    /*Declearation variables of left leg*/
    private int LeftLegVertexPtr;
    private FloatBuffer LeftLegVertices;
    private int LeftLegIndexPtr;
    private ShortBuffer LeftLegIndices;
    private int LeftLegTexcoordPtr;
    private FloatBuffer LeftLegTexcoords;
    private int LeftLegNormalPtr;
    private FloatBuffer LeftLegNormals;
    
    /*Declearation variables of right leg*/
    private int RightLegVertexPtr;
    private FloatBuffer RightLegVertices;
    private int RightLegIndexPtr;
    private ShortBuffer RightLegIndices;
    private int RightLegTexcoordPtr;
    private FloatBuffer RightLegTexcoords;
    private int RightLegNormalPtr;
    private FloatBuffer RightLegNormals;
    
    
    
    private boolean Initialized = false;
    
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material
        /* add other parameters that characterize this robot */) {
        this.material = material;
        // code goes here ...
    }
    
    /*Determine points of VBO sample square*/
//    private void InitializeVBO(GL2 gl)
//    {
//        float[] vertexArray = {-0.5f,  0.5f, 0,
//                                0.5f,  0.5f, 0,
//                                0.5f, -0.5f, 0,
//                               -0.5f, -0.5f, 0};
//        Vertices = Buffers.newDirectFloatBuffer(vertexArray);
//
//        short[] indexArray = {0, 1, 2, 0, 2, 3};
//        Indices = Buffers.newDirectShortBuffer(indexArray);
//        
//        float[] normalArray ={0, 0, 1,
//                              0, 0, 1,
//                              0, 0, 1,
//                              0, 0, 1};
//        Normals = Buffers.newDirectFloatBuffer(normalArray);
//        
//        int[] temp = new int[3];
//        gl.glGenBuffers(3, temp, 0);
//
//        VertexPtr = temp[0];
//        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, VertexPtr);
//        gl.glBufferData(gl.GL_ARRAY_BUFFER, Vertices.capacity() * Buffers.SIZEOF_FLOAT,
//                            Vertices, gl.GL_STATIC_DRAW);
//        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
//
//        IndexPtr = temp[1];
//        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, IndexPtr);
//        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, Indices.capacity() * Buffers.SIZEOF_SHORT,
//                            Indices, gl.GL_STATIC_DRAW);
//        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, 0);
//        
//        NormalPtr = temp[2];
//        gl.glBindBuffer(gl.GL_ARRAY_BUFFER,NormalPtr);
//        gl.glBufferData(gl.GL_ARRAY_BUFFER, Normals.capacity() * Buffers.SIZEOF_FLOAT,
//                            Normals, gl.GL_STATIC_DRAW);
//        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
//    }
    
    /*Determine points of Head*/
    private void Head(GL2 gl)
    {
        float[] vertexArray = {-0.05f,  0.05f, 1f,
                                0.05f,  0.05f, 1f,
                                0.05f, -0.05f, 1f,
                                -0.05f, -0.05f, 1f,
                                -0.05f, 0.05f, 0.9f,
                                0.05f, 0.05f, 0.9f,
                                0.05f, -0.05f, 0.9f,
                                -0.05f, -0.05f, 0.9f};
        HeadVertices = Buffers.newDirectFloatBuffer(vertexArray);

        short[] indexArray = {0, 1, 2, 
                              0, 2, 3, 
                              0, 5, 1, 
                              0, 5, 4, 
                              1, 6, 2, 
                              1, 6, 5, 
                              2, 7, 3, 
                              2, 7, 6, 
                              3, 4, 0, 
                              3, 4, 7,
                              4, 5, 6, 
                              4, 6, 7};
        HeadIndices = Buffers.newDirectShortBuffer(indexArray);
        
        float[] normalArray ={0, 0, 1,
                              0, 0, 1,
                              0, 0, 1,
                              0, 0, 1,
                              0, 0, 1, 
                              0, 0, 1,
                              0, 0, 1, 
                              0, 0, 1};
        HeadNormals = Buffers.newDirectFloatBuffer(normalArray);
        
        int[] temp = new int[3];
        gl.glGenBuffers(3, temp, 0);

        HeadVertexPtr = temp[0];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, HeadVertexPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, HeadVertices.capacity() * Buffers.SIZEOF_FLOAT,
                            HeadVertices, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

        HeadIndexPtr = temp[1];
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, HeadIndexPtr);
        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, HeadIndices.capacity() * Buffers.SIZEOF_SHORT,
                            HeadIndices, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        HeadNormalPtr = temp[2];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER,HeadNormalPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, HeadNormals.capacity() * Buffers.SIZEOF_FLOAT,
                            HeadNormals, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
    }
    
    /*Determine points of torso*/
    private void Torso(GL2 gl)
    {
        float[] vertexArray = {-0.2f, 0.06f, 0.89f, 
                                0.2f, 0.06f, 0.89f,
                                0.2f, -0.06f, 0.89f,
                                -0.2f, -0.06f, 0.89f,
                                -0.2f, 0.06f, 0.55f,
                                0.2f, 0.06f, 0.55f, 
                                0.2f, -0.06f, 0.55f,
                                -0.2f, -0.06f, 0.55f};
        TorsoVertices = Buffers.newDirectFloatBuffer(vertexArray);

        short[] indexArray = {0, 1, 2, 
                              0, 2, 3,
                              0, 5, 1, 
                              0, 5, 4, 
                              1, 6, 2, 
                              1, 6, 5, 
                              2, 7, 3, 
                              2, 7, 6, 
                              3, 4, 0, 
                              3, 4, 7,
                              4, 5, 6, 
                              4, 6, 7};
        TorsoIndices = Buffers.newDirectShortBuffer(indexArray);
        
        float[] normalArray ={0, 0, 1,
                              0, 0, 1,
                              0, 0, 1, 
                              0, 0, 1,
                              0, 0, 1,
                              0, 0, 1,
                              0, 0, 1,
                              0, 0, 1};
        TorsoNormals = Buffers.newDirectFloatBuffer(normalArray);
        
        int[] temp = new int[3];
        gl.glGenBuffers(3, temp, 0);

        TorsoVertexPtr = temp[0];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, TorsoVertexPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, TorsoVertices.capacity() * Buffers.SIZEOF_FLOAT,
                            TorsoVertices, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

        TorsoIndexPtr = temp[1];
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, TorsoIndexPtr);
        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, TorsoIndices.capacity() * Buffers.SIZEOF_SHORT,
                            TorsoIndices, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        TorsoNormalPtr = temp[2];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER,TorsoNormalPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, TorsoNormals.capacity() * Buffers.SIZEOF_FLOAT,
                            TorsoNormals, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        /*Ptrs sample VBO*/
//        if(!Initialized)
//        {
//            Initialized = true;
//            InitializeVBO(gl);
//        }
        
        /*Ptrs head*/
        Head(gl);
        
        /*Ptrs torso*/
        Torso(gl);
        
        /*Sheding function*/ 
        sheding(gl);
        
        /*Enable Draw Client State*/
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);
        //gl.glEnableClientState(gl.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(gl.GL_NORMAL_ARRAY);
        
        /*Draw VBO sample square*/
//        drawVBOSample(gl);
        
        /*Draw Head*/
        drawHead(gl);
        
        /*Draw Torso*/
        drawTorso(gl);

        /*Finish drawing, disable client state*/
        gl.glDisableClientState(gl.GL_VERTEX_ARRAY);
        gl.glDisable(gl.GL_LIGHTING);
    }
    
    private void sheding(GL2 gl){
        gl.glEnable(gl.GL_LIGHTING);
        gl.glEnable(gl.GL_LIGHT0);
        float lightPos[] = {2.0f,0.0f,3.0f,0.0f};
        float whiteColor[] = {1.0f, 1.0f, 1.0f, 1.0f}; 
        float pinkColor[]  = {1.0f, 0.5f, 0.5f, 1.0f};
        float greyColor[]  = {0.3f, 0.3f, 0.3f, 1.0f};
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_POSITION,lightPos,0);
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_DIFFUSE,whiteColor,0);
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_AMBIENT,pinkColor,0);
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_SPECULAR,greyColor,0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, pinkColor, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, greyColor, 0);
        gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, whiteColor, 0);
        gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, 25.0f);
    }
    
    /*Function to draw the VBO sample square*/
//    private void drawVBOSample(GL2 gl){
//        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, VertexPtr);
//        gl.glVertexPointer(3, gl.GL_FLOAT, 0, 0);
//        
//        //gl.glBindBuffer(gl.GL_ARRAY_BUFFER, TexcoordPtr);
//        //gl.glTexCoordPointer(3, gl.GL_FLOAT, 0, 0);
//        
//        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, NormalPtr);
//        gl.glNormalPointer(gl.GL_FLOAT, 0, 0);
//        
//        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, IndexPtr);
//        gl.glDrawElements(gl.GL_TRIANGLES, Indices.capacity(), gl.GL_UNSIGNED_SHORT, 0);
//    }
    
    /*Function to draw head of the robot*/
    private void drawHead(GL2 gl){
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, HeadVertexPtr);
        gl.glVertexPointer(3, gl.GL_FLOAT, 0, 0);
        
        //gl.glBindBuffer(gl.GL_ARRAY_BUFFER, HeadTexcoordPtr);
        //gl.glTexCoordPointer(3, gl.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, HeadNormalPtr);
        gl.glNormalPointer(gl.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, HeadIndexPtr);
        gl.glDrawElements(gl.GL_TRIANGLES, HeadIndices.capacity(), gl.GL_UNSIGNED_SHORT, 0);
    }
    
    /*Function to draw torso of the robot*/
    private void drawTorso(GL2 gl){
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, TorsoVertexPtr);
        gl.glVertexPointer(3, gl.GL_FLOAT, 0, 0);
        
        //gl.glBindBuffer(gl.GL_ARRAY_BUFFER, TorsoTexcoordPtr);
        //gl.glTexCoordPointer(3, gl.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, TorsoNormalPtr);
        gl.glNormalPointer(gl.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, TorsoIndexPtr);
        gl.glDrawElements(gl.GL_TRIANGLES, HeadIndices.capacity(), gl.GL_UNSIGNED_SHORT, 0);
    }
}
