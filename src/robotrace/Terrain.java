package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the terrain.
 */
class Terrain {

    /**
     * Can be used to set up a display list.
     */
    public Terrain() {
        // code goes here ...
    }
    
    private boolean Initialized = false;
    
    private Texture tex;
    private Texture tex_water;
    private Texture tex_sand;
    
    private Random rnd = new Random();
    
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
    
    private void makeCylinder(GL2 gl, GLU glu, GLUT glut,float height, float base){
        gl.glColor3f(0.64f,0.16f,0.16f);
        gl.glPushMatrix();
        gl.glRotatef(-90,1.0f,0.0f,0.0f);
        glu.gluCylinder(glu.gluNewQuadric(), base, base-(0.2*base), height, 20, 20);
        gl.glPopMatrix();
    }

    private void makeTree(GL2 gl, GLU glu, GLUT glut,float height, float base)
    {
        float angle;
        makeCylinder(gl,glu,glut,height, base); 
        gl.glTranslatef(0.0f, height, 0.0f);
        height -= height*.2; base-= base*0.3;
        for(int a= 0; a<3; a++)
        {
            angle = rnd.nextInt(50)+20;
            if(angle >48)
                angle = -(rnd.nextInt(50)+20);
            if (height >1){
                gl.glPushMatrix();
                gl.glRotatef(angle,1,0.0f,1);
                makeTree(gl,glu,glut,height,base);
                gl.glPopMatrix();
            }
        }
    }

    private int tree = 0;

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) throws IllegalAccessException {
        java.lang.reflect.Method method = null;
        if(!Initialized)
        {
            Initialized =true;
            tex = loadTexture("terrain.jpg",gl);
            tex_water = loadTexture("water.jpg",gl);
            tex_sand = loadTexture("sand.jpg",gl);
            
            try {
              method = gl.getClass().getMethod("gl"+"GenLists", int.class);
            } catch (SecurityException e) {} catch (NoSuchMethodException e) {}
            
            try {
                tree = (int)method.invoke(gl, 1);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
            //tree=gl.glGenLists(1);
            try {
              method = gl.getClass().getMethod("gl"+"NewList", int.class,int.class);
            } catch (SecurityException e) {} catch (NoSuchMethodException e) {}
            try {
                method.invoke(gl,tree, gl.GL_COMPILE);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
            makeTree(gl,glu,glut,4,0.2f);
            try {
              method = gl.getClass().getMethod("gl"+"EndList");
            } catch (SecurityException e) {} catch (NoSuchMethodException e) {}
            try {
                method.invoke(gl);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
            //gl.glEndList();
        }
        gl.glPushMatrix();
        gl.glTranslatef(15, 15, 0);
        gl.glRotatef(90, 1.0f,0.0f,0.0f);
        try {
              method = gl.getClass().getMethod("gl"+"CallList", int.class);
            } catch (SecurityException e) {} catch (NoSuchMethodException e) {}
            
            try {
                method.invoke(gl,tree);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        gl.glPopMatrix();
        gl.glPushMatrix();
        gl.glTranslatef(-15, -15, 0);
        gl.glRotatef(90, 1.0f,0.0f,0.0f);
        try {
              method = gl.getClass().getMethod("gl"+"CallList", int.class);
            } catch (SecurityException e) {} catch (NoSuchMethodException e) {}
            
            try {
                method.invoke(gl,tree);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Terrain.class.getName()).log(Level.SEVERE, null, ex);
            }
        gl.glPopMatrix();
        //gl.glCallList(tree);
        
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        tex.enable(gl);
        tex.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);   
        double sampleDistance = 0.2;
        for(double x=-20;x<=20;x+=sampleDistance)
        {
            for(double y=-20;y<=20;y+=sampleDistance)
            {
                //gl.glTexCoord2d(y, y);
                gl.glColor3f(0.4f, 0.4f, 0.4f);
                gl.glVertex3d(x,y,0.6*Math.cos(0.3*x+0.2*y)+0.4*Math.cos(x-0.5*y));
                Vector v = new Vector(
                        -0.4*Math.sin(x-0.5*y)-0.18*Math.sin(0.3*x+0.2*y),
                        0.2*Math.sin(x-0.5*y)-0.12*Math.sin(0.3*x+0.2*y), 
                        2);
                v=v.normalized();
                gl.glNormal3d(
                        v.x,
                        v.y, 
                        v.z);
                gl.glTexCoord2d((x%10+10)/20, (y%10+10)/20);              
                
                gl.glVertex3d(x+sampleDistance,y+sampleDistance,0.6*Math.cos(0.3*(x+sampleDistance)+0.2*(y+sampleDistance))+0.4*Math.cos((x+sampleDistance)-0.5*(y+sampleDistance)));
                v = new Vector(
                        -0.4*Math.sin((x+sampleDistance)-0.5*(y+sampleDistance))-0.18*Math.sin(0.3*(x+sampleDistance)+0.2*(y+sampleDistance)),
                        0.2*Math.sin((x+sampleDistance)-0.5*(y+sampleDistance))-0.12*Math.sin(0.3*(x+sampleDistance)+0.2*(y+sampleDistance)), 
                        2);
                v=v.normalized();
                gl.glNormal3d(
                        v.x,
                        v.y, 
                        v.z);
                if((x%10+sampleDistance+10)/20>1 || (y%10+sampleDistance+10)/20>1)
                    gl.glTexCoord2d(1, 1);
                else
                    gl.glTexCoord2d((x%10+sampleDistance+10)/20, (y%10+sampleDistance+10)/20);
                //System.out.println(String.format("x %f y %f", (x+sampleDistance+20)/40,(y+sampleDistance+20)/40));
            }
        }      
        gl.glEnd();
        tex.disable(gl);
        
        tex_water.enable(gl);
        tex_water.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);        
        gl.glVertex3d(-20,-20,0);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(0, 0);
        gl.glVertex3d(20,-20,0);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(0, 1);
        gl.glVertex3d(20,20,0);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(1, 1);
        
        gl.glVertex3d(-20,-20,0);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(0, 0);
        gl.glVertex3d(-20,20,0);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(1, 0);
        gl.glVertex3d(20,20,0);
        gl.glNormal3d(0,0,1);  
        gl.glTexCoord2d(1, 1);
        
        gl.glEnd();
        tex_water.disable(gl);
        
        tex_sand.enable(gl);
        tex_sand.bind(gl);
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);        
        gl.glVertex3d(-10,-10,0.5);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(0, 0);
        gl.glVertex3d(10,-10,0.5);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(0, 1);
        gl.glVertex3d(10,10,0.5);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(1, 1);
        
        gl.glVertex3d(-10,-10,0.5);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(0, 0);
        gl.glVertex3d(-10,10,0.5);
        gl.glNormal3d(0,0,1);
        gl.glTexCoord2d(1, 0);
        gl.glVertex3d(10,10,0.5);
        gl.glNormal3d(0,0,1);  
        gl.glTexCoord2d(1, 1);
        
        gl.glEnd();
        tex_sand.disable(gl);
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
    }

    /**
     * Computes the elevation of the terrain at (x, y).
     */
    public float heightAt(float x, float y) {
        return (float)(0.6*Math.cos(0.3*x+0.2*y)+0.4*Math.cos(x-0.5*y)); // <- code goes here
    }
}
