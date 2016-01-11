package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import java.nio.*;
import com.jogamp.common.nio.Buffers;
import java.util.*;
import com.jogamp.opengl.util.texture.*;
import java.io.File;

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
    
    public float t = 0;
    
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
        public boolean textured = false;
        public Texture tex;
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
    
    public Texture loadTexture(String file,GL2 gl) 
    {
        Texture result = null;
        try {
            // Try to load from local folder.
            result = TextureIO.newTexture(new File(file), false);
        } catch(Exception e1) {
            // Try to load from /src folder instead.
            try {
                result = TextureIO.newTexture(new File("src/robotrace/" + file), false);
            } catch(Exception e2) {              
            }
        }
            
        if(result != null) {
            System.out.println("Loaded " + file);
            result.enable(gl);
        }
        return result;
    }
    
    private void InitializeVBO(GL2 gl)
    {
        // initialize all vertex,index,normal for each part
        InitilizeVBO(gl,new Vector(-4,-4, 42),new Vector(8,8,8),new Vector(0,0,0),"head");
        InitilizeVBO(gl,new Vector( 1, 4, 46),new Vector(3,0.1,1.4),new Vector(0,0,0),"leftEye");
        InitilizeVBO(gl,new Vector(-4, 4, 46),new Vector(3,0.1,1.4),new Vector(0,0,0),"rightEye");
        InitilizeVBO(gl,new Vector(-4,-2, 30),new Vector(8,4,12),new Vector(0,0,0),"torso");    
        
        InitilizeVBO(gl,new Vector(-6,-1, 29+14),new Vector(2,2,30),new Vector(0,0,-29),"rightArm");
        InitilizeVBO(gl,new Vector( 4,-1, 29+14),new Vector(2,2,30),new Vector(0,0,-29),"leftArm");
        InitilizeVBO(gl,new Vector(-3,-1, 30+2),new Vector(2,2,30),new Vector(0,0,-30),"rightLeg");
        InitilizeVBO(gl,new Vector( 1,-1, 30+2),new Vector(2,2,30),new Vector(0,0,-30),"leftLeg");   
        
        parts.get("torso").textured = true;
        parts.get("torso").tex = loadTexture("enderman.jpg",gl);
        parts.get("head").textured = true;
        parts.get("head").tex = loadTexture("enderman.jpg",gl);
        parts.get("rightArm").textured = true;
        parts.get("rightArm").tex = loadTexture("enderman.jpg",gl);
        parts.get("leftArm").textured = true;
        parts.get("leftArm").tex = loadTexture("enderman.jpg",gl);
        parts.get("rightLeg").textured = true;
        parts.get("rightLeg").tex = loadTexture("enderman.jpg",gl);
        parts.get("leftLeg").textured = true;
        parts.get("leftLeg").tex = loadTexture("enderman.jpg",gl);
        
        
        // initialize stick figure
        InitilizeVBO(gl,new Vector(-4,-2, 30),new Vector(8,2,2),new Vector(0,0,0),"stick figure torso");
        InitilizeVBO(gl,new Vector(-6,-1, 14),new Vector(2,2,30),new Vector(0,0,0),"stick figure rightArm");
        InitilizeVBO(gl,new Vector( 4,-1, 14),new Vector(2,2,30),new Vector(0,0,0),"stick figure leftArm");
        InitilizeVBO(gl,new Vector(-3,-1, 2),new Vector(2,2,30),new Vector(0,0,0),"stick figure rightLeg");
        InitilizeVBO(gl,new Vector( 1,-1, 2),new Vector(2,2,30),new Vector(0,0,0),"stick figure leftLeg");      
    }
    
    public void updateAnimation(float tAnim)
    {
        // reserved for round 2 animimation
        parts.get("leftArm").phi=-30 * (float)Math.cos(2 * Math.PI * tAnim);
        parts.get("rightArm").phi=30 * (float)Math.cos(2 * Math.PI * tAnim);
        parts.get("leftLeg").phi=30 * (float)Math.sin(2 * Math.PI * tAnim);
        parts.get("rightLeg").phi=-30 * (float)Math.sin(2 * Math.PI * tAnim);
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
        gl.glEnableClientState(gl.GL_TEXTURE_COORD_ARRAY);
        gl.glEnableClientState(gl.GL_NORMAL_ARRAY);
        
        gl.glPushMatrix();
        
        // apply direction
        
        //gl.glRotatef(30, 0f, 0f, 1f);
        gl.glTranslated(position.x, position.y, position.z);
        double angle =-Math.toDegrees(Math.atan(direction.x/direction.y));
        //System.out.println("Angle " + String.format("%f", angle));
        if( direction.y>0)
            angle = 180+angle;
        gl.glRotatef((float)angle+180, 0f, 0f, 1f);
        
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
        gl.glDisableClientState(gl.GL_TEXTURE_COORD_ARRAY);
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
        gl.glRotatef(target.phi, 1f, 0f, 0f);
        
        // bind array and draw
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, target.VertexPtr);
        gl.glVertexPointer(3, gl.GL_FLOAT, 0, 0);    
        if(target.textured)
        {
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, target.TexcoordPtr);
            gl.glTexCoordPointer(3, gl.GL_FLOAT, 0, 0);  
            target.tex.enable(gl);
            target.tex.bind(gl);
        }   
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, target.NormalPtr);
        gl.glNormalPointer(gl.GL_FLOAT, 0, 0);      
        gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, target.IndexPtr);
        gl.glDrawElements(gl.GL_TRIANGLES, target.Indices.capacity(), gl.GL_UNSIGNED_SHORT, 0);
        if(target.textured)
        {
            target.tex.disable(gl);
        }
        gl.glPopMatrix();
    }
    
    // used to initialize a box shaped vbo
    private void InitilizeVBO(GL2 gl,Vector position,Vector size,Vector offset,String part)
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
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+0.0f,
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+0.0f,
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+0.0f, 
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+0.0f,
            // buttom face
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+((float)size.z)/scale,
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+((float)size.z)/scale,
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+((float)size.z)/scale, 
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+((float)size.z)/scale,
            // top face
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+0.0f,
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+0.0f, 
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+((float)size.z)/scale,
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+((float)size.z)/scale, 
            // front face
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+0.0f,
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+0.0f,
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+((float)size.z)/scale,
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+((float)size.z)/scale,
            // back face
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+0.0f,
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+0.0f,
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+((float)size.z)/scale,
            ((float)offset.x)/scale+0.0f,                   ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+((float)size.z)/scale,
            // left face
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+0.0f, 
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+0.0f,
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+((float)size.y)/scale,  ((float)offset.z)/scale+((float)size.z)/scale, 
            ((float)offset.x)/scale+((float)size.x)/scale,  ((float)offset.y)/scale+0.0f,                   ((float)offset.z)/scale+((float)size.z)/scale
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
        
        float[] uv =
        {
            0.0f,0.0f,
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,1.0f,
            // buttom face
            0.0f,0.0f,
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,1.0f,
            // top face
            0.0f,0.0f,
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,1.0f,
            // front face
            0.0f,0.0f,
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,1.0f,
            // back face
            0.0f,0.0f,
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,1.0f,
            // left face
            0.0f,0.0f,
            1.0f,0.0f,
            1.0f,1.0f,
            0.0f,0.0f,
            0.0f,1.0f,
            1.0f,1.0f,
            // right face           
        };
        
        // data conversion to buffers
        target.Vertices = Buffers.newDirectFloatBuffer(vertex);
        target.Indices = Buffers.newDirectShortBuffer(index);
        target.Normals = Buffers.newDirectFloatBuffer(normal);  
        target.Texcoords = Buffers.newDirectFloatBuffer(uv);  
        
        // use a temp variable to hold the pointers in gpu   
        int[] temp = new int[4];
        gl.glGenBuffers(4, temp, 0);
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
        target.TexcoordPtr = temp[3];
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER,target.TexcoordPtr);
        gl.glBufferData(gl.GL_ARRAY_BUFFER, target.Texcoords.capacity() * Buffers.SIZEOF_FLOAT,
                            target.Texcoords, gl.GL_STATIC_DRAW);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, 0);   
        
        // add the part to map for later usage   
        parts.put(part,target);
    }
}
