package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

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

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        if(!Initialized)
        {
            Initialized =true;
            tex = loadTexture("terrain.jpg",gl);
            tex_water = loadTexture("water.jpg",gl);
            tex_sand = loadTexture("sand.jpg",gl);
        }
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
