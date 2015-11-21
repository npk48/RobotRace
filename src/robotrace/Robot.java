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
    
    private boolean Initialized = false;
    
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(GL2 gl,Material material
        /* add other parameters that characterize this robot */) {
        this.material = material;
        // code goes here ...
    }
    
    private void InitializeVBO(GL2 gl)
    {
        float[] vertexArray = {-0.5f,  0.5f, 0,
                                0.5f,  0.5f, 0,
                                0.5f, -0.5f, 0,
                               -0.5f, -0.5f, 0};
        Vertices = Buffers.newDirectFloatBuffer(vertexArray);

        short[] indexArray = {0, 1, 2, 0, 2, 3};
        Indices = Buffers.newDirectShortBuffer(indexArray);
        
        float[] normalArray ={0, 0, 1,
                              0, 0, 1,
                              0, 0, 1,
                              0, 0, 1};
        Normals = Buffers.newDirectFloatBuffer(normalArray);
        
        int[] temp = new int[3];
        gl.glGenBuffers(3, temp, 0);

        VertexPtr = temp[0];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, VertexPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, Vertices.capacity() * Buffers.SIZEOF_FLOAT,
                            Vertices, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);

        IndexPtr = temp[1];
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, IndexPtr);
        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, Indices.capacity() * Buffers.SIZEOF_SHORT,
                            Indices, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        NormalPtr = temp[2];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER,NormalPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, Normals.capacity() * Buffers.SIZEOF_FLOAT,
                            Normals, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        if(!Initialized)
        {
            Initialized = true;
            InitializeVBO(gl);
        }
        
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
        
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);
        //gl.glEnableClientState(gl.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(gl.GL_NORMAL_ARRAY);

        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, VertexPtr);
        gl.glVertexPointer(3, gl.GL_FLOAT, 0, 0);
        
        //gl.glBindBuffer(gl.GL_ARRAY_BUFFER, TexcoordPtr);
        //gl.glTexCoordPointer(3, gl.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, NormalPtr);
        gl.glNormalPointer(gl.GL_FLOAT, 0, 0);
        
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, IndexPtr);
        gl.glDrawElements(gl.GL_TRIANGLES, Indices.capacity(), gl.GL_UNSIGNED_SHORT, 0);

        gl.glDisableClientState(gl.GL_VERTEX_ARRAY);
        gl.glDisable(gl.GL_LIGHTING);
    }
}
