package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import java.nio.*;
import com.jogamp.common.nio.Buffers;
import java.util.*;

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
    
    /** each part of robot**/
    class Part
    {
        public int VertexPtr;
        public FloatBuffer Vertices;
        public int IndexPtr;
        public ShortBuffer Indices;
        public int TexcoordPtr;
        public FloatBuffer Texcoords;
        public int NormalPtr;
        public FloatBuffer Normals;
        public Vector position;
        public float theta;
        public float phi;
    }
    
    // map used to hold all parts
    Map<String, Part> parts =new HashMap<String, Part>();
    
    // check if vbos are initialized, since they only need to be
    // initialized once, passing array to gpu every draw call has
    // poor efficiency
    private boolean Initialized = false;
    
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material
        /* add other parameters that characterize this robot */) {
        this.material = material;
        // code goes here ...
    }
    
    private void InitializeVBO(GL2 gl)
    {
        // initialize all vertex,index,normal for each part
        InitilizeVBO(gl,new Vector(-4,-4, 42),new Vector(8,8,8),"head");
        InitilizeVBO(gl,new Vector( 1, 4, 46),new Vector(3,0.1,1.4),"leftEye");
        InitilizeVBO(gl,new Vector(-4, 4, 46),new Vector(3,0.1,1.4),"rightEye");
        InitilizeVBO(gl,new Vector(-4,-2, 30),new Vector(8,4,12),"torso");
        InitilizeVBO(gl,new Vector(-6,-1, 14),new Vector(2,2,30),"rightArm");
        InitilizeVBO(gl,new Vector( 4,-1, 14),new Vector(2,2,30),"leftArm");
        InitilizeVBO(gl,new Vector(-3,-1, 2),new Vector(2,2,30),"rightLeg");
        InitilizeVBO(gl,new Vector( 1,-1, 2),new Vector(2,2,30),"leftLeg");       
        
        // initialize stick figure
        InitilizeVBO(gl,new Vector(-4,-2, 30),new Vector(8,2,2),"stick figure torso");
        InitilizeVBO(gl,new Vector(-6,-1, 14),new Vector(2,2,30),"stick figure rightArm");
        InitilizeVBO(gl,new Vector( 4,-1, 14),new Vector(2,2,30),"stick figure leftArm");
        InitilizeVBO(gl,new Vector(-3,-1, 2),new Vector(2,2,30),"stick figure rightLeg");
        InitilizeVBO(gl,new Vector( 1,-1, 2),new Vector(2,2,30),"stick figure leftLeg");      
    }
    
    public void updateAnimation(float tAnim)
    {
        // reserved for round 2 animimation
        //parts.get("leftArm").phi=Math.sin(tAnim);
        //parts.get("rightArm").phi=0;
        //parts.get("leftLeg").phi=0;
        //parts.get("rightLeg").phi=0;
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        // check if initialized and build the vbos
        if(!Initialized)
        {
            Initialized = true;
            InitializeVBO(gl);
        }       
        
        // reserved for round 2 animation
        updateAnimation(tAnim);
              
        // enable array mode
        gl.glEnableClientState(gl.GL_VERTEX_ARRAY);
        // reserved for round 2 texture
        //gl.glEnableClientState(gl.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(gl.GL_NORMAL_ARRAY);
        
        gl.glPushMatrix();
        
        // apply direction
        double angle =Math.toDegrees(Math.atan((direction.x - position.x)/(direction.y-position.y)));
        if(Double.isNaN(angle))
            angle = -90;
        gl.glRotatef((float)angle, 0f, 0f, 1f);
        //gl.glRotatef(30, 0f, 0f, 1f);
        gl.glTranslated(position.x, position.y, position.z);
        
        if(stickFigure)
        {
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, new float[]{0.0f,0.0f,0.0f,1.0f}, 0);
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, new float[]{0.0f,0.0f,0.0f,1.0f}, 0);
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, new float[]{0.0f,0.0f,0.0f,1.0f}, 0);
            gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, 0.0f);
            draw(gl,"stick figure torso");
            draw(gl,"stick figure leftArm");
            draw(gl,"stick figure rightArm");
            draw(gl,"stick figure leftLeg");
            draw(gl,"stick figure rightLeg"); 
        } else
        {
            // apply configured material
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, material.diffuse, 0);
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, material.ambient, 0);
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, material.specular, 0);
            gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, material.shininess);
            draw(gl,"head");
            draw(gl,"torso");
            draw(gl,"leftArm");
            draw(gl,"rightArm");
            draw(gl,"leftLeg");
            draw(gl,"rightLeg"); 
            
            // apply a different material for eye
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_DIFFUSE, new float[]{1.0f,0.0f,1.0f,0.5f}, 0);
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT, new float[]{1.0f,0.0f,1.0f,0.5f}, 0);
            gl.glMaterialfv(gl.GL_FRONT, gl.GL_SPECULAR, new float[]{0.0f,0.0f,0.0f,1.0f}, 0);
            gl.glMaterialf(gl.GL_FRONT, gl.GL_SHININESS, 90.0f);
            draw(gl,"leftEye");
            draw(gl,"rightEye");    
        }
        
        gl.glPopMatrix();
        
        // exit array mode
        gl.glDisableClientState(gl.GL_NORMAL_ARRAY);
        gl.glDisableClientState(gl.GL_VERTEX_ARRAY);      
    }
    
    
    // draw function for each part
    private void draw(GL2 gl,String part)
    {
        gl.glPushMatrix();
        Part target = parts.get(part);
        // reserved for round2 animation
        //gl.glRotatef(target.theta, 0f, 0f, 1f);
        //gl.glRotatef(target.phi,)
        // translate part to relative position
        gl.glTranslated(target.position.x, target.position.y, target.position.z);
        
        // bind array and draw
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, target.VertexPtr);
        gl.glVertexPointer(3, gl.GL_FLOAT, 0, 0);       
        //gl.glBindBuffer(gl.GL_ARRAY_BUFFER, target.TexcoordPtr);
        //gl.glTexCoordPointer(3, gl.GL_FLOAT, 0, 0);       
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, target.NormalPtr);
        gl.glNormalPointer(gl.GL_FLOAT, 0, 0);      
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, target.IndexPtr);
        gl.glDrawElements(gl.GL_TRIANGLES, target.Indices.capacity(), gl.GL_UNSIGNED_SHORT, 0);
        gl.glPopMatrix();
    }
    
    // used to initialize a box shaped vbo
    private void InitilizeVBO(GL2 gl,Vector position,Vector size,String part)
    {
        Part target = new Part();
        target.position = position; 
        // set the scalling to 1 meter divide by 50
        float scale = 50.0f;
        target.position.x /= scale;
        target.position.y /= scale;
        target.position.z /= scale;
        
        // duplicate vertex in order to specify normal per face
        float[] vertex = {
            0.0f,                   0.0f,                   0.0f,
            0.0f,                   ((float)size.y)/scale,  0.0f,
            ((float)size.x)/scale,  ((float)size.y)/scale,  0.0f, 
            ((float)size.x)/scale,  0.0f,                   0.0f,
            // buttom face
            0.0f,                   0.0f,                   ((float)size.z)/scale,
            0.0f,                   ((float)size.y)/scale,  ((float)size.z)/scale,
            ((float)size.x)/scale,  ((float)size.y)/scale,  ((float)size.z)/scale, 
            ((float)size.x)/scale,  0.0f,                   ((float)size.z)/scale,
            // top face
            0.0f,                   ((float)size.y)/scale,  0.0f,
            ((float)size.x)/scale,  ((float)size.y)/scale,  0.0f, 
            0.0f,                   ((float)size.y)/scale,  ((float)size.z)/scale,
            ((float)size.x)/scale,  ((float)size.y)/scale,  ((float)size.z)/scale, 
            // front face
            0.0f,                   0.0f,                   0.0f,
            ((float)size.x)/scale,  0.0f,                   0.0f,
            0.0f,                   0.0f,                   ((float)size.z)/scale,
            ((float)size.x)/scale,  0.0f,                   ((float)size.z)/scale,
            // back face
            0.0f,                   0.0f,                   0.0f,
            0.0f,                   ((float)size.y)/scale,  0.0f,
            0.0f,                   0.0f,                   ((float)size.z)/scale,
            0.0f,                   ((float)size.y)/scale,  ((float)size.z)/scale,
            // left face
            ((float)size.x)/scale,  ((float)size.y)/scale,  0.0f, 
            ((float)size.x)/scale,  0.0f,                   0.0f,
            ((float)size.x)/scale,  ((float)size.y)/scale,  ((float)size.z)/scale, 
            ((float)size.x)/scale,  0.0f,                   ((float)size.z)/scale
            // right face          
        };
        short[] index = {
            0,1,2,
            0,3,2,
            // buttom face
            4,5,6,
            4,7,6,
            // top face
            8,10,11,
            8,9,11,
            // front face
            12,14,15,
            12,13,15,
            // back face
            16,18,19,
            16,17,19,
            // left face
            20,22,23,
            20,21,23
            // right face
        };
        float[] normal = {
           0.0f, 0.0f,-1.0f,
           0.0f, 0.0f,-1.0f,
           0.0f, 0.0f,-1.0f,
           0.0f, 0.0f,-1.0f,
          // buttom
           0.0f, 0.0f, 1.0f,
           0.0f, 0.0f, 1.0f,
           0.0f, 0.0f, 1.0f,
           0.0f, 0.0f, 1.0f,
          // top
           0.0f, 1.0f, 0.0f,
           0.0f, 1.0f, 0.0f,
           0.0f, 1.0f, 0.0f,
           0.0f, 1.0f, 0.0f,
          // front
           0.0f,-1.0f, 0.0f,
           0.0f,-1.0f, 0.0f,
           0.0f,-1.0f, 0.0f,
           0.0f,-1.0f, 0.0f,
          // back
          -1.0f, 0.0f, 0.0f,
          -1.0f, 0.0f, 0.0f,
          -1.0f, 0.0f, 0.0f,
          -1.0f, 0.0f, 0.0f,
          // left
           1.0f, 0.0f, 0.0f,
           1.0f, 0.0f, 0.0f,
           1.0f, 0.0f, 0.0f,
           1.0f, 0.0f, 0.0f
          // right  
        };
        
        // data conversion to buffers
        target.Vertices = Buffers.newDirectFloatBuffer(vertex);
        target.Indices = Buffers.newDirectShortBuffer(index);
        target.Normals = Buffers.newDirectFloatBuffer(normal);   
        
        // use a temp variable to hold the pointers in gpu   
        int[] temp = new int[3];
        gl.glGenBuffers(3, temp, 0);
        target.VertexPtr = temp[0];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, target.VertexPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, target.Vertices.capacity() * Buffers.SIZEOF_FLOAT,
                            target.Vertices, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);
        target.IndexPtr = temp[1];
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, target.IndexPtr);
        gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, target.Indices.capacity() * Buffers.SIZEOF_SHORT,
                            target.Indices, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, 0);       
        target.NormalPtr = temp[2];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER,target.NormalPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, target.Normals.capacity() * Buffers.SIZEOF_FLOAT,
                            target.Normals, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);    
        
        // add the part to map for later usage   
        parts.put(part,target);
    }
}
