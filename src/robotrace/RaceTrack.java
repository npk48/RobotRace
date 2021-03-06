package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
class RaceTrack {
    
    private boolean Initialized =false;
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;

    /** Array with 3N control points, where N is the number of segments. */
    private Vector[] controlPoints = null;

    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }
    
    /**
     * Constructor for a spline track.
     */
    public RaceTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
    }
    
    private Texture tex;
    
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
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        if(!Initialized)
        {
            Initialized = true;
            tex = loadTexture("track.jpg",gl);
        }
        if (null == controlPoints) {
            // draw the test track
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            //Draw the bound lines of the track (5 lines indicate 4 lanes)
            for (int laneBound = 0; laneBound <= 4; laneBound++) {
                gl.glBegin(GL2.GL_LINE_LOOP);
                double sampleDistance = 0.01;
                for (double i = 0; i <= 1; i += sampleDistance) {
                    Vector radius;
                    radius = getTangent(i).cross(new Vector(0, 0, 1));
                    radius.normalized();
                    radius.x *= laneWidth;
                    radius.y *= laneWidth;
                    gl.glColor3f(0f, 0f, 0f);
                    gl.glVertex3d(getPoint(i).x + (laneBound - 2) * 2 * radius.x, 
                            getPoint(i).y + (laneBound - 2) * 2 * radius.y, 
                            getPoint(i).z);
                }
                gl.glEnd();
            }
            for (int laneBound = 0; laneBound <= 3; laneBound++) {
                // bind texture
                tex.enable(gl);
                tex.bind(gl);
                gl.glBegin(GL2.GL_TRIANGLE_STRIP);
                double sampleDistance = 0.0001;
                int count = 0;
                for (double i = 0; i <= 1; i += sampleDistance) {
                    double u1=0,u2=0,u3=0,u4 =0;
                    
                    switch (count%3) {
                        case 0:
                            u1=0;
                            u2=0;
                            u3=1;
                            u4=0;
                            break;
                        case 1:
                            u1=0;
                            u2=1;
                            u3=1;
                            u4=1;
                            break;
                        case 2:
                            u1=1;
                            u2=1;
                            u3=0;
                            u4=1;
                            break;
                        default:
                            break;
                    }
                    //double scale = (count%250)/250;
                    double scale = count/((1/2.5)*1/sampleDistance);
                    Vector radius;
                    radius = getTangent(i).cross(new Vector(0, 0, 1));
                    radius.normalized();
                    radius.x *= laneWidth;
                    radius.y *= laneWidth;
                    gl.glColor3f(0.2f, 0.2f, 0.2f);
                    gl.glVertex3d(getPoint(i).x + (laneBound - 2) * 2 * radius.x, 
                            getPoint(i).y + (laneBound - 2) * 2 * radius.y, 
                            getPoint(i).z);
                    gl.glNormal3d(0,0,1);
                    // tex coord
                    gl.glTexCoord2d(u1*scale, u2*scale);
                    
                    gl.glVertex3d(getPoint(i + sampleDistance).x + (laneBound - 1) * 2 * radius.x, 
                            getPoint(i + sampleDistance).y + (laneBound - 1) * 2 * radius.y, 
                            getPoint(i + sampleDistance).z);
                    gl.glNormal3d(0,0,1);
                    // tex coord
                    gl.glTexCoord2d(u3*scale, u4*scale);
                    count++;
                }
                gl.glEnd();
                tex.disable(gl);
            }
            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            double sampleDistance = 0.0001;
            for(double i = 0; i < 1; i += sampleDistance){
                Vector radius = new Vector(0,0,0);
                radius = getTangent(i).cross(new Vector(0,0,1));
                radius.normalized();
                radius.x *= laneWidth;
                radius.y *= laneWidth;
                gl.glColor3f(0.1f, 0.1f, 0.1f);
                gl.glVertex3d(getPoint(i).x + 2 * 2 * radius.x, 
                        getPoint(i).y + 2 * 2 * radius.y,
                        getPoint(i).z);
                gl.glVertex3d(getPoint(i + sampleDistance).x + 2 * 2 * radius.x,
                        getPoint(i + sampleDistance).y + 2 * 2 * radius.y,
                        getPoint(i+ sampleDistance).z - 1);
            }
            gl.glEnd();
            
            gl.glDisable(GL2.GL_COLOR_MATERIAL);
        } else {
            // draw the spline track
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            tex.enable(gl);
            tex.bind(gl);
            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            for (int laneBound = 0; laneBound <= 3; laneBound++) {
                for(int i = 0; i<controlPoints.length-3;i+=3)
                {
                    Vector P1 = controlPoints[i];
                    Vector P2 = controlPoints[i+1];
                    Vector P3 = controlPoints[i+2];
                    Vector P4 = controlPoints[i+3];
                    double sampleDistance = 0.1;
                    for (double j=0;j<=1;j+=sampleDistance)
                    {
                        Vector P = getCubicBezierPoint(j,P1,P2,P3,P4);
                        Vector radius = new Vector(0,0,0);
                        radius = getCubicBezierTangent(j,P1,P2,P3,P4).cross(new Vector(0,0,1));
                        radius.normalized();
                        radius.x *= laneWidth;
                        radius.y *= laneWidth;
                        gl.glColor3f(0.2f, 0.2f, 0.2f);
                        gl.glVertex3d(P.x + 2 * 2 * radius.x,P.y + 2 * 2 * radius.y,P.z);
                        gl.glNormal3d(0,0,1);
                        gl.glTexCoord2d(0, 0);
                    
                        Vector Pn = getCubicBezierPoint(j+sampleDistance,P1,P2,P3,P4);
                        gl.glColor3f(0.2f, 0.2f, 0.2f);
                        gl.glVertex3d(Pn.x + 2 * 2 * radius.x,Pn.y + 2 * 2 * radius.y,Pn.z);
                        gl.glNormal3d(0,0,1);
                        gl.glTexCoord2d(1, 1);
                    }
                }            
            }         
            gl.glEnd();
            tex.disable(gl);
            gl.glDisable(GL2.GL_COLOR_MATERIAL);
        }
    }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t) {
        if (null == controlPoints) {
            Vector pointAtT = getPoint(t);
            Vector tangentAtT = getTangent(t);
            Vector normal = tangentAtT.cross(new Vector(0, 0, 1));
            normal = normal.normalized();
            return pointAtT.add(normal.scale((lane - 3) * 2 * laneWidth+0.5*laneWidth));
        } else {
            Vector pointAtT = getPoint(t);
            return pointAtT;
        }
    }
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t) {
        if (null == controlPoints) {
            return getTangent(t); 
        } else {
            return Vector.O; // <- code goes here
        }
    }

    /**
     * Returns a point on the test track at 0 <= t < 1.
     */
    public Vector getPoint(double t) {
        Vector pointAtT;
        /*get the point vector when t*/
        pointAtT = new Vector((10 * Math.cos(2 * Math.PI * t)), 
                (14 * Math.sin(2 * Math.PI * t)), 
                1);
        
        return pointAtT;
    }

    /**
     * Returns a tangent on the test track at 0 <= t < 1.
     */
    private Vector getTangent(double t) {
        double tangentX;
        double tangentY;
        double tangentZ;
        double length;
        Vector tangentAtT;
        /* using formula tangent vector T(t) = P'(t)/|P(t)|*/
        /* where P'(t) is the derivation of P(t)*/
        //differentiate of x y z
        tangentX = -20 * Math.PI * Math.sin(2 * Math.PI * t);
        tangentY = 28 * Math.PI * Math.cos(2 * Math.PI * t);
        tangentZ = 0;
        length = Math.sqrt((tangentX * tangentX) + (tangentY * tangentY) + (tangentZ * tangentZ));
            
        tangentX = tangentX / length;
        tangentY = tangentY / length;
        tangentZ = tangentZ / length;
            
        tangentAtT = new Vector(tangentX, tangentY, tangentZ);
        return tangentAtT;
    }
    
    /**
     * Returns a point on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    private Vector getCubicBezierPoint(double t, Vector P0, Vector P1,
                                                 Vector P2, Vector P3) {
        Vector cubicBezierPoint;
        //Use formula
        //P(u)=(1−u)^3*P0 +3u*(1−u)^2*P1 +3u^2(1−u)*P2 +u^3*P3
        //Where u is t here
        cubicBezierPoint = P0.scale(Math.pow(1 - t, 3));
        cubicBezierPoint = cubicBezierPoint.add(P1.scale(3 * t * Math.pow(1 - t, 2)));
        cubicBezierPoint = cubicBezierPoint.add(P2.scale(3 * Math.pow(t, 2) * (1 - t)));
        cubicBezierPoint = cubicBezierPoint.add(P3.scale(Math.pow(t, 3)));
        
        return cubicBezierPoint; // <- code goes here
    }
    
    /**
     * Returns a tangent on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    private Vector getCubicBezierTangent(double t, Vector P0, Vector P1,
                                                   Vector P2, Vector P3) {
        Vector cubicBezierTangent;
        Vector tempVector;//Used to calculate by using formula
        //The tangent is the derivation of the formula P(u)
        //P'(u) = 3((P3-3P2+3P1-P0)*t^2+(2P2-4P1+2P0)*t+P1-P0)
        //P'(u) = 
        cubicBezierTangent = P1.subtract(P0);
        tempVector = P2.scale(2).subtract(P1.scale(4)).add(P0.scale(2)).scale(t);
        cubicBezierTangent = cubicBezierTangent.add(tempVector);
        tempVector = P3.subtract(P2.scale(3).add(P1.scale(3)).subtract(P0)).scale(Math.pow(t, 2));
        cubicBezierTangent = cubicBezierTangent.add(tempVector);
        cubicBezierTangent = cubicBezierTangent.scale(3);
        
        return cubicBezierTangent; // <- code goes here
    }
}
