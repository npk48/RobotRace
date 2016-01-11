package robotrace;

import javax.media.opengl.GL;
import static javax.media.opengl.GL2.*;
import java.awt.Font;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.util.Random;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Handles all of the RobotRace graphics functionality,
 * which should be extended per the assignment.
 * 
 * OpenGL functionality:
 * - Basic commands are called via the gl object;
 * - Utility commands are called via the glu and
 *   glut objects;
 * 
 * GlobalState:
 * The gs object contains the GlobalState as described
 * in the assignment:
 * - The camera viewpoint angles, phi and theta, are
 *   changed interactively by holding the left mouse
 *   button and dragging;
 * - The camera view width, vWidth, is changed
 *   interactively by holding the right mouse button
 *   and dragging upwards or downwards;
 * - The center point can be moved up and down by
 *   pressing the 'q' and 'z' keys, forwards and
 *   backwards with the 'w' and 's' keys, and
 *   left and right with the 'a' and 'd' keys;
 * - Other settings are changed via the menus
 *   at the top of the screen.
 * 
 * Textures:
 * Place your "track.jpg", "brick.jpg", "head.jpg",
 * and "torso.jpg" files in the same folder as this
 * file. These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively.
 * Be aware, these objects are already defined and
 * cannot be used for other purposes. The texture
 * objects can be used as follows:
 * 
 * gl.glColor3f(1f, 1f, 1f);
 * track.bind(gl);
 * gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0);
 * gl.glVertex3d(0, 0, 0);
 * gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0);
 * gl.glTexCoord2d(1, 1);
 * gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1);
 * gl.glVertex3d(0, 1, 0);
 * gl.glEnd(); 
 * 
 * Note that it is hard or impossible to texture
 * objects drawn with GLUT. Either define the
 * primitives of the object yourself (as seen
 * above) or add additional textured primitives
 * to the GLUT object.
 */
public class RobotRace extends Base {
    
    /** Array of the four robots. */
    private final Robot[] robots;
    
    /** Instance of the camera. */
    private final Camera camera;
    
    /** Instance of the race track. */
    private final RaceTrack[] raceTracks;
    
    /** Instance of the terrain. */
    private final Terrain terrain;
    
    private final TextRenderer txt = new TextRenderer(new Font("Times New Roman", Font.PLAIN, 24), true, true);
    
    private float t_previous = 0;
    
    private Random rnd = new Random();
    
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        
        //Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD
            /* add other parameters that characterize this robot */);
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER
            /* add other parameters that characterize this robot */);
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD
            /* add other parameters that characterize this robot */);

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE
            /* add other parameters that characterize this robot */);
        
        // Initialize the camera
        camera = new Camera();
        
        // Initialize the race tracks
        raceTracks = new RaceTrack[5];
        
        // Test track
        raceTracks[0] = new RaceTrack();
        
        // O-track
        raceTracks[1] = new RaceTrack(new Vector[] {
            new Vector(-2,0,3),
            new Vector(-2,0.5,3),
            new Vector(-2,1,3),
            new Vector(-2,1.5,3),
            new Vector(-2,2,3),
            new Vector(-1.5,2,3),
            new Vector(-1.5,1.5,3),
            new Vector(-1.5,1,3),
            new Vector(-1.5,0.5,3),
        });
        
        // L-track
        raceTracks[2] = new RaceTrack(new Vector[] { 
            /* add control points */
        });
        
        // C-track
        raceTracks[3] = new RaceTrack(new Vector[] { 
            /* add control points */
        });
        
        // Custom track
        raceTracks[4] = new RaceTrack(new Vector[] { 
           /* add control points */
        });
        
        // Initialize the terrain
        terrain = new Terrain();
    }
    
    /**
     * Called upon the start of the application.
     * Primarily used to configure OpenGL.
     */
    @Override
    public void initialize() {
	
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
		
	    // Normalize normals.
        gl.glEnable(GL_NORMALIZE);
        
        // Enable textures. 
        gl.glEnable(GL_TEXTURE_2D);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
		
	    // Try to load four textures, add more if you like.
        track = loadTexture("track.jpg");
        brick = loadTexture("brick.jpg");
        head = loadTexture("head.jpg");
        torso = loadTexture("torso.jpg");
    }
    
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        // Modify this to meet the requirements in the assignment.
        glu.gluPerspective(gs.vWidth, (float)gs.w / (float)gs.h, 0.1*gs.vDist, 100*gs.vDist);
        
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
               
        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);
        glu.gluLookAt(camera.eye.x(),    camera.eye.y(),    camera.eye.z(),
                      camera.center.x(), camera.center.y(), camera.center.z(),
                      camera.up.x(),     camera.up.y(),     camera.up.z());
    }
    
    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        // Draw the axis frame.
        if (gs.showAxes) {
            drawAxisFrame();
        }
        
        // Enable light
        gl.glEnable(gl.GL_LIGHTING);  
        // Enable light source 0
        gl.glEnable(gl.GL_LIGHT0);    
        // Set light souce position from camera direction but infinite
        float lightPos[] = {(float)camera.eye.x,(float)camera.eye.y,(float)camera.eye.z,0.0f};
        // Obtain the vector from camera to view
        Vector direction = new Vector(gs.cnt.x-camera.eye.x,gs.cnt.y-camera.eye.y,gs.cnt.z-camera.eye.z);
        // Rotate to up-left by 10 degree
        direction.y -= gs.vDist*Math.cos(45)*Math.tan(10);
        direction.z -= gs.vDist*Math.sin(45)*Math.tan(10);
        float lightDirection[]= { (float)direction.x, (float)direction.y, (float)direction.z };      
        // Set light color to white
        float lightColor[] = {1.0f, 1.0f, 1.0f, 1.0f};
        // Apply light       
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_POSITION,lightPos,0);
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_SPOT_DIRECTION, lightDirection,0);
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_AMBIENT,lightColor,0);        
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_DIFFUSE,lightColor,0);       
        gl.glLightfv(gl.GL_LIGHT0,gl.GL_SPECULAR,lightColor,0);
        
        // Get the position and direction of the first robot.
        //robots[0].position = raceTracks[gs.trackNr].getLanePoint(0, 0);
        //robots[0].direction = raceTracks[gs.trackNr].getLaneTangent(0, 0);
        
        //Get the moving 4 robots with time gs.tAnim gs.tAnim is in seconds, divided by different values
        //to make the robots have different speed.
        float step = gs.tAnim - t_previous;
        robots[0].t += step/(rnd.nextInt(35)+10);
        robots[0].position = raceTracks[gs.trackNr].getLanePoint(1,    robots[0].t);
        robots[0].direction = raceTracks[gs.trackNr].getLaneTangent(1, robots[0].t);
        robots[1].t += step/(rnd.nextInt(35)+10);
        robots[1].position = raceTracks[gs.trackNr].getLanePoint(2,    robots[1].t);
        robots[1].direction = raceTracks[gs.trackNr].getLaneTangent(2, robots[1].t);
        robots[2].t += step/(rnd.nextInt(35)+10);
        robots[2].position = raceTracks[gs.trackNr].getLanePoint(3,    robots[2].t);
        robots[2].direction = raceTracks[gs.trackNr].getLaneTangent(3, robots[2].t);
        robots[3].t += step/(rnd.nextInt(35)+10);
        robots[3].position = raceTracks[gs.trackNr].getLanePoint(4,    robots[3].t);
        robots[3].direction = raceTracks[gs.trackNr].getLaneTangent(4, robots[3].t);
        t_previous = gs.tAnim;
        
        // Draw the first robot.
        robots[0].draw(gl, glu, glut, gs.showStick, gs.tAnim);
        
        // Draw other robots.
        //robots[3].direction = robots[2].direction = robots[1].direction = robots[0].direction;
        //robots[1].position.x = -0.5;
        //robots[2].position.x = -1;
        //robots[3].position.x = -1.5;
        robots[1].draw(gl, glu, glut, gs.showStick, gs.tAnim);
        robots[2].draw(gl, glu, glut, gs.showStick, gs.tAnim);
        robots[3].draw(gl, glu, glut, gs.showStick, gs.tAnim);
        
        // Draw the race track.
        raceTracks[gs.trackNr].draw(gl, glu, glut);
        
        // Draw the terrain.
        //terrain.draw(gl, glu, glut);
        try {
            //Draw the terrain 
            terrain.draw(gl, glu, glut);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(RobotRace.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // disable light
        gl.glDisable(gl.GL_LIGHTING);
        
        gl.glPushMatrix();
        gl.glRotatef(90, 1, 0, 0);
        String timeStamp = new java.text.SimpleDateFormat("HH.mm.ss").format(new java.util.Date());
        txt.begin3DRendering();
        txt.setColor(0.0f, 0.0f, 0.0f, 1); 
        txt.draw3D("Time:"+timeStamp, -10, 5, 0,0.1f);
        txt.end3DRendering();
        gl.glPopMatrix();
    }
    
    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue),
     * and origin (yellow).
     */
    public void drawAxisFrame() {
        // draw original point
        gl.glColor3f(1,1,0);
        glut.glutSolidSphere(0.025f, 50, 50);       
        
        // draw x axis
        gl.glColor3f(1, 0, 0);   
        gl.glPushMatrix();          
        gl.glScalef(100f, 1f, 1f);
        gl.glTranslatef(0.005f, 0f, 0f);
        glut.glutSolidCube(0.01f);
        gl.glPopMatrix();
        // draw x axis arrow
        gl.glColor3f(0,0,0);
        gl.glPushMatrix();    
        gl.glRotatef(90f, 0f, 1f, 0f);
        gl.glTranslatef(0f, 0f, 1f);
        glut.glutSolidCone(0.025f, 0.1f, 50, 50);            
        gl.glPopMatrix();
        
        // draw y axis
        gl.glColor3f(0, 1, 0);   
        gl.glPushMatrix();          
        gl.glScalef(1f, 100f, 1f);
        gl.glTranslatef(0f, 0.005f, 0f);
        glut.glutSolidCube(0.01f);
        gl.glPopMatrix();
        // draw y axis arrow
        gl.glColor3f(0,0,0);
        gl.glPushMatrix();    
        gl.glRotatef(-90f, 1f, 0f, 0f);
        gl.glTranslatef(0f, 0f, 1f);
        glut.glutSolidCone(0.025f, 0.1f, 50, 50);            
        gl.glPopMatrix();
        
        // draw z axis
        gl.glColor3f(0, 0, 1);   
        gl.glPushMatrix();          
        gl.glScalef(1f, 1f, 100f);
        gl.glTranslatef(0f, 0f, 0.005f);
        glut.glutSolidCube(0.01f);
        gl.glPopMatrix();
        // draw z axis arrow
        gl.glColor3f(0,0,0);
        gl.glPushMatrix();    
        gl.glRotatef(-90f, 0f, 0f, 1f);
        gl.glTranslatef(0f, 0f, 1f);
        glut.glutSolidCone(0.025f, 0.1f, 50, 50);            
        gl.glPopMatrix();
    }
 
    /**
     * Main program execution body, delegates to an instance of
     * the RobotRace implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    } 
}
